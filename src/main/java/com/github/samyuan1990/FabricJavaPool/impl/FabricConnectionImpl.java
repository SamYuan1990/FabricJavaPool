package com.github.samyuan1990.FabricJavaPool.impl;

import java.util.Collection;

import com.github.samyuan1990.FabricJavaPool.ExecuteResult;
import com.github.samyuan1990.FabricJavaPool.RunTimeException;
import com.github.samyuan1990.FabricJavaPool.Util;
import com.github.samyuan1990.FabricJavaPool.api.FabricConnection;
import org.hyperledger.fabric.sdk.*;

public class FabricConnectionImpl implements FabricConnection {

    public FabricConnectionImpl() {
        this.hfclient = HFClient.createNewInstance();
    }

    public FabricConnectionImpl(HFClient hfclient, Channel mychannel, User user) {
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

    public ExecuteResult query(String chainCode, String fcn, String... arguments) throws Exception {
        ChaincodeID cci = ChaincodeID.newBuilder().setName(chainCode).build();
        return this.query(cci, fcn, arguments);
    }

    public ExecuteResult query(ChaincodeID chaincodeID, String fcn, String... arguments) throws Exception {
        QueryByChaincodeRequest transactionProposalRequest = hfclient.newQueryProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(arguments);
        transactionProposalRequest.setUserContext(getUser());
        Collection<ProposalResponse> queryPropResp = getMychannel().queryByChaincode(transactionProposalRequest);
        return processProposalResponses(queryPropResp);
    }

    public ExecuteResult invoke(ChaincodeID chaincodeID, String fcn, String... arguments) throws Exception {
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

    public ExecuteResult invoke(String chainCode, String fcn, String... arguments) throws Exception {
        ChaincodeID cci = ChaincodeID.newBuilder().setName(chainCode).build();
        return this.invoke(cci, fcn, arguments);
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
