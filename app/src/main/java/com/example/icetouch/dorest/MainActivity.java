package com.example.icetouch.dorest;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button enter;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView title;
        Button show;
        int count = getSharedPreferences("common", MODE_PRIVATE).getInt("InTimes", 0);

        title = (TextView) findViewById(R.id.title);
        enter = (Button) findViewById(R.id.enter);
        show = (Button) findViewById(R.id.show);
        title.setText("冬试");
        enter.setText("考试入口");
        show.setText("成绩单");

        if(count == 0) {
            AlertDialog.Builder welcome = new AlertDialog.Builder(MainActivity.this);
            welcome.setTitle("欢迎")
                    .setIcon(R.drawable.dric_legal)
                    .setMessage(R.string.welcome)
                    .setPositiveButton("谢谢", null);
            welcome.create().show();
        }
        getSharedPreferences("common", MODE_PRIVATE).edit().putInt("InTimes", ++count).apply();

        PunishmentJudge();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestShowActivity.class);
                startActivity(intent);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSharedPreferences("common", MODE_PRIVATE).getBoolean("isDONE_1", false)) {
                    Intent intent = new Intent(MainActivity.this, GradeActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "至少完成一项考试，才能查看成绩！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void PunishmentJudge(){
        if(getSharedPreferences("common", MODE_PRIVATE).getBoolean("isON", false)) {
            AlertDialog.Builder punish = new AlertDialog.Builder(MainActivity.this);
            punish.setTitle("警告")
                    .setIcon(R.drawable.dric_illegal)
                    .setMessage(String.format(getResources().getString(R.string.punishment), "考试"))
                    .setPositiveButton("确定", null);
            punish.create().show();
            enter.setVisibility(View.INVISIBLE);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}