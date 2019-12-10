package com.fastcase.community.service;

import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.mapper.QuestionMapper;
import com.fastcase.community.mapper.UserMapper;
import com.fastcase.community.model.Question;
import com.fastcase.community.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PageInfo list(Integer page, Integer size) {

        PageHelper.startPage(page,size);
        List<QuestionDTO> questions = questionMapper.list();
/*

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }*/
        PageInfo<Question> pageInfo = new PageInfo(questions,5);
        return pageInfo;
    }
}
