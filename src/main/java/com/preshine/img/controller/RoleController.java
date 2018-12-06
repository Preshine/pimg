package com.preshine.img.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.preshine.img.entity.Resources;
import com.preshine.img.entity.Role;
import com.preshine.img.entity.RoleRes;
import com.preshine.img.entity.UserRole;
import com.preshine.img.service.*;
import com.preshine.img.util.Regex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/role")
public class RoleController {

    private static final Logger LOGGER = LogManager.getLogger(RoleController.class);


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
    public List<Role> list(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","OPTIONS,GET,POST");        //请求放行
        return roleService.selectList(null);
    }

    @RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addOrEdit(@RequestBody Map<String, Object> requestBody,
                              HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer roleId = (Integer)requestBody.get("roleId");
        String name = (String)requestBody.get("name");
        String description = (String)requestBody.get("description");
        Role role;
        String msg;
        if (roleId != null) {
            role = roleService.selectById(roleId);
            msg = "修改角色[" + name + "]成功！";
        } else {
            msg = "新增角色[" + name + "]成功！";
            role = new Role();
            role.setCreateTime(new Date());
            role.setStatus(1);
        }
        role.setName(name);
        role.setDescription(description);
        roleService.insertOrUpdate(role);
        model.put("success", true);
        model.put("message", msg);

        return model;
    }

    @RequestMapping(value = "/addOrEdit1", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addOrEdit1(Integer roleId, String name, String description,
                              HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","OPTIONS,GET,POST");        //请求放行
        ModelMap model = new ModelMap();
        Role role;
        String msg;
        if (roleId != null) {
            role = roleService.selectById(roleId);
            msg = "修改角色[" + name + "]成功！";
        } else {
            msg = "新增角色[" + name + "]成功！";
            role = new Role();
            role.setCreateTime(new Date());
            role.setStatus(1);
        }
        role.setName(name);
        role.setDescription(description);
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

    @RequestMapping(value = "/handleStatus", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap handleStatus(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer roleId = (Integer)requestBody.get("roleId");
        Integer status = (Integer)requestBody.get("status");
        Role role = roleService.selectById(roleId);
        role.setStatus(status);
        roleService.updateById(role);
        model.put("success", true);
        model.put("message", "禁用角色[" + roleId + "]成功！");

        return model;
    }

    @RequestMapping(value = "/getUsersByrole")
    @ResponseBody
    public Map<String, Object> getUsersByRole(Integer roleId,
                                              @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                              @RequestParam(required = false) String mobileOrUserName,
                           HttpServletRequest request, HttpServletResponse response) {
        List<UserRole> userRoles =  userRoleService.selectList(new EntityWrapper<UserRole>()
                .where("role_id={0}", roleId));

        List<Integer> userAccounts = userRoles.parallelStream().map(u -> u.getUserAccount()).collect(Collectors.toList());

        String mobile = null, userName = null;
        if (mobileOrUserName != null && !mobileOrUserName.equals("")) {
            if (Regex.isMobile(mobileOrUserName)) {
//                mobile = userName =  mobileOrUserName;
                mobile =  mobileOrUserName;
            } else {
                userName = mobileOrUserName;
            }
        }
        Page<Map<String, Object>> page = new Page<>(current, pageSize);
        if (userAccounts != null && userAccounts.size() > 0) {
            page = userService.getUsersByRole(page, userName, mobile, userAccounts);
        }
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total", page.getTotal());
        pagination.put("pageSize", page.getSize());
        pagination.put("current", page.getCurrent());

        result.put("list", page.getRecords());
        result.put("pagination", pagination);

        return result;
    }

    @RequestMapping(value = "/getResTreeList")
    @ResponseBody
    public List<Map<String, Object>> getResTreeList(Integer roleId,
                                                    HttpServletRequest request) {

        List<RoleRes> roleRes = roleResService.selectList(new EntityWrapper<RoleRes>().where("role_id={0}", roleId));
        List<Integer> resIds = roleRes.parallelStream().map(r -> r.getResId()).collect(Collectors.toList());

        List<Resources> resources = resourcesService.selectList(new EntityWrapper<Resources>().in("id", resIds));
        return resourcesService.getResourcesTreeData1(resources);
    }

    @RequestMapping(value = "/getResByRoleId")
    @ResponseBody
    public Map<String, Object> getResByRoleId(Integer roleId,
                                                    HttpServletRequest request) {
        List<RoleRes> roleRes = roleResService.selectList(new EntityWrapper<RoleRes>().where("role_id={0}", roleId));
        List<Integer> resIds = roleRes.parallelStream().map(r -> r.getResId()).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("checked", resIds);
        result.put("halfChecked", Collections.EMPTY_LIST);

        return result;
    }

    @RequestMapping(value = "/addorEditRoleRes")
    @ResponseBody
    public Map<String, Object> addorEditRoleRes(@RequestBody Map<String, Object> requestBody,
                                              HttpServletRequest request) {
        Integer roleId = (Integer)requestBody.get("roleId");
        String res = (String)requestBody.get("res");

        List<RoleRes> roleRess = Arrays.stream(res.split(",")).map(r -> {
            RoleRes roleRes = new RoleRes();
            roleRes.setRoleId(roleId);
            roleRes.setResId(Integer.valueOf(r));
            return roleRes;
        }).collect(Collectors.toList());

        roleResService.delete(new EntityWrapper<RoleRes>().where("role_id={0}", roleId));
        roleResService.insertBatch(roleRess);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "分配资源成功");

        return result;
    }


}

