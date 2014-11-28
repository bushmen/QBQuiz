package com.example.fbuszko.qbquiz;

import android.content.ContentValues;
import android.content.Context;

import com.example.fbuszko.qbquiz.dao.QuestionsDataSource;
import com.example.fbuszko.qbquiz.database.MySQLiteHelper;
import com.example.fbuszko.qbquiz.model.QuizQuestion;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    private static int [] ids = new int[]{R.drawable.aaron_rodgers, R.drawable.andy_dalton, R.drawable.brian_hoyer, R.drawable.cam_newton, R.drawable.drew_stanton, R.drawable.jay_cutler,
            R.drawable.joe_flacco, R.drawable.kyle_orton, R.drawable.matt_ryan, R.drawable.matthew_stafford, R.drawable.peyton_manning, R.drawable.tony_romo};
    private static String [] names = new String[]{"Aaron Rodgers", "Andy Dalton", "Brian Hoyer", "Cam Newton", "Drew Stanton", "Jay Cutler", "Joe Flacco", "Kyle Orton", "Matt Ryan",
            "Matthew Stafford", "Peyton Manning", "Tony Romo"};

    public static void generateQuestions(Context context){
        for(int i = 0; i < ids.length; i++){
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_IMG_URL, "drawable://" + ids[i]);
            values.put(MySQLiteHelper.COLUMN_ANSWER, names[i]);
            values.put(MySQLiteHelper.COLUMN_IS_ANSWERED, 0);
            context.getContentResolver().insert(QuestionsProvider.CONTENT_URI, values);
        }
    }
}
