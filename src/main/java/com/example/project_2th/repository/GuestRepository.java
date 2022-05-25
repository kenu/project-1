package com.example.project_2th.repository;

import com.example.project_2th.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GuestRepository extends JpaRepository<User,Long> {


    @Query(value = "select * from user where user_phone = :user_phone and user_gym = :user_gym"
            ,nativeQuery=true)
    User findByUserIdAndUserGym(
            @Param("user_phone") String userPhone,
            @Param("user_gym") String userGym);

    User findByUserPhoneAndUserGym(String userPhone, String userGym);

    User findByUserId(Long userId);

    @EntityGraph(attributePaths = {"calendarList"})
    @Query(value = "select u from User u")
    List<User> findAllByFetchJoin();
}
