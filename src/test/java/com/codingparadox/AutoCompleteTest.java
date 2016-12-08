package com.codingparadox;

import org.testng.annotations.Test;
import org.testng.annotations.Test;

import com.codingparadox.elastic.AutoComplete;

public class AutoCompleteTest {
	
	@Test
	public void termSuggester(){
		AutoComplete complete = new AutoComplete();
		System.out.println(complete.termSuggester("testsuggest", 
									"testindex", "user", "para"));
	}
}