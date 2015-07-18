	/*
    public void mOnClick(View v){
        SQLiteDatabase db;
        ContentValues row;

        switch (v.getId()){
            case R.id.insert :
                db = mHelper.getWritableDatabase();
                // insert 메서드로 삽입
                row = new ContentValues();
                row.put("eng", "boy");
                row.put("han", "머스마");
                db.insert("dic", null, row);
                // SQL
                db.execSQL("insert into dic values(null, 'girl', '가시나');");
                mHelper.close();
                mText.setText("insert success");
                break;
            case R.id.delete :
                db = mHelper.getWritableDatabase();
                db.delete("dic", null, null);
                mHelper.close();
                mText.setText("delete success");
                break;
            case R.id.update :
                db = mHelper.getWritableDatabase();
                row = new ContentValues();
                row.put("han", "소년");
                db.update("dic", row, "eng = 'boy'", null);
                mHelper.close();
                mText.setText("update success");
                break;
            case R.id.select :
                db = mHelper.getReadableDatabase();
                Cursor cursor;

                cursor = db.rawQuery("select eng, han from dic", null);

                String result = "";
                while(cursor.moveToNext()){
                    String eng = cursor.getString(0);
                    String han = cursor.getString(1);
                    result += (eng + " = " + han + "\n");
                }

                if(result.length() == 0){
                    mText.setText("empty set");
                } else {
                    mText.setText(result);
                }
                cursor.close();
                mHelper.close();
                break;
        }
    }
	 */