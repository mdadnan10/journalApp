package com.adnantech.journalApp.scheduler;

import com.adnantech.journalApp.cache.AppCache;
import com.adnantech.journalApp.entity.JournalEntry;
import com.adnantech.journalApp.entity.User;
import com.adnantech.journalApp.enums.Sentiment;
import com.adnantech.journalApp.repository.UserRepositoyImpl;
import com.adnantech.journalApp.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserSceduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoyImpl userRepository;

    @Autowired
    private AppCache appCache;

//    @Scheduled(cron = "0 0 9 * * SUN")
    @Scheduled(cron = "0 * * ? * *")
    public void fetUsersAndSendSAMail() {
        log.info("Sending Sentiment Analysis for users");
        List<User> userForSA = userRepository.getUserForSA();
        for (User user : userForSA){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> filteredEntries = journalEntries.stream().
                    filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(JournalEntry::getSentiment)
                    .toList();
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : filteredEntries){
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()){
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }


            if (mostFrequentSentiment != null) {
                emailService.sendEmail(user.getEmail(), "Sentiment For Previous Week", mostFrequentSentiment.toString());
            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }

}
