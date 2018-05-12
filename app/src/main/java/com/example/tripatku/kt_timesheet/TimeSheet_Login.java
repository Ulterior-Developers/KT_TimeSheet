package ulterion.develop.tripatku.kt_timesheet;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimeSheet_Login extends AppCompatActivity {

    ProgressBar mainPrgbar;
    int counterValue=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sheet__login);
        TextView splash_text = (TextView)findViewById(R.id.textView);
        splash_text.setText("Time is money. Wasted time means wasted money means trouble. \n" +
                "- Shirley Temple");
        mainPrgbar=(ProgressBar)findViewById(R.id.progressBar);
        newTimer();
    }



    public void newTimer(){
        new CountDownTimer(10000,1){
            public void onTick(long millSecondsTime){
                mainPrgbar.setProgress(counterValue);
                if (counterValue==100){
                    Intent nextPag = new Intent(TimeSheet_Login.this,LoginWindow.class);
                    startActivity(nextPag);
                }
                ++counterValue;
            }
            public void onFinish(){

            }
        }.start();
    }


}
