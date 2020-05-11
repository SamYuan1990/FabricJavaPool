package com.github.samyuan1990.FabricJavaPool.impl;

import com.github.samyuan1990.FabricJavaPool.ExecuteResult;
import com.github.samyuan1990.FabricJavaPool.util.TestUtil;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class FabricContractConnectImplTest {

    @Test
    public void query() throws Exception {
        Network mockNetWork = mock(Network.class);
        Contract mockContract = mock(Contract.class);
        Mockito.when(mockNetWork.getContract(TestUtil.myCC)).thenReturn(mockContract);
        Mockito.when(mockContract.evaluateTransaction(any(), any())).thenReturn("90".getBytes());
        FabricContractConnectImpl fCCI = new FabricContractConnectImpl(mockNetWork);
        ExecuteResult eR = fCCI.query(TestUtil.myCC, "query", "a");
        Assert.assertEquals("90", eR.getResult());

    }

    @Test
    public void invoke() throws Exception {
        Network mockNetWork = mock(Network.class);
        Contract mockContract = mock(Contract.class);
        Mockito.when(mockNetWork.getContract(TestUtil.myCC)).thenReturn(mockContract);
        Mockito.when(mockContract.submitTransaction(any(), any())).thenReturn("90".getBytes());
        FabricContractConnectImpl fCCI = new FabricContractConnectImpl(mockNetWork);
        ExecuteResult eR = fCCI.invoke(TestUtil.myCC, "query", "a");
        Assert.assertEquals("90", eR.getResult());
    }
}