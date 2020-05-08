package com.github.samyuan1990.FabricJavaPool;

import java.util.Collection;
import org.hyperledger.fabric.sdk.*;

public class FabricConnection {

    public Channel getMychannel() {
        return mychannel;
    }

    public void setMychannel(Channel mychannel) {
        this.mychannel = mychannel;
    }

    private Channel mychannel;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public String query(String chainCodeName, String chainCodeVersion, String fcn, String... arguments)  throws Exception {
        String payload = "";
        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(chainCodeName)
                    .setVersion(chainCodeVersion)
                    .build();
        HFClient hfclient = HFClient.createNewInstance();
        QueryByChaincodeRequest transactionProposalRequest = hfclient.newQueryProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(arguments);
        transactionProposalRequest.setUserContext(getUser());
        Collection<ProposalResponse> queryPropResp = getMychannel().queryByChaincode(transactionProposalRequest);
        for (ProposalResponse response:queryPropResp) {
            if (response.getStatus() == ChaincodeResponse.Status.SUCCESS) {
                payload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
                return payload;
            }
        }
        return payload;
    }

    public String invoke(String chaincodeName, String chainCodeVersion, String fcn, String... arguments) throws Exception {
        String payload = "";
        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName)
                    .setVersion(chainCodeVersion)
                    .build();
        HFClient hfclient = HFClient.createNewInstance();
        TransactionProposalRequest transactionProposalRequest = hfclient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(arguments);
        //transactionProposalRequest.setProposalWaitTime(500);
        transactionProposalRequest.setUserContext(getUser());
        Collection<ProposalResponse> invokePropResp = getMychannel().sendTransactionProposal(transactionProposalRequest);
        for (ProposalResponse response : invokePropResp) {
            if (response.getStatus() == ChaincodeResponse.Status.SUCCESS) {
                payload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
            } else {
                return "";
            }
        }
        getMychannel().sendTransaction(invokePropResp); //CompletableFuture<BlockEvent.TransactionEvent> events
        return payload;
    }
}
