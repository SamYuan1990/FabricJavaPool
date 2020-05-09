package com.github.samyuan1990.FabricJavaPool;

import java.util.Collection;

import org.hyperledger.fabric.sdk.ProposalResponse;

public class ExecuteResult {
    public String getResult() {
        return result;
    }

    public Collection<ProposalResponse> getPropResp() {
        return propResp;
    }

    private String result;
    private Collection<ProposalResponse> propResp;

    public ExecuteResult(String payload, Collection<ProposalResponse> propResp) {
        this.result = payload;
        this.propResp = propResp;
    }
}
