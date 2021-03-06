package hurrys.corp.delivery.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.zcw.togglebutton.ToggleButton;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import hurrys.corp.delivery.Activities.Login;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.R;


public class OfflineFragment extends Fragment  implements
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    public OfflineFragment() {
        // Required empty public constructor
    }

    private TextView earnings,paymentdate;
    private CircleImageView pp;
    private Button go;
    private Session session;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigation;
    Boolean mSlideState=false;

    private LocationRequest mLocationRequest;



    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private final float DEFAULT_ZOOM = 15;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_offline, container, false);


        earnings=v.findViewById(R.id.earnings);
        pp=v.findViewById(R.id.pp);
        go=v.findViewById(R.id.go);
        paymentdate=v.findViewById(R.id.date);

        session=new Session(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Places.initialize(getContext(), "AIzaSyBpffV9yHzav26I5CBZxT0BdJuo06MsK3I");
        placesClient = Places.createClient(getContext());
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        final DecimalFormat form = new DecimalFormat("0.00");

        FirebaseDatabase.getInstance().getReference().child("DeliveryPartner")
                .child(session.getusername())
                .child("PendingAmount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            earnings.setText("\u00a3"+form.format(Double.parseDouble(dataSnapshot.getValue().toString())));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        if(session.getstage().equals("Reach")){
            Fragment fragment = new ReachStore();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame_container, fragment).commit();
        }
        else  if(session.getstage().equals("Pick")){
            Fragment fragment = new PickupOrder();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame_container, fragment).commit();
        }
        else  if(session.getstage().equals("Delivery")){
            Fragment fragment = new OrderDelivery();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame_container, fragment).commit();
        }
        else  if(session.getstage().equals("ReachCustomer")){
            Fragment fragment = new ReachCustomer();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame_container, fragment).commit();
        }


        FirebaseDatabase.getInstance().getReference().child("Masters")
                .child("PaymentDate")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String a = dataSnapshot.getValue().toString();
                            if(a.equals("1")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.MONDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else if(a.equals("2")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.TUESDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("3")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.WEDNESDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("4")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.THURSDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("5")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.FRIDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("6")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.SATURDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("7")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.SUNDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Status").setValue("Active");
                session.setstatus("Active");

                if(getActivity()!=null) {
                    Fragment fragment = new Neworder();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                }

            }
        });


        if(getActivity()!=null)
        navigation=(NavigationView)getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);

        mDrawerLayout=(DrawerLayout)getActivity().findViewById(R.id.drawer);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                else{
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        View headerview = navigation.getHeaderView(0);
        final LinearLayout l1=(LinearLayout)headerview.findViewById(R.id.l1);
        final LinearLayout l2=(LinearLayout)headerview.findViewById(R.id.l2);
        final LinearLayout l3=(LinearLayout)headerview.findViewById(R.id.l3);
        final LinearLayout l4=(LinearLayout)headerview.findViewById(R.id.l4);
        final LinearLayout l5=(LinearLayout)headerview.findViewById(R.id.l5);
        final LinearLayout l6=(LinearLayout)headerview.findViewById(R.id.l6);
        final LinearLayout edit=(LinearLayout)headerview.findViewById(R.id.edit);
        final ToggleButton status=headerview.findViewById(R.id.status);
        final CircleImageView pp1=headerview.findViewById(R.id.pp);
        final TextView name=headerview.findViewById(R.id.name);
        final TextView userid=headerview.findViewById(R.id.userid);


        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        for (int i = 0; i < fm.getBackStackEntryCount() - 1; ++i) {
//            fm.popBackStack();
//        }
//
//
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    System.exit(0);
                    return true;
                }
                return false;
            }
        });


        userid.setText(session.getusername());
        name.setText(session.getname());


        if(session.getstatus().equals("Active")){
            status.setToggleOn(true);
        }
        else{
            status.setToggleOff(false);
        }


        status.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    mDrawerLayout.closeDrawers();
                    FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Status").setValue("Active");
                    session.setstatus("Active");
                    if(getActivity()!=null) {
                        Fragment fragment = new Neworder();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    }
                }
                else{
                    mDrawerLayout.closeDrawers();
                    FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Status").setValue("InActive");
                    session.setstatus("InActive");
                    if(getActivity()!=null) {
                        Fragment fragment = new OfflineFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    }
                }
            }
        });


        FirebaseDatabase.getInstance().getReference().child("DeliveryPartner")
                .child(session.getusername())
                .child("Doc1")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            if(getContext()!=null) {
                                if(!TextUtils.isEmpty(dataSnapshot.getValue().toString())) {
                                    Glide.with(getContext())
                                            .load(dataSnapshot.getValue().toString())
                                            .into(pp);
                                    Glide.with(getContext())
                                            .load(dataSnapshot.getValue().toString())
                                            .into(pp1);
                                    session.setpp(dataSnapshot.getValue().toString());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new ProfileDetails();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                }

            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new PaymentFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                }

            }
        });


        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new PastOrders();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                }

            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new SupportFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                }


            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new AboutUs();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                }

            }
        });

        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                session.setusername("");
                session.settoken("");
                session.setname("");
                session.setpp("");
                session.setpassword("");
                session.setextras("");
                session.setnumber("");
                session.setpincode("");
                session.setsub("");
                session.setrange("");
                session.settoken("");
                session.setcart("");
                session.setdaaddress("");
                session.setdadist("");
                session.setdaf("");
                session.setdal("");
                session.setdaloc("");
                session.setdaname("");
                session.setstatus("");

                startActivity(new Intent(getActivity(),
                        Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                if(getActivity()!=null) {
                    getActivity().finish();
                }

            }
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
//
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps))
                .draggable(true));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
            mMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps))
//                    .title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            mMap.setMapType(mMap.MAP_TYPE_NORMAL);

    }


    @Override
    public void onCameraMove() {

        mMap.clear();

    }


    @Override
    public void onCameraIdle() {

        mMap.clear();
//        mMap.addMarker(new MarkerOptions()
//                .position(mMap.getCameraPosition().target)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps))
//                .title("Marker"));



    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
}

