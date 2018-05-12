package ulterion.develop.tripatku.kt_timesheet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;


public class AttendancePage extends AppCompatActivity {

    TextView result,inDT,outDT,statusField,workLogs,initTime;
    ImageView locIcon;
    Geocoder geocoder;
    List<Address> addressList;
    Context mContext;
    GPSTracker gpsT;
    double latti,longi;
    String addressStr,fullAddress,areaStr,cityStr,countryStr,zipCode,indateValue,outdateValue,DateString,loginLoc,logoutLoc,statusData;
    Button inTime,outTime;
    long dateDiff,inD,outD,HHs,MINs,Secs;
    SimpleDateFormat SDF;
    int LOC_CONST = 0; // if location fetched successfully
    ProgressBar resultProgrs;
    int counterValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_page);

        Intent nextPag = new Intent(AttendancePage.this,TimeSheet_Home.class);
        startActivity(nextPag);

        setTitle("Fill Attendance");
        resultProgrs=(ProgressBar)findViewById(R.id.progressBar2);
        locIcon=(ImageView)findViewById(R.id.imageView);
        result=(TextView)findViewById(R.id.textView4);
        inDT=(TextView)findViewById(R.id.textView3);
        outDT=(TextView)findViewById(R.id.textView5);
        statusField=(TextView)findViewById(R.id.textView6);
        workLogs=(TextView)findViewById(R.id.WorkLogView);
        initTime=(TextView)findViewById(R.id.InitTime);
        inTime=(Button)findViewById(R.id.button4);
        outTime=(Button)findViewById(R.id.button5);
        geocoder = new Geocoder(this, Locale.getDefault());
        mContext = this;
        SDF = new SimpleDateFormat("HH:mm:ss");
        initTime.setText(SDF.format(System.currentTimeMillis()));
        resultProgrs.setVisibility(resultProgrs.INVISIBLE);

        inTime.setEnabled(true);
        outTime.setEnabled(false);
        statusField.setText("Status: Login pending.!");
        loginLoc = getLocationNow();
        if(LOC_CONST == 0){
            locIcon.setImageResource(R.drawable.location_pointer);
        }else{
            locIcon.setImageResource(R.drawable.location_error);
        }

        result.setText(loginLoc);

        inTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTime.setVisibility(initTime.INVISIBLE);
                SDF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                inD = System.currentTimeMillis();
                indateValue= SDF.format(inD);
                inDT.setText("In Time : "+indateValue);
                inTime.setEnabled(false);
                outTime.setEnabled(true);
                statusField.setText("Status: Logout pending.!");
                SDF = new SimpleDateFormat("dd-MM-yyyy");
                DateString = SDF.format(inD);
            }
        });

        outTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SDF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                outD = System.currentTimeMillis();
                outdateValue = SDF.format(outD);
                outDT.setText("Out Time : "+outdateValue);
                outTime.setEnabled(false);
                inTime.setEnabled(false);
                dateDiff = outD - inD;
                Secs = ((dateDiff/1000)%60);
                MINs = (((dateDiff/1000)/60)%60);
                HHs = ((((dateDiff/1000)/60)/60)%60);
                statusData = "Work Effort: "+ HHs+" Hours "+MINs+" Minutes";
                statusField.setText(statusData);

                resultProgrs.setVisibility(resultProgrs.VISIBLE);
                new CountDownTimer(5000,10){
                    public void onTick(long millSecondsTime){
                        resultProgrs.setProgress(counterValue);
                        ++counterValue;
                        if (counterValue==100){
                            inTime.setVisibility(inTime.INVISIBLE);
                            outTime.setVisibility(inTime.INVISIBLE);
                            inDT.setVisibility(inTime.INVISIBLE);
                            outDT.setVisibility(inTime.INVISIBLE);
                            result.setVisibility(inTime.INVISIBLE);
                            statusField.setVisibility(inTime.INVISIBLE);
                            locIcon.setVisibility(locIcon.INVISIBLE);
                            logoutLoc = getLocationNow();
                            if(LOC_CONST == 1){
                                logoutLoc=loginLoc="Error -> Location";
                            }
                            setTitle("Work Log - " + DateString);
                            workLogs.setTextColor(Color.GRAY);
                            workLogs.append("Location Detail: \n" + "\t\t1. Login from: " + loginLoc + "\n\t\t2. Logout from: "+ logoutLoc
                                    +"\n\nTime Detail: \n"+ "\t\t1. Login Time: " + indateValue + "\n\t\t2. Logout Time: " + outdateValue + "\n\n" + statusData);
                            onFinish();
                        }
                    }
                    public void onFinish(){
                        resultProgrs.setVisibility(resultProgrs.INVISIBLE);
                    }
                }.start();
            }
        });

    }


    /********** Get current location **********/

    public String getLocationNow(){

        //Checking permissions

        if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AttendancePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            fullAddress="Location permission not granted..!";
            LOC_CONST = 1;

        }else{
            gpsT = new GPSTracker(mContext,AttendancePage.this);


            //check if GPS is enabled
            gpsT.getLocation();

            if(gpsT.canGetLocation()){
                latti = gpsT.getLatitude();
                longi = gpsT.getLongitude();

                try {
                    addressList = geocoder.getFromLocation(latti,longi,1);
                    addressStr = addressList.get(0).getAddressLine(0);
                    areaStr = addressList.get(0).getLocality();
                    cityStr = addressList.get(0).getAdminArea();
                    countryStr = addressList.get(0).getCountryName();
                    zipCode = addressList.get(0).getPostalCode();

                    fullAddress = addressStr+", "+areaStr+", "+cityStr+", "+countryStr+", "+zipCode;

                } catch (IOException e) {
                    e.printStackTrace();
                    LOC_CONST = 1;
                }finally {
                }

            }
            else{
                fullAddress="Cannot get location...Please allow location service and restart the application.";
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                LOC_CONST = 1;
                gpsT.showSettingsAlert();
            }
        }
        return fullAddress;
    }


    @Override
    public void onBackPressed() {

    }
}
