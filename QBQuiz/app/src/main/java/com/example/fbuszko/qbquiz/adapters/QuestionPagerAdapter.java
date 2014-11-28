package com.example.fbuszko.qbquiz.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fbuszko.qbquiz.R;
import com.example.fbuszko.qbquiz.listeners.Listener;
import com.example.fbuszko.qbquiz.model.QuizQuestion;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class QuestionPagerAdapter extends PagerAdapter {

    private List<QuizQuestion> mQuestions;
    private Context mContext;
    private Listener mListener;

    public QuestionPagerAdapter(List<QuizQuestion> questions, Context context, Listener listener) {
        mQuestions = questions;
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final QuizQuestion question = mQuestions.get(position);
        View view = View.inflate(mContext, R.layout.question_page, null);
        ImageView questionImage = (ImageView)view.findViewById(R.id.question_image);
        ImageLoader.getInstance().displayImage(question.getImageUrl(), questionImage);

        ImageView tickImage = (ImageView) view.findViewById(R.id.tick);
        if(question.isAnswered() == 1){
            tickImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.green_tick));
            tickImage.setVisibility(View.VISIBLE);
        }

        final EditText questionEditText = (EditText) view.findViewById(R.id.question_edit_text);
        Button checkButton = (Button) view.findViewById(R.id.check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckClicked(questionEditText.getText().toString(), question);
            }
        });

        view.setTag(position);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}