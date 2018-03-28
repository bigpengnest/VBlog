package com.BigPeng.VBlog.util;

public class RedisKeyUtil {
    private static String SPLIT=":";

    private static String BIZ_EVENTQUEUE="EVENT_QUEUE";

    //粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";


    //redis中事件队列的key值
    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }

    //redis中关注者的key值
    //用户保存用户关注对象的id值
    //可以获取question的关注者集合
    public static String getFollwerKey(int userId,int entityType){
        return BIZ_FOLLOWER + SPLIT +String.valueOf(userId)+SPLIT+String.valueOf(entityType);
    }

    //redis中被关注者的key值
    //用户保存关注该用户的Id值
    //可以获取用户的关注question的集合
    public static String getFollweeKey(int entityId,int entityType){
        return BIZ_FOLLOWEE + SPLIT +String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
    }

}
