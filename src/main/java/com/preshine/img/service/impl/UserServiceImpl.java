package com.preshine.img.service.impl;

import com.preshine.img.entity.User;
import com.preshine.img.mapper.UserMapper;
import com.preshine.img.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
