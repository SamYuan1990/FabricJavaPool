package com.github.samyuan1990.FabricJavaPool;

import org.hyperledger.fabric.sdk.ChaincodeID;

public class Util {

    private Util() {

    }

    public static ChaincodeID generateChainCodeID(String myChannel, String version) {
        return ChaincodeID.newBuilder().setName(myChannel).setVersion(version).build();
    }
}
