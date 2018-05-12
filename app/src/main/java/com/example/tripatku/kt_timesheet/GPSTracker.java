package ulterion.develop.tripatku.kt_timesheet;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by tripatku on 9/18/2017.
 */

public class GPSTracker extends Service {

    private Context mContext;
    Activity activity;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location;

    // Longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    // 10 meters
    // The minimum time between updates in milliseconds

    private static final long MIN_TIME_BW_UPDATES = 0;

    // 1 minute
    // Declaring a Location Manager
    protected LocationManager locMGR;

    double latt,longt;



    public GPSTracker(){

    }

    public GPSTracker(Context context, Activity activity){
        this.mContext = context;
        this.activity = activity;

    }

    public Location getLocation(){

        try{
            locMGR = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = locMGR.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting Network status
            isNetworkEnabled = locMGR.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){
                // No network provider is enabled
            }else{

                this.canGetLocation = true;
                if(isNetworkEnabled){
                    int requestPermissionsCode = 50;

                    locMGR.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,mLocationListener);

                    if(locMGR != null){
                        location = locMGR.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latt = location.getLatitude();
                            longt = location.getLongitude();
                        }
                    }
                }
            }
            // If GPS enabled, get latitude/longitude using GPS Services
            if(isGPSEnabled){
                if(location == null){
                    if(ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 50);
                    }else{
                        locMGR.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,mLocationListener);

                        if(locMGR != null){
                            location = locMGR.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null){
                                latt = location.getLatitude();
                                longt = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    /** Stop using GPS listener
     * Calling this method will stop using GPS the app.
     */

    public void stopUsingGPS(){

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                latt = location.getLatitude();
                longt= location.getLongitude();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    /**

     * Function to get latitude

     */

    public double getLatitude() {
        if (location != null) {
            latt = location.getLatitude();
        }

        // return latitude

        return latt;
    }


    /**     * Function to get longitude

     */

    public double getLongitude() {
        if (location != null) {
            longt = location.getLongitude();
        }

        // return longitude

        return longt;
    }

    /**     * Function to check GPS/Wi-Fi enabled     *

     * @return boolean     */

    public boolean canGetLocation() {

        return this.canGetLocation;
    }


    /**     * Function to show settings alert dialog.

     * On pressing the Settings button it will launch Settings Options.     */



    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title

        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing the cancel button

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message

        alertDialog.show();
    }

    @Override    public IBinder onBind(Intent arg0) {
        return null;
    }
}
