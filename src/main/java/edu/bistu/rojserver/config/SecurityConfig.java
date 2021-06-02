package edu.bistu.rojserver.config;

import edu.bistu.rojserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder)
    {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/register")
                .permitAll()
                .antMatchers("/management/problem")
                .hasAnyAuthority("PROBLEM_AUTHOR", "CONTEST_ORGANIZER", "ROOT")
                .antMatchers("/management/contest")
                .hasAnyAuthority("CONTEST_ORGANIZER", "ROOT")
                .antMatchers("/management/**")
                .hasAnyAuthority("ROOT")
                .and()
                .csrf()
                .disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authentication")
                .defaultSuccessUrl("/").failureUrl("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/post_logout")
                .and()
                .headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web)
    {
        //web.ignoring().antMatchers("/", "/login", "/register");
    }
}
