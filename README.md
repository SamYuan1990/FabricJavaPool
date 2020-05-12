# FabricJavaPool
**A Connection pool manager for Fabric development**

based on [fabric-sdk-java](https://github.com/hyperledger/fabric-sdk-java)  1.4.6 and JDK8
following design of JDBC
Basing common pool and Fabric Java SDK, able with cache support for query with memcache.
Will provide you a config and a pool object of channel obj base on User.
Mostly used as query chain code for a specific user.

# Why
Assuming we have a webUI for client, and the UI need request times to fabric network to fetch data.
For performance exception, we don't want to have IO session many times.
So a connection pool basing on user's msp, and try to reuse the connection on java server side by session or cookie. 

# Sample usage:
## Gradle
```
	implementation group: 'com.github.samyuan1990', name:'FabricJavaPool', version: '0.0.2'
```
For SNAPSHOT version
```
   repositories {
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    }
	implementation group: 'com.github.samyuan1990', name:'FabricJavaPool', version: '0.0.2-SNAPSHOT'
```

## Pool config
Fabric network config file in json format and file path as configNetworkPath.
```
configNetworkPath=./src/test/resources/Networkconfig.json
maxTotal=10
maxIdle=8
minIdle=2
maxWaitMillis=1000
# if you want to use fabric gateway
walletPath=./src/test/resources/crypto-wallet/peerOrganizations/org1.example.com/users
# if you want to use cache
UseCache=true
cacheURL=127.0.0.1:11211
cacheTimeout=300
```

## Get Connection
```
        //if you want to use fabric gateway
        GenericObjectPool<FabricConnection> pool = FabricConnectionPoolFactory.getPool(TestUtil.userName, TestUtil.myChannel);
        //if you want to use fabric java sdk
        GenericObjectPool<FabricConnection> pool = FabricConnectionPoolFactory.getPool(TestUtil.getUser(), TestUtil.myChannel);

        // By Default it will read pool config from ./resources/FabricJavaPool.properties
        try {
            FabricConnection fabricConnectionImpl = fabricConnectionPool.borrowObject();
            FabricConnection fabricConnectionImpl2 = fabricConnectionPool.borrowObject();
            String rs = fabricConnectionImpl.query(TestUtil.chaincodeID, "query", "a");
            String rs2 = fabricConnectionImpl2.query(TestUtil.chaincodeID, "query", "a");
            }
            fabricConnectionPool.returnObject(fabricConnectionImpl);
            fabricConnectionPool.returnObject(fabricConnectionImpl2);
        } catch (Exception e) {
            e.printStackTrace();
        }
```
## Make ChainCode
```
        ChaincodeID cci = Util.generateChainCodeID(TestUtil.myCC, TestUtil.myCCVersion);
```
## Query
```
FabricConnection myConnection = myChannelPool.borrowObject();
ExecuteResult rs = myConnection.query("mycc", "query", "a");
Assert.assertEquals("90", rs.getResult());
```
## Invoke
```
FabricConnection myConnection = myChannelPool.borrowObject();
ExecuteResult rs = myConnection.invoke("mycc", "query", "a");
Assert.assertEquals("90", rs.getResult());
```
## Query or Invoke exception
```
                myConnection.invoke(TestUtil.chaincodeID, "error", "a");
            } catch (RunTimeException e) {
                Assert.assertEquals(ChaincodeResponse.Status.FAILURE, e.getStatus());
                Assert.assertEquals(Util.errorHappenDuringQuery, e.getMsg());
```
## Query or Invoke exception due to chaincode results are different on peers
```
            myConnection.query(TestUtil.chaincodeID, "query", "a");
        } catch (RunTimeException e) {
            Assert.assertEquals(ChaincodeResponse.Status.SUCCESS, e.getStatus());
            Assert.assertEquals(Util.resultOnPeersDiff, e.getMsg());
        }
```

# For develop this project
```
gradle clean build
```
Optional:

Copy crypto-config to your local byfn
```
byfn.sh up
```

Start memcache
```
docker run -p 11211:11211 --name memcache memcached
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

# Version
0.0.1 as basic version
0.0.2 Add query and invoke support, prop file support.

# To do
0.0.3
* Add cache from memcache, cache logic as time duration.

0.0.4
* Add cache refresh support by new block listener.