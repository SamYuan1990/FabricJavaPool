package com.github.samyuan1990.FabricJavaPool;

import java.io.Serializable;
import java.util.Collection;

import org.hyperledger.fabric.sdk.ProposalResponse;

public class ExecuteResult implements Serializable {
    public String getResult() {
        return result;
    }

    public Collection<ProposalResponse> getPropResp() {
        return propResp;
    }

    private String result;
    private static Collection<ProposalResponse> propResp;

    public ExecuteResult(String payload, Collection<ProposalResponse> propResp) {
        this.result = payload;
        this.propResp = propResp;
    }

}
