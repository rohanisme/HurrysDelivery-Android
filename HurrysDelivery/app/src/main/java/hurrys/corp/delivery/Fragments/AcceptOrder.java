package hurrys.corp.delivery.Fragments;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.R;
import hurrys.corp.delivery.Services.LocationService;

import static android.content.Context.ACTIVITY_SERVICE;

public class AcceptOrder extends Fragment {

    public AcceptOrder() {
        // Required empty public constructor
    }

    private ImageView back;
    private TextView distance,price,vendorname,vendoraddress,deliveryname,deliveryaddress;
    private Button accept,decline;
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
    String Time="";
    int temp =0;
    ProgressBar progressbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_accept_order, container, false);

        session = new Session(getContext());
        pushid = getArguments().getString("pushid");
        back=v.findViewById(R.id.back);
        decline=v.findViewById(R.id.decline);
        distance=v.findViewById(R.id.distance);
        price=v.findViewById(R.id.price);
        vendorname=v.findViewById(R.id.vendorname);
        vendoraddress=v.findViewById(R.id.vendoraddress);
        deliveryname=v.findViewById(R.id.deliveryname);
        deliveryaddress=v.findViewById(R.id.deliveryaddress);
        accept=v.findViewById(R.id.accept);
        progressbar=v.findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
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

        DecimalFormat form = new DecimalFormat("0.00");

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        hashMapMarker = new HashMap<>();

        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child(session.getusername()).exists()) {
                                price.setText("\u00a3" + form.format(Math.round(Double.parseDouble(dataSnapshot.child("DeliveryPrice").getValue().toString()) * 100.0) / 100.0));
                                if (dataSnapshot.child("SellerLoc").exists())
                                    sellerloc = dataSnapshot.child("SellerLoc").getValue().toString();
                                if (dataSnapshot.child("LocationCoordinates").exists())
                                    customerloc = dataSnapshot.child("LocationCoordinates").getValue().toString();


                                deliveryname.setText(dataSnapshot.child("CName").getValue().toString());
                                orderid = dataSnapshot.child("OrderNo").getValue().toString();
                                deliveryaddress.setText(dataSnapshot.child("Flat").getValue().toString()+","+dataSnapshot.child("Address").getValue().toString());
                                vendorname.setText(dataSnapshot.child("SellerName").getValue().toString());
                                vendoraddress.setText(dataSnapshot.child("SellerAddress").getValue().toString());

                                if (dataSnapshot.child("SellerName").exists())
                                    z = dataSnapshot.child("SellerName").getValue().toString();

                                String s[] = sellerloc.replaceAll("\\s+","").split(",");

                                String r[] = customerloc.replaceAll("\\s+","").split(",");

                                float[] results = new float[1];
                                try {
                                    Location.distanceBetween(Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(r[0]), Double.parseDouble(r[1]), results);
                                    Time = getDurationForRoute(sellerloc, customerloc);
                                    double dist = results[0] / 1000;
                                    DecimalFormat form = new DecimalFormat("0.00");
                                    distance.setText(Time + " | " + form.format(dist) + " Miles to delivery");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mMapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap mMap) {
                                        googleMap = mMap;

                                        sydney = new LatLng(51.8910853, -0.4979753);

                                        try {

                                            //                    boolean success = googleMap.setMapStyle(
                                            //                            MapStyleOptions.loadRawResourceStyle(
                                            //                                    getContext(), R.raw.style_json));
                                            googleMap.clear();

                                            a = sellerloc.split(",");
                                            sydney = new LatLng(Double.parseDouble(a[0]), Double.parseDouble(a[1]));
                                            googleMap.addMarker(new MarkerOptions().position(sydney)
                                                    .title(z)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hurrysvendor)));

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
                                                    .target(midPoint(Double.parseDouble(a[0]), Double.parseDouble(a[1]), Double.parseDouble(b[0]), Double.parseDouble(b[1])))
                                                    .zoom(13)
                                                    .bearing(angleBteweenCoordinate(Double.parseDouble(a[0]), Double.parseDouble(a[1]), Double.parseDouble(b[0]), Double.parseDouble(b[1]))).build();


                                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBounds.build(), 10));


                                        } catch (Resources.NotFoundException e) {
                                            Log.e("MapsActivityRaw", "Can't find style.", e);
                                        }
                                    }
                                });
                            }
                        }
                        else{
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to reject the order!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    final EditText editText = new EditText(getContext());
                                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("Please mention the reason for rejection")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    if(TextUtils.isEmpty(editText.getText().toString())){
                                                        Toast.makeText(getContext(),"Enter Reason",Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

                                                    String reason=editText.getText().toString();
                                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child(session.getusername()).removeValue();
                                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Rejections").child("Delivery").push();
                                                    ref1.child("PushId").setValue(ref1.getKey());
                                                    ref1.child("Reason").setValue(reason);
                                                    ref1.child("OrderNo").setValue(orderid);
                                                    ref1.child("OrderPushId").setValue(pushid);
                                                    ref1.child("DeliveryPartnerId").setValue(session.getusername());

                                                    if(getActivity()!=null){
                                                        sweetAlertDialog.dismiss();
                                                        getActivity().onBackPressed();
                                                    }

                                                }
                                            });

                                    dialog.setCustomView(editText);
                                    dialog.setCancelable(false);
                                    dialog.show();
