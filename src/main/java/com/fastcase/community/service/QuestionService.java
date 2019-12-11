package com.fastcase.community.service;

import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.mapper.QuestionMapper;
import com.fastcase.community.mapper.UserMapper;
import com.fastcase.community.model.Question;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        PageInfo<Question> pageInfo = new PageInfo(questions,5);
        return pageInfo;
    }
    public PageInfo listByUserId(Integer userId,Integer page, Integer size) {

        PageHelper.startPage(page,size);
        List<QuestionDTO> questions = questionMapper.listByUserId(userId);
        PageInfo<Question> pageInfo = new PageInfo(questions,5);
        return pageInfo;
    }

    public QuestionDTO getById(Integer id) {
        QuestionDTO questionDTO = questionMapper.getById(id);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        } else {
            // 更新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }
}
