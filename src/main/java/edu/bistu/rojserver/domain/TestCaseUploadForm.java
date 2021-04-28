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
    private MultipartFile input;
    private MultipartFile output;

    public TestCaseEntity convertToEntity() throws IOException
    {
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setInputFileName(input.getOriginalFilename());
        testCaseEntity.setInput(input.getBytes());
        testCaseEntity.setOutputFileName(output.getOriginalFilename());
        testCaseEntity.setOutput(output.getBytes());
        return testCaseEntity;
    }
}
