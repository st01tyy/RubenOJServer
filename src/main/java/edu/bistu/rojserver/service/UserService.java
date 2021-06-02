package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.dao.repository.UserRepository;
import edu.bistu.rojserver.domain.RegisterForm;
import edu.bistu.rojserver.property.WebServerProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService
{
    private final UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private WebServerProperty webServerProperty;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void registerNewUser(RegisterForm registerForm) throws IllegalArgumentException, SQLIntegrityConstraintViolationException
    {
        if(!registerForm.getPassword().equals(registerForm.getPasswordConfirm()))
            throw new IllegalArgumentException("两次密码输入不一致");
        if(userRepository.existsByUsername(registerForm.getUsername()))
            throw new SQLIntegrityConstraintViolationException("用户名已存在");
        UserEntity userEntity = createUserEntity(registerForm.getUsername(), registerForm.getPassword());
        UserEntity res = userRepository.save(userEntity);
        log.info("user " + res.getUsername() + "registered successfully");
    }

    private UserEntity createUserEntity(String validatedUsername, String validatedPassword)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(validatedUsername);
        userEntity.setPassword(passwordEncoder.encode(validatedPassword));
        return userEntity;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        log.info("loadUserByUsername(" + s + ")");
        Optional<UserEntity> result = userRepository.findUserEntityByUsername(s);
        if(result.isEmpty())
        {
            log.info("result is empty");
            throw new UsernameNotFoundException("未找到名为：" + s + "的用户");
        }
        return result.get();
    }

    public Slice<UserEntity> getUserList(int page, String roleName, String username)
    {
        Pageable pageable = PageRequest.of(page, webServerProperty.getPage_size());
        if(roleName != null)
            return userRepository.findAllByRoleEquals(pageable, UserEntity.Role.valueOf(roleName));
        if(username != null)
            return userRepository.findAllByUsernameLike(pageable, username + "%");
        return userRepository.findUsers(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeUserRole(String username, String roleName) throws Exception
    {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElse(null);
        if(userEntity == null)
            return;
        UserEntity.Role role = UserEntity.Role.valueOf(roleName);
        userEntity.setRole(role);
        userRepository.saveAndFlush(userEntity);
    }
}
