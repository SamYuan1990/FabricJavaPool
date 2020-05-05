# FabricJavaPool
A Pool project for Fabric Java SDK, like JDBC pool

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
        ObjectPool<Channel>  myChannelPool= new FabricJavaPool("./src/test/resources/Networkconfig.json",getUser(),"mychannel");
        try {
            Channel myChannel = myChannelPool.borrowObject();
            assertNotEquals("Test borrow item channel not null",myChannel,null);
            assertEquals("Test borrow item channel",myChannel.isInitialized(),true);
            Channel myChannel2 = myChannelPool.borrowObject();
            assertNotEquals("Test borrow item channel2 not null",myChannel2,null);
            assertEquals("Test borrow item channel2",myChannel2.isInitialized(),true);
            assertEquals("Test item should diff",myChannel2.equals(myChannel),false);
            myChannelPool.returnObject(myChannel);
            myChannelPool.returnObject(myChannel2);
            String rs=Query(myChannel,"mycc","query","a");
            assertEquals("90",rs);
            String rs2=Query(myChannel2,"mycc","query","a");
            assertNotEquals("91",rs2);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
export ORG\_GRADLE\_PROJECT_LocalFabric=true
gradle clean test jacocoTestReport jacocoTestCoverageVerification build
```

# Supported version
TBD

# Version
v0.1 as basic version

# To do
1) add ci/cd test basing on first network pipeline
2) release to mvn

v0.2 as multi version support

3) ci/cd test with different version and update support map
