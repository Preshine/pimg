package com.preshine.img.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.preshine.img.entity.User;
import com.preshine.img.entity.UserRole;
import com.preshine.img.service.IUserRoleService;
import com.preshine.img.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Preshine
 * @since 2018-11-13
 */
@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> list(HttpServletRequest request, HttpServletResponse response) {
        Page<Map<String, Object>> page = new Page<>(1, 10);
        page = userService.getUserPage(page);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total", page.getTotal());
        pagination.put("pageSize", page.getSize());
        pagination.put("current", page.getCurrent());

        result.put("list", page.getRecords());
        result.put("pagination", pagination);
        return result;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ModelMap add(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        String userName = (String)requestBody.get("userName");
        String mobile = (String)requestBody.get("mobile");
        String email = (String)requestBody.get("email");
        Integer sex = (Integer)requestBody.get("sex");
        String remark = (String)requestBody.get("remark");
        User user = new User();
        user.setMobile(mobile);
        user.setEmail(email);
        user.setUserName(userName);
        user.setSex(sex);
        user.setIsDelete(0);
        userService.insert(user);

        model.put("success", true);
        model.put("message", "新增用户[" + userName + "]成功！");

        return model;
    }

    @RequestMapping(value = "/saveUserRole")
    @ResponseBody
    public ModelMap saveUserRole(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer userId = (Integer)requestBody.get("userId");
        String roleIds = (String)requestBody.get("roleIds");
        User user = userService.selectById(userId);
        userRoleService.delete(new EntityWrapper<UserRole>().where("user_account={0}", userId));
        List<UserRole> userRoles =Arrays.stream(roleIds.split(",")).map(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserAccount(userId);
            userRole.setRoleId(Integer.valueOf(roleId));
            return userRole;
        }).collect(Collectors.toList());
        userRoleService.insertBatch(userRoles);

        model.put("success", true);
        model.put("message", "用户角色分配成功！");

        return model;
    }

}

