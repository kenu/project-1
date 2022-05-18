package com.example.project_2th.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.project_2th.entity.User;
import com.example.project_2th.entity.UserExercieVideos;
import com.example.project_2th.entity.UserExercies;
import com.example.project_2th.entity.UserPostures;
import com.example.project_2th.repository.DeepPosturesRepository;
import com.example.project_2th.repository.ExinfoRepository;
import com.example.project_2th.repository.GuestRepository;
import com.example.project_2th.repository.UserVideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class Restmember {
    @Autowired
    private final GuestRepository guestRepository;

    @Autowired
    private final ExinfoRepository exinfoRepository;

    @Autowired
    private final UserVideoRepository userVideoRepository;

    @Autowired
    private final DeepPosturesRepository deepPosturesRepository;

    @GetMapping(value = "/calendarView")
    public List<UserExercies> calendarView(String userId, Date exDay, HttpServletRequest req, HttpServletResponse res) throws Exception {

        List<UserExercies> result = guestRepository
                .findByUserId(Long.valueOf(userId))
                .getUserExerciesList();

        List<UserExercies> exinfoList = null;
        Date day = null;
        for(int i = 0; i < result.size(); i++){
            day = result.get(i).getExDay();
            if (day.equals(exDay)) {
                System.out.println(day);
                day = exDay;
                break;
            }
        }

        exinfoList = exinfoRepository.findByExDay(day);


        return exinfoList;

    }
    @GetMapping(value = "insertExURL")
    public String inertURL(HttpServletRequest req)throws Exception {
        System.out.println("저장할려는중");
        //System.out.println(request.getParameter("cnt"));
        String cnt = req.getParameter("cnt");
        Long user_id = Long.valueOf(req.getParameter("userId"));
        Long ex_seq = Long.valueOf(req.getParameter("exSeq"));

        User user = guestRepository.findByUserId(user_id);
        UserExercies userExercies = exinfoRepository.findByExSeq(ex_seq);

        ServletInputStream input = req.getInputStream();



        double randomValue = Math.random();
        String file_name = Double.toString((randomValue * 100) + 1);
        FileOutputStream out = new FileOutputStream(new File("C:\\user\\projectVideo\\" + file_name + ".webm"));


        byte[] charBuffer = new byte[128];

        int bytesRead = -1;
        while ((bytesRead = input.read(charBuffer)) > 0) {
            //System.out.println("저장중");
            out.write(charBuffer, 0, bytesRead);
        }

        input.close();
        out.close();

        System.out.println("저장 끝");

        // video 파일 저장
        UserExercieVideos userExercieVideos = new UserExercieVideos();
        userExercieVideos.setUser(user);
        userExercieVideos.setFileName(file_name);
        userExercieVideos.setUserExercies(userExercies);

        userVideoRepository.save(userExercieVideos);

        // cnt 데이터 update
        userExercies.setCnt(cnt);
        exinfoRepository.save(userExercies);

        //exinfoRepository.inertCNT(cnt , ex_seq);
        return "main";
    }


    @RequestMapping(value="/insertBadImage.do", method= {RequestMethod.GET, RequestMethod.POST})
    public void insertBadImage(HttpServletRequest request) throws Exception {

        //String fileName = file.getOriginalFilename();
        //System.out.print(request.getParameter("data"));
        System.out.println(request.getParameter("ai_comment"));

        String ai_comment =  request.getParameter("ai_comment");

        Long ex_seq = Long.valueOf(request.getParameter("ex_seq"));

        UserExercies userExercies = exinfoRepository.findByExSeq(ex_seq);

        UserExercieVideos result = userVideoRepository.findByUserExercies(userExercies);
        //ServletInputStream input = request.getInputStream();


        double randomValue = Math.random();
        String pose_result = Double.toString((randomValue*100)+1);
        FileOutputStream out = new FileOutputStream(new File("C:\\user\\badImage\\"+pose_result+".jpg"));

        byte[] charBuffer = new byte[128];

        int bytesRead = 0;
        while ((bytesRead = System.in.read()) != -1) {
            //System.out.println("저장중");
            out.write(bytesRead);
        }

        UserPostures userPostures = new UserPostures();
        userPostures.setPose_result(pose_result);
        userPostures.setAi_comment(ai_comment);
        userPostures.setUserExercieVideos(result);

        deepPosturesRepository.save(userPostures);
    }

}

















