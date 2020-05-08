package com.github.samyuan1990.FabricJavaPool.util;

import java.io.File;
import java.nio.file.Paths;
import org.hyperledger.fabric.sdk.User;
import static java.lang.String.format;

public class Util {
    private static String configUserPath = "./src/test/resources/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore";

    public static String myChannel = "mychannel";

    public static String myCC = "mycc";

    public static String myCCVersion = "1.0";

    public static String netWorkConfig = "./src/test/resources/Networkconfig.json";

    private Util() {
    }

    public static User getUser() {
        User appuser = null;
        File sampleStoreFile = new File(System.getProperty("user.home") + "/test.properties");
        if (sampleStoreFile.exists()) { //For testing start fresh
            sampleStoreFile.delete();
        }
        final SampleStore sampleStore = new SampleStore(sampleStoreFile);
        try {
            appuser = sampleStore.getMember("peer1", "Org1", "Org1MSP",
                    new File(String.valueOf(findFileSk(Paths.get(configUserPath).toFile()))),
                    new File("./src/test/resources/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appuser;
    }

    private static File findFileSk(File directory) {
        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));
        if (null == matches) {
            throw new RuntimeException(format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }
        if (matches.length != 1) {
            throw new RuntimeException(format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }
        return matches[0];
    }
}
