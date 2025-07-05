package com.yashjhade.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import com.yashjhade.journalApp.entity.JournalEntry;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
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
//
//public void  saveEntry(JournalEntry journalEntry, String userName){
//    User user= userService.findByUserName(userName);
//    JournalEntry saved = journalEntryRepository.save(journalEntry);
//    user.getJournalEntries().add(saved);
//    userService.saveEntry(user);
//}



    public void saveEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUserName(userName);

       try{ if (user == null) {
            throw new RuntimeException("User not found: " + userName);
        }
        // Save journal entry
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        // Initialize journalEntries list if null
        if (user.getJournalEntries() == null) {
            user.setJournalEntries(new ArrayList<>());
        }
        user.getJournalEntries().add(saved);
        // Save user with new entry
        userService.saveUser(user);
    }
       catch (Exception e) {
            log.error("Error",e);       }
       }



    public void saveEntry(JournalEntry journalEntry) {

        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
    return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
    return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String userName) {
        try {
            User user = userService.findByUserName(userName);

            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
