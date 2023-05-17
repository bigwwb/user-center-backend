package com.wwb.center.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwb.center.common.BaseResponse;
import com.wwb.center.common.ErrorCode;
import com.wwb.center.common.ResultUtils;
import com.wwb.center.domain.User;
import com.wwb.center.domain.request.UserLoginRequest;
import com.wwb.center.domain.request.UserRegisterRequest;
import com.wwb.center.exception.BusinessException;
import com.wwb.center.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.wwb.center.constant.UserConstant.ADMIN_ROLE;
import static com.wwb.center.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author wwb
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR ,"请求参数中存在空值");
        }
        long res = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.ok(res);
    }
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数中存在空值");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.ok(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        int res = userService.userLogout(request);
        return ResultUtils.ok(res);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User curUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (curUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录");
        }
        //用户信息在session存活期间变化 所以获取确认存活后再查询一遍
        User user = userService.getById(curUser.getId());
        User safeUser = userService.getSafetyUser(user);
        return ResultUtils.ok(safeUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> userList(String username, HttpServletRequest request) {
        //仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只允许管理员操作");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        //可选择针对字段查询,重新写一条sql语句
        List<User> userList = userService.list(queryWrapper);
        //脱敏
        List<User> safeUserList = userList.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.ok(safeUserList);
    }

    @DeleteMapping("{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        //仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只允许管理员操作");
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id不存在");
        }
        boolean res = userService.removeById(id);
        return ResultUtils.ok(res);
    }

    /**
     * 判定是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
