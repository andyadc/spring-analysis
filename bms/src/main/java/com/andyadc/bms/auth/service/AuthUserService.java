package com.andyadc.bms.auth.service;

import com.andyadc.bms.auth.dto.AuthUserDTO;
import com.andyadc.bms.auth.entity.AuthMenu;
import com.andyadc.bms.auth.entity.AuthUser;
import com.andyadc.bms.auth.mapper.AuthMapper;
import com.andyadc.bms.auth.mapper.AuthUserMapper;
import com.andyadc.bms.security.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthUserService {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserService.class);
    private AuthMapper authMapper;
    private AuthUserMapper userMapper;
    private PasswordService passwordService;

    public AuthUserDTO findByUsername(String username) {
        AuthUser authUser = userMapper.findByUsername(username);
        if (authUser == null) {
            logger.error("Cannot find auth user, username - [{}]", username);
            return null;
        }

        AuthUserDTO dto = new AuthUserDTO();
        List<AuthMenu> menus = authMapper.selectMenuByUserId(authUser.getId());
        List<String> permissionList = menus.stream().map(AuthMenu::getPermission).collect(Collectors.toList());
        dto.setId(authUser.getId());
        dto.setUsername(username);
        dto.setPassword(authUser.getPassword());
        dto.setAuthorities(permissionList);
        return dto;
    }

    public void register(AuthUserDTO dto) {
        String password = passwordService.encode(dto.getPassword());
        AuthUser authUser = new AuthUser();
        authUser.setPassword(password);
        authUser.setUsername(dto.getUsername());
        authUser.setStatus(1);
        authUser.setDeleted(0);
        authUser.setVersion(1);
        authUser.setCreateTime(LocalDateTime.now());
        authUser.setUpdateTime(LocalDateTime.now());
        int result = authMapper.insertUserSelective(authUser);
    }

    @Autowired
    public void setAuthMapper(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @Autowired
    public void setUserMapper(AuthUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
}
