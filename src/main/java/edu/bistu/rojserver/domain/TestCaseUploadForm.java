package edu.bistu.rojserver.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Data
@Slf4j
public class TestCaseUploadForm
{
    private Long problemID;
    private MultipartFile inputFile;
    private MultipartFile outputFile;
}
