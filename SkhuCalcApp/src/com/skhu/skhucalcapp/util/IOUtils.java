package com.skhu.skhucalcapp.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class IOUtils {
	final static String FILE_NAME = "hello.txt";

	public static void writeToFile(String fileName, String body) {
		FileOutputStream fos = null;
		try {
			final File dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/skhuCalc/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			final File myFile = new File(dir, fileName + ".txt");
			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			fos = new FileOutputStream(myFile);
			fos.write(body.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
