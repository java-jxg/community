package com.fastcase.community.service;

import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.exception.CustomizeErrorCode;
import com.fastcase.community.exception.CustomizeException;
import com.fastcase.community.mapper.QuestionExMapper;
import com.fastcase.community.mapper.QuestionMapper;
import com.fastcase.community.mapper.UserMapper;
import com.fastcase.community.model.Question;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExMapper questionExMapper;

    @Autowired
    private UserMapper userMapper;

    public PageInfo list(String search, Integer page, Integer size) {

        PageHelper.startPage(page,size);
        List<QuestionDTO> questions = null;
        if(search == null){
            questions = questionExMapper.list();
        }else{
            try {
                questions = questionExMapper.listBySearch(search);
            }catch (Exception e){
                throw new CustomizeException(CustomizeErrorCode.FIND_ERROR);
            }
        }

        PageInfo<Question> pageInfo = new PageInfo(questions,5);
        return pageInfo;
    }
    public PageInfo listByUserId(Long userId,Integer page, Integer size) {

        PageHelper.startPage(page,size);
        List<QuestionDTO> questions = questionExMapper.listByUserId(userId);
        PageInfo<Question> pageInfo = new PageInfo(questions,5);
        return pageInfo;
    }

    public QuestionDTO getById(Long id) {
        QuestionDTO questionDTO = questionExMapper.getById(id);
        if(questionDTO == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            // 更新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.updateByPrimaryKeyWithBLOBs(question);
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
