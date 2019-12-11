package com.fastcase.community.controller;

import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.mapper.QuestionMapper;
import com.fastcase.community.mapper.UserMapper;
import com.fastcase.community.model.Question;
import com.fastcase.community.model.User;
import com.fastcase.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model){
        QuestionDTO question = new QuestionDTO();
        question.setTitle("");
        question.setDescription("");
        question.setTag("");
        model.addAttribute("question",question);
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        System.out.println(id);
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("question",question);
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question,
            HttpServletRequest request,
            Model model) {
        if(question != null){
            model.addAttribute("question",question);
        }
        if (question.getTitle() == null || question.getTitle() == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (question.getDescription() == null || question.getDescription() == "") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (question.getTag() == null || question.getTag() == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        questionService.createOrUpdate(question);
        return "redirect:/";
    }


}
