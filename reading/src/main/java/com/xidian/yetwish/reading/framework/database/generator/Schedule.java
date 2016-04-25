package com.xidian.yetwish.reading.framework.database.generator;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SCHEDULE.
 */
public class Schedule {

    private Long sid;
    private Long uid;
    private Integer type;
    private String content;
    private Long scheduleTime;
    private String createTime;
    private String hostName;
    private Integer alertTime;
    private Integer alertType;
    private Integer priority;
    private String mediaLocalPath;
    private String attachmentDigest;

    public Schedule() {
    }

    public Schedule(Long sid) {
        this.sid = sid;
    }

    public Schedule(Long sid, Long uid, Integer type, String content, Long scheduleTime, String createTime, String hostName, Integer alertTime, Integer alertType, Integer priority, String mediaLocalPath, String attachmentDigest) {
        this.sid = sid;
        this.uid = uid;
        this.type = type;
        this.content = content;
        this.scheduleTime = scheduleTime;
        this.createTime = createTime;
        this.hostName = hostName;
        this.alertTime = alertTime;
        this.alertType = alertType;
        this.priority = priority;
        this.mediaLocalPath = mediaLocalPath;
        this.attachmentDigest = attachmentDigest;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Integer alertTime) {
        this.alertTime = alertTime;
    }

    public Integer getAlertType() {
        return alertType;
    }

    public void setAlertType(Integer alertType) {
        this.alertType = alertType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getMediaLocalPath() {
        return mediaLocalPath;
    }

    public void setMediaLocalPath(String mediaLocalPath) {
        this.mediaLocalPath = mediaLocalPath;
    }

    public String getAttachmentDigest() {
        return attachmentDigest;
    }

    public void setAttachmentDigest(String attachmentDigest) {
        this.attachmentDigest = attachmentDigest;
    }

}