package com.github.samyuan1990.FabricJavaPool;

import java.util.Collection;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

public class FabricConnection {

    public FabricConnection() {
        this.hfclient = HFClient.createNewInstance();
    }

    public FabricConnection(HFClient hfclient, Channel mychannel, User user) {
        this.hfclient = hfclient;
        this.mychannel = mychannel;
        this.user = user;
    }

    private HFClient hfclient;

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

    public ExecuteResult query(ChaincodeID chaincodeID, String fcn, String... arguments) throws RunTimeException, ProposalException, InvalidArgumentException {
        QueryByChaincodeRequest transactionProposalRequest = hfclient.newQueryProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(arguments);
        transactionProposalRequest.setUserContext(getUser());
        Collection<ProposalResponse> queryPropResp = getMychannel().queryByChaincode(transactionProposalRequest);
        return processProposalResponses(queryPropResp);
    }

    public ExecuteResult invoke(ChaincodeID chaincodeID, String fcn, String... arguments) throws RunTimeException, ProposalException, InvalidArgumentException {
        TransactionProposalRequest transactionProposalRequest = hfclient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(arguments);
        transactionProposalRequest.setUserContext(getUser());
        Collection<ProposalResponse> invokePropResp = getMychannel().sendTransactionProposal(transactionProposalRequest);
        ExecuteResult eR = processProposalResponses(invokePropResp);
        getMychannel().sendTransaction(invokePropResp); //CompletableFuture<BlockEvent.TransactionEvent> events
        return eR;
    }

    private ExecuteResult processProposalResponses(Collection<ProposalResponse> propResp) throws RunTimeException {
        String payload = "";
        int i = 0;
        for (ProposalResponse response : propResp) {
            if (response.getStatus() == ChaincodeResponse.Status.SUCCESS) {
                if (i == 0) {
                    payload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
                }
                String currentPayload = response.getProposalResponse().getResponse().getPayload().toStringUtf8();
                if (null != payload && null != currentPayload && !payload.equals(currentPayload)) {
                    throw new RunTimeException(response.getStatus(), Util.resultOnPeersDiff);
                }
            } else {
                throw new RunTimeException(response.getStatus(), Util.errorHappenDuringQuery);
            }
            i++;
        }
        return new ExecuteResult(payload, propResp);
    }
}
