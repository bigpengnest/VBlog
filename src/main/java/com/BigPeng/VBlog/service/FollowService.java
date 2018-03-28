package com.BigPeng.VBlog.service;

import com.BigPeng.VBlog.model.EntityType;
import com.BigPeng.VBlog.util.JedisAdapter;
import com.BigPeng.VBlog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean follow(int userId ,int entityType,int entityId){
        //userId为follower的Id
        String followerKey = RedisKeyUtil.getFollwerKey(userId, entityType);
        //entityId为followee的Id
        String followeeKey = RedisKeyUtil.getFollweeKey(entityId,entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followerKey,date.getTime(),String.valueOf(entityId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(userId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    public boolean unfollow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollwerKey(userId, entityType);
        String followeeKey = RedisKeyUtil.getFollweeKey(entityId,entityType);

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey, String.valueOf(entityId));
        tx.zrem(followeeKey, String.valueOf(userId));

        List<Object> ret = jedisAdapter.exec(tx, jedis);

        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }


    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    public List<Integer> getFollowers(int entityId, int entityType, int count) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityId,entityType);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityId, int entityType, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityId,entityType);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, offset, count));
    }


    public List<Integer> getFollowees(int entityId, int entityType, int count) {
        String followeeKey = RedisKeyUtil.getFollweeKey(entityId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int entityId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFollweeKey(entityId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, count));
    }

    public long getFollowerCount(int entityId, int entityType) {
        String followerKey = RedisKeyUtil.getFollwerKey(entityId,entityType);
        return jedisAdapter.zcard(followerKey);
    }
    public long getFolloweeCount(int entityId, int entityType) {
        String followeeKey = RedisKeyUtil.getFollweeKey(entityId,entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollwerKey(userId,entityType);
        return jedisAdapter.zscore(followerKey,String.valueOf(entityId))!=null;
    }
    public boolean isFollowee(int entityId,int entityType,int userId){
        String followeeKey = RedisKeyUtil.getFollweeKey(entityId,entityType);
        return jedisAdapter.zscore(followeeKey,String.valueOf(userId))!=null;
    }
}
