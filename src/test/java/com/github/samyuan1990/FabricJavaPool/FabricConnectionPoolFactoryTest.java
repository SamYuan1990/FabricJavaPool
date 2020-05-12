package com.github.samyuan1990.FabricJavaPool;

import com.github.samyuan1990.FabricJavaPool.api.FabricConnection;
import com.github.samyuan1990.FabricJavaPool.util.TestUtil;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FabricConnectionPoolFactoryTest {

    @Test
    public void getPool() {
        GenericObjectPool<FabricConnection> pool = FabricConnectionPoolFactory.getPool(TestUtil.userName, TestUtil.myChannel);
        Assert.assertNotEquals(null, pool);
    }

    @Test
    public void testGetPool() {
        GenericObjectPool<FabricConnection> pool = FabricConnectionPoolFactory.getPool(TestUtil.getUser(), TestUtil.myChannel);
        Assert.assertNotEquals(null, pool);
    }
}