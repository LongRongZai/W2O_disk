package com.small.web.disk.dao.mapperDao;

import com.small.web.disk.bean.UserBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    /**
     * 查询用户
     * 通过账号查询
     */
    @Select("select * from t_user where userAccount = #{item} and status = 'E'")
    UserBean queryUser(@Param("item") String userAccount);//@Param 占位符（封装过的）

    /**
     * 查询用户
     * 通过用户编码查询
     */
    @Select("select * from t_user where userNo = #{item} and status = 'E'")
    UserBean queryUserInfo(@Param("item") String userNo);

    /**
     * 插入用户
     */
    @Insert("insert into t_user(userNO,createTime,status,userAccount,userCapacity,userName,userPassword)values" +
            "(#{item.userNo},now(),'E',#{item.userAccount},'1GB',#{item.userName},#{item.userPassword})")
    Integer insertUser(@Param("item") UserBean userBean);

    /**
     * 更新用户最后一次登录时间
     */
    @Update("update t_user set lastLoginTime = now() where userAccount = #{item} and status = 'E'")
    Integer updateUserLastLoginTime(@Param("item") String userAccount);


}
