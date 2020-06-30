package com.example.icetouch.dorest;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class GradeActivity extends AppCompatActivity{
    private int real_i = 0, real_qt = 0;
    private String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView title, content;
        int EstimatedMaxNumber = 100;

        for(int i = 1; i < EstimatedMaxNumber; i++) {
            if(!getSharedPreferences("" + i, MODE_PRIVATE).getString("checker", "").equals(""))
            {
                int temp = getSharedPreferences("" + i, MODE_PRIVATE).getInt("id", 0);
                real_i = temp > real_i ? temp : real_i;
            }
        }

        for(int i = 1; i <= real_i; i++) {
            int temp = getSharedPreferences("" + i, MODE_PRIVATE).getInt("number", 0);
            real_qt += temp;
        }

        for(int i = 1; i <= real_i; i ++){
            s += String.format(getResources().getString(R.string.each),
                    getSharedPreferences("" + i, MODE_PRIVATE).getString("name", ""),
                    getSharedPreferences("" + i, MODE_PRIVATE).getInt("number", 0),
                    getSharedPreferences("" + i, MODE_PRIVATE).getInt("grade", 0),
                    getSharedPreferences("" + i, MODE_PRIVATE).getInt("number", 0) == 0 ? 0 : 10 *
                            getSharedPreferences("" + i, MODE_PRIVATE).getInt("grade", 0) /
                            getSharedPreferences("" + i, MODE_PRIVATE).getInt("number", 0),
                    getSharedPreferences("" + i, MODE_PRIVATE).getLong("restMinute", 0) == 0 ? 0 :
                            getSharedPreferences("" + i, MODE_PRIVATE).getInt("time", 0) - 1 - getSharedPreferences("" + i, MODE_PRIVATE).getLong("restMinute", 0),
                    getSharedPreferences("" + i, MODE_PRIVATE).getLong("restSecond", 0) == 0 ? 0 :
                            60 - getSharedPreferences("" + i, MODE_PRIVATE).getLong("restSecond", 0),
                    getSharedPreferences("" + i, MODE_PRIVATE).getString("checker", ""),
                    getSharedPreferences("" + i, MODE_PRIVATE).getString("tag", "无"));
        }

        title = (TextView) findViewById(R.id.grade_title);
        content = (TextView) findViewById(R.id.content);

        title.setText("成绩单");
        content.setText(getSharedPreferences("common", MODE_PRIVATE).getBoolean("isON", false)  ?
                String.format(getResources().getString(R.string.punishment), "考试与查看成绩") :
                String.format(getResources().getString(R.string.sum), real_i, real_qt) + s);
    }
}