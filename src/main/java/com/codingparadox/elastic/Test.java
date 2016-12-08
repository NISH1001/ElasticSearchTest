package com.codingparadox.elastic;

import java.util.Map;

import java.util.HashMap;
import java.util.List;

import java.io.IOException;

import java.lang.Math;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.suggest.SuggestAction;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;

import static org.elasticsearch.common.xcontent.XContentFactory.*;


import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;

import com.codingparadox.elastic.ClientFactory;
import com.codingparadox.utils.JSONParser;
import com.codingparadox.utils.JSONArrayParser;

public class Test {
	
	private Client client = ClientFactory.getClient();
	
	
	public Test(){}
	
	public void test() {

		try{

		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			
		}
		
	}
	
	public void insertTest() throws ElasticsearchException, IOException{
		//Client client = ClientFactory.getClient();
		IndexResponse response = client.prepareIndex("codingparadox", "user", "11")
				.setSource(jsonBuilder()
							.startObject()
								.field("user", "Saurav")
								.field("body", "I am saurav don.")
								.field("title", "jigri")
							.endObject()
						)
				.execute()
				.actionGet();
	}
	
	
	
	public void searchTest() throws ElasticsearchException, IOException {
		SearchResponse response = this.client.prepareSearch("codingparadox")
				.setTypes("user")
				.setSearchType(SearchType.DFS_QUERY_AND_FETCH)
				.setQuery(QueryBuilders.termQuery("body", "don"))
				//.setPostFilter(FilterBuilders.rangeFilter("_id").from(1).to(3))   // Filter
				.setFrom(0).setSize(1).setExplain(true)
				.execute()
				.actionGet();

		for(SearchHit hit : response.getHits()){
			System.out.println(hit.getId());
			System.out.println(hit.getSourceAsString());
			//System.out.println(hit);
		}
	}
	
	public void upsertTest() throws Exception{
		IndexRequest indexRequest = new IndexRequest("codingparadox", "user", "1");
		UpdateRequest updateRequest = new UpdateRequest("codingparadox", "user", "1")
				.doc(jsonBuilder()
					 .startObject()
					 	.field("user", "Nishan Pantha")
					 	.field("role", "superuser")
					 .endObject())
				.upsert(indexRequest);

		this.client.update(updateRequest).get();
	}
	
	public void sortTest(){
		SearchResponse response = this.client.prepareSearch("codingparadox")
				.setTypes("user")
				.setSearchType(SearchType.DFS_QUERY_AND_FETCH)
				.setQuery(QueryBuilders.termQuery("body", "am"))
				//.setPostFilter(FilterBuilders.rangeFilter("_id").from(1).to(3))   // Filter
				.execute()
				.actionGet();
		
		long totalHits = response.getHits().getTotalHits();
		int size = (int) Math.ceil(totalHits * 0.8);
		System.out.println("=========================" + size);
		System.out.println(response);
		
		response = this.client.prepareSearch("codingparadox")
				.setTypes("user")
				.setSearchType(SearchType.DFS_QUERY_AND_FETCH)
				.setQuery(QueryBuilders.termQuery("body", "I am"))
				//.addSort(SortBuilders.fieldSort("_score").order(SortOrder.ASC))
				.addSort("_score", SortOrder.DESC)
				.setFrom(0)
				.setSize(8)
				.setExplain(true)
				.execute()
				.actionGet();
		
		System.out.println("=========================");
		System.out.println(response);

/*		for(SearchHit hit : response.getHits()){
			System.out.println(hit.getId());
			System.out.println(hit.getSourceAsString());
			//System.out.println(hit);
		}
*/	}
	
	public void  testMapping() throws Exception{
		CreateIndexResponse cir = this.client.admin().indices().create(new CreateIndexRequest("blog")).actionGet();
		XContentBuilder mapping = jsonBuilder()
									.startObject()
									.startObject("user")
									.startObject("properties")

										.startObject("username")
											.field("type", "string")
										.endObject()

										.startObject("role")
											.field("type", "string")
										.endObject()

									.endObject()
									.endObject()
									.endObject();
		PutMappingResponse putMappingResponse = this.client.admin().indices()
					.preparePutMapping("blog")
					.setType("user")
					.setSource(mapping)
					.execute()
					.actionGet();
	}
	
	public void testBulkInsert() throws Exception{
		JSONArrayParser parser = new JSONArrayParser();
		List<String> strings = parser.parse("data/data.json");
		
		BulkRequestBuilder bulkRequest = this.client.prepareBulk();

		String index = "codingparadox";
		String type = "user";
		for(String s : strings){
			bulkRequest.add(this.client.prepareIndex(index, type)
									.setSource(s)
					);
		}
		BulkResponse response = bulkRequest.execute().actionGet();
		System.out.println(response);
	}
}



