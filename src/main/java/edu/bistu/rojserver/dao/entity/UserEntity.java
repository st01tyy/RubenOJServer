package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity(name = "user_information")
@Table(indexes = @Index(columnList = "username, password"))
public class UserEntity implements UserDetails
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

    @Column(length = 16, nullable = false)
    private String username;    //限长8-16位

    @Column(nullable = false)
    private String password;    //PasswordEncoder加密

    @Column(nullable = false)
    private Role role;  //default value = Role.USER

    public UserEntity()
    {
        role = Role.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
