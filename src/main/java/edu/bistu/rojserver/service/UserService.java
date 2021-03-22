package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.dao.repository.UserRepository;
import edu.bistu.rojserver.domain.RegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@Service
public class UserService
{
    private final UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

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
}
