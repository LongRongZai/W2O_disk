package com.small.web.disk.dao.mapperDao;

import com.small.web.disk.bean.AttachBean;
import com.small.web.disk.dao.provider.AttachProvider;
import com.small.web.disk.evt.AuditAttachEvt;
import com.small.web.disk.evt.MoveAttachEvt;
import com.small.web.disk.model.QueryAttachModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AttachMapper {

    /**
     * 上传附件
     */
    @Insert("insert into t_attach(userNO,createTime,status,attachNo,indexNo,attachName,attachSize,attachType,attachUrl,attachByteSize,auditStatus,attachViewUrl,createUser)values" +
            "(#{item.userNo},now(),'E',#{item.attachNo},#{item.indexNo},#{item.attachName},#{item.attachSize},#{item.attachType},#{item.attachUrl},#{item.attachByteSize},'C',#{item.attachViewUrl},#{item.createUser})")
    Integer uploadAttach(@Param("item") AttachBean attachBean);

    /**
     * 删除附件（修改状态）
     */
    @Update("update t_attach set status = 'D' where attachNo = #{item} and status = 'E'")
    Integer deleteAttach(@Param("item") String attachNo);

    /**
     * 修改附件名称
     */
    @Update("update t_attach set attachName = #{item.attachName} where attachNo = #{item.attachNo} and status = 'E'")
    Integer updateAttach(@Param("item") AttachBean attachBean);

    /**
     * 查询附件
     */
    @Select("select * from t_attach where attachNo = #{item} and status = 'E'")
    AttachBean queryAttach(@Param("item") String attachNo);

    /**
     * 将附件移出目录
     */
    @Update("update t_attach set indexNo = null where indexNo = #{item} and status = 'E'")
    Integer moveAttachWhenDelete(@Param("item") String indexNo);

    /**
     * 附件列表
     */
    @SelectProvider(type = AttachProvider.class, method = "queryAttachList")
    List<AttachBean> queryAttachList(QueryAttachModel model);

    /**
     * 用户附件总大小
     */
    @Select("select sum(attachByteSize) from t_attach where userNo = #{item} and status = 'E'")
    Long queryUserAttachSize(@Param("item") String userNo);

    /**
     * 修改附件审核状态
     */
    @Update("update t_attach set auditStatus = #{item.auditStatus},auditTime = now(),auditor = #{item.auditor} " +
            "where attachNo = #{item.attachNo} and status = 'E'")
    Integer updateAttachAuditStatus(@Param("item") AuditAttachEvt auditAttachEvt);

    /**
     * 删除附件（删除记录）
     */
    @Delete("delete from t_attach where attachNo = #{item} ")
    Integer deleteAttachFromServer(@Param("item") String attachNo);

    /**
     * 移动附件
     */
    @UpdateProvider(type = AttachProvider.class, method = "moveAttach")
    Integer moveAttach(MoveAttachEvt moveAttachEvt);
}
