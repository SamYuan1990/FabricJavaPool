package com.github.samyuan1990.FabricJavaPool.cache;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class FabricContractConnectImplCacheProxy implements InvocationHandler {

    Object obj;

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public FabricContractConnectImplCacheProxy(Object obj, String userName, String channelName, int timeout) {
        this.timeout = timeout;
        this.channelName = channelName;
        this.userName = userName;
        this.cacheURL = cacheURL;
        this.obj = obj;
    }

    MemcachedClient memcachedClient;
    String cacheURL;
    String userName;
    String channelName;
    int timeout;

    public FabricContractConnectImplCacheProxy(Object obj, String cacheURL, String userName, String channelName, int timeout) {
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

    String genericKey(String user, String channel, Object[] args) {
        String key = "";
        key = key.concat(args[0].toString());
        key = key.concat(args[1].toString());
        if (args.length > 2) {
            String[] list = (String[]) args[2];
            for (Object l : list) {
                key = key.concat(l.toString());
            }
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
        }
        return result;
    }
}