package com.codingparadox;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.codingparadox.elastic.CRUD;
import com.codingparadox.elastic.ClientFactory;

public class CRUDTest {
	
	private Client client = ClientFactory.getClient();
	private CRUD crud = new CRUD();
	
	private String id;
	
	
	@Test(priority=0, enabled=true, 
			dependsOnGroups={"ConnectionTest.connect"}
	)
	public void createIndex(){
		CreateIndexResponse createResponse = this.crud.createIndex("testindex");
		AssertJUnit.assertEquals(createResponse.isAcknowledged(), true);
	}
	
	@Test(dependsOnGroups={"ConnectionTest.connect"},
			dependsOnMethods="checkIndex", 
			enabled=false
		)
	public void deleteIndex(){
		DeleteIndexResponse deleteResponse = this.crud.deleteIndex("testindex");
		AssertJUnit.assertEquals(deleteResponse.isAcknowledged(), true);
	}
	
	@Test(enabled=false)
	public void checkIndex(){
		boolean exists = this.client.admin().indices()
							.prepareExists("testindex")
							.execute()
							.actionGet()
							.isExists();
		AssertJUnit.assertEquals(exists, true);
	}
	
	@Test(priority=1, enabled=true)
	public void insert() throws Exception{
		CRUD crud = new CRUD();
		IndexResponse indexResponse = this.crud
							.insertDataSingleFromFile("testindex", 
									"testtype", 
									"data/data.json");
		this.id = indexResponse.getId();
		AssertJUnit.assertEquals(indexResponse.isCreated(), true);
	}
	
	@Test(priority=4, enabled=false)
	public void delete(){
		CRUD crud = new CRUD();
		DeleteResponse deleteResponse = crud.delete("testindex", "testtype", this.id);
		AssertJUnit.assertEquals(deleteResponse.isFound(), true);
	}
	
	@Test(priority=2, enabled=true)
	public void readAll(){
		CRUD crud = new CRUD();

		List<Map<String, Object>> all = crud.getAllList("testindex", "testtype");
		
		for (Map<String, Object> map : all) {
			System.out.println(map.toString());
			//System.out.println(map.get("user"));
		}
	}
	
	
	@Test(priority=3, enabled=true)
	public void update(){
		CRUD crud = new CRUD();
		GetResponse response = crud.getById("testindex", "testtype", this.id);
		System.out.println(response.getSourceAsString());
	}
}


