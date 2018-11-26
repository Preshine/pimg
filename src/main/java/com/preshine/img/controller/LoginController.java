package com.preshine.img.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.preshine.img.config.LoginAspect;
import com.preshine.img.config.SessionUser;
import com.preshine.img.entity.*;
import com.preshine.img.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
@RequestMapping("/api/login")
public class LoginController {

    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);


    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleResService roleResService;

    @Autowired
    private IResourcesService resourcesService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap login(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST");        //请求放行
        ModelMap model = new ModelMap();
        String mobile = (String)requestBody.get("mobile");
        String password = (String)requestBody.get("password");
        if (mobile == null || mobile.equals("")) {
            model.put("success", false);
            model.put("message", "手机号不能为空");
        }
        if (password == null || password.equals("")) {
            model.put("success", false);
            model.put("message", "密码不能为空");
        }
        User user = userService.selectOne(new EntityWrapper<User>()
                .where("is_delete=0")
                .and("mobile={0} and password={1}", mobile, password));
        if (user == null) {
            model.put("success", false);
            model.put("message", "手机号或密码错误");
            return model;
        }


        Map<String, Object> result = new HashMap<>();
        List<String> currentAuthority = null;
        Map<String, String> obj = new HashMap<>();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        result.put("token", token);

        SessionUser sessionUser = new SessionUser();
        sessionUser.setToken(token);
        sessionUser.setAccountId(user.getId());
        sessionUser.setUserAccount(user.getUserAccount());
        sessionUser.setUserName(user.getUserName());
        sessionUser.setAvatar(user.getAvatar());
        sessionUser.setMobile(user.getMobile());
        sessionUser.setEmail(user.getEmail());

        List<UserRole> userRoles = userRoleService.selectList(new EntityWrapper<UserRole>()
                .where("user_account={0}", user.getId()));

        if (userRoles != null && userRoles.size() > 0) {
            List<Integer> roleIds = userRoles.parallelStream().map(r -> r.getRoleId()).collect(Collectors.toList());
            List<Role> roles = roleService.selectList(new EntityWrapper<Role>().in("id", roleIds));
            List<RoleRes> roleRes = roleResService.selectList(new EntityWrapper<RoleRes>().in("role_id",roleIds));
            List<Integer> resIds = roleRes.parallelStream().map(r -> r.getResId()).collect(Collectors.toList());
            List<Resources> resources = resourcesService.selectList(new EntityWrapper<Resources>().in("id", resIds));
            sessionUser.setUserRoles(roles);
            sessionUser.setUserResources(resources);

        }
        String userInfo = JSON.toJSONString(sessionUser);
        obj.put("userInfo", userInfo);
        LoginAspect.cache.put(token, obj);

        if (sessionUser.getUserResources() != null) {
            currentAuthority = sessionUser.getUserResources().parallelStream().map(r -> r.getPath()).collect(Collectors.toList());
            result.put("currentAuthority", currentAuthority);
        }

        model.put("success", true);
        model.put("message", "登录成功！");
        model.put("obj", result);
        return model;
    }

    @RequestMapping(value = "/login1", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap login1(String mobile, String password,
                          HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST");        //请求放行
        ModelMap model = new ModelMap();
        if (mobile == null || mobile.equals("")) {
            model.put("success", false);
            model.put("message", "手机号不能为空");
        }
        if (password == null || password.equals("")) {
            model.put("success", false);
            model.put("message", "密码不能为空");
        }
        User user = userService.selectOne(new EntityWrapper<User>()
                .where("is_delete=0")
                .and("mobile={0} and password={1}", mobile, password));
        if (user == null) {
            model.put("success", false);
            model.put("message", "手机号或密码错误");
            return model;
        }


        Map<String, Object> result = new HashMap<>();
        List<String> currentAuthority = null;
        Map<String, String> obj = new HashMap<>();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        result.put("token", token);

        SessionUser sessionUser = new SessionUser();
        sessionUser.setToken(token);
        sessionUser.setAccountId(user.getId());
        sessionUser.setUserAccount(user.getUserAccount());
        sessionUser.setUserName(user.getUserName());
        sessionUser.setAvatar(user.getAvatar());
        sessionUser.setMobile(user.getMobile());
        sessionUser.setEmail(user.getEmail());

        List<UserRole> userRoles = userRoleService.selectList(new EntityWrapper<UserRole>()
                .where("user_account={0}", user.getId()));

        if (userRoles != null && userRoles.size() > 0) {
            List<Integer> roleIds = userRoles.parallelStream().map(r -> r.getRoleId()).collect(Collectors.toList());
            List<Role> roles = roleService.selectList(new EntityWrapper<Role>().in("id", roleIds));
            List<RoleRes> roleRes = roleResService.selectList(new EntityWrapper<RoleRes>().in("role_id",roleIds));
            List<Integer> resIds = roleRes.parallelStream().map(r -> r.getResId()).collect(Collectors.toList());
            List<Resources> resources = resourcesService.selectList(new EntityWrapper<Resources>().in("id", resIds));
            sessionUser.setUserRoles(roles);
            sessionUser.setUserResources(resources);

        }
        String userInfo = JSON.toJSONString(sessionUser);
        obj.put("userInfo", userInfo);
        LoginAspect.cache.put(token, obj);

        if (sessionUser.getUserResources() != null) {
            currentAuthority = sessionUser.getUserResources().parallelStream().map(r -> r.getPath()).collect(Collectors.toList());
            result.put("currentAuthority", currentAuthority);
        }

        model.put("success", true);
        model.put("message", "登录成功！");
        model.put("obj", result);
        return model;
    }



}

