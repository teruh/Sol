package me.zacl.sol;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

   private static final int                         PERMISSION_REQUEST_FINE_LOCATION = 0;
   private              FusedLocationProviderClient fusedLocationClient;

   private TextView lastLocationTextView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

      lastLocationTextView = findViewById(R.id.last_location_text);

      findViewById(R.id.last_location_button).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            getLastKnownLocation();
         }
      });
   }

   private void getLastKnownLocation() {
      if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
          PackageManager.PERMISSION_GRANTED)) {
         fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                               @Override
                               public void onSuccess(Location location) {
                                  // Got last known location. In some rare situations this can be
                                  // null.
                                  if (location != null) {
                                     lastLocationTextView.setText(
                                         location.getLongitude() + " " + location.getLatitude());
                                  }
                               }
                            });
      }
      else {
         requestLocationPermission();
      }
   }

   private void requestLocationPermission() {
      AlertDialog         dialog;
      AlertDialog.Builder builder = new AlertDialog.Builder(this);

      builder.setMessage(R.string.permissions_req_dialog_message)
             .setTitle(R.string.permissions_req_dialog_title);

      builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
         }
      });

      if (ActivityCompat.shouldShowRequestPermissionRationale(
          this, Manifest.permission.ACCESS_FINE_LOCATION)) {
         builder.create();
      } else {
         ActivityCompat.requestPermissions(MainActivity.this, new String[] {
             Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
      }
   }
}
