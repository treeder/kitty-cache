package com.appoxy.kittycache;

/**
 * User: treeder
 * Date: Mar 24, 2009
 * Time: 7:40:14 PM
 */
public interface KCache {
    void put(Object key, Object value, Integer secondsToLive);
}
