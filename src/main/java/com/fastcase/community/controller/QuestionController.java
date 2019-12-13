package com.fastcase.community.controller;

import com.fastcase.community.Enum.CommentTypeEnum;
import com.fastcase.community.dto.CommentExDTO;
import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.service.CommentService;
import com.fastcase.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentExDTO> comments = commentService.listByQuestionId(id, CommentTypeEnum.QUESTION);
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
