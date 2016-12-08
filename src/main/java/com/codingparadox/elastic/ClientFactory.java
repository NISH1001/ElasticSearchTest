package com.codingparadox.elastic;

import org.elasticsearch.client.Client;

import com.codingparadox.elastic.ClientConfig;

public class ClientFactory {
	
	public ClientFactory(){}
	
	public static Client getClient(){
		ClientConfig clientConfig = new ClientConfig();
		return clientConfig.client();
	}
}
