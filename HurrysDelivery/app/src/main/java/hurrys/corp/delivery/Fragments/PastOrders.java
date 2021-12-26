package hurrys.corp.delivery.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.Orders.ViewHolder;
import hurrys.corp.delivery.Models.Orders.Orders;
import hurrys.corp.delivery.R;

public class PastOrders extends Fragment {



    TextView sdate,edate;

    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int year,month,dayofmonth;


    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;


    public PastOrders() {
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
        View v=inflater.inflate(R.layout.fragment_past_orders, container, false);


        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                getActivity().onBackPressed();
            }
        });


        sessions = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recycylerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("Orders");

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        sessions = new Session(getContext());
        final Query firebasequery = mref.orderByChild("DeliveryPartner").equalTo(sessions.getusername());


        FirebaseRecyclerAdapter<Orders, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Orders, ViewHolder>(
                        Orders.class,
                        R.layout.orders_row,
                        ViewHolder.class,
                        firebasequery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Orders order, int position) {
                        viewHolder.setDetails1(getContext(),order.OrderNo,order.Qty,order.Pushid,order.OrderDateTime,order.OrderValue,order.Status,order.CName,order.DeliveryCustomer);
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {


                                TextView pushid = v.findViewById(R.id.pushid);

                                Bundle bundle=new Bundle();
                                PastOrderDetails fragment= new PastOrderDetails();
                                bundle.putString("pushid",pushid.getText().toString());
                                fragment.setArguments(bundle);
                                if(getActivity()!=null) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                }


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
                        if(getItemCount()==0){
                            Toast.makeText(getContext(),"No Orders Found",Toast.LENGTH_LONG).show();
                        }
                    }

                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
//    public void generate(){
//
//        String a[]=sdate.getText().toString().split("-");
//        String b[]=edate.getText().toString().split("-");
//
//        Query ref=mref.orderByChild("OrderDate").startAt(a[2]+"-"+a[1]+"-"+a[0]).endAt(b[2]+"-"+b[1]+"-"+b[0]);
//
//
//        FirebaseRecyclerAdapter<Orders, ViewHolder> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<Orders, ViewHolder>(
//                        Orders.class,
//                        R.layout.orders_row,
//                        ViewHolder.class,
//                        ref
//                ) {
//                    @Override
//                    protected void populateViewHolder(ViewHolder viewHolder, Orders order, int position) {
//                        viewHolder.setDetails2(getContext(),order.OrderNo,order.ItemsDetails,order.Pushid,order.OrderDateTime,order.Subtotal,order.Status,order.SellerCommission,order.Subtotal,order.DeliveryCharges,order.DeliveryPartner,sessions.getusername(),order.DeliveryPrice,order.DeliveryCustomer);
//
//                    }
//
//                    @Override
//                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
//                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
//                            @Override
//                            public void onItemClick(View v, int position) {
//
//
//                                TextView pushid = v.findViewById(R.id.pushid);
//                                TextView date = v.findViewById(R.id.date);
//                                TextView orderid = v.findViewById(R.id.orderid);
//                                TextView status = v.findViewById(R.id.status);
//
//                                Bundle bundle=new Bundle();
//                                PastOrderDetails fragment= new PastOrderDetails();
//                                bundle.putString("pushid",pushid.getText().toString());
//                                bundle.putString("date",date.getText().toString());
//                                bundle.putString("orderno",orderid.getText().toString());
//                                bundle.putString("status",status.getText().toString());
//                                fragment.setArguments(bundle);
//                                if(getActivity()!=null) {
//                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                    fragmentManager.beginTransaction()
//                                            .addToBackStack(null)
//                                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onItemLongClick(View v, int position) {
//
//                            }
//                        });
//                        return viewHolder;
//                    }
//
//                    @Override
//                    protected void onDataChanged() {
//                        super.onDataChanged();
//                            if(getItemCount()==0){
//                                Toast.makeText(getContext(),"No Orders Found",Toast.LENGTH_LONG).show();
//                            }
//                    }
//                };
//
//        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
//    }
}
