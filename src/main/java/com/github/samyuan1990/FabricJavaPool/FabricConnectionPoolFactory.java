package com.github.samyuan1990.FabricJavaPool;

import com.github.samyuan1990.FabricJavaPool.Pool.FabricGatewayPool;
import com.github.samyuan1990.FabricJavaPool.Pool.FabricJavaPool;
import com.github.samyuan1990.FabricJavaPool.api.FabricConnection;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.hyperledger.fabric.sdk.User;

public class FabricConnectionPoolFactory {

    private FabricConnectionPoolFactory() {

    }

    public static GenericObjectPool<FabricConnection> getPool(String userName, String channel) {
        return new FabricGatewayPool(userName, channel);
    }

    public static GenericObjectPool<FabricConnection> getPool(User appUser, String channel) {
        return new FabricJavaPool(appUser, channel);
    }

}
