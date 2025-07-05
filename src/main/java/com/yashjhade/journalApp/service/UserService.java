package com.yashjhade.journalApp.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import com.yashjhade.journalApp.entity.User;
import com.yashjhade.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;   // ← NOT static!


//   private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    public void saveNewUser(User user){
    try{    user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }
    catch(Exception e){
      log.info("yjjjjjjjjjjjjjjjjjjjjjj");
      log.error("yjjjjjjjjjjjjjjjjjjjjjj");
      log.warn("yjjjjjjjjjjjjjjjjjjjjjj");
      log.debug("yjjjjjjjjjjjjjjjjjjjjjj");
      log.trace("yjjjjjjjjjjjjjjjjjjjjjj");
    }}

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    // … other methods …

    public List<User> getAll() {
    return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id){
    return userRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }


    public User findByUserName(@NonNull String userName) {
    return userRepository.findByUserName(userName);
    }
}

