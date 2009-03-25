package com.spaceprogram.kittycache;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A very simple cache using.
 *
 * User: treeder
 * Date: Mar 15, 2009
 * Time: 8:42:01 PM
 */
public class KittyCache implements KCache {

    private Map<String, Object[]> cache;
    /**
     * Used to restrict the size of the cache map.
     */
    private Queue<String> queue;
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

    public void put(String key, Object val, Integer seconds_to_store) {
        seconds_to_store = seconds_to_store != null ? seconds_to_store : 9999999;
        cache.put(key, new Object[]{System.currentTimeMillis() + seconds_to_store, val});
        queue.add(key);
        size.incrementAndGet();

        while (size.get() > maxSize && maxSize > 0) {
            String to_remove = queue.poll();
            if (to_remove != null) {
                remove(key);
            }
        }
    }

    public Object get(String key) {
        if (cache.containsKey(key)) {
            int expires = (Integer) cache.get(key)[0];
            if (expires - System.currentTimeMillis() > 0) {
                return cache.get(key)[1];
            } else {
                remove(key);
            }
        }
        return null;
    }

    public void remove(String key) {
        cache.remove(key);
        size.decrementAndGet();
    }

    public int size() {
        return size.get();
    }

    public int mapSize() {
        return cache.size();
    }

    public int queueSize() {
        return queue.size();
    }
}
