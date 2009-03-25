package com.spaceprogram.kittycache;

/**
 * User: treeder
 * Date: Mar 24, 2009
 * Time: 7:40:14 PM
 */
public interface KCache {
    void put(String key, Object value, Integer secondsToLive);
}
