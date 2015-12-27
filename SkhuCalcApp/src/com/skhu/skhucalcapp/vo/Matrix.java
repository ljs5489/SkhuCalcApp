package com.skhu.skhucalcapp.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Matrix {
	@JsonProperty("_id")
	public int id;
	@JsonProperty("major")
	public String major;
	
	@JsonProperty("susi_max")
	public int susi_max;
	@JsonProperty("susi_min")
	public int susi_min;
	@JsonProperty("jungsi_max")
	public int jungsi_max;
	@JsonProperty("jungsi_min")
	public int jungsi_min;
	
}
