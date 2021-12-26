package hurrys.corp.delivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Fragments.ApprovalWelcome;
import hurrys.corp.delivery.Fragments.Neworder;
import hurrys.corp.delivery.Fragments.OfflineFragment;
import hurrys.corp.delivery.Fragments.Welcome;
import hurrys.corp.delivery.R;

public class MainActivity extends AppCompatActivity {

    Session session;
    private static final String TAG = "MyFirebaseMsgService";

    ProgressBar progressBar;
    MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.progressbar);

        session = new Session(MainActivity.this);
        session.setisfirsttime("no");

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        if (!TextUtils.isEmpty(session.getusername())) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            session.settoken(token);
                            FirebaseMessaging.getInstance().subscribeToTopic("DeliveryPartner");
                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("MessagingToken").setValue(token);
                        }
                    });
        }




        FirebaseDatabase.getInstance().getReference().child("DeliveryPartner")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("Status").exists())
                                session.setstatus(dataSnapshot.child("Status").getValue().toString());
                            if(dataSnapshot.child("Submitted").exists())
                                session.setsubmitted("yes");
                            else
                                session.setsubmitted("no");


                            if(dataSnapshot.child("ApprovalStatus").exists()){
                                session.setapprovalstatus(dataSnapshot.child("ApprovalStatus").getValue().toString());
                                if(!session.getapprovalstatus().equals("Approved")){
                                    String comments="";
                                    if(dataSnapshot.child("Comments").exists()){
                                        comments=dataSnapshot.child("Comments").getValue().toString();
                                    }
                                    else{
                                        comments="Please proceed to fill the form";
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    Fragment fragment = new Welcome();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("comments",comments);
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();

                                }
                                else{
                                    if(session.getapprovalfirst().equals("Yes")){
                                        session.setapprovalfirst("No");
                                        progressBar.setVisibility(View.GONE);
                                        Fragment fragment = new ApprovalWelcome();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);

                                        FirebaseDatabase.getInstance().getReference().child("DeliveryPartner")
                                                .child(session.getusername())
                                                .child("Status")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()) {
                                                            if (dataSnapshot.getValue().toString().equals("Active")) {
                                                                session.setstatus("Active");
                                                                Fragment fragment = new Neworder();
                                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                                fragmentManager.beginTransaction()
                                                                        .addToBackStack(null)
                                                                        .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                                            } else {
                                                                session.setstatus("InActive");
                                                                Fragment fragment = new OfflineFragment();
                                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                                fragmentManager.beginTransaction()
                                                                        .addToBackStack(null)
                                                                        .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                                            }
                                                        }
                                                        else{
                                                            Fragment fragment = new OfflineFragment();
                                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                                            fragmentManager.beginTransaction()
                                                                    .addToBackStack(null)
                                                                    .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                    }

                                }
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        player = MediaPlayer.create(getApplicationContext(), R.raw.sound);

        FirebaseDatabase.getInstance().getReference().child("Orders")
                .orderByChild(session.getusername()).equalTo(session.getusername())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot v: dataSnapshot.getChildren()) {
                                if (v.child("Status").exists()) {
                                    if (v.child("Status").getValue().toString().equals("2")) {
                                        if(!player.isPlaying()) {
                                            player.start();
                                            player.setLooping(true);
                                        }
                                    } else {
                                        stopPlaying();
                                    }
                                } else {
                                    stopPlaying();
                                }
                                if(v.child("DeliveryStatus").exists()){
                                    stopPlaying();
                                }
                            }
                        }
                        else{
                            stopPlaying();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()) {
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("GPS Not Enabled")  // GPS not found
                                        .setMessage("Are you sure u want enable gps") // Want to enable?
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"e",Toast.LENGTH_LONG).show();
                            final SweetAlertDialog sDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            sDialog.setTitleText("Warning!");
                            sDialog.setContentText("Some PermissionActivity have been denied permanently. Please go to setting to enable!");
                            sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                }
                            });
                            sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sDialog.dismiss();
                                }
                            });
                            sDialog.setCancelable(false);
                            sDialog.show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> PermissionActivity, PermissionToken token) {
                    }
                })
                .check();

    }

    private void stopPlaying() {
        if (player != null) {
            if(player.isPlaying()) {
                player.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        final SweetAlertDialog sDialog = new SweetAlertDialog(this, SweetAlertDialog.BUTTON_CONFIRM);
        sDialog.setTitleText("App Update!");
        sDialog.setContentText("Please update the app for a faster and better experience!");
        sDialog.setConfirmText("Update");
        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    anfe.printStackTrace();
                }
            }
        });



        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        try {
            assert info != null;
            final int version = info.versionCode;


            FirebaseDatabase.getInstance().getReference().child("AppContent").child("Application").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        double vno = Double.parseDouble(dataSnapshot.child("DeliveryAppVersion").getValue().toString());
                        String imp = dataSnapshot.child("IMP").getValue().toString();
                        if (version != vno) {
                            if (imp.equals("No")) {
                                sDialog.show();
                                sDialog.setCancelText("No");
                                sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                });
                            } else {
                                sDialog.show();
                                sDialog.setCancelable(false);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



}
