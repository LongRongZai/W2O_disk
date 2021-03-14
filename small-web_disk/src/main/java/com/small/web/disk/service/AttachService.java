package com.small.web.disk.service;

import com.small.web.disk.bean.AdminBean;
import com.small.web.disk.bean.AttachBean;
import com.small.web.disk.bean.IndexBean;
import com.small.web.disk.config.DiskProperties;
import com.small.web.disk.dao.mapperDao.AdminMapper;
import com.small.web.disk.dao.mapperDao.AttachMapper;
import com.small.web.disk.dao.mapperDao.IndexMapper;
import com.small.web.disk.evt.*;
import com.small.web.disk.model.PluploadModel;
import com.small.web.disk.model.QueryAttachModel;
import com.small.web.disk.model.ServiceRespModel;
import com.small.web.disk.utils.UploadFileTool;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Service
public class AttachService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    AttachMapper attachMapper;
    @Resource
    IndexMapper indexMapper;
    @Resource
    DiskProperties diskProperties;
    @Resource
    AdminMapper adminMapper;

    /**
     * 目录附件列表
     */
    public ServiceRespModel indexAttachList(QueryIndexAttachEvt evt) {
        //检验入参合法性
        if (StringUtils.isBlank(evt.getIndexNo()))
            return new ServiceRespModel(-1, "用户编码不能为空", null);
        //查询附件列表
        QueryAttachModel model = new QueryAttachModel();
        model.setIndexNo(evt.getIndexNo());
        List<AttachBean> attachBeanList = attachMapper.queryAttachList(model);
        return new ServiceRespModel(1, "目录附件列表", attachBeanList);
    }

    /**
     * 上传附件
     */
    public ServiceRespModel upload(MultipartFile multipartFile, UploadAttachEvt evt, HttpServletRequest request) {
        logger.info(String.format("用户%s开始上传附件", request.getAttribute("Account")));
        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //获取上传时的附件名
            String fileName = multipartFile.getOriginalFilename();
            //获取附件类型
            String name = StringUtils.replace(fileName, " ", "");
            String fileType = name.substring(name.lastIndexOf(".") + 1);
            //将附件名设置为时间戳
            String timeStamp = System.currentTimeMillis() + "." + fileType;
            //限制上传的附件类型
            if (fileType.toLowerCase().equals("exe") || fileType.toLowerCase().equals("java") || fileType.toLowerCase().equals("sh"))
                return new ServiceRespModel(-1, "此附件为限制上传附件类型", null);
            //指定上传的位置
            String path = diskProperties.getAttachSavePath();
            //获取附件的输入流
            inputStream = multipartFile.getInputStream();
            //获取附件大小
            long fileByteSize = multipartFile.getSize();
            String fileSize = UploadFileTool.getPrintSize(inputStream.available());
            //判断该用户附件总大小是否超出限制
            QueryAttachModel model = new QueryAttachModel();
            model.setUserNo((String) request.getAttribute("No"));
            if (!attachMapper.queryAttachList(model).isEmpty()) {
                long totalSize = attachMapper.queryUserAttachSize((String) request.getAttribute("No")) + fileByteSize;
                if (totalSize > 104857600) {
                    return new ServiceRespModel(-1, "用户容量已满，附件上传失败", null);
                }
            }
            //路径+附件名
            File targetFile = new File(path + timeStamp);
            //如果之前的 String path = "d:/upload/" 没有在最后加 / ，那就要在 path 后面 + "/"
            //判断附件父目录是否存在
            if (!targetFile.getParentFile().exists()) {
                //不存在就创建一个
                targetFile.getParentFile().mkdir();
            }
            //获取附件的输出流
            outputStream = new FileOutputStream(targetFile);
            //将附件信息保存
            PluploadModel pluploadModel = new PluploadModel();
            pluploadModel.setName(fileName);
            pluploadModel.setSize(fileSize);
            pluploadModel.setType(fileType);
            String viewPath = diskProperties.getViewUrl() + timeStamp;
            pluploadModel.setViewPath(viewPath);//预览地址
            //将附件信息保存至数据库
            AttachBean attachBean = new AttachBean();
            attachBean.setAttachNo(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
            attachBean.setUserNo((String) request.getAttribute("No"));
            attachBean.setAttachSize(fileSize);
            attachBean.setAttachUrl(targetFile.getPath());
            attachBean.setAttachType(fileType);
            attachBean.setAttachName(fileName);
            attachBean.setIndexNo(evt.getIndexNo());
            attachBean.setAttachByteSize(fileByteSize);
            attachBean.setAttachViewUrl(viewPath);
            attachBean.setCreateUser((String) request.getAttribute("Account"));
            int info = attachMapper.uploadAttach(attachBean);
            if (info == 0)
                return new ServiceRespModel(-1, "附件信息上传数据库失败", null);
            //使用资源访问器FileCopyUtils的copy方法拷贝附件
            FileCopyUtils.copy(inputStream, outputStream);
            logger.info(String.format("用户%s上传附件成功", request.getAttribute("Account")));
            return new ServiceRespModel(1, "附件上传成功", pluploadModel);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("用户%s上传附件失败", request.getAttribute("Account")));
            return new ServiceRespModel(-1, "附件上传失败", null);
        } finally {
            //无论成功与否，都有关闭输入输出流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除附件
     */
    public ServiceRespModel deleteAttach(DeleteAttachEvt evt, HttpServletRequest request) {
        logger.info(String.format("用户%s开始删除附件%s", request.getAttribute("Account"), evt.getAttachNo()));
        //检验入参合法性
        if (StringUtils.isBlank(evt.getAttachNo()))
            return new ServiceRespModel(-1, "附件编码不能为空", null);
        //检验附件是否存在
        AttachBean attachBean = attachMapper.queryAttach(evt.getAttachNo());
        if (attachBean == null)
            return new ServiceRespModel(-1, "附件不存在", null);
        //删除附件
        int info = attachMapper.deleteAttach(evt.getAttachNo());
        if (info == 1) {
            logger.info(String.format("用户%s删除附件%s成功", request.getAttribute("Account"), evt.getAttachNo()));
            return new ServiceRespModel(1, "删除附件成功", null);

        }
        logger.error(String.format("%s删除附件%s失败", request.getAttribute("Account"), evt.getAttachNo()));
        return new ServiceRespModel(-1, "删除附件失败", null);
    }

    /**
     * 下载附件
     */
    public void downloadAttach(HttpServletResponse response, DownloadAttachEvt evt) throws Exception {
        logger.info(String.format("%s开始下载附件%s", evt.getUserNo(), evt.getAttachNo()));
        //检验入参合法性
        if (StringUtils.isBlank(evt.getAttachNo())) {
            logger.error("附件编码不能为空");
            return;
        }
        //检验附件是否存在
        AttachBean attachBean = attachMapper.queryAttach(evt.getAttachNo());
        if (attachBean == null) {
            logger.error("附件不存在");
            return;
        }
        String fileName = evt.getAttachName();// 设置文件名
        if (StringUtils.isBlank(fileName)) {
            logger.error("未设置附件名");
            return;
        }
        //设置文件路径
        String realPath = attachBean.getAttachUrl();
        File file = new File(realPath);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                logger.info(String.format("%s下载附件%s成功", evt.getUserNo(), evt.getAttachNo()));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(String.format("%s下载附件%s失败", evt.getUserNo(), evt.getAttachNo()));
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 修改附件名称
     */
    public ServiceRespModel updateAttach(UpdateAttachNameEvt evt) {
        //检验入参合法性
        if (StringUtils.isBlank(evt.getAttachNo()))
            return new ServiceRespModel(-1, "附件编码不能为空", null);
        //检验附件是否存在
        AttachBean attachBean = attachMapper.queryAttach(evt.getAttachNo());
        if (attachBean == null)
            return new ServiceRespModel(-1, "附件不存在", null);
        //修改附件名称
        attachBean.setAttachNo(evt.getAttachNo());
        attachBean.setAttachName(evt.getAttachName());
        int info = attachMapper.updateAttach(attachBean);
        if (info == 1)
            return new ServiceRespModel(1, "修改附件名称成功", null);
        return new ServiceRespModel(-1, "修改附件名称失败", null);
    }

    /**
     * 未审核附件列表
     */
    public ServiceRespModel NonCheckedAttachList(HttpServletRequest request) {
        // 查询管理员是否存在或被删除
        AdminBean adminBean = adminMapper.queryAdminInfo((String) request.getAttribute("No"));
        if (adminBean == null)
            return new ServiceRespModel(-1, "管理员不存在", null);
        //查询未审核附件
        QueryAttachModel model = new QueryAttachModel();
        model.setAuditStatus("C");
        List<AttachBean> attachBeanList = attachMapper.queryAttachList(model);
        return new ServiceRespModel(1, "未审核附件列表", attachBeanList);
    }

    /**
     * 附件审核
     */
    public ServiceRespModel auditAttach(AuditAttachEvt evt, HttpServletRequest request) {
        //检验入参合法性
        if (StringUtils.isBlank(evt.getAttachNo()))
            return new ServiceRespModel(-1, "附件编码不能为空", null);
        if (StringUtils.isBlank(evt.getAuditStatus()))
            return new ServiceRespModel(-1, "审核状态不能为空", null);
        if (StringUtils.isBlank(evt.getAuditor()))
            return new ServiceRespModel(-1, "审核人员不能为空", null);
        // 查询管理员是否存在或被删除
        AdminBean adminBean = adminMapper.queryAdminInfo((String) request.getAttribute("No"));
        if (adminBean == null)
            return new ServiceRespModel(-1, "管理员不存在", null);
        //检验附件是否存在
        AttachBean attachBean = attachMapper.queryAttach(evt.getAttachNo());
        if (attachBean == null) {
            return new ServiceRespModel(-1, "附件不存在", null);
        }
        //附件审核
        if (evt.getAuditStatus().equals("P")) {
            int info = attachMapper.updateAttachAuditStatus(evt);
            if (info == 1) {
                logger.info(String.format("%s附件审核通过", evt.getAttachNo()));
                return new ServiceRespModel(1, "附件审核通过", null);
            }
        }
        if (evt.getAuditStatus().equals("D")) {
            File targetFile = new File(attachBean.getAttachUrl());
            if (!targetFile.exists()) {
                return new ServiceRespModel(-1, "未能在服务器上找到该附件", null);
            }
            int info = attachMapper.deleteAttachFromServer(evt.getAttachNo());
            if (info == 1) {
                logger.info(String.format("%s附件审核未通过", evt.getAttachNo()));//删除附件
                targetFile.delete();
                return new ServiceRespModel(1, "附件审核未通过已删除", null);
            }
        }
        logger.info(String.format("%s附件审核失败", evt.getAttachNo()));
        return new ServiceRespModel(-1, "附件审核失败", null);
    }

    /**
     * 移动附件
     */
    public ServiceRespModel moveAttach(MoveAttachEvt evt) {
        //检验入参合法性
        if (StringUtils.isBlank(evt.getAttachNo()))
            return new ServiceRespModel(-1, "附件编码不能为空", null);
        //检验目录是否存在
        if (StringUtils.isNotBlank(evt.getIndexNo())) {
            IndexBean indexBean = indexMapper.queryIndex(evt.getIndexNo());
            if (indexBean == null) {
                return new ServiceRespModel(-1, "目标目录不存在", null);
            }
        }
        //移动附件
        int info = attachMapper.moveAttach(evt);
        if (info == 1) {
            return new ServiceRespModel(1, "附件移动成功", null);
        }
        return new ServiceRespModel(-1, "附件移动失败", null);
    }
}