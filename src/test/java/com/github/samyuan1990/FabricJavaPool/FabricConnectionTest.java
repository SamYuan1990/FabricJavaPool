package com.github.samyuan1990.FabricJavaPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.github.samyuan1990.FabricJavaPool.util.Util;
import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.peer.FabricProposalResponse;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class FabricConnectionTest {

    @Test
    public void getUser() {
        FabricConnection fc = new FabricConnection();
        fc.setUser(Util.getUser());
        User user = fc.getUser();
        Assert.assertEquals(Util.getUser().getName(), user.getName());
        Assert.assertEquals(Util.getUser().getEnrollment().getCert(), user.getEnrollment().getCert());
    }

    @Test
    public void query() throws ProposalException, InvalidArgumentException {
        Channel mockChannel = mock(Channel.class);
        ProposalResponse mockProposalResponse = mock(ProposalResponse.class);
        Collection<ProposalResponse> queryPropResp = new ArrayList<ProposalResponse>();
        queryPropResp.add(mockProposalResponse);
        Mockito.when(mockChannel.queryByChaincode(Mockito.any())).thenReturn(queryPropResp);
        Mockito.when(mockProposalResponse.getStatus()).thenReturn(ChaincodeResponse.Status.SUCCESS);
        Mockito.when(mockProposalResponse.getProposalResponse()).thenReturn(FabricProposalResponse.ProposalResponse.newBuilder().
                setResponse(org.hyperledger.fabric.protos.peer.FabricProposalResponse.Response.newBuilder().setMessage("90").setStatus(1).setPayload(com.google.protobuf.ByteString.copyFromUtf8("90")))
                .setPayload(com.google.protobuf.ByteString.copyFromUtf8("90")).build());
        try {
                FabricConnection myConnection = new FabricConnection();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.query(Util.myCC, Util.myCCVersion, "query", "a");
                Assert.assertEquals("90", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Test
    public void queryEmpty()  throws ProposalException, InvalidArgumentException {
        Channel mockChannel = mock(Channel.class);
        ProposalResponse mockProposalResponse = mock(ProposalResponse.class);
        Collection<ProposalResponse> queryPropResp = new ArrayList<ProposalResponse>();
        queryPropResp.add(mockProposalResponse);
        Mockito.when(mockChannel.queryByChaincode(Mockito.any())).thenReturn(queryPropResp);
        Mockito.when(mockProposalResponse.getStatus()).thenReturn(ChaincodeResponse.Status.FAILURE);
        try {
                FabricConnection myConnection = new FabricConnection();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.query(Util.myCC, Util.myCCVersion, "error", "a");
                Assert.assertEquals("", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Test
    public void invoke()  throws ProposalException, InvalidArgumentException {
        Channel mockChannel = mock(Channel.class);
        ProposalResponse mockProposalResponse = mock(ProposalResponse.class);
        Collection<ProposalResponse> queryPropResp = new ArrayList<ProposalResponse>();
        queryPropResp.add(mockProposalResponse);
        Mockito.when(mockChannel.sendTransactionProposal(Mockito.any())).thenReturn(queryPropResp);
        Mockito.when(mockProposalResponse.getStatus()).thenReturn(ChaincodeResponse.Status.SUCCESS);
        Mockito.when(mockProposalResponse.getProposalResponse()).thenReturn(FabricProposalResponse.ProposalResponse.newBuilder().
                setResponse(org.hyperledger.fabric.protos.peer.FabricProposalResponse.Response.newBuilder().setMessage("90").setStatus(1).setPayload(com.google.protobuf.ByteString.copyFromUtf8("90")))
                .setPayload(com.google.protobuf.ByteString.copyFromUtf8("90")).build());
        try {
                FabricConnection myConnection = new FabricConnection();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.invoke(Util.myCC, Util.myCCVersion, "query", "a");
                Assert.assertEquals("90", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Test
    public void invokeError()  throws ProposalException, InvalidArgumentException {
        Channel mockChannel = mock(Channel.class);
        ProposalResponse mockProposalResponse = mock(ProposalResponse.class);
        Collection<ProposalResponse> queryPropResp = new ArrayList<ProposalResponse>();
        queryPropResp.add(mockProposalResponse);
        Mockito.when(mockChannel.sendTransactionProposal(Mockito.any())).thenReturn(queryPropResp);
        Mockito.when(mockProposalResponse.getStatus()).thenReturn(ChaincodeResponse.Status.FAILURE);
            try {
                FabricConnection myConnection = new FabricConnection();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(Util.getUser());
                String rs = myConnection.invoke(Util.myCC, Util.myCCVersion, "error", "a");
                Assert.assertEquals("", rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}