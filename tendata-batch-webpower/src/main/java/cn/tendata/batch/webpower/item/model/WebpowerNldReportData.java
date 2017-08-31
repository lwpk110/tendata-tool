package cn.tendata.batch.webpower.item.model;

import org.joda.time.DateTime;

/**
 * Created by ernest on 2016/8/31.
 */
public class WebpowerNldReportData {

    private static final String formatStr = "yyyy-MM-dd HH:mm:ss";
    /**
     * 活动名称.
     */
    private String DMDcampaignID;
    private String DMDcampaignName;
    /**
     * 邮件主题名称.
     */
    private String DMDmailingID;
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
    private String DMDlogDate;
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

    public String getDMDcampaignID() {
        return DMDcampaignID;
    }

    public void setDMDcampaignID(String DMDcampaignID) {
        this.DMDcampaignID = DMDcampaignID;
    }

    public String getDMDcampaignName() {
        return DMDcampaignName;
    }

    public void setDMDcampaignName(String DMDcampaignName) {
        this.DMDcampaignName = DMDcampaignName;
    }

    public String getDMDmailingID() {
        return DMDmailingID;
    }

    public void setDMDmailingID(String DMDmailingID) {
        this.DMDmailingID = DMDmailingID;
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

    public String getDMDlogDate() {
        return DMDlogDate;
    }

    public void setDMDlogDate(DateTime DMDlogDate) {
        this.DMDlogDate = formatToDateTime(DMDlogDate) ;
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

    private String formatToDateTime(DateTime DMDlogDate) {
        return String.valueOf(DMDlogDate.toString(formatStr));
    }
}
