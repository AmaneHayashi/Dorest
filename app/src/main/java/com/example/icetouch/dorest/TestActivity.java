package com.example.icetouch.dorest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity{

    private Button entry;
    private CheckBox qta, qtb, qtc, qtd;
    private Button bt_submit;
    private CountDownTimer countDownTimer;
    private AlertDialog dialog;
    private String checker = "";
    private int grade = 0, next_times = 1, seed = 0;
    private final int isStart = 1, isEnd = 0;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = getApplicationContext();

        final String name = getIntent().getStringExtra("test_name");
        final int id = getIntent().getIntExtra("test_id", 0);
        final int numId = getIntent().getIntExtra("test_num", 0);
        final int timeId = getIntent().getIntExtra("test_word", 0);
        final String order = getIntent().getStringExtra("test_order");
        final int priority = getIntent().getIntExtra("test_priority", 0);
        final int imageArray[] = getIntent().getIntArrayExtra("test_imageArr");
        final String answer[] = getIntent().getStringArrayExtra("test_ans");
        JumpToTest(name, id, numId, order, timeId, priority, imageArray, answer);
    }

    public void JumpToTest(final String name, final int id, final int numId, final String order, final int timeId, final int priority, final int imageArray[], final String answer[]){
        setContentView(R.layout.activity_test);
        TextView label, reminder;

        label = (TextView) findViewById(R.id.label);
        reminder = (TextView) findViewById(R.id.reminder);
        entry = (Button) findViewById(R.id.bt_entrance);
        label.setText("题目说明");
        reminder.setText(String.format(getResources().getString(R.string.word), name, numId, order, timeId));
        entry.setText("开始");

        CheckForTest(priority);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSharedPreferences("common", MODE_PRIVATE).getBoolean("isDONE_" + id, false)) {
                    AlertDialog.Builder enterWarning = new AlertDialog.Builder(TestActivity.this);
                    enterWarning.setTitle("警告")
                            .setIcon(R.drawable.dric_illegal)
                            .setMessage("你已完成过本部分，返回还是重做？")
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    TestActivity.this.finish();
                                }
                            })
                            .setPositiveButton("重做", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ClearForRetry(id);
                                    int repeatedTimes = getSharedPreferences("" + id, MODE_PRIVATE).getInt("RepeatedTimes", 0);
                                    getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("RepeatedTimes", ++repeatedTimes).apply();
                                    Toast.makeText(TestActivity.this, "原有考试数据已清空，重新点击按钮即可开始考试！", Toast.LENGTH_SHORT).show();
                                    JumpToTest(name, id, numId, order, timeId, priority, imageArray, answer);
                                }
                            })
                            .setNeutralButton("教师模式", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AlertDialog.Builder againChoose = new AlertDialog.Builder(TestActivity.this);
                                    againChoose.setTitle("教师模式选择")
                                            .setIcon(R.drawable.dric_legal)
                                            .setMessage("请选择你需要的教师模式。")
                                            .setPositiveButton("重新阅卷", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ReJudge(name, id, priority, numId, timeId, answer);
                                                }
                                            })
                                            .setNeutralButton("讲题模式", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getSharedPreferences("" + id, MODE_PRIVATE).edit().putBoolean("isTeacherMode", true).apply();
                                                    Toast.makeText(TestActivity.this, "进入讲题模式", Toast.LENGTH_SHORT).show();
                                                    JumpToQT(name, id, numId, timeId, priority, imageArray, answer);
                                                }
                                            })
                                             .setNegativeButton("返回", null);
                                    againChoose.create().show();
                                }
                            });
                    enterWarning.create().show();
                }
                else {
                    SetIsON(isStart);
                    Toast.makeText(TestActivity.this, String.format(getResources().getString(R.string.reminder), timeId), Toast.LENGTH_SHORT).show();
                    countDownTimer = new CountDownTimer(timeId * 60 * 1000 + 500, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished < 301 * 1000) {
                                seed++;
                                ShowTimeWarn(seed);
                            }
                            getSharedPreferences("" + id, MODE_PRIVATE).edit().putLong("restMinute", millisUntilFinished / 1000 / 60).apply();
                            getSharedPreferences("" + id, MODE_PRIVATE).edit().putLong("restSecond",
                                    (millisUntilFinished - millisUntilFinished / 1000 / 60 * 1000 * 60) / 1000).apply();
                        }
                        @Override
                        public void onFinish() {
                            JudgeHelper(name, id, numId, timeId, priority, checker, grade);
                        }
                    }.start();
                    JumpToQT(name, id, numId, timeId, priority, imageArray, answer);
                }
            }
        });
    }

    public void JumpToQT(final String name, final int id, final int numId, final int timeId, final int priority, final int imageArray[], final String answer[]){
        setContentView(R.layout.activity_qt);

        ImageView qt;
        final Bitmap bitmap = readBitMap(getContext(), imageArray[next_times]);

        qt = (ImageView) findViewById(R.id.qt);
        qt.setImageBitmap(bitmap);
        qt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSharedPreferences("" + id, MODE_PRIVATE).getBoolean("isTeacherMode", false)) {
                    if(next_times == numId) {
                        getSharedPreferences("" + id, MODE_PRIVATE).edit().putBoolean("isTeacherMode", false).apply();
                        Toast.makeText(TestActivity.this, "退出讲题模式", Toast.LENGTH_SHORT).show();
                        TestActivity.this.finish();
                    }
                    else {
                        next_times++;
                        bitmap.recycle();
                        System.gc();
                        JumpToQT(name, id, numId, timeId, priority, imageArray, answer);
                    }
                }
                else {
                    bitmap.recycle();
                    System.gc();
                    JumpToQTA(name, id, numId, timeId, priority, imageArray, answer);
                }
            }
        });
    }

    public void JumpToQTA(final String name, final int id, final int numId, final int timeId, final int priority, final int imageArray[], final String answer[]){
        setContentView(R.layout.activity_qta);
        TextView label2;
        Button bt_qt, bt_timeReq, bt_diffReq;

        label2 = (TextView) findViewById(R.id.label2);
        qta = (CheckBox) findViewById(R.id.qta);
        qtb = (CheckBox) findViewById(R.id.qtb);
        qtc = (CheckBox) findViewById(R.id.qtc);
        qtd = (CheckBox) findViewById(R.id.qtd);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_qt = (Button) findViewById(R.id.bt_qt);
        bt_timeReq = (Button) findViewById(R.id.bt_timeReq) ;
        bt_diffReq = (Button) findViewById(R.id.bt_diffReq);

        label2.setText(name);
        qta.setText("A");
        qtb.setText("B");
        qtc.setText("C");
        qtd.setText("D");
        bt_qt.setText("返回读题");
        bt_qt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpToQT(name, id, numId, timeId, priority, imageArray, answer);
            }
        });
        bt_timeReq.setText("查看剩余时间");
        bt_timeReq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        bt_timeReq.getPaint().setAntiAlias(true);
        bt_timeReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TestActivity.this, String.format(getResources().getString(R.string.time),
                        getSharedPreferences("" + id, MODE_PRIVATE).getLong("restMinute", 0),
                        getSharedPreferences("" + id, MODE_PRIVATE).getLong("restSecond", 0)), Toast.LENGTH_SHORT).show();
            }
        });
        bt_diffReq.setText("本题较难，标记");
        bt_diffReq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        bt_diffReq.getPaint().setAntiAlias(true);
        bt_diffReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = getSharedPreferences("" + id, MODE_PRIVATE).getString("tag", "") + next_times + " ";
                getSharedPreferences("" + id, MODE_PRIVATE).edit().putString("tag", tag).apply();
                Toast.makeText(TestActivity.this, "已标记本题" ,Toast.LENGTH_SHORT).show();
            }
        });
        SetDorest(name, id, numId, timeId, priority, imageArray, answer);
    }

    public void SetDorest(final String name, final int id, final int numId, final int timeId, final int priority, final int imageArray[], final String answer[]){
        qta.setChecked(true);
        qtb.setChecked(true);
        qtc.setChecked(true);
        qtd.setChecked(true);
        qta.setChecked(false);
        qtb.setChecked(false);
        qtc.setChecked(false);
        qtd.setChecked(false);
        if(next_times == numId) {
            bt_submit.setText("提交答卷");
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder SubmitWarning = new AlertDialog.Builder(TestActivity.this);
                    SubmitWarning.setTitle("提示")
                            .setIcon(R.drawable.dric_legal)
                            .setMessage("你确定要提交吗？")
                            .setNegativeButton("否", null)
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checker += mJudgeHelper(id, numId, qta, qtb, qtc, qtd, answer);
                                    grade +=  10 * mJudgeHelper(id, numId, qta, qtb, qtc, qtd, answer);
                                    JudgeHelper(name, id, numId, timeId, priority, checker, grade);
                                }
                            });
                    SubmitWarning.create().show();
                }
            });
        }
        else {
            bt_submit.setText("下一题");
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checker += mJudgeHelper(id, next_times, qta, qtb, qtc, qtd, answer);
                    grade +=  10 * mJudgeHelper(id, next_times, qta, qtb, qtc, qtd, answer);
                    next_times++;
                    JumpToQT(name, id, numId, timeId, priority, imageArray, answer);
                }
            });
        }
    }

    public static Context getContext() {
        return context;
    }

    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    public int mJudgeHelper(int id, int s, CheckBox a, CheckBox b, CheckBox c, CheckBox d, final String answer[]){
        String aa = a.isChecked() ? "A" : "";
        String bb = b.isChecked() ? "B" : "";
        String cc = c.isChecked() ? "C" : "";
        String dd = d.isChecked() ? "D" : "";
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putString("" + s, aa + bb + cc + dd).apply();
        return answer[s].equals(aa + bb + cc + dd) ? 1 : 0;
    }

    public void JudgeHelper(String name, int id, int numId, int timeId, int priority, String check, int grade){
        Toast.makeText(TestActivity.this, "成绩单有更新，进入成绩单即可查看", Toast.LENGTH_SHORT).show();
        check += new String(new char[ numId - check.length()]).replace("\0", "X");
        SetIsON( isEnd );
        SaveForGrade(name, id, numId, timeId, priority, check, grade);
        SetForTest(id, priority);
        countDownTimer.cancel();
        TestActivity.this.finish();
    }

    public void CheckForTest(int priority) {
        getSharedPreferences("common", MODE_PRIVATE).edit().putBoolean("priority_0", true).apply();
        if(!getSharedPreferences("common", MODE_PRIVATE).getBoolean("priority_"+ (priority - 1), false)) {
            AlertDialog.Builder warning = new AlertDialog.Builder(TestActivity.this);
            warning.setTitle("提示")
                    .setIcon(R.drawable.dric_illegal)
                    .setMessage("你当前权限不足，不能进行该项目的考试。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TestActivity.this.finish();
                        }
                    });
            warning.create().show();
            entry.setVisibility(View.GONE);
        }
    }

    public void SetForTest(int id, int priority){
        getSharedPreferences("common", MODE_PRIVATE).edit().putBoolean("isDONE_" + id, true).apply();
        getSharedPreferences("common", MODE_PRIVATE).edit().putBoolean("priority_" + priority, true).apply();
    }

    public void ShowTimeWarn(int seed){
        if(seed == 2) {
            TimerTask task = new TimerTask() {
                public void run() {
                    dialog.dismiss();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 5000);
            AlertDialog.Builder timeWarn = new AlertDialog.Builder(TestActivity.this);
            timeWarn.setTitle("提醒")
                    .setIcon(R.drawable.dric_legal)
                    .setMessage(R.string.timeWarn)
                    .setPositiveButton("确定",null);
            dialog = timeWarn.create();
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void SetIsON(int flag) {
        switch (flag) {
            case isStart:
                getSharedPreferences("common", MODE_PRIVATE).edit().putBoolean("isON", true).apply();
                break;
            case isEnd:
                getSharedPreferences("common", MODE_PRIVATE).edit().putBoolean("isON", false).apply();
                break;
            default:
                break;
        }
    }

    public void ClearForRetry(int id){
        getSharedPreferences("common", MODE_PRIVATE).edit().putBoolean("isDONE_" + id, false).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putString("checker", "").apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("grade", 0).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putLong("restSecond", 0).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putLong("restMinute", 0).apply();
    }

    public void SaveForGrade(String name, int id, int numId, int timeId,  int priority, String check, int grade) {
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putString("name", name).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("id", id).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("priority", priority).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("number", numId).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("time", timeId).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putString("checker", check).apply();
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("grade", grade).apply();
    }

    public void ReJudge(String name, int id, int priority, int numId, int timeId, String answer[]){
        checker = "";
        grade = 0;
        for(int i = 1; i <= numId; i ++) {
            String temp = getSharedPreferences("" + id, MODE_PRIVATE).getString("" + i, "");
            checker += answer[i].equals(temp) ? "1" : "0";
            grade += 10 * (answer[i].equals(temp) ? 1 : 0);
        }
        int rejudgedTimes = getSharedPreferences("" + id, MODE_PRIVATE).getInt("RejudgedTimes", 0);
        getSharedPreferences("" + id, MODE_PRIVATE).edit().putInt("RejudgedTimes", ++rejudgedTimes).apply();
        Toast.makeText(TestActivity.this, "重新阅卷已完成", Toast.LENGTH_SHORT).show();
        SaveForGrade(name, id, numId, timeId, priority, checker, grade);
        SetForTest(id, priority);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(getSharedPreferences("common", MODE_PRIVATE).getBoolean("isON", false)) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Toast.makeText(this, "考试过程中，不允许退出！", Toast.LENGTH_SHORT).show();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}