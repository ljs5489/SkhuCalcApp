package com.skhu.skhucalcapp.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Score {
	@JsonProperty("_id")
	public int id;
	@JsonProperty("usr_type")
	public String usrType;
	@JsonProperty("apply_type")
	public int applyType;
	@JsonProperty("dept_type")
	public int deptType;
	@JsonProperty("kor")
	public int kor;
	@JsonProperty("kor_type")
	public String korType;
	@JsonProperty("math")
	public int math;
	@JsonProperty("math_type")
	public String mathType;
	@JsonProperty("eng")
	public int eng;
	@JsonProperty("ss")
	public int ss;
	@JsonProperty("ss_type")
	public String ssType;
	@JsonProperty("credit")
	public double credit;
}
