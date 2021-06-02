package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.LanguageEntity;
import edu.bistu.rojserver.service.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/management/language")
@Slf4j
public class LanguageManagementController
{
    @Resource
    private LanguageService languageService;

    @GetMapping
    public String showLanguageManagementPage(Model model)
    {
        model.addAttribute("languages", languageService.getAllLanguages());
        return "template_language_management";
    }

    @GetMapping("/add_language")
    public String showAddLanguagePage(Model model)
    {
        model.addAttribute("language", new LanguageEntity());
        return "template_add_language";
    }

    @PostMapping("/add_language")
    public String processNewLanguage(@ModelAttribute("language") LanguageEntity languageEntity, Model model)
    {
        log.info(languageEntity.getName());
        log.info(languageEntity.getCompileCommand());
        log.info(languageEntity.getExecuteCommand());
        String str = languageService.addLanguage(languageEntity);
        if(str == null)
            return "redirect:/management/language";
        model.addAttribute("failed", true);
        model.addAttribute("message", str);
        return "template_add_language";
    }
}
