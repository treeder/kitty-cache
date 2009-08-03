package com.appoxy.kittycache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


/**
 * User: treeder
 * Date: Mar 24, 2009
 * Time: 7:37:21 PM
 */
public class EhcacheWrapper implements KCache {
    private Cache ehcache;

    public EhcacheWrapper(Cache ehcache) {
        this.ehcache = ehcache;
    }

    public void put(Object key, Object value, Integer secondsToLive) {
        ehcache.put(new Element(key, value));
    }

}
