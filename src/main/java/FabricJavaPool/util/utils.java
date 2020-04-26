package FabricJavaPool.util;
import java.nio.file.Paths;

import com.google.protobuf.ByteString;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import java.io.BufferedWriter;
import java.util.Collection;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static java.lang.String.format;

public class utils {

    public static String config_network_path = "./src/main/resources/Networkconfig.json";
    public static String config_user_path = "./crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore";
	public static String HospitalCC ="HospitalCC";
	public static String MarketCC="MarketCC";
	public static HFClient hfclient = HFClient.createNewInstance();
 	public static User appuser = null;
	public static Channel mychannel = null;

    public static String Invoke(Channel mychannel,String chaincodeName,String fcn,String... arguments) {
		String payload="";
		try {
			ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName)
					.setVersion("1.0")
					.build();
			TransactionProposalRequest transactionProposalRequest = hfclient.newTransactionProposalRequest();
			transactionProposalRequest.setChaincodeID(chaincodeID);
			transactionProposalRequest.setFcn(fcn);
			transactionProposalRequest.setArgs(arguments);
			//transactionProposalRequest.setProposalWaitTime(500);
			transactionProposalRequest.setUserContext(appuser);

			Collection<ProposalResponse> invokePropResp = mychannel.sendTransactionProposal(transactionProposalRequest);
			for (ProposalResponse response : invokePropResp) {
				if (response.getStatus() == ChaincodeResponse.Status.SUCCESS) {
					//System.out.printf("Successful transaction proposal response Txid: %s from peer %s\n", response.getTransactionID(), response.getPeer().getName());
					//System.out.println("response :"+response);
					//System.out.println("response msg :"+response.getMessage());
					payload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
					//System.out.println(payload);
				}
			}

			mychannel.sendTransaction(invokePropResp);
		} catch (Exception e) {
			System.out.printf(e.toString());
		}

		return payload;
	}

	public static String Query(Channel mychannel,String chaincodeName,String fcn,String... arguments) {
		String payload="";
		try {
			ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName)
					.setVersion("1.0")
					.build();
			QueryByChaincodeRequest transactionProposalRequest = hfclient.newQueryProposalRequest();
			transactionProposalRequest.setChaincodeID(chaincodeID);
			transactionProposalRequest.setFcn(fcn);
			transactionProposalRequest.setArgs(arguments);
			//transactionProposalRequest.setProposalWaitTime(500);
			transactionProposalRequest.setUserContext(appuser);

			Collection<ProposalResponse> queryPropResp = mychannel.queryByChaincode(transactionProposalRequest);
			for(ProposalResponse response:queryPropResp) {
				if (response.getStatus() == ChaincodeResponse.Status.SUCCESS) {
					payload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
					//System.out.println(payload);
				}
			}
			//mychannel.queryByChaincode(queryPropResp);
		} catch (Exception e) {
			System.out.printf(e.toString());
		}

		return payload;
	}
}
