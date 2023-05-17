package com.wwb.center.service;
import java.util.Date;

import com.wwb.center.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    public void testAddUser(){
        User user = new User();
        user.setId(0L);
        user.setUsername("wwb");
        user.setUserAccount("root");
        user.setAvatarUrl("https://blog.csdn.net/m0_60824353");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123456");
        user.setEmail("123456");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
//        校验非空
        String userAccount = "wuwenbiao";
        String userPassword = "";
        String checkPassword = "12345678";
        String planetCode = "123";
        Long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
//        2.校验长度
        userAccount = "wwb";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
//        3.校验密码
        userPassword = "123456";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
//        4.是否含特殊字符
        userAccount = "#wuwenbiao";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
//        5.校验密码是否正确
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
//        6.存数据
        userAccount = "wuwenbiao";
        userPassword ="12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        User user = new User();
        user.setUserPassword(userPassword);
        user.setUserAccount(userAccount);
        boolean save = userService.save(user);
        System.out.println(save);
        Assertions.assertTrue(result > 0);
    }
}