package com.app.spotick.repository.admin.user;


import com.app.spotick.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<User,Long>, AdminUserQDSLRepository {


}
