package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.DownloadFile;
import edu.bistu.rojserver.dao.repository.CheckerFileRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CheckerService
{
    @Resource
    private CheckerFileRepository checkerFileRepository;

    public DownloadFile downloadChecker(Long checkerID)
    {
        return checkerFileRepository.download(checkerID);
    }
}
