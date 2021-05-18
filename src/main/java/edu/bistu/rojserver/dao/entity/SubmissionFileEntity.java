package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "submission_file")
public class SubmissionFileEntity
{
    @Id
    @Column(name = "submission_id")
    private Long submissionID;

    @Column(nullable = false, name = "source_file_name")
    private String sourceFileName;

    @Column(nullable = false, columnDefinition = "blob")
    private byte[] source;  //source file binary
}
