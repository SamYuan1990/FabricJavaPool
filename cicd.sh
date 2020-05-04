#!/usr/bin/env bash
git submodule update --init --recursive
cd fabric-samples
pwd
git branch -a
git checkout release-1.4
curl -vsS https://raw.githubusercontent.com/hyperledger/fabric/master/scripts/bootstrap.sh -o ./scripts/bootstrap.sh
chmod +x ./scripts/bootstrap.sh
./scripts/bootstrap.sh 1.4.4 1.4.4 1.4.4
cd ..
cp -r crypto-config ./fabric-samples/first-network
cp ./byfn.sh ./fabric-samples/first-network
cd fabric-samples/first-network
./byfn.sh up -i 1.4.4 -s couchdb -a
cd ../..
echo byfn=byfn
sudo echo 127.0.0.1       peer0.org2.example.com >> /etc/hosts
sudo echo 127.0.0.1       orderer.example.com >> /etc/hosts
sudo echo 127.0.0.1       peer0.org1.example.com >> /etc/hosts
gradle -v
gradle test