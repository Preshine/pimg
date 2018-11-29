package com.preshine.img.controller;


import com.preshine.img.entity.Resources;
import com.preshine.img.service.IResourcesService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Preshine
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/api/resources")
public class ResourcesController {

    private static final Logger LOGGER = LogManager.getLogger(ResourcesController.class);


    @Autowired
    private IResourcesService resourcesService;

    @RequestMapping(value = "/treeList")
    @ResponseBody
    public List<Map<String, Object>> getAllResources(HttpServletRequest request) {
        List<Resources> resources = resourcesService.selectList(null);
        return resourcesService.getResourcesTreeData(resources);
    }

    @RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addOrEdit(@RequestBody Map<String, Object> requestBody,
                        HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","OPTIONS,GET,POST");        //请求放行
        ModelMap model = new ModelMap();
        Integer resId = (Integer)requestBody.get("resId");
        String parentId = requestBody.get("parentId") + "";
        String name = (String)requestBody.get("name");
        String path = (String)requestBody.get("path");
        String var1 = (String)requestBody.get("var1");
        String var2 = (String)requestBody.get("var2");
        String var3 = (String)requestBody.get("var3");
        String resType = (String)requestBody.get("resType");
        Resources resources;
        String msg;
        if (resId != null) {
            resources = resourcesService.selectById(resId);
            resources.setUpdateTime(new Date());
            msg = "修改资源[" + name + "]成功！";
        } else {
            msg = "新增资源[" + name + "]成功！";
            resources = new Resources();
            resources.setIsDeleted(0);
            resources.setCreateTime(new Date());
        }
        if (parentId != null && !parentId.equals("")) {
            resources.setParentId(Integer.valueOf(parentId));
        } else {
            resources.setParentId(0);
        }
        resources.setPath(path);
        resources.setVar1(var1);
        resources.setVar2(var2);
        resources.setVar3(var3);
        resources.setResType(resType);
        resources.setName(name);
        resourcesService.insertOrUpdate(resources);

        model.put("success", true);
        model.put("message", msg);

        return model;
    }
    @RequestMapping(value = "/addOrEdit1", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addOrEdit1(Integer resId, String parentId, String name, String path, String var1,
                               String var2, String var3, String resType,
                              HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","OPTIONS,GET,POST");        //请求放行
        ModelMap model = new ModelMap();
        Resources resources;
        String msg;
        if (resId != null) {
            resources = resourcesService.selectById(resId);
            resources.setUpdateTime(new Date());
            msg = "修改资源[" + name + "]成功！";
        } else {
            msg = "新增资源[" + name + "]成功！";
            resources = new Resources();
            resources.setIsDeleted(0);
            resources.setCreateTime(new Date());
        }
        if (parentId != null && !parentId.equals("")) {
            resources.setParentId(Integer.valueOf(parentId));
        } else {
            resources.setParentId(0);
        }
        resources.setPath(path);
        resources.setVar1(var1);
        resources.setVar2(var2);
        resources.setVar3(var3);
        resources.setResType(resType);
        resources.setName(name);
        resourcesService.insertOrUpdate(resources);

        model.put("success", true);
        model.put("message", msg);

        return model;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap delete(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer resId = (Integer)requestBody.get("resId");
        resourcesService.deleteById(resId);
        model.put("success", true);
        model.put("message", "删除资源[" + resId + "]成功！");

        return model;
    }

    @RequestMapping(value = "/delete1", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap delete1(Integer resId,
                           HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","OPTIONS,GET,POST");        //请求放行
        ModelMap model = new ModelMap();
        resourcesService.deleteById(resId);
        model.put("success", true);
        model.put("message", "删除资源[" + resId + "]成功！");

        return model;
    }

    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap disable(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        Integer resId = (Integer)requestBody.get("resId");

        Resources resources = resourcesService.selectById(resId);
        resources.setIsDeleted(1);
        resourcesService.updateById(resources);
        model.put("success", true);
        model.put("message", "删除资源[" + resId + "]成功！");

        return model;
    }

}

