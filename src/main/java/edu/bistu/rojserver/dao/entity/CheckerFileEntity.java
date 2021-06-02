package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "checker_file")
public class CheckerFileEntity
{
    @Id
    @Column(name = "checker_id")
    private Long checkerID;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @Column(nullable = false, columnDefinition = "blob")
    private byte[] arr;
}
