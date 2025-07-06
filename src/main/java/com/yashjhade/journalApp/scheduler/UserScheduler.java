package com.yashjhade.journalApp.scheduler;

import lombok.extern.slf4j.Slf4j;
import com.yashjhade.journalApp.entity.JournalEntry;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.enums.Sentiment;
import com.yashjhade.journalApp.service.EmailService;
import com.yashjhade.journalApp.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    /** Runs every Sunday at 09:00 */
    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail() {

        List<User> users = userRepository.getUserForSA();   // your custom repo method
        log.info("Scheduler triggered – {} user(s) fetched for sentiment analysis", users.size());

        for (User user : users) {

            List<JournalEntry> recentEntries = user.getJournalEntries().stream()
                    .filter(e -> e.getDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .collect(Collectors.toList());


            // Build a frequency map of sentiments
            Map<Sentiment, Long> sentimentCounts = recentEntries.stream()
                    .map(JournalEntry::getSentiment)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

            // Find the most frequent sentiment
            Sentiment dominant = sentimentCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (dominant != null) {
                String message =
                        "Your dominant sentiment for the last 7 days is: **" + dominant + "**";

                emailService.sendEmail(
                        user.getEmail(),
                        "Weekly Sentiment Summary",
                        message);

                log.info("Sent sentiment mail to {} ({}) – {}", user.getUserName(), user.getEmail(), dominant);
            }
        }
    }
}
