package com.preshine.img.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.preshine.img.entity.Resources;
import com.preshine.img.mapper.ResourcesMapper;
import com.preshine.img.service.IResourcesService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Preshine
 * @since 2018-11-12
 */
@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources> implements IResourcesService {

    @Override
    public List<Map<String, Object>> getResourcesTreeData(List<Resources> resources) {
        return treeData(resources);
    }

    private List<Map<String, Object>> treeData(List<Resources> resources) {
        List<Map<String, Object>> treeData = new ArrayList<>();
        if (resources != null && resources.size() > 0) {
            List<Map<String, Object>> result = resources.parallelStream()
                    .map( res -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", res.getId());
                        map.put("parentId", res.getParentId());
                        map.put("name", res.getName());
                        map.put("text", res.getName());
                        map.put("key", res.getId().toString());
                        map.put("value", res.getId().toString());
                        map.put("label", res.getName());
                        map.put("resType", res.getResType());
                        map.put("var1", res.getVar1());
                        map.put("var2", res.getVar2());
                        map.put("var3", res.getVar3());
                        return map;
                    }).collect(Collectors.toList());

            getTreeData(0, result, treeData);
        }
        return treeData;
    }

    private List<Map<String, Object>> getTreeData(Integer parentId, List<Map<String, Object>> resources, List<Map<String, Object>> treeData) {

        return resources.stream()
                .filter( res -> (parentId == null && res.get("parentId") == null) || ( parentId != null && parentId.equals(res.get("parentId"))))
                .map( res -> {
                    List<Map<String, Object>> childrenList = resources.stream()
                            .filter( child -> res.get("id").equals(child.get("parentId")))
                            .collect(Collectors.toList());
                    if (childrenList.size() > 0) {
                        Map<String, Object> childMap = new HashMap<>();
                        if (parentId == 0) {
                            treeData.add(childMap);
                        }
                        childMap.put("id", res.get("id"));
                        childMap.put("parentId", res.get("parentId"));
                        childMap.put("name", res.get("name"));
                        childMap.put("text", res.get("text"));
                        childMap.put("key", res.get("key"));
                        childMap.put("value", res.get("value"));
                        childMap.put("label", res.get("label"));
                        childMap.put("resType", res.get("resType"));
                        childMap.put("var1", res.get("var1"));
                        childMap.put("var2", res.get("var2"));
                        childMap.put("var3", res.get("var3"));
                        childMap.put("children", getTreeData((Integer)res.get("id"), resources, treeData));
                        return childMap;
                    } else {
                        if (parentId == 0) {
                            treeData.add(res);
                        }
                        return res;
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getResourcesTreeData1(List<Resources> resources) {
        return treeData1(resources);
    }

    private List<Map<String, Object>> treeData1(List<Resources> resources) {
        List<Map<String, Object>> treeData = new ArrayList<>();
        if (resources != null && resources.size() > 0) {
            List<Map<String, Object>> roots = resources.stream().filter( res ->
                resources.stream().filter( r -> r.getId().equals(res.getParentId())).count() == 0
            ).map(res -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", res.getId());
                map.put("parentId", res.getParentId());
                map.put("name", res.getName());
                map.put("text", res.getName());
                map.put("key", res.getId().toString());
                map.put("value", res.getId().toString());
                map.put("label", res.getName());
                map.put("resType", res.getResType());

                map.put("var1", res.getVar1());
                map.put("var2", res.getVar2());
                map.put("var3", res.getVar3());
                return map;
            }).collect(Collectors.toList());
            List<Map<String, Object>> result = resources.parallelStream()
                    .map( res -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", res.getId());
                        map.put("parentId", res.getParentId());
                        map.put("name", res.getName());
                        map.put("text", res.getName());
                        map.put("key", res.getId().toString());
                        map.put("value", res.getId().toString());
                        map.put("label", res.getName());
                        map.put("resType", res.getResType());
                        map.put("var1", res.getVar1());
                        map.put("var2", res.getVar2());
                        map.put("var3", res.getVar3());
                        return map;
                    }).collect(Collectors.toList());


            roots.stream().forEach(root -> {
                treeData.add(root);
                getTreeData1(root, result, treeData);
            });
        }
        return treeData;
    }

    private void getTreeData1(Map<String, Object> parent, List<Map<String, Object>> resources, List<Map<String, Object>> treeData) {

        resources.stream()
                .filter( res -> (parent.get("id").equals(res.get("parentId"))))
                .forEach( res -> {
                    List<Map<String, Object>> childrens;
                    if (parent.get("children") != null) {
                        childrens = (List<Map<String, Object>>) parent.get("children");
                    } else {
                        childrens = new ArrayList<>();
                    }
                    childrens.add(res);
                    parent.put("children", childrens);
                    getTreeData1(res, resources, treeData);
                });
    }

}
