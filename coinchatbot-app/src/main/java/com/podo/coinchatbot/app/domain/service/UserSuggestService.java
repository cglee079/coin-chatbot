package com.podo.coinchatbot.app.domain.service;

import com.podo.coinchatbot.app.domain.model.UserSuggest;
import com.podo.coinchatbot.app.domain.repository.UserSuggestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSuggestService {

    private final UserSuggestRepository userSuggestRepository;

    public void insertNew(Long userId, String messageText) {
        UserSuggest userSuggest = new UserSuggest(userId, messageText);
        userSuggestRepository.save(userSuggest);
    }

}
