package com.github.samyuan1990.FabricJavaPool.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.github.samyuan1990.FabricJavaPool.ExecuteResult;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;

public class FabricConnectionImplCacheProxy extends FabricContractConnectImplCacheProxy implements InvocationHandler {


    public FabricConnectionImplCacheProxy(Object obj, String cacheURL, String userName, String channelName, int timeout) {
        super(obj, cacheURL, userName, channelName, timeout);
    }

    public FabricConnectionImplCacheProxy(String cacheURL, String user, String mychannel, int timeout) {
        super(cacheURL, user, mychannel, timeout);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (method.getName().equals("query")) {
            String key = genericKey(userName, channelName, args);
            result = memcachedClient.get(key);
            if (result != null) {
                System.out.println("hit");
                return result;
            }
            result = method.invoke(obj, args);
            ExecuteResult executeResult = (ExecuteResult) result;
            if (executeResult.getPropResp() == null) {
                return result;
            }
            for (ProposalResponse p : executeResult.getPropResp()) {
                TxReadWriteSetInfo txReadWriteSetInfo = p.getChaincodeActionResponseReadWriteSetInfo();
                for (TxReadWriteSetInfo.NsRwsetInfo nsRwsetInfo : txReadWriteSetInfo.getNsRwsetInfos()) {
                    KvRwset.KVRWSet rws = nsRwsetInfo.getRwset();
                    for (KvRwset.KVRead readList : rws.getReadsList()) {
                        String blockKey = readList.getKey();
                        memcachedClient.set(blockKey, timeout, key);
                    }
                }
            }
            memcachedClient.set(key, timeout, result);
            return result;
        }
        if (method.getName().equals("invoke")) {
            result = method.invoke(obj, args);
            ExecuteResult executeResult = (ExecuteResult) result;
            if (executeResult.getPropResp() == null) {
                return result;
            }
            for (ProposalResponse p : executeResult.getPropResp()) {
                TxReadWriteSetInfo txReadWriteSetInfo = p.getChaincodeActionResponseReadWriteSetInfo();
                for (TxReadWriteSetInfo.NsRwsetInfo nsRwsetInfo : txReadWriteSetInfo.getNsRwsetInfos()) {
                    KvRwset.KVRWSet rws = nsRwsetInfo.getRwset();
                    for (KvRwset.KVRead readList : rws.getReadsList()) {
                        String blockKey = readList.getKey();
                        String blockCache = memcachedClient.get(blockKey);
                        if (!blockCache.equals(null)) {
                            memcachedClient.delete(blockCache);
                            memcachedClient.delete(blockKey);
                        }
                    }
                }
            }
            return result;
        }
        result = method.invoke(obj, args);
        return result;
    }
}
