package com.preshine.img.service;

import com.preshine.img.entity.Resources;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Preshine
 * @since 2018-11-12
 */
public interface IResourcesService extends IService<Resources> {

    List<Map<String, Object>> getResourcesTreeData(List<Resources> resources);
    List<Map<String, Object>> getResourcesTreeData1(List<Resources> resources);

}
