package edu.bistu.rojserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user_info")
public class UserInfoController
{
    @GetMapping
    public String showUserInfoPage()
    {
        return "template_user_info";
    }
}
