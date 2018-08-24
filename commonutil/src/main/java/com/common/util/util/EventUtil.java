package com.common.util.util;

import com.common.util.bean.EventMsg;

import org.greenrobot.eventbus.EventBus;

/**
 * description ：
 * project name：Hss
 * author : Vincent
 * creation date: 2017/3/13 22:04
 *
 * @version 1.0
 */

public class EventUtil {

    //注册事件
    public static void register(Object context) {
        if (!EventBus.getDefault().isRegistered(context)) {
            EventBus.getDefault().register(context);
        }
    }
    //解除
    public static void unregister(Object context) {
        if (EventBus.getDefault().isRegistered(context)) {
            EventBus.getDefault().unregister(context);
        }
    }
    //发送消息
    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }

    /**
     * 获得一个EventMsg对象
     * @param type
     * @param o
     * @return
     */
    public static EventMsg getEventMsg(String type,Object o){
        EventMsg eventMsg = new EventMsg();
        eventMsg.setMsgContent(o);
        eventMsg.setMsgType(type);
        return eventMsg;
    }

}
