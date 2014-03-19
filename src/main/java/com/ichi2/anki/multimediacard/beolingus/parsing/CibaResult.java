
package com.ichi2.anki.multimediacard.beolingus.parsing;

import java.util.List;

public class CibaResult {
   	//private Exchange exchange;
   	private String is_CRI;
   	private List<Symbols> symbols;
   	private String word_name = "";

// 	public Exchange getExchange(){
//		return this.exchange;
//	}
//	public void setExchange(Exchange exchange){
//		this.exchange = exchange;
//	}
 	public String getIs_CRI(){
		return this.is_CRI;
	}
	public void setIs_CRI(String is_CRI){
		this.is_CRI = is_CRI;
	}
 	public List<Symbols> getSymbols(){
		return this.symbols;
	}
	public void setSymbols(List<Symbols> symbols){
		this.symbols = symbols;
	}
 	public String getWord_name(){
		return this.word_name;
	}
	public void setWord_name(String word_name){
		this.word_name = word_name;
	}
}
