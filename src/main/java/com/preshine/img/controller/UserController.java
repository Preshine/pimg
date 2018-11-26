package com.preshine.img.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.preshine.img.config.Logined;
import com.preshine.img.config.SessionUser;
import com.preshine.img.entity.Resources;
import com.preshine.img.entity.User;
import com.preshine.img.entity.UserRole;
import com.preshine.img.service.IUserRoleService;
import com.preshine.img.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
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

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);


    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    @RequestParam(value = "sex", required = false) Integer sex,
                                    @RequestParam(value = "userName", required = false) String userName,
                                    @RequestParam(value = "realName", required = false) String realName,
                                    @RequestParam(value = "mobile", required = false) String mobile,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "sorter", required = false) String sorter,
                                    HttpServletRequest request, HttpServletResponse response) {
        String sorterField = null;
        String sorterOrder = null;
        if (sorter != null && !sorter.equals("")) {
            sorterField = sorter.split("_")[0];
            sorterOrder = sorter.split("_")[1];
        }
        Page<Map<String, Object>> page = new Page<>(current, pageSize);
        page = userService.getUserPage(page, userName, mobile, email, realName, sex, sorterField, sorterOrder);
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
        user.setAvatar("http://www.1lcj.com/static/resources/upload/image/20181122/1542849233354081608.jpg");
        user.setIsDelete(0);
        user.setCreateTime(new Date());
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
        if (roleIds != null && !roleIds.equals("")) {
            List<UserRole> userRoles = Arrays.stream(roleIds.split(",")).map(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserAccount(userId);
                userRole.setRoleId(Integer.valueOf(roleId));
                return userRole;
            }).collect(Collectors.toList());
            userRoleService.insertBatch(userRoles);
        }

        model.put("success", true);
        model.put("message", "用户角色分配成功！");

        return model;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public ModelMap delete(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        String ids = (String)requestBody.get("ids");
//        List<Integer> userIds =Arrays.stream(ids.split(",")).map(userId -> Integer.valueOf(userId)).collect(Collectors.toList());
        User user = new User();
        user.setIsDelete(1);
        userService.update(user, new EntityWrapper<User>().in("id", ids.split(",")));

        model.put("success", true);
        model.put("message", "删除用户成功！");

        return model;
    }

    @Logined
    @RequestMapping(value = "/currentUser")
    @ResponseBody
    public ModelMap currentUser(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        SessionUser sessionUser = (SessionUser)request.getAttribute("sessionUser");

        model.put("success", true);
        model.put("obj", sessionUser);

        return model;
    }

    @Logined
    @RequestMapping(value = "/getMenuData")
    @ResponseBody
    public ModelMap getMenuData(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST");        //请求放行
        ModelMap model = new ModelMap();
        SessionUser sessionUser = (SessionUser)request.getAttribute("sessionUser");
        List<Resources> resources = sessionUser.getUserResources();

        model.put("success", true);
        model.put("obj", resources);

        return model;
    }

}

