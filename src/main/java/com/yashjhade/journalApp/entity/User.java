package com.yashjhade.journalApp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection ="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @Indexed(unique = true)
    private String email;
    private boolean sentimentAnalysis;

    @NonNull
    private String password;

    //ab journalentries dalege unique users k liye , ye journalEntries , journal_entries ka reference rakhegi...like foreign key

    @DBRef
    private List<JournalEntry> journalEntries=new ArrayList<>();
    private List<String> roles;

}
