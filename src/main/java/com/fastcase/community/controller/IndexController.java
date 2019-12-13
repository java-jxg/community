package com.fastcase.community.controller;

import com.fastcase.community.mapper.UserMapper;
import com.fastcase.community.service.QuestionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        @RequestParam(name="size",defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search) {
        PageInfo questionlist = questionService.list(search,page, size);
        model.addAttribute("questionlist", questionlist);
        model.addAttribute("search", search);
        return "index";
    }

}
