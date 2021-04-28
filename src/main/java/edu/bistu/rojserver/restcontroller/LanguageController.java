package edu.bistu.rojserver.restcontroller;

import edu.bistu.rojserver.domain.jsonmodel.Language;
import edu.bistu.rojserver.domain.jsonmodel.LanguageVerifyRequest;
import edu.bistu.rojserver.domain.jsonmodel.LanguageVerifyResult;
import edu.bistu.rojserver.service.LanguageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/verify_languages")
@CrossOrigin(origins = "*")
public class LanguageController
{
    @Resource
    private LanguageService languageService;

    @PostMapping
    public LanguageVerifyResult verifyLanguages(@RequestBody LanguageVerifyRequest request)
    {
        List<Language> languageList = languageService.getLanguagesByNames(request.getLanguageNameList());
        return new LanguageVerifyResult(languageList);
    }
}
