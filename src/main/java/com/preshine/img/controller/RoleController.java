package com.preshine.img.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.preshine.img.entity.*;
import com.preshine.img.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private IResourcesService resourcesService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRoleResService roleResService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Role> list(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        return roleService.selectList(null);
    }

    @RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addOrEdit(@RequestBody Map<String, Object> requestBody,
                              HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer roleId = (Integer)requestBody.get("roleId");
        String name = (String)requestBody.get("name");
        Role role;
        String msg;
        if (roleId != null) {
            role = roleService.selectById(roleId);
            msg = "修改角色[" + name + "]成功！";
        } else {
            msg = "新增角色[" + name + "]成功！";
            role = new Role();
            role.setCreateTime(new Date());
            role.setStatus(0);
        }
        role.setName(name);
        roleService.insertOrUpdate(role);
        model.put("success", true);
        model.put("message", msg);

        return model;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap delete(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer roleId = (Integer)requestBody.get("roleId");
        roleService.deleteById(roleId);
        model.put("success", true);
        model.put("message", "删除角色[" + roleId + "]成功！");

        return model;
    }

    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap disable(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer roleId = (Integer)requestBody.get("roleId");
        Role role = roleService.selectById(roleId);
        role.setStatus(1);
        roleService.updateById(role);
        model.put("success", true);
        model.put("message", "删除角色[" + roleId + "]成功！");

        return model;
    }

    @RequestMapping(value = "/getUsersByRole")
    @ResponseBody
    public List<User> getUsersByRole(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        Integer roleId = (Integer)requestBody.get("roleId");
        List<UserRole> userRoles =  userRoleService.selectList(new EntityWrapper<UserRole>()
                .where("role_id={0}", roleId));

        List<Integer> userAccounts = userRoles.parallelStream().map(u -> u.getUserAccount()).collect(Collectors.toList());

        List<User> users = userService.selectList(new EntityWrapper<User>()
                .in("user_account", userAccounts));

        return users;
    }

    @RequestMapping(value = "/getResTreeList")
    @ResponseBody
    public List<Map<String, Object>> getResTreeList(@RequestBody Map<String, Object> requestBody,
                                                    HttpServletRequest request) {
        Integer roleId = (Integer)requestBody.get("roleId");

        List<RoleRes> roleRes = roleResService.selectList(new EntityWrapper<RoleRes>().where("role_id={0}", roleId));
        List<Integer> resIds = roleRes.parallelStream().map(r -> r.getResId()).collect(Collectors.toList());

        List<Resources> resources = resourcesService.selectList(new EntityWrapper<Resources>().in("id", resIds));
        return resourcesService.getResourcesTreeData(resources);
    }


}

