package com.codingparadox.elastic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;


import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.index.search.MultiMatchQuery;

import com.codingparadox.elastic.ClientConfig;
import com.codingparadox.elastic.ClientFactory;
import com.codingparadox.utils.JSONParser;
import com.codingparadox.utils.JSONArrayParser;

/**
 * 
 * @author paradox
 *
 */
public class CRUD {
	
	private Client client = ClientFactory.getClient();
	
	public CRUD(){}
	
	public CRUD(Client client){
		this.client = client;
	}
	
	/**
	 * This function creates new index to the elasticsearch
	 * 
	 * @param indexName It is the name of the new index
	 * @return
	 */
	public CreateIndexResponse createIndex(String indexName){
		CreateIndexResponse createResponse = this.client.admin().indices()
								.create(Requests.createIndexRequest(indexName))
								.actionGet();
		return createResponse;
	}
	
	/**
	 * This function deletes a specific index
	 * 
	 * @param indexName It is the name of the index to delete
	 * @return
	 */
	public DeleteIndexResponse deleteIndex(String indexName){
		DeleteIndexResponse deleteResponse = this.client.admin().indices()
							.delete(Requests.deleteIndexRequest(indexName))
							.actionGet();
		return deleteResponse;
	}
	
	/**
	 * This function indexes a single json data with userdefined id.
	 * 
	 * @param index it is the name of the index
	 * @param type it is the name of the type
	 * @param id it is the id of new data
	 * @param data it is the data to be indexed
	 * @return IndexResponse
	 * @throws Exception
	 */
	public IndexResponse insertDataSingle(String index, String type, 
				String id, String data) throws Exception{
		IndexResponse indexResponse = this.client.prepareIndex(index, type, id)
				.setSource(data)
				.execute()
				.actionGet();
		return indexResponse;
	}
	
	/**
	 * This function indexes a single json data with auto-generated id.
	 * 
	 * @param index it is the name of the index
	 * @param type it is the name of the type
	 * @param data it is the data to be indexed
	 * @return IndexResponse
	 * @throws Exception
	 */
	public IndexResponse insertDataSingle(String index, String type, String data) throws Exception{
		IndexResponse response = this.client.prepareIndex(index, type)
				.setSource(data)
				.execute()
				.actionGet();
		return response;
	}

	/**
	 * This function indexes the single document from a json file
	 * with user-defined id.
	 * 
	 * @param index It is the name of the index
	 * @param type It is the type 
	 * @param id It is the id of the data
	 * @param filename It is the name of the file from which data is loaded
	 * @return IndexResponse
	 * @throws Exception
	 */
	public IndexResponse insertDataSingleFromFile(String index, String type,
			String id,
			String filename) throws Exception{
		JSONParser parser = new JSONParser();
		String data = parser.parse(filename);
		return this.insertDataSingle(index, type, id, data);
	}
	
	/**
	 * This function indexes the single document from a json file
	 * with auto-generated id.
	 * 
	 * @param index It is the name of the index
	 * @param type It is the type 
	 * @param filename It is the name of the file from which data is loaded
	 * @return IndexResponse
	 * @throws Exception
	 */
	public IndexResponse insertDataSingleFromFile(String index, String type,
			String filename) throws Exception{
		JSONParser parser = new JSONParser();
		String data = parser.parse(filename);
		return this.insertDataSingle(index, type, data);
	}
	
	/**
	 * This function indexes many documents(data) from list of json strings provided
	 * 
	 * @param index It is the name of the index.
	 * @param type It is the name of the type.
	 * @param jsonStrings It is the list of the json string(data)
	 * @return BulkdResponse
	 * @throws Exception
	 */
	public BulkResponse insertDataBulk(String index, String type,
			List<String> jsonStrings) throws Exception{
		
		BulkRequestBuilder bulkRequest = this.client.prepareBulk();

		for(String s : jsonStrings){
			bulkRequest.add(this.client.prepareIndex(index, type)
									.setSource(s)
					);
		}
		BulkResponse response = bulkRequest.execute().actionGet();
		return response;
	}
	
