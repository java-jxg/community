package com.fastcase.community.controller;

import com.fastcase.community.dto.AccessTokenDTO;
import com.fastcase.community.dto.GithubUser;
import com.fastcase.community.mapper.UserMapper;
import com.fastcase.community.model.User;
import com.fastcase.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    public String clientId;

    @Value("${github.client.secret}")
    public String clientSecret;

    @Value("${github.redirect.url}")
    public String clientUrl;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_url(clientUrl);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUseruser = githubProvider.getUser(accessToken);
        if(githubUseruser != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUseruser.getName());
            user.setAccountId(String.valueOf(githubUseruser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);

            request.getSession().setAttribute("user",githubUseruser);
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }
}
