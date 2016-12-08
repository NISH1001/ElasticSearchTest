package com.codingparadox.elastic;

import java.net.InetAddress;
import java.net.InetSocketAddress;


import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

public class ClientConfig {
	
	private String clusterName = "elasticsearch";
	private String hostName = "localhost";
	private int port = 9300;
	
	public Client client(){
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", this.clusterName).build();
		
		TransportClient client = new TransportClient(settings);

		TransportAddress address = new InetSocketTransportAddress(
				new InetSocketAddress(this.hostName, this.port)
			);

		client.addTransportAddress(address);

		return client;
	}
}