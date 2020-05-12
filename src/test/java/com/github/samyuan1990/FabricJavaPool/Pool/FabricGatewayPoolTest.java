package com.github.samyuan1990.FabricJavaPool.Pool;

import com.github.samyuan1990.FabricJavaPool.FabricConnectionPoolFactory;
import com.github.samyuan1990.FabricJavaPool.FabricJavaPoolConfig;
import com.github.samyuan1990.FabricJavaPool.api.FabricConnection;
import com.github.samyuan1990.FabricJavaPool.util.TestUtil;
import org.apache.commons.pool2.ObjectPool;
import org.junit.Test;

import static org.junit.Assert.*;

public class FabricGatewayPoolTest {

    @Test
    public void testGatewayPoolConfig() throws Exception {
        FabricJavaPoolConfig config = new FabricJavaPoolConfig();
        ObjectPool<FabricConnection> fGWP = new FabricGatewayPool(TestUtil.userName, TestUtil.myChannel, config);
        FabricConnection contractConnect1 = fGWP.borrowObject();
        FabricConnection contractConnect2 = fGWP.borrowObject();
        assertNotEquals(contractConnect1, contractConnect2);
    }

    @Test
    public void testGatewayPool() throws Exception {
        ObjectPool<FabricConnection> fGWP = FabricConnectionPoolFactory.getPool(TestUtil.userName, TestUtil.myChannel);
        FabricConnection contractConnect1 = fGWP.borrowObject();
        FabricConnection contractConnect2 = fGWP.borrowObject();
        assertNotEquals(contractConnect1, contractConnect2);
    }

    @Test public void testGatewayPoolException() {
        ObjectPool<FabricConnection>  fabricConnectionPool = new FabricGatewayPool(null, TestUtil.myChannel);
        try {
            fabricConnectionPool.borrowObject();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

}