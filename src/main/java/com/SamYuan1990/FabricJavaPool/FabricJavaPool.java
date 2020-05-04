/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.SamYuan1990.FabricJavaPool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;

public class FabricJavaPool extends GenericObjectPool<Channel> {

    public FabricJavaPool(String config_network_path,User appUser,String channel, GenericObjectPoolConfig config) {
            super(new ChannelPoolFactory(config_network_path,appUser,channel), config);
    }

    public FabricJavaPool(String config_network_path,User appUser,String channel, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
            super(new ChannelPoolFactory(config_network_path,appUser,channel), config, abandonedConfig);
    }

    public FabricJavaPool(String config_network_path,User appUser,String channel){
            super(new ChannelPoolFactory(config_network_path,appUser,channel));
    }


    private static class ChannelPoolFactory extends BasePooledObjectFactory<Channel> {

        private String config_network_path = "";
        private User appUser;
        private String channel="";

        ChannelPoolFactory(String config_network_path, User appUser, String channel){
            this.config_network_path=config_network_path;
            this.appUser=appUser;
            this.channel=channel;
        }

        @Override
        public Channel create() throws Exception {
            Channel myChannel;
            HFClient hfclient = HFClient.createNewInstance();
            CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
            hfclient.setCryptoSuite(cryptoSuite);
            NetworkConfig networkConfig = NetworkConfig.fromJsonFile(new File(config_network_path));//(config_network_path);
            hfclient.setUserContext(appUser);
            hfclient.loadChannelFromConfig(channel, networkConfig);
            myChannel = hfclient.getChannel(channel);
            myChannel.initialize();
            return myChannel;
        }

        @Override
        public PooledObject<Channel> wrap(Channel obj) {
            return new DefaultPooledObject<>(obj);
        }

        @Override
        public boolean validateObject(final PooledObject<Channel> pooledObject) {
            Channel pooledObj = pooledObject.getObject();
            return pooledObj.isInitialized() & !pooledObj.isShutdown();
        }
    }
}
