package com.app.spotick.repository.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.type.user.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    List<UserAuthority> findUserAuthorityByUser(User user);
    Optional<UserAuthority> findByUserIdAndAuthorityType(Long user_id, AuthorityType authorityType);
}
