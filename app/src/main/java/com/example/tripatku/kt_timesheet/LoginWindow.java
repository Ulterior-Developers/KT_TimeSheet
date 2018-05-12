package ulterion.develop.tripatku.kt_timesheet;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;




public class LoginWindow extends AppCompatActivity {

    private EditText loginID, pWD;
    private String loginName,loginPWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_window);
        TextView log_quote = (TextView)findViewById(R.id.login_quote);
        setTitle("Login");

        log_quote.setText("Time is money. Wasted time means wasted money means trouble. \n" +
                "- Shirley Temple");
        loginID=(EditText)findViewById(R.id.editText);
        pWD=(EditText)findViewById(R.id.editText2);
        loginName="k";
        loginPWD="k";
        Button submitLogin = (Button)findViewById(R.id.button);

        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser(view);
            }
        });
    }


    private void authenticateUser(View v){
        if (loginID.getText().toString().trim().isEmpty()){
           loginID.setError("Login ID cannot be blank!");
        } else if(pWD.getText().toString().trim().isEmpty()) {
            pWD.setError("Password cannot be blank!");
        }else if (loginID.getText().toString().equals(loginName) && pWD.getText().toString().equals(loginPWD)){
            Intent nextPag = new Intent(LoginWindow.this,AttendancePage.class);
            startActivity(nextPag);
        }
        else{
            Toast.makeText(getApplicationContext(), "Authentication failed..", Toast.LENGTH_LONG).show();
            loginID.setText("");
            pWD.setText("");
        }
    }

    private void byPassLog(View v) {
        Intent nextPag = new Intent(LoginWindow.this,AttendancePage.class);
        startActivity(nextPag);
    }


    @Override
    public void onBackPressed() {

    }
}