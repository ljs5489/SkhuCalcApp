package com.skhu.skhucalcapp.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolScore {
	@JsonProperty("_id")
	public int id;
	@JsonProperty("usr_type")
	public String usrType;
	@JsonProperty("apply_type")
	public int applyType;
	@JsonProperty("dept_type")
	public int deptType;
	@JsonProperty("scores")
	public String scores;
	@JsonProperty("result")
	public String result;
	
}
