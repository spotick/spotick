package com.app.spotick.repository.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

    List<UserAuthority> findUserAuthorityByUser(User user);
}
