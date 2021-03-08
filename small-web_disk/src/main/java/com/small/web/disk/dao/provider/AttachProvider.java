package com.small.web.disk.dao.provider;

import com.small.web.disk.evt.MoveAttachEvt;
import com.small.web.disk.model.QueryAttachModel;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class AttachProvider {
    //查询附件
    public String queryAttachList(final QueryAttachModel model) {
        SQL sql = new SQL() {
            {
                SELECT("attachNo,indexNo,userNo,attachName,attachSize,attachByteSize,attachType,attachViewUrl,auditStatus,auditTime,auditor,createTime");
                FROM("t_attach");
                WHERE("status = 'E'");
                if (StringUtils.isNotBlank(model.getIndexNo())) {
                    WHERE("indexNo = #{indexNo}");
                }
                if (StringUtils.isNotBlank(model.getUserNo())) {
                    WHERE("userNo = #{userNo}");
                }
                if (StringUtils.isNotBlank(model.getAuditStatus()) && model.getAuditStatus().equals("C")) {
                    WHERE("auditStatus = 'C'");
                }
            }
        };
        return sql.toString();
    }

    //移动附件
    public String moveAttach(final MoveAttachEvt evt) {
        SQL sql = new SQL() {
            {
                UPDATE("t_attach");
                WHERE("status = 'E' and attachNo = #{attachNo}");
                if (StringUtils.isNotBlank(evt.getIndexNo())) {
                    SET("indexNo = #{indexNo}");
                }
                if (StringUtils.isBlank(evt.getIndexNo())) {
                    SET("indexNo = null");
                }
            }
        };
        return sql.toString();
    }
}
