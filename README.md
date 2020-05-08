# FabricJavaPool
**A Connection pool manager for Fabric development**

based on [fabric-sdk-java](https://github.com/hyperledger/fabric-sdk-java) 
following design of JDBC

# Why
Assuming we have a webUI for client, and the UI need request times to fabric network to fetch data.
For performance exception, we don't want to have IO session many times.
So a connection pool basing on user's msp, and try to reuse the connection on java server side by session or cookie. 

# How
Basing common pool and Fabric Java SDK.
Will provide you a config and a pool object of channel obj base on User.
Mostly used as query chain code for a specific user.
Sample usage:
```
	implementation group: 'com.github.samyuan1990', name:'FabricJavaPool', version: '0.0.1'
```
For SNAPSHOT version
```
   repositories {
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    }
	implementation group: 'com.github.samyuan1990', name:'FabricJavaPool', version: '0.0.1-SNAPSHOT'
```
Get Connection
```
        ObjectPool<FabricConnection>  fabricConnectionPool = new FabricJavaPool(TestUtil.netWorkConfig, TestUtil.getUser(), TestUtil.myChannel);
        try {
            FabricConnection fabricConnection = fabricConnectionPool.borrowObject();
            FabricConnection fabricConnection2 = fabricConnectionPool.borrowObject();
            String rs = fabricConnection.query(TestUtil.chaincodeID, "query", "a");
            String rs2 = fabricConnection2.query(TestUtil.chaincodeID, "query", "a");
            }
            fabricConnectionPool.returnObject(fabricConnection);
            fabricConnectionPool.returnObject(fabricConnection2);
        } catch (Exception e) {
            e.printStackTrace();
        }
```
Generate ChaincodeID cci = Util.generateChainCodeID(TestUtil.myCC, TestUtil.myCCVersion); Obj
```
        ChaincodeID cci = Util.generateChainCodeID(TestUtil.myCC, TestUtil.myCCVersion);
```
Query
```
FabricConnection myConnection = myChannelPool.borrowObject();
String rs = myConnection.query(cci, "query", "a");
```
Invoke
```
FabricConnection myConnection = myChannelPool.borrowObject();
String rs = myConnection.invoke(cci, "query", "a");
```

# For dev
Copy crypto-config to your local byfn
```
byfn.sh up
```

Add ect hosts with
```
127.0.0.1       peer0.org2.example.com
127.0.0.1       orderer.example.com
127.0.0.1       peer0.org1.example.com
```
After change.
```
export ORG_GRADLE_PROJECT_LocalFabric=true
gradle clean build
```

# Supported version
Jdk 8
Fabric 1.4.6

# Version
0.0.1 as basic version

# To do
0.0.2
* Add query and invoke support
* Add prop file support.
* Mock test support(optional)

0.0.3
* Add cache from memcache, cache logic as time duration.

0.0.4
* Add cache refresh support by new block listener.