package com.xidian.yetwish.reading.framework.service;

/**
 * API接口回调
 * Created by Yetwish on 2016/4/28 0028.
 */
public interface ApiCallback<T> {

    /**
     * 接口调用成功
     *
     * @param data 返回数据
     */
    void onDataReceived(T data);

    /**
     * 接口调用异常
     * @param code    异常码
     * @param reason  异常原因
     */
    void onException(int code, String reason);
}
