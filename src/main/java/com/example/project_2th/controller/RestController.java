package com.example.project_2th.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.example.project_2th.entity.*;
import com.example.project_2th.repository.PosturesRepository;
import com.example.project_2th.repository.ExinfoRepository;
import com.example.project_2th.repository.UserRepository;
import com.example.project_2th.repository.VideoRepository;
import com.example.project_2th.response.ExerciesResponse;
import com.example.project_2th.service.ExerciesService;
import com.example.project_2th.service.ExerciesVideoService;
import com.example.project_2th.service.PostruesService;
import com.example.project_2th.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@Slf4j
public class RestController {

    @Autowired
    private final ExerciesService exerciesService;

    @Autowired
    private final ExerciesVideoService exerciesVideoService;

    @Autowired
    private final PostruesService postruesService;

    @Autowired
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(RestController.class);

    @PostMapping(value = "/calendarView")
    public ResponseEntity<List<ExerciesResponse>> calendarView(@RequestBody  Calendar calendar) throws Exception {
        logger.info("calendarView perfom");
        logger.info("user : " + calendar.getUser());
        logger.info("day : " + calendar.getExDay());

        List<ExerciesResponse> exinfoList = exerciesService.calendarExinfo(calendar);
        logger.info("exinfoList : {}",exinfoList);
        return ResponseEntity.ok().body(exinfoList);
    }

    @PostMapping(value = "insertExURL")
    public String insertExURL(HttpServletRequest req) throws Exception {
        logger.info("insertExURL perfom");
        String cnt = req.getParameter("cnt");
        Long user_id = Long.valueOf(req.getParameter("userId"));
        Long ex_seq = Long.valueOf(req.getParameter("exSeq"));
        ServletInputStream inputStream = req.getInputStream();

        exerciesVideoService.videoSave(cnt, user_id, ex_seq, inputStream);
        logger.info("[createVideo] cnt : {},userId : {}, exSeq : {},stream : {}"
                ,cnt,user_id,ex_seq,inputStream);

        return "main";
    }

    @GetMapping(value = "/insertPose")
    public ResponseEntity<Map<String, Object>> getVideoinfo(HttpServletRequest req) throws Exception {
        logger.info("insertPose perfom : param =  {}",req.getParameter("videoSeq"));

        if (req.getParameter("videoSeq") == null){
            logger.error("videoSeq : null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Long videoSeq = Long.valueOf(req.getParameter("videoSeq"));
        Map<String,Object> videoInfo = exerciesVideoService.selectVideoInfo(videoSeq);

        logger.info("[response] exinfo : {},postures : {}"
                ,videoInfo.get("exinfo")
                ,videoInfo.get("postures"));
        return ResponseEntity.ok().body(videoInfo);
    }

    @PostMapping(value = "/insertBadImage")
    public void insertBadImage(HttpServletRequest request) throws Exception {
        logger.info("insertBadImage perfom return void");
        String ai_comment = request.getParameter("ai_comment");
        Long ex_seq = Long.valueOf(request.getParameter("ex_seq"));
        logger.info("void ai_comment : {} , ex_seq : {}",ai_comment,ex_seq);

        postruesService.badeImage(ai_comment, ex_seq);
    }


    @PatchMapping("/updateMonth")
    public void updateMonth(HttpServletRequest req){
        logger.info("updateMonth perfom return void [params] userExpireDate : {} ,userId : {}"
        ,req.getParameter("userExpireDate"),req.getParameter("userId"));
        userService.updateMonth(req);
    }

}

















