package com.github.samyuan1990.FabricJavaPool;

import java.io.File;
import com.github.samyuan1990.FabricJavaPool.util.Util;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.junit.Assert;
import org.junit.Test;

public class FabricConnectionTest {

    @Test
    public void getUser() {
        FabricConnection fc = new FabricConnection();
        fc.setUser(Util.getUser());
        User user = fc.getUser();
        Assert.assertEquals(Util.getUser().getName(), user.getName());
        Assert.assertEquals(Util.getUser().getEnrollment().getCert(), user.getEnrollment().getCert());
    }

    @Test
    public void query() {
        if (System.getenv().containsKey("ORG_GRADLE_PROJECT_LocalFabric") && System.getenv("ORG_GRADLE_PROJECT_LocalFabric").equals("true")) {
            try {
                FabricConnection myConnection = new FabricConnection();
                HFClient hfclient = HFClient.createNewInstance();
                CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
                hfclient.setCryptoSuite(cryptoSuite);
                NetworkConfig networkConfig = NetworkConfig.fromJsonFile(new File(Util.netWorkConfig));
                hfclient.setUserContext(Util.getUser());
                hfclient.loadChannelFromConfig(Util.myChannel, networkConfig);
                Channel myChannel = hfclient.getChannel(Util.myChannel);
                myChannel.initialize();
                myConnection.setMychannel(myChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.query(Util.myCC, Util.myCCVersion, "query", "a");
                Assert.assertEquals("90", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void queryEmpty() {
        if (System.getenv().containsKey("ORG_GRADLE_PROJECT_LocalFabric") && System.getenv("ORG_GRADLE_PROJECT_LocalFabric").equals("true")) {
            try {
                FabricConnection myConnection = new FabricConnection();
                HFClient hfclient = HFClient.createNewInstance();
                CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
                hfclient.setCryptoSuite(cryptoSuite);
                NetworkConfig networkConfig = NetworkConfig.fromJsonFile(new File(Util.netWorkConfig));
                hfclient.setUserContext(Util.getUser());
                hfclient.loadChannelFromConfig(Util.myChannel, networkConfig);
                Channel myChannel = hfclient.getChannel(Util.myChannel);
                myChannel.initialize();
                myConnection.setMychannel(myChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.query(Util.myCC, Util.myCCVersion, "error", "a");
                Assert.assertEquals("", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void invoke() {
        if (System.getenv().containsKey("ORG_GRADLE_PROJECT_LocalFabric") && System.getenv("ORG_GRADLE_PROJECT_LocalFabric").equals("true")) {
            try {
                FabricConnection myConnection = new FabricConnection();
                HFClient hfclient = HFClient.createNewInstance();
                CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
                hfclient.setCryptoSuite(cryptoSuite);
                NetworkConfig networkConfig = NetworkConfig.fromJsonFile(new File(Util.netWorkConfig));
                hfclient.setUserContext(Util.getUser());
                hfclient.loadChannelFromConfig(Util.myChannel, networkConfig);
                Channel myChannel = hfclient.getChannel(Util.myChannel);
                myChannel.initialize();
                myConnection.setMychannel(myChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.invoke(Util.myCC, Util.myCCVersion, "query", "a");
                Assert.assertEquals("90", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void invokeError() {
        if (System.getenv().containsKey("ORG_GRADLE_PROJECT_LocalFabric") && System.getenv("ORG_GRADLE_PROJECT_LocalFabric").equals("true")) {
            try {
                FabricConnection myConnection = new FabricConnection();
                HFClient hfclient = HFClient.createNewInstance();
                CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
                hfclient.setCryptoSuite(cryptoSuite);
                NetworkConfig networkConfig = NetworkConfig.fromJsonFile(new File(Util.netWorkConfig));
                hfclient.setUserContext(Util.getUser());
                hfclient.loadChannelFromConfig(Util.myChannel, networkConfig);
                Channel myChannel = hfclient.getChannel(Util.myChannel);
                myChannel.initialize();
                myConnection.setMychannel(myChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.invoke(Util.myCC, Util.myCCVersion, "error", "a");
                Assert.assertEquals("", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}