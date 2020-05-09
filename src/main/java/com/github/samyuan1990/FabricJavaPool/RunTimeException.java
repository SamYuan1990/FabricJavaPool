package com.github.samyuan1990.FabricJavaPool;

import org.hyperledger.fabric.sdk.ChaincodeResponse;

public class RunTimeException extends Exception {

    private ChaincodeResponse.Status status;

    public ChaincodeResponse.Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    private String msg;

    public RunTimeException(ChaincodeResponse.Status status, String msg) {
        super(msg);
        this.status = status;
        this.msg = msg;
    }

}
