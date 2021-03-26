package edu.bistu.rojserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController
{
    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session)
    {
        Object val = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if(val != null)
            model.addAttribute("loginFailed", true);
        return "template_login";
    }
}
