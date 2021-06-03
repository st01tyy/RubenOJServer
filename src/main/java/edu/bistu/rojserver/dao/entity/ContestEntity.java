package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "contest")
public class ContestEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id")
    private Long contestID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "start_time")
    private String startTime;

    @Column(nullable = false, name = "end_time")
    private String endTime;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private UserEntity userEntity;
}
