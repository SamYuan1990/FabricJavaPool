package com.github.samyuan1990.FabricJavaPool.impl;

import java.nio.charset.StandardCharsets;

import com.github.samyuan1990.FabricJavaPool.ExecuteResult;
import com.github.samyuan1990.FabricJavaPool.api.FabricConnection;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;


public class FabricContractConnectImpl implements FabricConnection {

    private Network network;

    public FabricContractConnectImpl(Network network) {
        this.network = network;
    }

    @Override
    public ExecuteResult query(String chainCode, String fcn, String... arguments) throws Exception {
        Contract contract = network.getContract(chainCode);
        byte[] queryAllResult = contract.evaluateTransaction(fcn, arguments);
        return new ExecuteResult(new String(queryAllResult, StandardCharsets.UTF_8), null);
    }

    @Override
    public ExecuteResult invoke(String chainCode, String fcn, String... arguments) throws Exception {
        Contract contract = network.getContract(chainCode);
        byte[] invokeAllResult = contract.submitTransaction(fcn, arguments);
        return new ExecuteResult(new String(invokeAllResult, StandardCharsets.UTF_8), null);
    }
}
