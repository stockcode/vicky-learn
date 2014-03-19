
package com.ichi2.anki.multimediacard.beolingus.parsing;

import java.util.List;

public class Exchange{
   	private List word_done;
   	private String word_er;
   	private String word_est;
   	private List word_ing;
   	private List word_past;
   	private List word_pl;
   	private List word_third;

 	public List getWord_done(){
		return this.word_done;
	}
	public void setWord_done(List word_done){
		this.word_done = word_done;
	}
 	public String getWord_er(){
		return this.word_er;
	}
	public void setWord_er(String word_er){
		this.word_er = word_er;
	}
 	public String getWord_est(){
		return this.word_est;
	}
	public void setWord_est(String word_est){
		this.word_est = word_est;
	}
 	public List getWord_ing(){
		return this.word_ing;
	}
	public void setWord_ing(List word_ing){
		this.word_ing = word_ing;
	}
 	public List getWord_past(){
		return this.word_past;
	}
	public void setWord_past(List word_past){
		this.word_past = word_past;
	}
 	public List getWord_pl(){
		return this.word_pl;
	}
	public void setWord_pl(List word_pl){
		this.word_pl = word_pl;
	}
 	public List getWord_third(){
		return this.word_third;
	}
	public void setWord_third(List word_third){
		this.word_third = word_third;
	}
}
