package com.yashjhade.journalApp.repository;

import com.yashjhade.journalApp.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
    // Changed from ObjectId to String
}