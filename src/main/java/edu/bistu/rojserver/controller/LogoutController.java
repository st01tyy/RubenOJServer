package edu.bistu.rojserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LogoutController
{
    @GetMapping("/post_logout")
    public String prepareLogout(HttpSession session)
    {
        session.setAttribute("showGoodBye", true);
        return "redirect:/";
    }
}