//                                    FirebaseDatabase.getInstance().getReference().child("PendingOrders")
//                                            .child(pushid)
//                                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    if(dataSnapshot.exists()){
//                                                        FirebaseDatabase.getInstance().getReference().child("PendingOrders").child(pushid).removeValue();
//                                                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid);
//                                                        ref.child("DeliveryPartner").setValue(sessions.getusername());
//                                                        ref.child("DeliveryNumber").setValue(sessions.getnumber());
//                                                        ref.child("DeliveryStatus").setValue(sessions.getusername()+" 1");
//                                                        ref.child(sessions.getusername()).setValue(sessions.getusername());
//                                                        ref.child("DeliveryName").setValue(sessions.getname());
//
//                                                        sweetAlertDialog.dismiss();
//                                                        Intent intent = new Intent(getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                        startActivity(intent);
//                                                    }
//                                                    else{
//                                                        sweetAlertDialog.dismiss();
//                                                        Toast.makeText(getContext(),"Task has been Taken!",Toast.LENGTH_LONG).show();
//                                                        if(getActivity()!=null)
//                                                            getActivity().onBackPressed();
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                }
//                                            });

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

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to accept the order!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    FirebaseDatabase.getInstance().getReference().child("Orders")
                                            .child(pushid)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        if(dataSnapshot.child(session.getusername()).exists()) {
                                                            if (dataSnapshot.child("SellerLoc").exists())
                                                                session.setpickup(dataSnapshot.child("SellerLoc").getValue().toString().replaceAll("\\s+",""));
                                                            if (dataSnapshot.child("LocationCoordinates").exists())
                                                                session.setdelivery(dataSnapshot.child("LocationCoordinates").getValue().toString().replaceAll("\\s+",""));
                                                            if (dataSnapshot.child("CName").exists())
                                                                session.setdname(dataSnapshot.child("CName").getValue().toString());
                                                            if (dataSnapshot.child("Number").exists())
                                                                session.setdnumber(dataSnapshot.child("Number").getValue().toString());
                                                            if (dataSnapshot.child("OrderNo").exists())
                                                                session.setorderid(dataSnapshot.child("OrderNo").getValue().toString());
                                                            if (dataSnapshot.child("Address").exists())
                                                                session.setdaddress(dataSnapshot.child("Address").getValue().toString());
                                                            if (dataSnapshot.child("Flat").exists())
                                                                session.setflat(dataSnapshot.child("Flat").getValue().toString());
                                                            if (dataSnapshot.child("SellerName").exists())
                                                                session.setcname(dataSnapshot.child("SellerName").getValue().toString());
                                                            if (dataSnapshot.child("SellerAddress").exists())
                                                                session.setcaddress(dataSnapshot.child("SellerAddress").getValue().toString());
                                                            if (dataSnapshot.child("SellerNumber").exists())
                                                                session.setcnumber(dataSnapshot.child("SellerNumber").getValue().toString());
                                                            if (dataSnapshot.child("Qty").exists())
                                                                session.settotalitem(dataSnapshot.child("Qty").getValue().toString());
                                                            if (dataSnapshot.child("Payment").exists())
                                                                session.setpaymenttype(dataSnapshot.child("Payment").getValue().toString());
                                                            if (dataSnapshot.child("Total").exists())
                                                                session.setordervalue(dataSnapshot.child("Total").getValue().toString());
                                                            if (dataSnapshot.child("DeliveryInstructions").exists())
                                                                session.setinstructions(dataSnapshot.child("DeliveryInstructions").getValue().toString());


                                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid);
                                                            ref.child("DeliveryPartner").setValue(session.getusername());
                                                            ref.child("DeliveryNumber").setValue(session.getnumber());
                                                            ref.child("DeliveryStatus").setValue(session.getusername() + " 1");
                                                            ref.child("DeliveryImage").setValue(session.getpp());
                                                            ref.child(session.getusername()).setValue(session.getusername());
                                                            ref.child("DeliveryName").setValue(session.getname());

                                                            startLocationService();
                                                            session.setstatus("InActive");
                                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Status").setValue("InActive");
                                                            session.setstage("Reach");
                                                            session.setstagepushid(pushid);
                                                            Fragment fragment = new ReachStore();
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("pushid", pushid);
                                                            fragment.setArguments(bundle);
                                                            if (getActivity() != null) {
                                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                fragmentManager.beginTransaction()
                                                                        .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                                            }
                                                        }
                                                        else{
                                                            sweetAlertDialog.dismiss();
                                                            Toast.makeText(getContext(),"Task has been Taken!",Toast.LENGTH_LONG).show();
                                                            if(getActivity()!=null)
                                                                getActivity().onBackPressed();
                                                        }
                                                    }
                                                    else{
                                                        sweetAlertDialog.dismiss();
                                                        Toast.makeText(getContext(),"Task has been Taken!",Toast.LENGTH_LONG).show();
                                                        if(getActivity()!=null)
                                                            getActivity().onBackPressed();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

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


    public String getDurationForRoute(String origin, String destination) throws InterruptedException, ApiException, IOException {
        // - We need a context to access the API
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCPhxfpptoIc1yca5U8mXIigIajoERQCdE")
                .build();

        // - Perform the actual request
        DirectionsResult directionsResult = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .origin(origin)
                .destination(destination)
                .await();

        // - Parse the result
        DirectionsRoute route = directionsResult.routes[0];
        DirectionsLeg leg = route.legs[0];
        Duration duration = leg.duration;
        return duration.humanReadable;
    }

}
