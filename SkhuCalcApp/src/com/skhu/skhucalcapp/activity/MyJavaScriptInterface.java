package com.skhu.skhucalcapp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class MyJavaScriptInterface{
	private WebView webView;
	private Handler tempHandler;
	private Context temp;
	
	
	public MyJavaScriptInterface(WebView webView, Handler tempHandler, Context temp) {
		this.webView = webView;//선언된 웹뷰를 대입
		this.tempHandler=tempHandler;
		this.temp= temp;

	}

	@JavascriptInterface
	public void receiveCode(String json) {
		Message msg = new Message();
		Bundle bundle = msg.getData();
		bundle.putString("json", json);
		msg.setData(bundle);
		tempHandler.sendMessage(msg);
	}

	@JavascriptInterface
	public void showToast(String text){
		Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
		
	}

	private Context getApplicationContext() {
		return this.temp;
	}

	public void sendCode(String json) {
		try {
			webView.loadUrl("javascript:receiveCode('"
					+ json + "')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
