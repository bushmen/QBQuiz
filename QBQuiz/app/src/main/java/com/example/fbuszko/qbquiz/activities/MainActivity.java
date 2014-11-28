package com.example.fbuszko.qbquiz.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.fbuszko.qbquiz.DataGenerator;
import com.example.fbuszko.qbquiz.QuestionsProvider;
import com.example.fbuszko.qbquiz.R;
import com.example.fbuszko.qbquiz.adapters.QuestionPagerAdapter;
import com.example.fbuszko.qbquiz.dao.QuestionsDataSource;
import com.example.fbuszko.qbquiz.database.MySQLiteHelper;
import com.example.fbuszko.qbquiz.listeners.Listener;
import com.example.fbuszko.qbquiz.model.QuizQuestion;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements Listener {

    @InjectView(R.id.question_pager)
    ViewPager mQuestionPager;

    private QuestionsDataSource dataSource;
    private List<QuizQuestion> mQuestions = new ArrayList<QuizQuestion>();
    private Vibrator vibe;


    private String [] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_IMG_URL, MySQLiteHelper.COLUMN_ANSWER,
            MySQLiteHelper.COLUMN_IS_ANSWERED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        dataSource = new QuestionsDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataGenerator.generateQuestions(this);
        Cursor cursor = getContentResolver().query(QuestionsProvider.CONTENT_URI, allColumns, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            mQuestions.add(cursorToQuestion(cursor));
            cursor.moveToNext();
        }
        mQuestionPager.setAdapter(new QuestionPagerAdapter(mQuestions, this, this));
        mQuestionPager.setCurrentItem(0);

        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckClicked(String answer, QuizQuestion question) {
        RelativeLayout page= (RelativeLayout)mQuestionPager.findViewWithTag(mQuestionPager.getCurrentItem());
        final ImageView tick = (ImageView) page.findViewById(R.id.tick);
        if(question.getAnswer().equals(answer)){
            question.setAnswered(1);
            dataSource.updateQuestion(question);
            if(tick.getVisibility() == View.VISIBLE){
                tick.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tick_animation_dissapear));
                tick.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tick.setImageDrawable(getResources().getDrawable(R.drawable.green_tick));
                        tick.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tick_animation));
                    }
                }, 300);
            } else {
                tick.setImageDrawable(getResources().getDrawable(R.drawable.green_tick));
                tick.setVisibility(View.VISIBLE);
                tick.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tick_animation));
            }


            mQuestionPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mQuestionPager.setCurrentItem(mQuestionPager.getCurrentItem() + 1);
                }
            }, 2000);
        } else {
            if(tick.getVisibility() == View.INVISIBLE) {
                tick.setImageDrawable(getResources().getDrawable(R.drawable.red_tick));
                tick.setVisibility(View.VISIBLE);
                tick.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tick_animation));
            } else {
                tick.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tick_animation_wrong_repeat));
                View view = mQuestionPager.findViewWithTag(mQuestionPager.getCurrentItem());
                Animation mWiggleEffectAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle_effect);
                view.startAnimation(mWiggleEffectAnimation);
                vibe.vibrate(200);
            }
        }
    }

    private QuizQuestion cursorToQuestion(Cursor cursor) {
        QuizQuestion question = new QuizQuestion();
        question.setId(cursor.getLong(0));
        question.setImageUrl(cursor.getString(1));
        question.setAnswer(cursor.getString(2));
        question.setAnswered(cursor.getInt(3));
        return question;
    }
}
