package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.domain.RegisterForm;
import edu.bistu.rojserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegisterController
{
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping
    public String showRegisterPage(Model model)
    {
        Object registerForm = model.getAttribute("registerForm");
        if(!(registerForm instanceof RegisterForm))
        {
            log.info("creating new registerForm");
            model.addAttribute("registerForm", new RegisterForm());
        }
        return "template_register";
    }

    @PostMapping
    public String processRegisterRequest(@Valid @ModelAttribute("registerForm") RegisterForm registerForm, Errors errors, HttpSession session)
    {
        log.info("processing new register request");
        if(errors.hasErrors())
        {
            log.info("invalid register request");
            for (FieldError error : errors.getFieldErrors())
            {
                log.info("error field name: " + error.getField());
            }
            return "template_register";
        }
        try
        {
            userService.registerNewUser(registerForm);
            log.info("valid register request");
            session.setAttribute("fromRegister", true);
            return "redirect:/login";
        }
        catch (IllegalArgumentException | SQLIntegrityConstraintViolationException e)
        {
            log.info("invalid register request");
            registerForm.setRegisterFailed(true);
            registerForm.setErrorMessage(e.getMessage());
            return "template_register";
        }
    }
}
