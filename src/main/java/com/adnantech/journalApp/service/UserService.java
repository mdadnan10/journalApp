package com.adnantech.journalApp.service;

import com.adnantech.journalApp.entity.User;
import com.adnantech.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public boolean saveNewUser(User user){
        boolean saved = false;
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepository.save(user);
            saved = true;
        } catch (Exception e){
            log.info("Dhunchak");
            log.error(e.getLocalizedMessage());
            log.warn("Warn Warn");
            log.debug("Debugged");
            throw new RuntimeException("An error occurred while creating User.", e);
        }
        return saved;
    }

    public void createAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER", "ADMIN"));
        userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteUserById(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

//    public boolean updateEntry(JournalEntry newJournalEntry, ObjectId oldId){
//        JournalEntry oldJournal = findById(oldId).orElse(null);
//        if (oldJournal != null){
//            oldJournal.setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle() : oldJournal.getTitle());
//            oldJournal.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent() : oldJournal.getContent());
//            userRepository.save(oldJournal);
//            return true;
//        } else return false;
//    }


}
