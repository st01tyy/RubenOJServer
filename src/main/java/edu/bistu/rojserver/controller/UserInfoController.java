package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.UserEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user_info")
public class UserInfoController
{
    @GetMapping
    public String showUserInfoPage(@AuthenticationPrincipal UserEntity userEntity, Model model)
    {
        model.addAttribute("role", userEntity.getRole().name());
        return "template_user_info";
    }
}
