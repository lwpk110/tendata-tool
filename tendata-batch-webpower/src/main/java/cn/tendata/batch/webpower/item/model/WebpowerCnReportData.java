package cn.tendata.batch.webpower.item.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * {@inheritDoc}
 *
 * Created by ernest on 2017/8/28.
 */
public class WebpowerCnReportData {

    Pattern dmdLogDatePattern1 = Pattern.compile("^[1-9]\\d{3}/([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|[1-2][0-9]|3[0-1])\\s+(\\d|20|21|22|23|[0-1]\\d):(\\d|[0-5]\\d)$");
    Pattern dmdLogDatePattern2 = Pattern.compile("^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$");

    private static final String formatStr_1 = "yyyy/MM/dd HH:mm";
    private static final String formatStr_2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 活动名称.
     */
    private String DMDcampaignName;
    /**
     * 邮件主题名称.
     */
    private String DMDmailingName;
    /**
     * 事件类型.
     */
    private String DMDtype;

    /**
     * 邮件地址.
     */

    private String email;
    /**
     * 事件发生时间.
     */
    private DateTime DMDlogDate;
    /**
     * 点击的连接名称.
     */
    private String DMDclickName;
    /**
     * 点击的连接路径.
     */
    private String DMDclickUrl;
    /**
     * 退回信息.
     */
    private String DMDbounceMessage;
    /**
     * ip地址.
     */
    private String DMDipAddress;
    /**
     * 客户端类型.
     */
    private String DMDclient;

    public String getDMDcampaignName() {
        return DMDcampaignName;
    }

    public void setDMDcampaignName(String DMDcampaignName) {
        this.DMDcampaignName = DMDcampaignName;
    }

    public String getDMDmailingName() {
        return DMDmailingName;
    }

    public void setDMDmailingName(String DMDmailingName) {
        this.DMDmailingName = DMDmailingName;
    }

    public String getDMDtype() {
        return DMDtype;
    }

    public void setDMDtype(String DMDtype) {
        this.DMDtype = DMDtype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DateTime getDMDlogDate() {
        return DMDlogDate;
    }

    public void setDMDlogDate(String DMDlogDate) {
        this.DMDlogDate = this.formatToDateTime(DMDlogDate);
    }

    public String getDMDclickName() {
        return DMDclickName;
    }

    public void setDMDclickName(String DMDclickName) {
        this.DMDclickName = DMDclickName;
    }

    public String getDMDclickUrl() {
        return DMDclickUrl;
    }

    public void setDMDclickUrl(String DMDclickUrl) {
        this.DMDclickUrl = DMDclickUrl;
    }

    public String getDMDbounceMessage() {
        return DMDbounceMessage;
    }

    public void setDMDbounceMessage(String DMDbounceMessage) {
        this.DMDbounceMessage = DMDbounceMessage;
    }

    public String getDMDipAddress() {
        return DMDipAddress;
    }

    public void setDMDipAddress(String DMDipAddress) {
        this.DMDipAddress = DMDipAddress;
    }

    public String getDMDclient() {
        return DMDclient;
    }

    public void setDMDclient(String DMDclient) {
        this.DMDclient = DMDclient;
    }

    private DateTime formatToDateTime(String dateStr) {
        Matcher matcher1 = dmdLogDatePattern1.matcher(dateStr);
        if(matcher1.matches()){
            return DateTimeFormat
                .forPattern(formatStr_1)
                .parseDateTime(dateStr);
        }else{
            return DateTimeFormat
                .forPattern(formatStr_2)
                .parseDateTime(dateStr);
        }
    }
}
