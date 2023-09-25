package com.zwash.auth.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zwash.auth.pojos.User;




@Repository
public interface UserRepository extends  JpaRepository<User, Long> {


 @Query("select u from User u where u.username = ?1")
 Optional<User> findByUsername(String username);

 @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
 User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

 @Modifying
 @Query("UPDATE User u SET u.password = :password WHERE u.username = :username")
 int updatePassword(@Param("username") String username, @Param("password") String password);


}