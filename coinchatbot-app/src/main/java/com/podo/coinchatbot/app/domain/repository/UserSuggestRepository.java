package com.podo.coinchatbot.app.domain.repository;

import com.podo.coinchatbot.app.domain.model.UserSuggest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSuggestRepository extends JpaRepository<UserSuggest, Long> {
}
