package com.github.samyuan1990.FabricJavaPool;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FabricJavaPoolConfigTest {

    @Test
    public void setConfigNetworkPath() {
        FabricJavaPoolConfig fJPC = new FabricJavaPoolConfig("AAA");
        Assert.assertEquals("AAA", fJPC.getConfigNetworkPath());
    }

    @Test
    public void loadConfig() {
        FabricJavaPoolConfig fJPC = new FabricJavaPoolConfig();
        Assert.assertEquals("./src/test/resources/Networkconfig.json", fJPC.getConfigNetworkPath());
        Assert.assertEquals(10, fJPC.getMaxTotal());
        Assert.assertEquals(8, fJPC.getMaxIdle());
        Assert.assertEquals(2, fJPC.getMinIdle());
        Assert.assertEquals(1000, fJPC.getMaxWaitMillis());
        Assert.assertEquals("./src/test/resources/crypto-wallet/peerOrganizations/org1.example.com/users", fJPC.getWalletPath());
    }
}