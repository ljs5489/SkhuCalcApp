package com.skhu.skhucalcapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.skhu.skhucalcapp.vo.Matrix;
import com.skhu.skhucalcapp.vo.SchoolScore;
import com.skhu.skhucalcapp.vo.Score;

public class SkhuDBAdmin extends SQLiteOpenHelper {
	public final static String DATABASE_NAME = "SkhuDB.db";

	public SkhuDBAdmin(Context context) {
		super(context, DATABASE_NAME, null, 1);
		//context.deleteDatabase(DATABASE_NAME); //기존 DB를 날린다.
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createScoreDB2(db);
		//createSchoolScoreDB(db);
	}

	public void createScoreDB2(SQLiteDatabase db) {
		db.execSQL("create table score_db2 ("
				+ "_id integer primary key AUTOINCREMENT,"
				+ "c_date timestamp default CURRENT_TIMESTAMP,"
				+ "major text,"
				+ "susi_max integer,"
				+ "susi_min integer,"
				+ "jungsi_max integer,"
				+ "jungsi_min integer)");
		Log.d("createScoreDB2", "createScoreDB2");
	}
	/*
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
	*/
	public void deleteScore(Score score) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from score_db2 where _id = " + score.id);
		db.close();
		this.close();
	}
	public void deleteAllScore() {//일괄 삭제
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from score_db2 where _id>=0");
		db.close();
		this.close();
	}

	public List<Matrix> queryScore() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		cursor = db
				.rawQuery(
						"select _id, "
						+ "major, "
						+ "susi_max, "
						+ "susi_min, "
						+ "jungsi_max, "
						+ "jungsi_min "
						+ "from score_db2",
						null);
		List<Matrix> matrixList = new ArrayList<Matrix>();

		while (cursor.moveToNext()) {
			Matrix score = new Matrix();
			score.id = cursor.getInt(0);
			score.major = cursor.getString(1);
			score.susi_max = cursor.getInt(2);
			score.susi_min = cursor.getInt(3);
			score.jungsi_max = cursor.getInt(4);
			score.jungsi_min = cursor.getInt(5);
			matrixList.add(score);
		}
		cursor.close();
		db.close();
		this.close();
		return matrixList;
	}

//here
	/*
	public String textScore() {
		String txt = "";
		List<Matrix> scores = queryScore();

		for (Matrix score : scores) {
			txt += (score.major
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
*/

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
