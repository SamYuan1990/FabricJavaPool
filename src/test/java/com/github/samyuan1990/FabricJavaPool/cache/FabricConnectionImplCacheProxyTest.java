package com.github.samyuan1990.FabricJavaPool.cache;

import java.lang.reflect.Method;

import com.github.samyuan1990.FabricJavaPool.ExecuteResult;
import net.rubyeye.xmemcached.MemcachedClient;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FabricConnectionImplCacheProxyTest {

    static String noQuery() {
        return "a";
    }

    static ExecuteResult query(String a, String b) {
        return new ExecuteResult("a", null);
    }

    static ExecuteResult invoke(String a, String b) {
        return new ExecuteResult("a", null);
    }

    @Test
    public void invokeFunctionQuery() throws Throwable {
        FabricConnectionImplCacheProxy test = new FabricConnectionImplCacheProxy("test", "user", "mychannel", 10);
        Method method = FabricConnectionImplCacheProxyTest.class.getDeclaredMethod("query", String.class, String.class);
        MemcachedClient memcachedClient = mock(MemcachedClient.class);
        when(memcachedClient.get((String) any())).thenReturn(null);
        //when(memcachedClient.set((String) any(), (int) any(), (String) any())).thenReturn(true);
        test.setMemcachedClient(memcachedClient);
        ExecuteResult rs = (ExecuteResult) test.invoke(null, method, new Object[]{"1", "2"});
        Assert.assertEquals("a", rs.getResult());
    }

    @Test
    public void invokeFunctionQuery2() throws Throwable {
        FabricConnectionImplCacheProxy test = new FabricConnectionImplCacheProxy("test", "user", "mychannel", 10);
        Method method = FabricConnectionImplCacheProxyTest.class.getDeclaredMethod("query", String.class, String.class);
        MemcachedClient memcachedClient = mock(MemcachedClient.class);
        when(memcachedClient.get((String) any())).thenReturn(new ExecuteResult("b", null));
        //when(memcachedClient.set((String) any(), (int) any(), (String) any())).thenReturn(true);
        test.setMemcachedClient(memcachedClient);
        ExecuteResult rs = (ExecuteResult) test.invoke(null, method, new Object[]{"1", "2"});
        Assert.assertEquals("b", rs.getResult());
    }

    @Test
    public void invokeFunctionOthers() throws Throwable {
        FabricConnectionImplCacheProxy test = new FabricConnectionImplCacheProxy("test", "test", "test", 10);
        Method method = FabricConnectionImplCacheProxyTest.class.getDeclaredMethod("noQuery");
        String rs = (String) test.invoke(null, method, null);
        Assert.assertEquals("a", rs);
    }

    @Test
    public void invokeFunctionInvoke() throws Throwable {
        FabricConnectionImplCacheProxy test = new FabricConnectionImplCacheProxy("test", "user", "mychannel", 10);
        Method method = FabricConnectionImplCacheProxyTest.class.getDeclaredMethod("invoke", String.class, String.class);
        MemcachedClient memcachedClient = mock(MemcachedClient.class);
        when(memcachedClient.get((String) any())).thenReturn("b");
        //when(memcachedClient.set((String) any(), (int) any(), (String) any())).thenReturn(true);
        test.setMemcachedClient(memcachedClient);
        ExecuteResult rs = (ExecuteResult) test.invoke(null, method, new Object[]{"1", "2"});
        Assert.assertEquals("a", rs.getResult());
    }
}