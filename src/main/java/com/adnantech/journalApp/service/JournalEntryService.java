package com.adnantech.journalApp.service;

import com.adnantech.journalApp.entity.JournalEntry;
import com.adnantech.journalApp.entity.User;
import com.adnantech.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while saving the blog.", e);
        }
    }

    public List<JournalEntry> getAll() {
       return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName){
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(u -> u.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }
        return removed;
    }

    public void updateEntry(JournalEntry newJournalEntry, ObjectId oldId){
        JournalEntry oldJournal = findById(oldId).orElse(null);
        if (oldJournal != null){
            oldJournal.setDate(LocalDateTime.now());
            oldJournal.setTitle(newJournalEntry.getTitle() != null && !newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle() : oldJournal.getTitle());
            oldJournal.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent() : oldJournal.getContent());
            journalEntryRepository.save(oldJournal);
        }
    }

//    public List<JournalEntry> findByUserName(String userName){
//
//    }

}
