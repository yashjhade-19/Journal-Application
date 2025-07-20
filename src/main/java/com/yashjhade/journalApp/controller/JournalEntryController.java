package com.yashjhade.journalApp.controller;

import com.yashjhade.journalApp.dto.JournalEntryDTO;
import com.yashjhade.journalApp.entity.JournalEntry;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.service.JournalEntryService;
import com.yashjhade.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal APIs")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary="Get all journal entries of user")
    public ResponseEntity<?> getAllJournalEntriesofUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> all = user.getJournalEntries();
        if(all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Operation(summary="Create journal entry of user")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntryDTO myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            // Create new journal entry with generated ID
            JournalEntry entry = new JournalEntry(
                    myEntry.getTitle(),
                    myEntry.getContent(),
                    new Date(),
                    myEntry.getSentiment()
            );

            JournalEntry savedEntry = journalEntryService.saveEntry(entry, userName);
            return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myid}")
    @Operation(summary="Get journal entry of user by id")
    public ResponseEntity<?> getjournalEntryById(@PathVariable String myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Check if the journal belongs to this user
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
        if (journalEntry.isPresent() && user.getJournalEntries().contains(journalEntry.get())) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myid}")
    @Operation(summary="Delete journal entry of user by id")
    public ResponseEntity<?> deletejournalEntryByid(@PathVariable String myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        journalEntryService.deleteById(myid, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{myid}")
    @Operation(summary="Update journal entry of user by id")
    public ResponseEntity<?> updateJournalEntrybyid(@PathVariable String myid,
                                                    @RequestBody JournalEntryDTO newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Optional<JournalEntry> optionalEntry = journalEntryService.findById(myid);
        if (optionalEntry.isEmpty()) {
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }

        JournalEntry entry = optionalEntry.get();

        // Verify the entry belongs to the user
        if (!user.getJournalEntries().contains(entry)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        // Update the entry
        entry.setTitle(newEntry.getTitle());
        entry.setContent(newEntry.getContent());
        entry.setDate(new Date());
        entry.setSentiment(newEntry.getSentiment());

        JournalEntry updatedEntry = journalEntryService.saveEntry(entry);
        return new ResponseEntity<>(updatedEntry, HttpStatus.OK);
    }
}