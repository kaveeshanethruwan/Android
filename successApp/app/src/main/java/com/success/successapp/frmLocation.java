package com.success.successapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class frmLocation extends AppCompatActivity implements LocationListener {

    Location myLocation;
    LocationManager myLocationManager;

    private TextView txtlocation;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_location);

        myLocationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        txtlocation=findViewById(R.id.txtlocation);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 50, this);
        myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50,50,this);

        if(myLocation==null){
            myLocation=myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            myLocation=myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        else{
            txtlocation.setText("Longitude : " + myLocation.getLongitude() + "\n Latitude : " + myLocation.getLongitude());
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        myLocation=location;
        txtlocation.setText("Longitude : " + myLocation.getLongitude() + "\n Latitude : " + myLocation.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
