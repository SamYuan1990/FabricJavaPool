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


# Supported version
TBD

# To do
v0.1 as basic version

0) add query and invoke test base on first network
1) add ci/cd test basing on first network pipeline
2) able to read config file
3) documentation
5) add release version table
6) lint
7) test coverage
8) release to mvn

v0.2 as multi version support

9) ci/cd test with different version and update support map
