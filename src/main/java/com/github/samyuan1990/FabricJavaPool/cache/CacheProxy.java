package com.github.samyuan1990.FabricJavaPool.cache;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class CacheProxy implements InvocationHandler {

    private Object obj;
    private MemcachedClient memcachedClient;
    private String cacheURL;
    private String userName;
    private String channelName;
    private int timeout;

    public CacheProxy(Object obj, String cacheURL, String userName, String channelName, int timeout) {
        this.timeout = timeout;
        this.channelName = channelName;
        this.userName = userName;
        this.cacheURL = cacheURL;
        MemcachedClientBuilder memcachedClientBuilder = new XMemcachedClientBuilder(AddrUtil.getAddresses(this.cacheURL));
        try {
            memcachedClient = memcachedClientBuilder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.obj = obj;
    }

    private String genericKey(String user, String channel, Object[] args) {
        String key = "";
        key = key.concat(args[0].toString());
        key = key.concat(args[1].toString());
        String[] list = (String[]) args[2];
        for (Object l : list) {
            key = key.concat(l.toString());
        }
        key = key.concat(user.concat(channel));
        return key;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (!method.getName().equals("query")) {
            result = method.invoke(obj, args);
        } else {
            String key = genericKey(userName, channelName, args);
            result = memcachedClient.get(key);
            if (result != null) {
                System.out.println("hit");
                return result;
            }
            result = method.invoke(obj, args);
            memcachedClient.set(key, timeout, result);
            result = memcachedClient.get(key);
        }
        return result;
    }
}