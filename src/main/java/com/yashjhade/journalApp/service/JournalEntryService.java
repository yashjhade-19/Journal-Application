package com.yashjhade.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import com.yashjhade.journalApp.entity.JournalEntry;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public JournalEntry saveEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found: " + userName);
        }

        // Save journal entry
        JournalEntry saved = journalEntryRepository.save(journalEntry);

        // Initialize journalEntries list if null
        if (user.getJournalEntries() == null) {
            user.setJournalEntries(new ArrayList<>());
        }

        // Add entry to user's journal list
        user.getJournalEntries().add(saved);
        userService.saveUser(user);

        return saved;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(String id) { // Changed to String
        return journalEntryRepository.findById(id);
    }

    public void deleteById(String id, String userName) { // Changed to String
        try {
            User user = userService.findByUserName(userName);
            if (user != null && user.getJournalEntries() != null) {
                boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
                if (removed) {
                    userService.saveUser(user);
                    journalEntryRepository.deleteById(id);
                }
            }
        } catch (Exception e) {
            log.error("Error deleting journal entry", e);
            throw e;
        }
    }
}