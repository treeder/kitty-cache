package com.appoxy.kittycache;

import junit.framework.Assert;
import org.junit.Test;

/**
 * User: treeder
 * Date: Mar 15, 2009
 * Time: 9:05:34 PM
 */
public class CacheTest {

    @Test
    public void basicOps(){
        String s = "This is a string I want to cache...";
        KittyCache cache = new KittyCache(100);
        cache.put("x", s);

        Assert.assertEquals(s, cache.get("x"));
        Assert.assertEquals(1, cache.size());

        Object o = cache.remove("x");
        Assert.assertEquals(o, s);

        Assert.assertEquals(0, cache.size());
        Assert.assertEquals(0, cache.mapSize());
    }

    @Test
    public void testOverage(){
        KittyCache cache = new KittyCache(100);
        for(int i = 0; i < 110; i++){
            cache.put(i+"", i, 500);
        }
        Assert.assertEquals(100, cache.size());
        Assert.assertEquals(100, cache.mapSize());
        Assert.assertEquals(100, cache.queueSize());
    }
}
