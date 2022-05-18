package com.example.project_2th.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
public class UserCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 숫자가 증가
    private Long id;

    private Date exDay;

    private String timeDiff;
    private String userWeight;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}