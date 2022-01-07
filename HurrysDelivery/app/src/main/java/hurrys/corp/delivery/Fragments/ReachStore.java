package hurrys.corp.delivery.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.R;


public class ReachStore extends Fragment {

    public ReachStore() {
        // Required empty public constructor
    }


    private TextView sname,view,help,smobilenumber,saddress;
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
    ProgressBar progressBar;

    int temp =0 ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_reach_store, container, false);


        session = new Session(getContext());
        pushid = session.getstagepushid();

        accept=v.findViewById(R.id.accept);
        sname=v.findViewById(R.id.sname);
        view=v.findViewById(R.id.view);
        help=v.findViewById(R.id.help);
        smobilenumber=v.findViewById(R.id.smobilenumber);
        saddress=v.findViewById(R.id.saddress);
        scall=v.findViewById(R.id.scall);
        sdirections=v.findViewById(R.id.sdirections);
        progressBar=v.findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);


        DecimalFormat form = new DecimalFormat("0.00");

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();


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

        hashMapMarker = new HashMap<>();


        sellerloc = session.getpickup();
        customerloc = session.getdelivery();
        sname.setText("Reach "+session.getcname());
        smobilenumber.setText(session.getcnumber());
        orderid = session.getorderid();
        saddress.setText(session.getcaddress());

        progressBar.setVisibility(View.GONE);

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

        sellerloc = session.getpickup();
        customerloc = session.getdelivery();
        sname.setText("Reach "+session.getcname());
        smobilenumber.setText(session.getcnumber());
        orderid = session.getorderid();
        saddress.setText(session.getcaddress());

        z = session.getcname();
        sellerloc = session.getpickup();
        customerloc = session.getdelivery();

        progressBar.setVisibility(View.GONE);



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
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hurrysvendor)));

                                        b = customerloc.split(",");
                                        sydney = new LatLng(Double.parseDouble(b[0]), Double.parseDouble(b[1]));
                                        googleMap.addMarker(new MarkerOptions().position(sydney)
                                                .title("Home")
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));


                                        List<LatLng> path = new ArrayList();
                                        GeoApiContext context = new GeoApiContext.Builder()
                                                .apiKey("AIzaSyCnaq_nQX-2yYsEQHJifUmS2q3Je5N-f7c")
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



                                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBounds.build(), 10));



                                    } catch (Resources.NotFoundException e) {
                                        Log.e("MapsActivityRaw", "Can't find style.", e);
                                    }
                                }
                            });



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new PickupOrder();
                Bundle bundle=new Bundle();
                bundle.putString("pushid",pushid);
                fragment.setArguments(bundle);
                if(getActivity()!=null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
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


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you have reached the store!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    session.setstage("Pick");
                                    session.setstagepushid(pushid);
                                    Fragment fragment = new PickupOrder();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("pushid",pushid);
                                    fragment.setArguments(bundle);
                                    if(getActivity()!=null) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                    }
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

}