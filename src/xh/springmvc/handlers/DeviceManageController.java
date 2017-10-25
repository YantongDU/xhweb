package xh.springmvc.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;
import xh.func.plugin.DownLoadUtils;
import xh.func.plugin.FlexJSON;
import xh.func.plugin.FormToWord;
import xh.func.plugin.FunUtil;
import xh.func.plugin.GsonUtil;
import xh.mybatis.bean.*;
import xh.mybatis.service.*;

@Controller
@RequestMapping(value = "/devicemanage")
public class DeviceManageController {
    private boolean success;
    private String message;
    private FunUtil funUtil = new FunUtil();
    protected final Log log = LogFactory.getLog(DeviceManageController.class);
    private FlexJSON json = new FlexJSON();
    private WebLogBean webLogBean = new WebLogBean();

    /**
     * 查询所有流程
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public void selectAll(HttpServletRequest request,
                          HttpServletResponse response) {
        this.success = true;
        int start = funUtil.StringToInt(request.getParameter("start"));
        int limit = funUtil.StringToInt(request.getParameter("limit"));
        String user=funUtil.loginUser(request);
        WebUserBean userbean=WebUserServices.selectUserByUser(user);
        int roleId=userbean.getRoleId();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", limit);
        map.put("user", user);
        map.put("roleId", roleId);

        HashMap result = new HashMap();
        result.put("success", success);
        result.put("items", DeviceManageService.selectAll(map));
        result.put("totals", DeviceManageService.dataCount(map));
        response.setContentType("application/json;charset=utf-8");
        String jsonstr = json.Encode(result);
        try {
            response.getWriter().write(jsonstr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/applyProgress", method = RequestMethod.GET)
    public void applyProgress(HttpServletRequest request,
                              HttpServletResponse response) {
        this.success = true;
        int id = funUtil.StringToInt(request.getParameter("id"));
        HashMap result = new HashMap();
        result.put("success", success);
        result.put("items", DeviceManageService.applyProgress(id));
        response.setContentType("application/json;charset=utf-8");
        String jsonstr = json.Encode(result);
        log.debug(jsonstr);
        try {
            response.getWriter().write(jsonstr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 申请
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/insertDeviceManage", method = RequestMethod.POST)
    public void insertDeviceManage(HttpServletRequest request,
                          HttpServletResponse response) {
        this.success = true;
        String jsonData = request.getParameter("formData");
        DeviceManageBean bean = GsonUtil.json2Object(jsonData, DeviceManageBean.class);
        bean.setUserName(funUtil.loginUser(request));
        bean.setTime(funUtil.nowDate());
        log.info("data==>" + bean.toString());

        int rst = DeviceManageService.insertDeviceManage(bean);
        if (rst == 1) {
            this.message = "业务变更申请信息已经成功提交";
            webLogBean.setOperator(funUtil.loginUser(request));
            webLogBean.setOperatorIp(funUtil.getIpAddr(request));
            webLogBean.setStyle(1);
            webLogBean.setContent("业务变更申请信息，data=" + bean.toString());
            WebLogService.writeLog(webLogBean);

            //----发送通知邮件
            sendNotify(bean.getUser_MainManager(), "业务变更申请信息已经成功提交,请审核。。。", request);
            //----END
        } else {
            this.message = "业务变更申请信息提交失败";
        }
        HashMap result = new HashMap();
        result.put("success", success);
        result.put("result", rst);
        result.put("message", message);
        response.setContentType("application/json;charset=utf-8");
        String jsonstr = json.Encode(result);
        log.debug(jsonstr);
        try {
            response.getWriter().write(jsonstr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 管理方审核
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/checkedOne", method = RequestMethod.POST)
    public void checkedOne(HttpServletRequest request,
                           HttpServletResponse response) {
        this.success = true;
        int id = funUtil.StringToInt(request.getParameter("id"));
        int checked = funUtil.StringToInt(request.getParameter("checked"));
        String note1 = request.getParameter("note1");
        String user = request.getParameter("user");
        DeviceManageBean bean = new DeviceManageBean();
        bean.setId(id);
        if(checked == 1){
            bean.setChecked(1);
        } else{
            bean.setChecked(-1);
        }
        bean.setUser1(funUtil.loginUser(request));
        bean.setTime1(funUtil.nowDate());
        bean.setNote1(note1);
        log.info("data==>" + bean.toString());
        int rst = DeviceManageService.checkedOne(bean);
        if (rst == 1) {
            this.message = "审核提交成功";
            webLogBean.setOperator(funUtil.loginUser(request));
            webLogBean.setOperatorIp(funUtil.getIpAddr(request));
            webLogBean.setStyle(5);
            webLogBean.setContent("审核遥控申请，data=" + bean.toString());
            WebLogService.writeLog(webLogBean);

            //----发送通知邮件
            sendNotify(user, "业务变更信息审核，请管理部门领导审核并处理。。。", request);
            //----END
        } else {
            this.message = "审核提交失败";
        }
        HashMap result = new HashMap();
        result.put("success", success);
        result.put("result", rst);
        result.put("message", message);
        response.setContentType("application/json;charset=utf-8");
        String jsonstr = json.Encode(result);
        log.debug(jsonstr);
        try {
            response.getWriter().write(jsonstr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 管理方审核
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/checkedTwo", method = RequestMethod.POST)
    public void checkedTwo(HttpServletRequest request,
                           HttpServletResponse response) {
        this.success = true;
        int id = funUtil.StringToInt(request.getParameter("id"));
        int checked = funUtil.StringToInt(request.getParameter("checked"));
        String note2 = request.getParameter("note2");
        String user3 = request.getParameter("user");
        DeviceManageBean bean = new DeviceManageBean();
        bean.setId(id);
        if(checked == 2){
            bean.setChecked(2);
        }else if(checked ==1) {
            bean.setChecked(1);
        }
        bean.setUser2(funUtil.loginUser(request));
        bean.setTime2(funUtil.nowDate());
        bean.setNote2(note2);
        int rst = DeviceManageService.checkedTwo(bean);
        if (rst == 1) {
            this.message = "通知经办人处理成功";
            webLogBean.setOperator(funUtil.loginUser(request));
            webLogBean.setOperatorIp(funUtil.getIpAddr(request));
            webLogBean.setStyle(5);
            webLogBean.setContent("通知经办人处理(业务变更申请)，data=" + bean.toString());
            WebLogService.writeLog(webLogBean);

            //----发送通知邮件
            sendNotify(user3, "业务变更完成,通知用户确认。。。", request);
            //----END
        } else {
            this.message = "通知用户确认失败";
        }
        HashMap result = new HashMap();
        result.put("success", success);
        result.put("result", rst);
        result.put("message", message);
        response.setContentType("application/json;charset=utf-8");
        String jsonstr = json.Encode(result);
        log.debug(jsonstr);
        try {
            response.getWriter().write(jsonstr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 用户确认
     * @param request
     * @param response
     */
    @RequestMapping(value = "/sureFile", method = RequestMethod.POST)
    public void sureFile(HttpServletRequest request,
                         HttpServletResponse response) {
        this.success = true;
        int id = funUtil.StringToInt(request.getParameter("id"));
        String note3 = request.getParameter("note3");
        DeviceManageBean bean = new DeviceManageBean();
        bean.setId(id);
        bean.setTime3(funUtil.nowDate());
        bean.setNote3(note3);
        bean.setChecked(3);
        int rst = DeviceManageService.sureFile(bean);
        if (rst == 1) {
            this.message = "确认遥控完成信息成功";
            webLogBean.setOperator(funUtil.loginUser(request));
            webLogBean.setOperatorIp(funUtil.getIpAddr(request));
            webLogBean.setStyle(5);
            webLogBean.setContent("确认遥控完成信息，data=" + bean.toString());
            WebLogService.writeLog(webLogBean);


        } else {
            this.message = "确认遥控完成信息失败";
        }
        HashMap result = new HashMap();
        result.put("success", success);
        result.put("result", rst);
        result.put("message", message);
        response.setContentType("application/json;charset=utf-8");
        String jsonstr = json.Encode(result);
        log.debug(jsonstr);
        try {
            response.getWriter().write(jsonstr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 发送邮件--遥控申请
     * @param recvUser	邮件接收者
     * @param content	邮件内容
     * @param request
     */
    public void sendNotify(String recvUser,String content,HttpServletRequest request){
        //----发送通知邮件
        EmailBean emailBean = new EmailBean();
        emailBean.setTitle("遥控申请");
        emailBean.setRecvUser(recvUser);
        emailBean.setSendUser(funUtil.loginUser(request));
        emailBean.setContent(content);
        emailBean.setTime(funUtil.nowDate());
        EmailService.insertEmail(emailBean);
        //----END
    }

}
