package edu.bistu.rojserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitForm
{
    private Long problemID;
    private MultipartFile sourceFile;
    private String language;
}
