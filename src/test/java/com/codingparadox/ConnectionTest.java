package com.codingparadox;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.Assert;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;

import com.codingparadox.elastic.ClientFactory;

public class ConnectionTest {
	
	@Test(groups="ConnectionTest.connect")
	public void connect(){
		Client client = ClientFactory.getClient();

		ClusterHealthResponse healths = client.admin().cluster().prepareHealth().get();
		String clusterName = healths.getClusterName();
		AssertJUnit.assertEquals(clusterName, "elasticsearch");
	}
}
