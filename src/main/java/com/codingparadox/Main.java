package com.codingparadox;

import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.codingparadox.elastic.CRUD;
import com.codingparadox.elastic.Test;
import com.codingparadox.elastic.AutoComplete;

import com.codingparadox.utils.JSONArrayParser;
import com.codingparadox.utils.JSONParser;

public class Main {
	
	public static void main(String[] args){
		System.out.println("I am paradox.");
		try{
			CRUD crud = new CRUD();
			crud.deleteIndex("testindex");
			//crud.getAllList("testindex", "testtype");
			
			AutoComplete ac = new AutoComplete();
			//ac.test();
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}
}