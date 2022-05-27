package com.example.project_2th.service;


import com.example.project_2th.entity.Exercies;
import com.example.project_2th.entity.ExerciesVideo;
import com.example.project_2th.entity.User;
import com.example.project_2th.repository.ExinfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ExerciesService {

    @Autowired
    private ExinfoRepository exinfoRepository;


    public Exercies exerciesInfo(Exercies exercies){

        exercies.setExDay(Date.valueOf(LocalDate.now()));
        exinfoRepository.save(exercies);
        Exercies exinfo = exinfoRepository.findByOne(exercies.getUser().getUserId(), exercies.getExName());
        return exinfo;
    }
}
