package com.skhu.skhucalcapp.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Code {
	@JsonProperty("code") //"code"���� �κ��� ���ڸ� �Է¹޴´�. ���ڿ��� ���� �̸��� �ȴ�.
	public int code;
}
