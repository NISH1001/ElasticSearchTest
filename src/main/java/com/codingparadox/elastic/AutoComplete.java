package com.codingparadox.elastic;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggester;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestion;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;

import com.codingparadox.elastic.ClientFactory;
import com.codingparadox.utils.JSONParser;

public class AutoComplete {

	private Client client = ClientFactory.getClient();
	
	public AutoComplete(){}
	public void test(){
		
		try{

			System.out.println("testing...");
			this.testFuzzy();

		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			
		}
	}
	
	public SuggestResponse termSuggester(String suggestName, String index, String field, 
			String term){
		SuggestBuilder.SuggestionBuilder termSuggestionBuilder = 
					new TermSuggestionBuilder(suggestName)
						.suggestMode("always")
						.text(term)
						.field(field);
		
		SuggestRequestBuilder requestBuilder = this.client.prepareSuggest(index)
				.addSuggestion(termSuggestionBuilder);
		return requestBuilder.get();
	}
	
	public SuggestResponse phraseSuggester(String suggestName, String index, String field,
				String phrase){
		PhraseSuggestion phraseSuggestion = new PhraseSuggestion();
		
		SuggestBuilder.SuggestionBuilder phraseSuggestionBuilder = 
				new PhraseSuggestionBuilder(suggestName)
				//.size(1)
				.text(phrase)
				.field(field);
		
		SuggestRequestBuilder requestBuilder = this.client.prepareSuggest("codingparadox")
				.addSuggestion(phraseSuggestionBuilder);
		
		return requestBuilder.get();
	}
	
	public SuggestResponse completionSuggester(String suggestName, String index,
				String field, String text){
		CompletionSuggestionBuilder completionBuilder = new CompletionSuggestionBuilder(suggestName);
		completionBuilder.text(text);
		completionBuilder.field(field);
		
		
		SuggestResponse suggestResponse = 
					this.client.prepareSuggest("people")
					.addSuggestion(completionBuilder)
					.execute()
					.actionGet();
		return suggestResponse;
	}
	
	public SearchResponse fuzzySearch(String index, String field, String term){
		QueryBuilder query = QueryBuilders.fuzzyQuery(field, term)
								.fuzziness(Fuzziness.TWO);
		
		SearchResponse response = this.client.prepareSearch(index)
									.setQuery(query)
									.execute()
									.actionGet();
		return response;
	}
	
	public void testFuzzy() throws Exception{
/*		CRUD crud = new CRUD();
		crud.createIndexBulk("people", "people", "data/bulkdata.json");
*/		
		SearchResponse fuzzyResponse = this.fuzzySearch(
										"people", 
										"user", 
										"Dgya");
		
		System.out.println(fuzzyResponse);
	}
	
}





