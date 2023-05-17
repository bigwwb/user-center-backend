package com.wwb.center.mapper;

import com.wwb.center.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author wwb
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-05-16 16:45:17
* @Entity com.wwb.center.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 用户所有信息
     */
    User selectByUsernameAndPassword(@Param("userAccount") String userAccount, @Param("userPassword") String userPassword);

}




