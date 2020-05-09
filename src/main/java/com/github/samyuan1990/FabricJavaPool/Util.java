package com.github.samyuan1990.FabricJavaPool;

import org.hyperledger.fabric.sdk.ChaincodeID;

public class Util {

    private Util() {

    }

    public static String resultOnPeersDiff = "Result on Peers not same.";

    public static String errorHappenDuringQuery = "Error happen during query.";

    public static ChaincodeID generateChainCodeID(String myChannel, String version) {
        return ChaincodeID.newBuilder().setName(myChannel).setVersion(version).build();
    }
}
