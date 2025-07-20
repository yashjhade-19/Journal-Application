package com.yashjhade.journalApp.entity;

import lombok.*;
import com.yashjhade.journalApp.enums.Sentiment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.UUID;

@Document(collection="journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    @Id
    private String id; // Changed from ObjectId to String

    @NonNull
    private String title;
    private String content;
    private Date date;
    private Sentiment sentiment;

    // Add this constructor to auto-generate ID
    public JournalEntry(String title, String content, Date date, Sentiment sentiment) {
        this.id = UUID.randomUUID().toString(); // Generate unique string ID
        this.title = title;
        this.content = content;
        this.date = date;
        this.sentiment = sentiment;
    }
}