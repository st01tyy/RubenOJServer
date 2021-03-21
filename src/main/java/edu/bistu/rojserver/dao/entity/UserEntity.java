package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "user_information")
@Table(indexes = @Index(columnList = "username, password"))
public class UserEntity
{
    enum Role
    {
        /**
         * USER = 做题，参赛
         * PROBLEM_AUTHOR = USER + 出题
         * CONTEST_ORGANIZER = PROBLEM_AUTHOR + 组织比赛
         * ROOT = CONTEST_ORGANIZER + 管理用户权限
         */

        USER, PROBLEM_AUTHOR, CONTEST_ORGANIZER, ROOT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;

    @Column(unique = true, length = 16, nullable = false)
    private String username;    //限长8-16位

    @Column(nullable = false)
    private String password;    //MD5加密

    @Column(nullable = false)
    private Role role;  //default value = Role.USER

    public UserEntity()
    {
        role = Role.USER;
    }
}
