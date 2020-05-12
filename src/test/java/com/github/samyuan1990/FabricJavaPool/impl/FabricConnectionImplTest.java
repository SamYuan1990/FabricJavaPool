package com.github.samyuan1990.FabricJavaPool.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.github.samyuan1990.FabricJavaPool.ExecuteResult;
import com.github.samyuan1990.FabricJavaPool.RunTimeException;
import com.github.samyuan1990.FabricJavaPool.Util;
import com.github.samyuan1990.FabricJavaPool.util.TestUtil;
import org.hyperledger.fabric.protos.peer.FabricProposalResponse;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class FabricConnectionImplTest {

    @Test
    public void getUser() {
        FabricConnectionImpl fc = new FabricConnectionImpl();
        fc.setUser(TestUtil.getUser());
        User user = fc.getUser();
        Assert.assertEquals(TestUtil.getUser().getName(), user.getName());
        Assert.assertEquals(TestUtil.getUser().getEnrollment().getCert(), user.getEnrollment().getCert());
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
                FabricConnectionImpl myConnection = new FabricConnectionImpl();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(TestUtil.getUser());
                ExecuteResult rs = myConnection.query(TestUtil.chaincodeID, "query", "a");
                Assert.assertEquals("90", rs.getResult());
                Assert.assertEquals(queryPropResp, rs.getPropResp());
        } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Test
    public void query2() throws ProposalException, InvalidArgumentException {
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
            FabricConnectionImpl myConnection = new FabricConnectionImpl();
            myConnection.setMychannel(mockChannel);
            myConnection.setUser(TestUtil.getUser());
            ExecuteResult rs = myConnection.query(TestUtil.myCC, "query", "a");
            Assert.assertEquals("90", rs.getResult());
            Assert.assertEquals(queryPropResp, rs.getPropResp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryErrorDealToPeers() throws Exception {
        Channel mockChannel = mock(Channel.class);
        ProposalResponse mockProposalResponse0 = mock(ProposalResponse.class);
        ProposalResponse mockProposalResponse1 = mock(ProposalResponse.class);
        Mockito.when(mockProposalResponse0.getStatus()).thenReturn(ChaincodeResponse.Status.SUCCESS);
        Mockito.when(mockProposalResponse0.getProposalResponse()).thenReturn(FabricProposalResponse.ProposalResponse.newBuilder().
                setResponse(org.hyperledger.fabric.protos.peer.FabricProposalResponse.Response.newBuilder().setMessage("90").setStatus(1).setPayload(com.google.protobuf.ByteString.copyFromUtf8("90")))
                .setPayload(com.google.protobuf.ByteString.copyFromUtf8("90")).build());
        Mockito.when(mockProposalResponse1.getStatus()).thenReturn(ChaincodeResponse.Status.SUCCESS);
        Mockito.when(mockProposalResponse1.getProposalResponse()).thenReturn(FabricProposalResponse.ProposalResponse.newBuilder().
                setResponse(org.hyperledger.fabric.protos.peer.FabricProposalResponse.Response.newBuilder().setMessage("91").setStatus(1).setPayload(com.google.protobuf.ByteString.copyFromUtf8("91")))
                .setPayload(com.google.protobuf.ByteString.copyFromUtf8("91")).build());
        Collection<ProposalResponse> queryPropResp = new ArrayList<ProposalResponse>();
        queryPropResp.add(mockProposalResponse0);
        queryPropResp.add(mockProposalResponse1);
        Mockito.when(mockChannel.queryByChaincode(Mockito.any())).thenReturn(queryPropResp);
        try {
            FabricConnectionImpl myConnection = new FabricConnectionImpl();
            myConnection.setMychannel(mockChannel);
            myConnection.setUser(TestUtil.getUser());
            myConnection.query(TestUtil.chaincodeID, "query", "a");
        } catch (RunTimeException e) {
            Assert.assertEquals(ChaincodeResponse.Status.SUCCESS, e.getStatus());
            Assert.assertEquals(Util.resultOnPeersDiff, e.getMsg());
        }
    }

    @Test
    public void queryError()  throws Exception {
        Channel mockChannel = mock(Channel.class);
        ProposalResponse mockProposalResponse = mock(ProposalResponse.class);
        Collection<ProposalResponse> queryPropResp = new ArrayList<ProposalResponse>();
        queryPropResp.add(mockProposalResponse);
        Mockito.when(mockChannel.queryByChaincode(Mockito.any())).thenReturn(queryPropResp);
        Mockito.when(mockProposalResponse.getStatus()).thenReturn(ChaincodeResponse.Status.FAILURE);
        try {
                FabricConnectionImpl myConnection = new FabricConnectionImpl();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(TestUtil.getUser());
                myConnection.query(TestUtil.chaincodeID, "error", "a");
            } catch (RunTimeException e) {
                Assert.assertEquals(ChaincodeResponse.Status.FAILURE, e.getStatus());
                Assert.assertEquals(Util.errorHappenDuringQuery, e.getMsg());
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
                FabricConnectionImpl myConnection = new FabricConnectionImpl();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(TestUtil.getUser());
                ExecuteResult rs = myConnection.invoke(TestUtil.chaincodeID, "query", "a");
                Assert.assertEquals("90", rs.getResult());
                Assert.assertEquals(queryPropResp, rs.getPropResp());
        } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Test
    public void invoke2()  throws ProposalException, InvalidArgumentException {
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
            FabricConnectionImpl myConnection = new FabricConnectionImpl();
            myConnection.setMychannel(mockChannel);
            myConnection.setUser(TestUtil.getUser());
            ExecuteResult rs = myConnection.invoke(TestUtil.myCC, "query", "a");
            Assert.assertEquals("90", rs.getResult());
            Assert.assertEquals(queryPropResp, rs.getPropResp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void invokeError()  throws Exception {
        Channel mockChannel = mock(Channel.class);
        ProposalResponse mockProposalResponse = mock(ProposalResponse.class);
        Collection<ProposalResponse> queryPropResp = new ArrayList<ProposalResponse>();
        queryPropResp.add(mockProposalResponse);
        Mockito.when(mockChannel.sendTransactionProposal(Mockito.any())).thenReturn(queryPropResp);
        Mockito.when(mockProposalResponse.getStatus()).thenReturn(ChaincodeResponse.Status.FAILURE);
            try {
                FabricConnectionImpl myConnection = new FabricConnectionImpl();
                myConnection.setMychannel(mockChannel);
                myConnection.setUser(TestUtil.getUser());
                myConnection.invoke(TestUtil.chaincodeID, "error", "a");
            } catch (RunTimeException e) {
                Assert.assertEquals(ChaincodeResponse.Status.FAILURE, e.getStatus());
                Assert.assertEquals(Util.errorHappenDuringQuery, e.getMsg());
            }
    }
}