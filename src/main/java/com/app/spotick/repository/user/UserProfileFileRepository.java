package com.app.spotick.repository.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileFileRepository extends JpaRepository<UserProfileFile, Long> {
}
