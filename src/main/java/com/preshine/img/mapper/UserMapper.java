package com.preshine.img.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.preshine.img.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Preshine
 * @since 2018-11-13
 */
public interface UserMapper extends BaseMapper<User> {

    List<Map<String, Object>> getUserPage(@Param("pagination") Pagination pagination, @Param("userName") String userName,
                                          @Param("mobile") String mobile, @Param("email") String email, @Param("realName") String realName,
                                          @Param("sex") Integer sex, @Param("sorterField") String sorterField, @Param("sorterOrder") String sorterOrder);

    List<Map<String, Object>> getUsersByRole(@Param("pagination") Pagination pagination, @Param("userName") String userName, @Param("mobile") String mobile, @Param("userAccounts") List<Integer> userAccounts);

}
