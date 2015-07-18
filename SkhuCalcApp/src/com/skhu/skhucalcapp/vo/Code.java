package com.skhu.skhucalcapp.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Code {
	@JsonProperty("code") //"code"다음 부분의 숫자를 입력받는다. 문자열이 변수 이름이 된다.
	public int code;
}
