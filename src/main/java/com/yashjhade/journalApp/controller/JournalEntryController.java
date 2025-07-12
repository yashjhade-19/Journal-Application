package com.yashjhade.journalApp.controller;

import com.yashjhade.journalApp.dto.JournalEntryDTO;
import com.yashjhade.journalApp.entity.JournalEntry;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.service.JournalEntryService;
import com.yashjhade.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
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
    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    String userName= authentication.getName();

    User user= userService.findByUserName(userName);

    if (user == null) {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
List<JournalEntry> all=user.getJournalEntries();
if(all!=null  && !all.isEmpty()){
    return new ResponseEntity<>(all, HttpStatus.OK);
}
return new ResponseEntity<>(HttpStatus.NOT_FOUND);

}


    @PostMapping
    @Operation(summary="create journal entry of user")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntryDTO myEntry){

        JournalEntry entry = new JournalEntry();
        entry.setTitle(myEntry.getTitle());
        entry.setContent(myEntry.getContent());
        entry.setDate(new Date());

        try {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        myEntry.setDate(new Date());
        journalEntryService.saveEntry(entry,userName);
        return new ResponseEntity<>(entry, HttpStatus.CREATED);
    }catch(Exception e){
        e.printStackTrace();  // Console mein actual problem dikhayega
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("id/{myid}")
    @Operation(summary="Get journal entry of user by id")
    public  ResponseEntity<?> getjournalEntryById(@PathVariable String myid){
        ObjectId objectId = new ObjectId(myid);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();

        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry= journalEntryService.findById(objectId);
            if(journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }

     }
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @DeleteMapping("id/{myid}")
    @Operation(summary="Delete journal entry of user by id")
    public  ResponseEntity<?> deletejournalEntryByid(@PathVariable String myid){
        ObjectId objectId = new ObjectId(myid);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();

    journalEntryService.deleteById(objectId,userName);

     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{myid}")
    @Operation(summary="Update journal entry of user by id")
    public ResponseEntity<?> updateJournalEntrybyid(@PathVariable String myid,
                                                    @RequestBody JournalEntryDTO newEntry
                                                    ) {
        ObjectId objectId = new ObjectId(myid);
        JournalEntry entry = new JournalEntry();
        entry.setTitle(newEntry.getTitle());
        entry.setContent(newEntry.getContent());
        entry.setDate(new Date());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();

    JournalEntry old = journalEntryService.findById(objectId).orElse(null);
    if(old !=null)
    {
      old.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : old.getTitle() );
      old.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : old.getContent());
      journalEntryService.saveEntry(old);
      return new ResponseEntity<>(old,HttpStatus.OK);
    }
return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

         }

