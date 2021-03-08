package com.small.web.disk.dao.mapperDao;

import com.small.web.disk.bean.IndexBean;
import com.small.web.disk.dao.provider.AttachProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IndexMapper {
    /**
     * 查询目录
     */
    @Select("select * from t_index where indexNo = #{item} and status = 'E'")
    IndexBean queryIndex(@Param("item") String indexNo);

    /**
     * 插入目录
     */
    @Insert("insert into t_index(indexNo,createTime,indexName,status,userNo)values" +
            "(#{item.indexNo},now(),#{item.indexName},'E',#{item.userNo})")
    Integer insertIndex(@Param("item") IndexBean indexBean);

    /**
     * 修改目录
     */
    @Update("update t_index set indexName = #{item.indexName} where indexNo = #{item.indexNo} and status = 'E'")
    Integer updateIndex(@Param("item") IndexBean indexBean);

    /**
     * 删除目录（修改状态）
     */
    @Update("update t_index set status = 'D' where indexNo = #{item} and status = 'E'")
    Integer deleteIndex(@Param("item") String indexNo);

    /**
     * 用户目录列表
     */
    @Select("select * from t_index where userNo = #{item} and status = 'E'")
    List<IndexBean> userIndexList(@Param("item") String userNo);

}
