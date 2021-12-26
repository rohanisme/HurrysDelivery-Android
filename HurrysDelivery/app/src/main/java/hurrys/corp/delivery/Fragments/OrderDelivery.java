package hurrys.corp.delivery.Fragments;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.Earnings;
import hurrys.corp.delivery.R;
import hurrys.corp.delivery.Services.LocationService;

import static android.content.Context.ACTIVITY_SERVICE;


public class OrderDelivery extends Fragment {


    private TextView sname,smobilenumber,saddress,view,help,scustomername;
    private Button scall,sdirections,accept;

    private MapView mMapView;
    private GoogleMap googleMap;
    private LatLng sydney;
    private HashMap<String, Marker> hashMapMarker;
    private String b[]=new String[2];
    private String a[]=new String[2];
    private String sellerloc,customerloc;

    private Session session;
    private String pushid="";
    private String z="";
    String orderid="";

    double gtot=0,dbalance=0;
    double deliverycharges=0,distance = 0;

    double charges=0;
    ProgressBar progressBar;

    int temp = 0;

    public OrderDelivery() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_order_delivery, container, false);


        session = new Session(getContext());
        pushid = session.getstagepushid();

        accept=v.findViewById(R.id.accept);
        sname=v.findViewById(R.id.sname);
        scustomername=v.findViewById(R.id.scustomername);
        smobilenumber=v.findViewById(R.id.smobilenumber);
        saddress=v.findViewById(R.id.saddress);
        scall=v.findViewById(R.id.scall);
        sdirections=v.findViewById(R.id.sdirections);
        view=v.findViewById(R.id.view);
        help=v.findViewById(R.id.help);

        DecimalFormat form = new DecimalFormat("0.00");

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        progressBar = v.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        hashMapMarker = new HashMap<>();

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new SupportFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(temp==0) {
                        temp++;
                        if(getContext()!=null) {
                            SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure you want to exit!")
                                    .setConfirmText("Yes")
                                    .setCancelText("No")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            System.exit(0);
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            temp = 0;
                                        }
                                    });

                            sDialog.setCancelable(false);
                            sDialog.show();
                        }
                    }
                }
                return false;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment fragment = new PickupOrder();
