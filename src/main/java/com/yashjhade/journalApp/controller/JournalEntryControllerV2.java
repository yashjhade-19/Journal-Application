package com.yashjhade.journalApp.controller;

import com.yashjhade.journalApp.entity.JournalEntry;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.service.JournalEntryService;
import com.yashjhade.journalApp.service.UserService;
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
public class JournalEntryControllerV2 {


@Autowired
private JournalEntryService journalEntryService;

@Autowired
private UserService userService;


@GetMapping
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
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry){
    try {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        myEntry.setDate(new Date());
        journalEntryService.saveEntry(myEntry,userName);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
    }catch(Exception e){
        e.printStackTrace();  // Console mein actual problem dikhayega
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("id/{myid}")
    public  ResponseEntity<?> getjournalEntryByid(@PathVariable ObjectId myid){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();

        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry= journalEntryService.findById(myid);
            if(journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }

     }
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @DeleteMapping("id/{myid}")
    public  ResponseEntity<?> deletejournalEntryByid(@PathVariable ObjectId myid){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();

    journalEntryService.deleteById(myid,userName);

     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{myid}")
    public ResponseEntity<?> updateJournalEntrybyid(@PathVariable ObjectId myid,
                                                    @RequestBody JournalEntry newEntry
                                                    ) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();

    JournalEntry old = journalEntryService.findById(myid).orElse(null);
    if(old !=null)
    {
      old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle() );
      old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
      journalEntryService.saveEntry(old);
      return new ResponseEntity<>(old,HttpStatus.OK);
    }
return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

         }

