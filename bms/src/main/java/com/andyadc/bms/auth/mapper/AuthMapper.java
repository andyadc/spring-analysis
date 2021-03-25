package com.andyadc.bms.auth.mapper;

import com.andyadc.bms.auth.entity.AuthMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMapper {

    List<AuthMenu> selectMenuByUserId(Long userId);
}
