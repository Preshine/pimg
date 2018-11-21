package com.preshine.img.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.preshine.img.entity.User;
import com.preshine.img.mapper.UserMapper;
import com.preshine.img.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Preshine
 * @since 2018-11-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Page<Map<String, Object>> getUserPage(Page page, String userName, String mobile, String email, String realName, Integer sex, String sorterField, String sorterOrder) {
        page.setRecords(baseMapper.getUserPage(page,userName, mobile, email, realName, sex, sorterField, sorterOrder));
        return page;
    }

    @Override
    public Page<Map<String, Object>> getUsersByRole(Page page, String userName, String mobile, List<Integer> userAccounts) {
        page.setRecords(baseMapper.getUsersByRole(page, userName, mobile, userAccounts));
        return page;
    }

}