//                Bundle bundle=new Bundle();
//                bundle.putString("pushid",pushid);
//                fragment.setArguments(bundle);
//                if(getActivity()!=null) {
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .addToBackStack(null)
//                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
//                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
                .child("DeliveryPrice")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            charges = Double.parseDouble(dataSnapshot.getValue().toString());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





        sellerloc = session.getpickup();
        customerloc = session.getdelivery();
        sname.setText(session.getorderid());
        scustomername.setText(session.getdname());
        smobilenumber.setText(session.getdnumber());
        saddress.setText(session.getflat()+","+session.getdaddress());

        sellerloc = session.getpickup();
        customerloc = session.getdelivery();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                sydney=new LatLng( 51.8910853,-0.4979753);

                try {

                    //                    boolean success = googleMap.setMapStyle(
                    //                            MapStyleOptions.loadRawResourceStyle(
                    //                                    getContext(), R.raw.style_json));


                    googleMap.clear();

                    a=sellerloc.split(",");
                    sydney=new LatLng(Double.parseDouble(a[0]),Double.parseDouble(a[1]));
                    googleMap.addMarker(new MarkerOptions().position(sydney)
                            .title(z)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));

                    b = customerloc.split(",");
                    sydney = new LatLng(Double.parseDouble(b[0]), Double.parseDouble(b[1]));
                    googleMap.addMarker(new MarkerOptions().position(sydney)
                            .title("Home")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));


                    List<LatLng> path = new ArrayList();
                    GeoApiContext context = new GeoApiContext.Builder()
                            .apiKey("AIzaSyCPhxfpptoIc1yca5U8mXIigIajoERQCdE")
                            .build();


                    DirectionsApiRequest req = DirectionsApi.getDirections(context, sellerloc, customerloc);
                    try {
                        DirectionsResult res = req.await();

                        //Loop through legs and steps to get encoded polylines of each step
                        if (res.routes != null && res.routes.length > 0) {
                            DirectionsRoute route = res.routes[0];

                            if (route.legs != null) {
                                for (int i = 0; i < route.legs.length; i++) {
                                    DirectionsLeg leg = route.legs[i];
                                    if (leg.steps != null) {
                                        for (int j = 0; j < leg.steps.length; j++) {
                                            DirectionsStep step = leg.steps[j];
                                            if (step.steps != null && step.steps.length > 0) {
                                                for (int k = 0; k < step.steps.length; k++) {
                                                    DirectionsStep step1 = step.steps[k];
                                                    EncodedPolyline points1 = step1.polyline;
                                                    if (points1 != null) {
                                                        //Decode polyline and add points to list of route coordinates
                                                        List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                        for (com.google.maps.model.LatLng coord1 : coords1) {
                                                            path.add(new LatLng(coord1.lat, coord1.lng));
                                                        }
                                                    }
                                                }
                                            } else {
                                                EncodedPolyline points = step.polyline;
                                                if (points != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                    for (com.google.maps.model.LatLng coord : coords) {
                                                        path.add(new LatLng(coord.lat, coord.lng));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    //Draw the polyline
                    if (path.size() > 0) {
                        PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.parseColor("#FFBC00")).width(10);
                        googleMap.addPolyline(opts);
                    }





                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(midPoint(Double.parseDouble(a[0]),Double.parseDouble(a[1]),Double.parseDouble(b[0]),Double.parseDouble(b[1])))
                            .zoom(13)
                            .bearing(angleBteweenCoordinate(Double.parseDouble(a[0]),Double.parseDouble(a[1]),Double.parseDouble(b[0]),Double.parseDouble(b[1]))).build();


                    progressBar.setVisibility(View.GONE);


                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBounds.build(), 10));



                } catch (Resources.NotFoundException e) {
                    Log.e("MapsActivityRaw", "Can't find style.", e);
                }
            }
        });





        scall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + smobilenumber.getText().toString()));
                startActivity(intent);
            }
        });

        sdirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a[] = sellerloc.split(",");
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + a[0] + "," + a[1]));
                startActivity(intent);
            }
        });


        final Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String date1 = df.format(currentDate);

        SimpleDateFormat df1 = new SimpleDateFormat("dd, MMM yyyy  HH:MM");
        final String date2 = df1.format(currentDate);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you have arrived the drop location!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();


                                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("PendingAmount");
                                                        dref.runTransaction(new Transaction.Handler() {
                                                            @NonNull
                                                            @Override
                                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                                double value = 0;
                                                                if (currentData.getValue() != null) {
                                                                    value = Double.parseDouble(currentData.getValue().toString()) + charges;
                                                                    value = value * 100;
                                                                    value = Math.round(value);
                                                                    value = value / 100;
                                                                    dbalance = Double.parseDouble(currentData.getValue().toString()) + charges;
                                                                    dbalance = dbalance * 100;
                                                                    dbalance = Math.round(dbalance);
                                                                    dbalance = dbalance / 100;
                                                                }
                                                                currentData.setValue(value);


                                                                return Transaction.success(currentData);
                                                            }

                                                            @Override
                                                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                                                                DatabaseReference rref1 = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Transactions").push();
                                                                Earnings earnings = new Earnings(rref1.getKey(), session.getusername(), Double.toString(dbalance), date1, Double.toString(charges), "Online",
                                                                        "Cr", "Delivery Total", orderid, "Pending",pushid);
                                                                rref1.setValue(earnings);

                                                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerStatus").setValue(null);
                                                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryStatus").setValue(null);
                                                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child(session.getusername()).setValue(null);
                                                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("5");
                                                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveredDate").setValue(date1);
                                                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveredDateTime").setValue(date2);
                                                                    stopLocationService();
                                                                        if(getActivity()!=null) {
                                                                            Fragment fragment = new ReachCustomer();
                                                                            Bundle bundle = new Bundle();
                                                                            bundle.putString("pushid",pushid);
                                                                            bundle.putString("charges",""+charges);
                                                                            fragment.setArguments(bundle);
                                                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                            fragmentManager.beginTransaction()
                                                                                    .add(R.id.frame_container, fragment).commitAllowingStateLoss();
                                                                            sweetAlertDialog.dismiss();
                                                                        }
                                                                }

                                                        });
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }


            }
        });



        return v;
    }

    private LatLng midPoint(double lat1, double long1, double lat2,double long2)
    {

        return new LatLng((lat1+lat2)/2, (long1+long2)/2);

    }

    private float angleBteweenCoordinate(double lat1, double long1, double lat2,
                                         double long2) {

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng;

        return (float)brng;
    }


    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(getActivity(), LocationService.class);
            serviceIntent.setAction(LocationService.ACTION_START_FOREGROUND_SERVICE);
//        this.startService(serviceIntent);
            if(getActivity()!=null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    getActivity().startForegroundService(serviceIntent);
                } else {
                    getActivity().startService(serviceIntent);
                }
            }
        }
    }

    private void stopLocationService(){
        try {
            if (isLocationServiceRunning()) {
                Intent intent = new Intent(getActivity(), LocationService.class);
                intent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    if(getActivity()!=null)
                        getActivity().startForegroundService(intent);
                } else {
                    if(getActivity()!=null)
                        getActivity().stopService(intent);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isLocationServiceRunning() {
        try {
            if(getActivity()!=null) {
                ActivityManager manager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
                assert manager != null;
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if ("hurrys.corp.delivery.Services.LocationService".equals(service.service.getClassName())) {
                        Log.d("TAG", "isLocationServiceRunning: location service is already running.");
                        return true;
                    }
                }
                Log.d("TAG", "isLocationServiceRunning: location service is not running.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }




}