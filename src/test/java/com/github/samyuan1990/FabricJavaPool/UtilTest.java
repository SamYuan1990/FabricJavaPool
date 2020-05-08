package com.github.samyuan1990.FabricJavaPool;

import com.github.samyuan1990.FabricJavaPool.util.TestUtil;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void generateChainCodeID() {
        ChaincodeID cci = Util.generateChainCodeID(TestUtil.myCC, TestUtil.myCCVersion);
        Assert.assertEquals(cci.getName(), TestUtil.myCC);
        Assert.assertEquals(cci.getVersion(), TestUtil.myCCVersion);
    }
}