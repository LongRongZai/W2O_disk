package com.small.web.disk.dao.mapperDao;

import com.small.web.disk.bean.AdminBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminMapper {
    /**
     * 查询管理员
     * 通过账号查询
     */
    @Select("select * from t_admin where adminAccount = #{item} and status = 'E'")
    AdminBean queryAdmin(@Param("item") String adminAccount);

    /**
     * 查询管理员
     * 通过编码查询
     */
    @Select("select * from t_admin where adminNo = #{item} and status = 'E'")
    AdminBean queryAdminInfo(@Param("item") String adminNo);

    /**
     * 更新管理员最后一次登录时间
     */
    @Update("update t_admin set lastLoginTime = now() where adminAccount = #{item} and status = 'E'")
    Integer updateAdminLastLoginTime(@Param("item") String adminAccount);
}
