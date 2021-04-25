package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.entity.LanguageEntity;
import edu.bistu.rojserver.dao.repository.LanguageRepository;
import edu.bistu.rojserver.property.KafkaProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class LanguageService
{
    @Resource
    private LanguageRepository languageRepository;

    @Resource
    private KafkaProperty kafkaProperty;

    public List<LanguageEntity> getAllLanguages()
    {
        return languageRepository.findAll();
    }

    @SuppressWarnings("BusyWait")
    public String addLanguage(LanguageEntity languageEntity)
    {
        if(languageRepository.existsByName(languageEntity.getName()))
            return "数据库中已存在该语言";
        languageEntity = languageRepository.saveAndFlush(languageEntity);
        log.info(String.valueOf(kafkaProperty.isWindows()));
        log.info(String.valueOf(kafkaProperty.getDefaultPartitionNumber()));
        String command;
        if(kafkaProperty.isWindows())
            command = "cmd /C call " + kafkaProperty.getLocation() + "\\kafka-topics.bat";
        else
            command = kafkaProperty.getLocation() + "/kafka-topics.sh";
        command += " --create --topic LANGUAGE-"
                + languageEntity.getLanguageID()
                + " --partitions "
                + kafkaProperty.getDefaultPartitionNumber()
                + " --bootstrap-server "
                + kafkaProperty.getServer();
        log.info("executing command: " + command);
        try
        {
            Process process = Runtime.getRuntime().exec(command);
            while(process.isAlive()){Thread.sleep(500);}
            if(process.exitValue() == 0)
                return null;
            else
            {
                languageRepository.delete(languageEntity);
                return "创建topic失败";
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
            languageRepository.delete(languageEntity);
            return "创建topic失败";
        }
    }
}
