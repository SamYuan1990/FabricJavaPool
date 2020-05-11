package com.github.samyuan1990.FabricJavaPool;

import com.github.samyuan1990.FabricJavaPool.impl.FabricContractConnectImpl;
import com.github.samyuan1990.FabricJavaPool.util.TestUtil;
import org.apache.commons.pool2.ObjectPool;
import org.junit.Test;

import static org.junit.Assert.*;

public class FabricGatewayPoolTest {

    @Test
    public void testGatewayPool() throws Exception {
        ObjectPool<FabricContractConnectImpl> fGWP = new FabricGatewayPool(TestUtil.userName, TestUtil.myChannel);
        FabricContractConnectImpl contractConnect1 = fGWP.borrowObject();
        FabricContractConnectImpl contractConnect2 = fGWP.borrowObject();
        assertNotEquals(contractConnect1, contractConnect2);
    }

    @Test public void testGatewayPoolException() {
        ObjectPool<FabricContractConnectImpl>  fabricConnectionPool = new FabricGatewayPool(null, TestUtil.myChannel);
        try {
            fabricConnectionPool.borrowObject();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

}