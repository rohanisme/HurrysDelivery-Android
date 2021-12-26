package hurrys.corp.delivery.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.OrderDetails.OrderDetails;
import hurrys.corp.delivery.Models.OrderDetails.OrderDetails1;
import hurrys.corp.delivery.Models.OrderDetails.ViewHolder;
import hurrys.corp.delivery.R;


public class PastOrderDetails extends Fragment {

    private ImageView back;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;
    private String pushid="",vid="",did="",earned="";
    private Button rate;
    private TextView date,tt1,tt2,orderid,help,cname,mobilenumber,address,sname,smobilenumber,saddress;
    private LinearLayout t1,t2,customer,store;

    public PastOrderDetails() {
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
        View v=inflater.inflate(R.layout.fragment_past_order_details, container, false);

        back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });

        if(getArguments()!=null)
            pushid=getArguments().getString("pushid");


        date=v.findViewById(R.id.date);
        orderid=v.findViewById(R.id.orderid);
        rate=v.findViewById(R.id.rate);
        tt1=v.findViewById(R.id.tt1);
        tt2=v.findViewById(R.id.tt2);
        t1=v.findViewById(R.id.t1);
        t2=v.findViewById(R.id.t2);
        orderid=v.findViewById(R.id.orderid);
        help=v.findViewById(R.id.help);
        cname=v.findViewById(R.id.cname);
        mobilenumber=v.findViewById(R.id.mobilenumber);
        address=v.findViewById(R.id.address);
        sname=v.findViewById(R.id.sname);
        smobilenumber=v.findViewById(R.id.smobilenumber);
        saddress=v.findViewById(R.id.saddress);
        customer=v.findViewById(R.id.customer);
        store=v.findViewById(R.id.store);


//        customer.setVisibility(View.VISIBLE);
//        store.setVisibility(View.GONE);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer.setVisibility(View.VISIBLE);
                store.setVisibility(View.GONE);

                t1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                t2.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tt1.setTextColor(Color.parseColor("#00B246"));
                tt2.setTextColor(Color.parseColor("#55565b"));
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer.setVisibility(View.GONE);
                store.setVisibility(View.VISIBLE);

                t2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                t1.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tt2.setTextColor(Color.parseColor("#00B246"));
                tt1.setTextColor(Color.parseColor("#55565b"));
            }
        });

        sessions = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("Orders").child(pushid).child("Cart");


            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                            orderid.setText("Order ID : #"+dataSnapshot.child("OrderNo").getValue().toString().substring(5));
                            date.setText(dataSnapshot.child("OrderDateTime").getValue().toString());


                            vid = dataSnapshot.child("Seller").getValue().toString();
                            did = dataSnapshot.child("DeliveryPartner").getValue().toString();
                            earned = dataSnapshot.child("DeliveryPrice").getValue().toString();

                            cname.setText(dataSnapshot.child("CName").getValue().toString());
                            mobilenumber.setText(dataSnapshot.child("Number").getValue().toString());
                            address.setText(dataSnapshot.child("Address").getValue().toString());
                                sname.setText(dataSnapshot.child("SellerName").getValue().toString());
                                smobilenumber.setText(dataSnapshot.child("Number").getValue().toString());
                                saddress.setText(dataSnapshot.child("SellerCity").getValue().toString());


                                if(dataSnapshot.child("DeliveryCustomer").exists()){
                                    rate.setVisibility(View.GONE);
                                }
                                else
                                    rate.setVisibility(View.VISIBLE);

//                                if(dataSnapshot.child("OrderType").getValue().toString().equals("Others")){
                                    mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");

                                    FirebaseRecyclerAdapter<OrderDetails, ViewHolder> firebaseRecyclerAdapter =
                                            new FirebaseRecyclerAdapter<OrderDetails, ViewHolder>(
                                                    OrderDetails.class,
                                                    R.layout.orders_details_row,
                                                    ViewHolder.class,
                                                    mref
                                            ) {
                                                @Override
                                                protected void populateViewHolder(ViewHolder viewHolder, OrderDetails orderDetails, int position) {
                                                    viewHolder.setDetails(getContext(),orderDetails.Image,orderDetails.Name,orderDetails.Price,orderDetails.Qty,orderDetails.Total,orderDetails.Units);

                                                }

                                                @Override
                                                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                    final  ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                                                    viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                                        @Override
                                                        public void onItemClick(View v, final int position) {

                                                        }

                                                        @Override
                                                        public void onItemLongClick(View v, int position) {

                                                        }
                                                    });
                                                    return viewHolder;
                                                }

                                                @Override
                                                protected void onDataChanged() {
                                                    super.onDataChanged();

                                                }

                                            };

                                    mRecyclerView.setAdapter(firebaseRecyclerAdapter);
//                                }
//                                else{
//
//                                    mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");
//                                    FirebaseRecyclerAdapter<OrderDetails1, ViewHolder> firebaseRecyclerAdapter =
//                                            new FirebaseRecyclerAdapter<OrderDetails1, ViewHolder>(
//                                                    OrderDetails1.class,
//                                                    R.layout.orders_details_row1,
//                                                    ViewHolder.class,
//                                                    mref
//                                            ) {
//                                                @Override
//                                                protected void populateViewHolder(ViewHolder viewHolder, OrderDetails1 orderDetails, int position) {
//                                                    viewHolder.setDetails1(getContext(),orderDetails.Name,orderDetails.Price,orderDetails.Type,orderDetails.Qty);
//
//                                                }
//
//                                                @Override
//                                                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//                                                    final  ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
//                                                    viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
//                                                        @Override
//                                                        public void onItemClick(View v, final int position) {
//
//                                                        }
//
//                                                        @Override
//                                                        public void onItemLongClick(View v, int position) {
//
//                                                        }
//                                                    });
//                                                    return viewHolder;
//                                                }
//
//                                                @Override
//                                                protected void onDataChanged() {
//                                                    super.onDataChanged();
//
//                                                }
//
//                                            };
//
//                                    mRecyclerView.setAdapter(firebaseRecyclerAdapter);
//                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment= new DeliveryRatings();
                    Bundle bundle=new Bundle();
                    bundle.putString("pushid",pushid);
                    bundle.putString("vendor",vid);
                    bundle.putString("delivery",did);
                    bundle.putString("earned",earned);
                    fragment.setArguments(bundle);
                    if(getActivity()!=null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    }
                }
            });


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


        return v;
    }
}
