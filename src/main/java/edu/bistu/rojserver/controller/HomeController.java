package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Iterator;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController
{
    @GetMapping
    public String showHomePage(Model model, @AuthenticationPrincipal UserEntity userEntity, HttpSession session)
    {
        if(userEntity != null)
        {
            Object val = session.getAttribute("showWelcome");
            if(val == null)
            {
                session.setAttribute("showWelcome", true);
                model.addAttribute("showWelcome", true);
            }
        }
        else
        {
            Object val = session.getAttribute("showGoodBye");
            if(val != null)
            {
                session.removeAttribute("showGoodBye");
                model.addAttribute("showGoodBye", true);
            }
        }
        return "template_home";
    }
}
