package com.skhu.skhucalcapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.skhu.skhucalcapp.vo.SchoolScore;
import com.skhu.skhucalcapp.vo.Score;

public class SkhuDBHelper extends SQLiteOpenHelper {
	public final static String DATABASE_NAME = "SkhuDB.db";

	public SkhuDBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		//context.deleteDatabase(DATABASE_NAME); //기존 DB를 날린다.
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createScoreDB(db);
		//createSchoolScoreDB(db);
	}

	public void createScoreDB(SQLiteDatabase db) {
		db.execSQL("create table score_db ("
				+ "_id integer primary key AUTOINCREMENT,"
				+ "c_date timestamp default CURRENT_TIMESTAMP,"
				+ "usr_type text," 
				+ "apply_type integer,"
				+ "dept_type integer," 
				+ "kor integer," 
				+ "kor_type text,"
				+ "math integer," 
				+ "math_type text," 
				+ "eng integer,"
				+ "ss integer, " 
				+ "ss_type text, "
				+ "credit double)");
		//Log.d("createScoreDB", "createScoreDB");
	}
	public void insertScore(Score score) {
		SQLiteDatabase db = this.getWritableDatabase();
		// insert 메서드로 삽입
		ContentValues row = new ContentValues();
		
		row.put("usr_type", score.usrType);
		row.put("apply_type", score.applyType);
		row.put("dept_type", score.deptType);
		row.put("kor", score.kor);
		row.put("kor_type", score.korType);
		row.put("math", score.math);
		row.put("math_type", score.mathType);
		row.put("eng", score.eng);
		row.put("ss", score.ss);
		row.put("ss_type", score.ssType);
		row.put("credit", score.credit);
		//여기에 적절하지 않은 값(예를들어 false를 넣으려했다가 죽었던것)

		db.insert("score_db", null, row);
		Log.d("insertScore", "insertScore");
		db.close();
		this.close();
	
	}
	public void deleteScore(Score score) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from score_db where _id = " + score.id);
		db.close();
		this.close();
	}
	public void deleteAllScore() {//일괄 삭제
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from score_db where _id>=0");
		db.close();
		this.close();
	}

	public List<Score> queryScore() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		cursor = db
				.rawQuery(
						"select _id, "
						+ "usr_type, "
						+ "apply_type, "
						+ "dept_type, "
						+ "kor, "
						+ "kor_type, "
						+ "math, "
						+ "math_type, "
						+ "eng, "
						+ "ss, "
						+ "ss_type, "
						+ "credit "
						+ "from score_db",
						null);
		List<Score> scoreList = new ArrayList<Score>();

		while (cursor.moveToNext()) {
			Score score = new Score();
			score.id = cursor.getInt(0);
			score.usrType = cursor.getString(1);
			score.applyType = cursor.getInt(2);
			score.deptType = cursor.getInt(3);
			score.kor = cursor.getInt(4);
			score.korType = cursor.getString(5);
			score.math = cursor.getInt(6);
			score.mathType = cursor.getString(7);
			score.eng = cursor.getInt(8);
			score.ss = cursor.getInt(9);
			score.ssType = cursor.getString(10);
			score.credit = cursor.getDouble(11);
			scoreList.add(score);
		}
		cursor.close();
		db.close();
		this.close();
		return scoreList;
	}


	public String textScore() {
		String txt = "";
		List<Score> scores = queryScore();

		for (Score score : scores) {
			txt += (score.usrType.equals("society")? "인문" : "공학") + " ";
			txt += ((score.applyType == 1)? "수능" : "일반") + " ";
			txt += score.kor + " ";
			txt += ((score.korType == null)? "선택안함" : score.korType) + "  ";
			txt += score.eng + " ";
			txt += score.math + " ";
			txt += ((score.mathType == null)? "선택안함" : score.mathType) + " ";
			txt += score.ss + " ";
			txt += ((score.ssType == null)? "선택안함" : score.ssType) + " "; 
			txt += score.credit + " ";//학생부 점수
			//txt += " 0";//학생부 점수
			txt += "\n";
		}
		return txt;
	}



	public String exportText() {
		String txt = "계열 지원유형 국어 국어유형 영어 수학 수학유형 탐구 탐구유형 수능 학생부\n";
		txt += textScore() + "\n";
		return txt;
	}

	public void createTestDB(SQLiteDatabase db) {
		db.execSQL("create table test_db ( _id integer primary key AUTOINCREMENT,"
				+ " name TEXT )");
	}
/*
	public void insertTest() {
		SQLiteDatabase db = this.getWritableDatabase();
		// insert 메서드로 삽입
		ContentValues row = new ContentValues();
		row.put("name", "testtest");
		db.insert("test_db", null, row);
		// SQL
		// db.execSQL("insert into test_db(name) values(null, 'girl', '가시나');");
		this.close();
	}
*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("drop table if exist");
	}
}
