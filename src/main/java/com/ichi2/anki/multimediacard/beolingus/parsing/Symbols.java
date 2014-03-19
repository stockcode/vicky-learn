
package com.ichi2.anki.multimediacard.beolingus.parsing;

import java.util.List;

public class Symbols{
   	private List parts;
   	private String ph_am;
   	private String ph_am_mp3;
   	private String ph_en;
   	private String ph_en_mp3;
   	private String ph_other;
   	private String ph_tts_mp3;

 	public List getParts(){
		return this.parts;
	}
	public void setParts(List parts){
		this.parts = parts;
	}
 	public String getPh_am(){
		return this.ph_am;
	}
	public void setPh_am(String ph_am){
		this.ph_am = ph_am;
	}
 	public String getPh_am_mp3(){
		return this.ph_am_mp3;
	}
	public void setPh_am_mp3(String ph_am_mp3){
		this.ph_am_mp3 = ph_am_mp3;
	}
 	public String getPh_en(){
		return this.ph_en;
	}
	public void setPh_en(String ph_en){
		this.ph_en = ph_en;
	}
 	public String getPh_en_mp3(){
		return this.ph_en_mp3;
	}
	public void setPh_en_mp3(String ph_en_mp3){
		this.ph_en_mp3 = ph_en_mp3;
	}
 	public String getPh_other(){
		return this.ph_other;
	}
	public void setPh_other(String ph_other){
		this.ph_other = ph_other;
	}
 	public String getPh_tts_mp3(){
		return this.ph_tts_mp3;
	}
	public void setPh_tts_mp3(String ph_tts_mp3){
		this.ph_tts_mp3 = ph_tts_mp3;
	}
}
