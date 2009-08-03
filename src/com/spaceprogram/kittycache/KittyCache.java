package com.appoxy.kittycache;

import java.util.Map;
import java.util.Queue;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A very simple cache using java.util.concurrent.
 * <p/>
 * User: treeder
 * Date: Mar 15, 2009
 * Time: 8:42:01 PM
 */
public class KittyCache implements KCache {

    private Map<Object, Object[]> cache;
    /**
     * Used to restrict the size of the cache map.
     */
    private Queue queue;
    private int maxSize;
    /**
     * Using this integer because ConcurrentLinkedQueue.size is not constant time.
     */
    private AtomicInteger size = new AtomicInteger();

    public KittyCache(int maxSize) {
        this.maxSize = maxSize;
        cache = new ConcurrentHashMap(maxSize);
        queue = new ConcurrentLinkedQueue();
    }

    public void put(Object key, Object val) {
        put(key, val, null);
    }

    public void put(Object key, Object val, Integer seconds_to_store) {
        if(key == null) throw new RuntimeException("Key cannot be null!");
        seconds_to_store = seconds_to_store != null ? seconds_to_store : 9999999;
        cache.put(key, new Object[]{System.currentTimeMillis() + seconds_to_store, val});
        queue.add(key);
        size.incrementAndGet();

        while (size.get() > maxSize && maxSize > 0) {
            Object toRemove = queue.poll();
            if(toRemove == null) break;
//            System.out.println("toRemove=" + toRemove + " size=" + size.get() + " maxSize=" + maxSize);
            if (toRemove != null) {
                remove(key);
            }
        }
    }

    public Object get(Object key) {
        if (cache.containsKey(key)) {
            Long expires = (Long) cache.get(key)[0];
            if (expires - System.currentTimeMillis() > 0) {
                return cache.get(key)[1];
            } else {
                remove(key);
            }
        }
        return null;
    }

    /** Returns boolean to stay compatible with ehcache and memcached.
     *
     * @see #removeGet for alternate version.
     *
     */
    public boolean remove(Object key) {
        return removeGet(key) != null;
    }

    public Object removeGet(Object key){
        Object[] entry = cache.remove(key);
//        System.out.println("entry=" + entry);
        if (entry != null) {
            return entry[1];
        }
        size.decrementAndGet();
        return null;
    }


    public int size() {
        return size.get();
    }


    public Map getAll(Collection collection) {
        Map ret = new HashMap();
        for (Object o : collection) {
            ret.put(o, cache.get(o));
        }
        return ret;
    }

    public void clear() {
        cache.clear();
    }

    public int mapSize() {
        return cache.size();
    }

    public int queueSize() {
        return queue.size();
    }
}
