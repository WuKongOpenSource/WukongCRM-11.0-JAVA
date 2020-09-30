package com.kakarote.core.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzhiwei
 * redis简易封装,如有需要的方法不存在可以在此类中添加
 * 也可以使用RedisTemplate
 */
@Service
@Slf4j
public class RedisImpl implements Redis {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    //key操作

    /**
     * 删除key 可传多个key
     *
     * @param keys keys
     */
    @Override
    public void del(Object... keys) {
        redisTemplate.delete(Arrays.asList(keys));
    }

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key key
     * @return time
     */
    @Override
    public Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 给指定的key设置过期时间  单位为毫秒
     *
     * @param key     key
     * @param timeout 过期时间 单位是秒
     */
    @Override
    public void expire(String key, Integer timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }


    /**
     * 移除key的过期时间，将key永久保存
     *
     * @param key key
     */
    @Override
    public void persist(String key) {
        redisTemplate.persist(key);
    }

    /**
     * 检验该key是否存在 存在返回true
     *
     * @param key key
     */
    @Override
    public boolean exists(String key) {
        Boolean exists = redisTemplate.hasKey(key);
        return exists != null ? exists : false;
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key key
     * @return DataType
     */
    @Override
    public DataType getType(String key) {
        return redisTemplate.type(key);
    }


    /**
     * set字符串
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);

    }

    /**
     * set字符串 并加上失效时间  以豪秒为单位
     *
     * @param key    key
     * @param value  value
     * @param second 失效时间 单位为秒
     */
    @Override
    public void setex(Object key, Integer second, Object value) {
        redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }

    /**
     * 当key不存在时 设置key value
     *
     * @param key   key
     * @param value value
     * @return boolean true为成功，可能为空
     */
    @Override
    public Boolean setNx(String key, Long timeout, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }


    /**
     * 根据key获取value
     *
     * @param key key
     * @return value
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取所有(一个或多个)给定 key 的值
     *
     * @param keys keys
     * @return values
     */
    @Override
    public List<Object> mGet(Object... keys) {
        return redisTemplate.opsForValue().multiGet(Arrays.asList(keys));
    }

    /**
     * 同时设置多个key，value
     *
     * @param map map
     */
    @Override
    public void mSet(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 所有给定的key都不存在时，设置多个key，value
     *
     * @param map map
     */
    @Override
    public void mSetNx(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSetIfAbsent(map);
    }


    /**
     * 当前key存在时  向这个key对应value的默认追加上此字符
     *
     * @param key   key
     * @param value 要追加的字符
     */
    @Override
    public void appendStr(Object key, String value) {
        redisTemplate.opsForValue().append(key, value);
    }


    /**
     * 删除key中指定的hashkey的值  相当于一个key中存储着map值 这个方法就是删除这个key中的map里面的一个或多个key
     *
     * @param key      key
     * @param hashKeys hashKeys
     */
    @Override
    public void hdel(Object key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 添加一个hash值
     *
     * @param key     key
     * @param hashKey 相当于 map中的key
     * @param value   存储的值 相当于map中的value
     */
    @Override
    public void put(Object key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }


    /**
     * 添加一个map
     *
     * @param key key
     * @param map map
     */
    @Override
    public void putAll(String key, Map<Object, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取redis中的map
     *
     * @param key key
     * @return map
     */
    @Override
    public Map<Object, Object> getRedisMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 返回这个key中的所有value
     *
     * @param key key
     * @return value
     */
    @Override
    public List<Object> getValues(Object key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 判断key中的hashKey是否存在
     *
     * @param key     key
     * @param hashKey hashKey
     */
    @Override
    public Boolean hashMapKey(Object key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 从左入栈
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void lpush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从右入栈
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void rpush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }


    /**
     * 从左出栈
     *
     * @param key key
     * @return value
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T lPop(String key) {
        return (T) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从右出栈
     *
     * @param key key
     * @return value
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T rPop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }


    /**
     * 获取该key index处的元素
     *
     * @param key   key
     * @param index index
     * @return value
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getKeyIndex(String key, int index) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取列表的长度
     *
     * @param key key
     * @return length
     */
    @Override
    public Long getLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取key中下标从start到end处的值
     *
     * @param key   key
     * @param start 开始下标
     * @param end   结束下标
     * @return values
     */
    @Override
    public List<Object> range(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    //set操作  无序集合

    /**
     * 向集合中添加
     *
     * @param key    key
     * @param values values
     */
    @Override
    public void addSet(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 移除并取出第一个元素
     *
     * @param key key
     * @return value
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getSet(String key) {
        return (T) redisTemplate.opsForSet().pop(key);
    }

    /**
     * 返回集合中所有的元素
     *
     * @param key key
     * @return values
     */
    @Override
    public Set<Object> getSets(String key) {
        return redisTemplate.opsForSet().members(key);
    }


    /**
     * 返回集合中的长度
     *
     * @param key key
     * @return length
     */
    @Override
    public Long getSetsNum(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 返回集合中的所有元素
     *
     * @param key key
     * @return values
     */
    @Override
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }


    //zSet操作 有序集合

    /**
     * 添加数据
     * <p>
     * 添加方式：
     * 1.创建一个set集合
     * Set<ZSetOperations.TypedTuple<Object>> sets=new HashSet<>();
     * 2.创建一个有序集合
     * ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>(value,排序的数值，越小越在前);
     * 4.放入set集合
     * sets.add(objectTypedTuple1);
     * 5.放入缓存
     * reidsImpl.Zadd("zSet", list);
     *
     * @param key    key
     * @param tuples tuples
     */
    @Override
    public void zadd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        redisTemplate.opsForZSet().add(key, tuples);
    }


    /**
     * 返回 分数在min至max之间的数据 按分数值递减(从大到小)的次序排列。
     *
     * @param key key
     * @param min min
     * @param max max
     * @return values
     */
    @Override
    public Set<Object> reverseRange(String key, Double min, Double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

}
