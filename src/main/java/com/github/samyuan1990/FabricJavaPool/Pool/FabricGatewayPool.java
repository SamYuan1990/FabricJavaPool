package com.github.samyuan1990.FabricJavaPool.Pool;

import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.samyuan1990.FabricJavaPool.FabricJavaPoolConfig;
import com.github.samyuan1990.FabricJavaPool.api.FabricConnection;
import com.github.samyuan1990.FabricJavaPool.cache.CacheProxy;
import com.github.samyuan1990.FabricJavaPool.impl.FabricConnectionImpl;
import com.github.samyuan1990.FabricJavaPool.impl.FabricContractConnectImpl;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;

public class FabricGatewayPool extends GenericObjectPool<FabricConnection> {

    public FabricGatewayPool(String userName, String channel) {
        super(new ContractPoolFactory(userName, channel), new FabricJavaPoolConfig());
    }

    public FabricGatewayPool(String userName, String channel, FabricJavaPoolConfig config) {
        super(new ContractPoolFactory(config, userName, channel), config);
    }

    private static class ContractPoolFactory extends BasePooledObjectFactory<FabricConnection> {

        private FabricJavaPoolConfig config =  new FabricJavaPoolConfig();

        ContractPoolFactory(String userName, String channel) {
            this.userName = userName;
            this.channel = channel;
        }

        ContractPoolFactory(FabricJavaPoolConfig config, String userName, String channel) {
            this.config = config;
            this.userName = userName;
            this.channel = channel;
        }

        private String userName;
        private String channel;

        @Override
        public FabricConnection create() throws Exception {
            Path walletDirectory = Paths.get(config.getWalletPath());
            Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

            // Path to a common connection profile describing the network.
            Path networkConfigFile = Paths.get(config.getConfigNetworkPath());

            // Configure the gateway connection used to access the network.
            Gateway.Builder builder = Gateway.createBuilder()
                    .identity(wallet, userName)
                    .networkConfig(networkConfigFile);

            // Create a gateway connection
            Gateway gateway = builder.connect();
            // Obtain a smart contract deployed on the network.
            FabricContractConnectImpl fCCI = new FabricContractConnectImpl(gateway.getNetwork(channel));
            if (config.isUseCache()) {
                CacheProxy proxy = new CacheProxy(fCCI, config.getCacheURL(), userName, channel, config.getCacheTimeout());
                return (FabricConnection) Proxy.newProxyInstance(FabricContractConnectImpl.class.getClassLoader(), new Class[]{FabricConnection.class}, proxy);
            } else {
                return fCCI;
            }
        }

        @Override
        public PooledObject<FabricConnection> wrap(FabricConnection obj) {
            return new DefaultPooledObject<>(obj);
        }

    }
}