	/**
	 * This function indexes many documents from a json file an array of jsons.
	 * 
	 * @param index It is the name of the index
	 * @param type It is the name of the type
	 * @param filename It is the name of the file that contains array of jsons
	 * @return BulkResponse
	 * @throws Exception
	 */
	public BulkResponse insertDataBulk(String index, String type,
			String filename) throws Exception{
		JSONArrayParser parser = new JSONArrayParser();
		List<String> strings = parser.parse(filename);
		return this.insertDataBulk(index, type, strings);
	}
	
	/**
	 * This function returns the single data for a specific id for given index and type
	 * 
	 * @param index It is the name of the index.
	 * @param type It is the name of the type
	 * @param id It is the id of which data is retrieved
	 * @return Map
	 */
	public Map<String, Object> getByIdAsMap(String index, String type, String id){
		GetResponse response  = this.client.prepareGet(index, type, id)
				.execute()
				.actionGet();
		return response.getSourceAsMap();
	}
	
	public GetResponse getById(String index, String type, String id){
		GetResponse response = this.client.prepareGet(index, type, id)
				.execute()
				.actionGet();
		return response;
	}
	
	/**
	 * This function returns all the data of an index
	 * 
	 * @param index It is the name of the index
	 * @param type It is the name of the type
	 * @return List
	 */
	public List<Map<String, Object>> getAllList(String index, String type){
		SearchResponse response = this.client.prepareSearch(index)
						.setTypes(type)
						.setQuery(QueryBuilders.matchAllQuery())
						.execute()
						.actionGet();
		List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();

		for(SearchHit hit : response.getHits()){
			toReturn.add(hit.getSource());
		}
		
		return toReturn;
	}

	public SearchResponse getAll(String index, String type){
		SearchResponse response = this.client.prepareSearch(index)
						.setTypes(type)
						.setQuery(QueryBuilders.matchAllQuery())
						.execute()
						.actionGet();
		return response;
	}
	
	/**
	 * This function performs multi match query for the given query string
	 * 
	 * @param index It is the name of the index
	 * @param type It is the name of the type
	 * @param queryString It is the actual query string
	 * @param fields These are any no. of fields provided
	 * @return 
	 * */
	public SearchResponse multiMatchQuery(String index, 
					String type, 
					String queryString, 
					String... fields){
		QueryBuilder qb = QueryBuilders.multiMatchQuery(queryString, fields);
		SearchResponse searchResponse = this.client.prepareSearch(index)
				.setTypes(type)
				.setSearchType(SearchType.DFS_QUERY_AND_FETCH)
				//.setQuery(query)
				//.setQuery(QueryBuilders.termQuery(query.get("field"), query.get("value")))
				.setQuery(qb)
				.execute()
				.actionGet();
		return searchResponse;
	}
	
	/**
	 * This function updates the given document indexed in the elasticsearch
	 * 
	 * @param index This is the name of the index
	 * @param type This is the type for the index
	 * @param id This is the id of the document to be updated
	 * @param data This is the new data to update the document
	 * @throws Exception
	 */
	public void update(String index, String type, String id, String data) throws Exception{
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(index);
		updateRequest.type(type);
		updateRequest.id(id);
		updateRequest.doc(data);
		this.client.update(updateRequest).get();
	}
	
	public void updateFromFile(String index, String type, String id,
			String fileName) throws Exception{
		
		JSONParser parser = new JSONParser();
		String data = parser.parse(fileName);
		this.update(index, type, id, data);
	}
	
	public void upsert(String index, String type, String id, String data) throws Exception{
		IndexRequest indexRequest = new IndexRequest(index, type, id);
		UpdateRequest updateRequest = new UpdateRequest(index, type, id)
				.doc(data)
				.upsert(indexRequest);
		this.client.update(updateRequest).get();
	}
	
	public void upserFromFile(String index, String type, String id,
			String fileName) throws Exception {
		JSONParser parser = new JSONParser();
		String data = parser.parse(fileName);
		this.upsert(index, type, id, data);
	}
	
	/**
	 * This function deletes the document of a specific id
	 * 
	 * @param index It is the name of the index.
	 * @param type It is the name of the type.
	 * @param id It is the id of the document to be deleted.
	 * @return
	 */
	public DeleteResponse delete(String index, String type, String id){
		DeleteResponse deleteResponse = this.client.prepareDelete(index, type, id)
				.execute()
				.actionGet();
		return deleteResponse;
	}

}