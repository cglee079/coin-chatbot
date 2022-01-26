package com.podo.coinchatbot.admin.repository;

import com.podo.coinchatbot.admin.domain.User;
import com.podo.coinchatbot.core.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    int countByUserStatus(UserStatus userStatus);
}
