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
public class UserEntity implements UserDetails
{
    public enum Role
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;

    @Column(unique = true, length = 16)
    private String username;    //限长8-16位

    @Column(nullable = false)
    private String password;    //PasswordEncoder加密

    @Column(nullable = false)
    private Role role = Role.USER;  //default value = Role.USER

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