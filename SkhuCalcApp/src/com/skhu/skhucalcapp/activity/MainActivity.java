package com.skhu.skhucalcapp.activity;

import java.net.URLEncoder;
import java.util.Calendar;

import com.skhu.skhucalcapp.R;
import com.skhu.skhucalcapp.R.id;
import com.skhu.skhucalcapp.R.layout;
import com.skhu.skhucalcapp.db.SkhuDBHelper;
import com.skhu.skhucalcapp.util.IOUtils;
import com.skhu.skhucalcapp.util.JacksonUtils;
import com.skhu.skhucalcapp.vo.Code;
import com.skhu.skhucalcapp.vo.SchoolScore;
import com.skhu.skhucalcapp.vo.Score;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
	private WebView webView;
	private MyJavaScriptInterface JSinterface;
	private SkhuDBHelper skhuDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startWebView();
	}
	
	public void startWebView() {
		skhuDB = new SkhuDBHelper(this);
		webView = (WebView) findViewById(R.id.webView);

		JSinterface = new MyJavaScriptInterface(webView,
				targetHandler,
				getApplicationContext());
		if (webView != null) {
			webView.loadUrl("file:///android_asset/skhuCalcApp.html");
			webView.getSettings().setJavaScriptEnabled(true);
			webView.addJavascriptInterface(JSinterface, "SkhuCalcApp");
			//여기에 "SkhuCalcApp"로 셋팅하였는데, 이것으로 접근한다.

			webView.setWebViewClient(new WebViewClient() {
				public void onPageFinished(WebView view, String url) {
					JSinterface.sendCode("{code : 1}");
				}
			});
		}
	}
	
	/*
	@Override
	public void onBackPressed() {		
		alertSaveDB();
		super.onBackPressed();//이거 왜 까먹냐!
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			alertSaveDB();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void alertSaveDB() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("저장");
		alertDialogBuilder
				.setMessage("결과 "+(
						skhuDB.queryScore().size()) + " 종료하시겠습니까 ?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Calendar calendar = Calendar.getInstance();
						String title = calendar.get(Calendar.YEAR) + ""
								+ (calendar.get(Calendar.MONTH) + 1) + ""
								+ calendar.get(Calendar.DATE) + ""
								+ calendar.get(Calendar.HOUR) + ""
								+ calendar.get(Calendar.MINUTE) + ""
								+ calendar.get(Calendar.SECOND);
						String body = skhuDB.exportText();
						
						IOUtils.writeToFile(title, body);
						moveTaskToBack(true);
					}
				})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								moveTaskToBack(true);
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	*/
	
	
	Handler targetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String json = msg.getData().getString("json"), resJson;
			Score score;

			if (json == null)
				return;
			Code code = JacksonUtils.jsonToObject(json, Code.class);

			switch (code.code) {
			case 1://받아서 넣기
				score = JacksonUtils.jsonToObject(json, Score.class);
				skhuDB.insertScore(score);
				break;
			case 3://보내기
				JSinterface.sendCode(JacksonUtils.objectToJson(skhuDB
						.queryScore()));
				break;
			case 5:
				Calendar calendar = Calendar.getInstance();
				String title = calendar.get(Calendar.YEAR) + ""
						+ (calendar.get(Calendar.MONTH) + 1) + ""
						+ calendar.get(Calendar.DATE) + ""
						+ calendar.get(Calendar.HOUR) + ""
						+ calendar.get(Calendar.MINUTE) + ""
						+ calendar.get(Calendar.SECOND);
				String body = skhuDB.exportText();

				Intent intent = new Intent(Intent.ACTION_SEND);
				
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"yhn@skhu.ac.kr"});
				intent.putExtra(Intent.EXTRA_SUBJECT, title + " 계산 결과 목록");
				intent.putExtra(Intent.EXTRA_TEXT, body);

				try {
					startActivity(Intent.createChooser(intent, "데이터를 저장합니다."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(MainActivity.this,
							"There are no email clients installed.",
							Toast.LENGTH_SHORT).show();
				}
				break;	
			case 6: //아이디를 받아서 삭제하는 코드
				score = JacksonUtils.jsonToObject(json, Score.class);
				skhuDB.deleteScore(score);
				JSinterface.sendCode(JacksonUtils.objectToJson(skhuDB
						.queryScore()));
				break;
			case 7:
				//일괄 삭제
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

				dialog.setTitle("확인");
				dialog
						.setMessage("일괄 삭제 하시겠습니까 ?")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								skhuDB.deleteAllScore();
								JSinterface.sendCode(
									JacksonUtils.objectToJson(skhuDB.queryScore())
									);
							}
						})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										//moveTaskToBack(true);
										dialog.cancel();
									}
								});			
				dialog.create().show();// show it
			break;

			default:
				Toast.makeText(MainActivity.this,"예기치 못한 오류입니다.",Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}