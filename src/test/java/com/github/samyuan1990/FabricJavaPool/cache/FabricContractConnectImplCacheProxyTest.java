package com.github.samyuan1990.FabricJavaPool.cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.rubyeye.xmemcached.MemcachedClient;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FabricContractConnectImplCacheProxyTest {

    static String noQuery() {
        return "a";
    }

    static String query(String a, String b) {
        return "a";
    }

    @Test
    public void genericKey() {
        FabricContractConnectImplCacheProxy test = new FabricContractConnectImplCacheProxy("test", "test", "test", 10);
        String rs = test.genericKey("user", "mychannel", new Object[]{"1", "2"});
        Assert.assertEquals("12usermychannel", rs);
    }

    @Test
    public void invokeForQuery() throws Throwable {
        FabricContractConnectImplCacheProxy test = new FabricContractConnectImplCacheProxy("test", "user", "mychannel", 10);
        Method method = FabricContractConnectImplCacheProxyTest.class.getDeclaredMethod("query", String.class, String.class);
        MemcachedClient memcachedClient = mock(MemcachedClient.class);
        when(memcachedClient.get((String) any())).thenReturn(null);
        //when(memcachedClient.set((String) any(), (int) any(), (String) any())).thenReturn(true);
        test.setMemcachedClient(memcachedClient);
        String rs = (String) test.invoke(null, method, new Object[]{"1", "2"});
        Assert.assertEquals("a", rs);
    }

    @Test
    public void invokeForQuery2() throws Throwable {
        FabricContractConnectImplCacheProxy test = new FabricContractConnectImplCacheProxy("test", "user", "mychannel", 10);
        Method method = FabricContractConnectImplCacheProxyTest.class.getDeclaredMethod("query", String.class, String.class);
        MemcachedClient memcachedClient = mock(MemcachedClient.class);
        when(memcachedClient.get((String) any())).thenReturn("b");
        //when(memcachedClient.set((String) any(), (int) any(), (String) any())).thenReturn(true);
        test.setMemcachedClient(memcachedClient);
        String rs = (String) test.invoke(null, method, new Object[]{"1", "2"});
        Assert.assertEquals("b", rs);
    }

    @Test
    public void invokeNonQuery() throws Throwable {
        FabricContractConnectImplCacheProxy test = new FabricContractConnectImplCacheProxy("test", "test", "test", 10);
        Method method = FabricContractConnectImplCacheProxyTest.class.getDeclaredMethod("noQuery");
        String rs = (String) test.invoke(null, method, null);
        Assert.assertEquals("a", rs);
    }
}