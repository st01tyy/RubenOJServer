package edu.bistu.rojserver.domain;

import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Data
@Slf4j
public class TestCaseUploadForm
{
    private Long problemID;
    private MultipartFile inputFile;
    private MultipartFile outputFile;

    public TestCaseEntity convertToEntity() throws IOException
    {
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setInputFileName(inputFile.getOriginalFilename());
        testCaseEntity.setInput(inputFile.getBytes());
        testCaseEntity.setOutputFileName(outputFile.getOriginalFilename());
        testCaseEntity.setOutput(outputFile.getBytes());
        return testCaseEntity;
    }
}
