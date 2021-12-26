package hurrys.corp.delivery.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.willy.ratingbar.RotationRatingBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.Earnings;
import hurrys.corp.delivery.Models.OrderDetails.OrderDetails;
import hurrys.corp.delivery.Models.OrderDetails.OrderDetails1;
import hurrys.corp.delivery.Models.OrderDetails.ViewHolder;
import hurrys.corp.delivery.R;


public class OrderDetailsFragment extends Fragment {


    ImageView back;
    private TextView orderid,date,accept,name,address,picked,total;
    private  ImageView call,direction;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;

    String pushid="",status="";
    String number="",slocation="",clocation="";
    String type="";
    View v;

    double gtot=0,dbalance=0;
    double deliverycharges=0;

    double charges=0;

    String vendor="",ordno="",user="";
    double id=0,ratings=0;


    public OrderDetailsFragment() {
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
        View v=inflater.inflate(R.layout.fragment_order_details, container, false);


//        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
//        bottomnavigation.setVisibility(View.GONE);
        back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        pushid=getArguments().getString("pushid");
        status=getArguments().getString("status");
        type=getArguments().getString("type");
        charges=Double.parseDouble(getArguments().getString("total").substring(1));

        date=v.findViewById(R.id.date);
        orderid=v.findViewById(R.id.orderid);
        accept=v.findViewById(R.id.accept);
        name=v.findViewById(R.id.name);
        address=v.findViewById(R.id.address);
        call=v.findViewById(R.id.call);
//        direction=v.findViewById(R.id.direction);
//        picked=v.findViewById(R.id.picked);
//        total=v.findViewById(R.id.total);
//
//        date.setText(getArguments().getString("date"));
//        orderid.setText(getArguments().getString("orderno"));
//
//        sessions = new Session(getContext());
//        mRecyclerView = v.findViewById(R.id.recyclerView);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
////        mLayoutManager.setReverseLayout(true);
////        mLayoutManager.setStackFromEnd(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mref = mFirebaseDatabase.getReference().child("Orders").child(pushid).child("Cart");
//
//        if(status.equals("5")||status.equals("10")) {
//
//            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                if (dataSnapshot.child("Payment").getValue().toString().equals("CASH")) {
//                                    DecimalFormat form = new DecimalFormat("0.00");
//                                    gtot = Double.parseDouble(dataSnapshot.child("Total").getValue().toString());
//                                    total.setText("\u00a3" + form.format(gtot));
//                                } else {
//                                    total.setText("\u20b90.00");
//                                }
//
//                                if(dataSnapshot.child("DeliveryPrice").exists()){
//                                    deliverycharges=Double.parseDouble(dataSnapshot.child("DeliveryPrice").getValue().toString());
//                                }
//
//                                picked.setVisibility(View.GONE);
//                                accept.setVisibility(View.GONE);
//                                name.setVisibility(View.GONE);
//                                address.setVisibility(View.GONE);
//                                call.setVisibility(View.GONE);
//                                direction.setVisibility(View.GONE);
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//        }
//        else{
//            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
////                            orderid.setText("Order ID : #"+dataSnapshot.child("OrderNo").getValue().toString().substring(5));
////                            date.setText(dataSnapshot.child("OrderDateTime").getValue().toString());
////                            if(!dataSnapshot.child("Payment").getValue().toString().equals("CASH")) {
////                                total.setText("\u20b90");
////                                gtot=0;
////                            }
////                            else {
////                                total.setText("\u00a3" + dataSnapshot.child("Total").getValue().toString());
////                                gtot=Double.parseDouble(dataSnapshot.child("Total").getValue().toString());
////                            }
//
//                                if(dataSnapshot.child("DeliveryStatus").exists())
//                                    status=dataSnapshot.child("DeliveryStatus").getValue().toString();
//
//                                if (dataSnapshot.child("Status").getValue().toString().equals("1") || dataSnapshot.child("Status").getValue().toString().equals("2")) {
//                                    picked.setVisibility(View.GONE);
//                                } else {
//                                    picked.setVisibility(View.VISIBLE);
//                                }
//
//                                ordno=dataSnapshot.child("OrderNo").getValue().toString();
//                                if(dataSnapshot.child("Seller").exists()){
//                                    vendor=dataSnapshot.child("Seller").getValue().toString();
//                                }
//
//                                if(dataSnapshot.child("UserId").exists()){
//                                    user=dataSnapshot.child("UserId").getValue().toString();
//                                }
//
//                                if (dataSnapshot.child("Payment").getValue().toString().equals("CASH")) {
//                                    DecimalFormat form = new DecimalFormat("0.00");
//                                    gtot = Double.parseDouble(dataSnapshot.child("Total").getValue().toString());
//                                    total.setText("\u00a3" + form.format(gtot));
//                                } else {
//                                    total.setText("\u20b90.00");
//                                }
//
//                                if(dataSnapshot.child("DeliveryPrice").exists()){
//                                    deliverycharges=Double.parseDouble(dataSnapshot.child("DeliveryPrice").getValue().toString());
//                                }
//                                slocation = dataSnapshot.child("SellerLoc").getValue().toString();
//                                clocation = dataSnapshot.child("LocationCoordinates").getValue().toString();
//
//                                if (status.equals(sessions.getusername() + " 1")) {
//                                    accept.setText("PickUp Address");
//                                    picked.setText("Picked");
//                                    if (dataSnapshot.child("SellerAddress").exists())
//                                        address.setText(dataSnapshot.child("SellerAddress").getValue().toString());
//                                    if (dataSnapshot.child("SellerName").exists())
//                                        name.setText(dataSnapshot.child("SellerName").getValue().toString());
//                                    if (dataSnapshot.child("SellerNumber").exists())
//                                        number = dataSnapshot.child("SellerNumber").getValue().toString();
//                                    slocation = dataSnapshot.child("SellerLoc").getValue().toString();
//                                } else {
//                                    accept.setText("Drop Address");
//                                    picked.setText("Delivered");
//                                    address.setText(dataSnapshot.child("Address").getValue().toString());
//                                    name.setText(dataSnapshot.child("CName").getValue().toString());
//                                    number = dataSnapshot.child("Number").getValue().toString();
//                                    clocation = dataSnapshot.child("LocationCoordinates").getValue().toString();
//                                }
//
//
//                                if(dataSnapshot.child("OrderType").getValue().toString().equals("Others")){
//                                    mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");
//
//                                    FirebaseRecyclerAdapter<OrderDetails, ViewHolder> firebaseRecyclerAdapter =
//                                            new FirebaseRecyclerAdapter<OrderDetails, ViewHolder>(
//                                                    OrderDetails.class,
//                                                    R.layout.orders_details_row,
//                                                    ViewHolder.class,
//                                                    mref
//                                            ) {
//                                                @Override
//                                                protected void populateViewHolder(ViewHolder viewHolder, OrderDetails orderDetails, int position) {
//                                                    viewHolder.setDetails(getContext(),orderDetails.Image,orderDetails.Name,orderDetails.Price,orderDetails.PushId,orderDetails.Qty,orderDetails.Total,orderDetails.Units);
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
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//        }
//
//
//
//        final Date currentDate = new Date(System.currentTimeMillis());
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        final String date1 = df.format(currentDate);
//
//        SimpleDateFormat df1 = new SimpleDateFormat("dd, MMM yyyy  HH:MM");
//        final String date2 = df1.format(currentDate);
//
//
//
//        direction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Fragment fragment = new OrderDetailsMap();
//                Bundle bundle=new Bundle();
//                bundle.putString("seller",slocation);
//                bundle.putString("customer",clocation);
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .addToBackStack(null)
//                        .replace(R.id.frame_container, fragment).commit();
//
////                String a[] = location.split(",");
////                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
////                        Uri.parse("http://maps.google.com/maps?daddr=" + a[0] + "," + a[1]));
////                startActivity(intent);
//            }
//        });
//
//        call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + number));
//                startActivity(intent);
//
//            }
//        });
//
//        TextView help=v.findViewById(R.id.help);
//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Coming Soon!");
//                sDialog.setCancelable(false);
//                sDialog.show();
//            }
//        });
//
//
//
//        picked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(picked.getText().toString().equals("Picked")){
//
//                    picked.setEnabled(false);
//
//
//                    SweetAlertDialog sDialog  =  new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Are you sure?")
//                            .setContentText("You have arrived at pick up location?")
//                            .setCancelText("No")
//                            .setConfirmText("Yes")
//                            .showCancelButton(true)
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                    sweetAlertDialog.dismiss();
//
//
//                                    final View ratingDialogView = getLayoutInflater().inflate(R.layout.ratings, null);
//                                    final AlertDialog ratingDialog = new AlertDialog.Builder(getActivity()).create();
//                                    ratingDialog.setView(ratingDialogView);
//                                    ratingDialog.show();
//                                    RotationRatingBar ratingBar = ratingDialogView.findViewById(R.id.ratebar);
//                                    ratingBar.setNumStars(5);
//                                    ratingBar.setStepSize(0.5f);
//                                    ratingBar.setMinimumStars(1);
//
//                                    EditText comments=ratingDialogView.findViewById(R.id.comments);
//                                    Button submit=ratingDialogView.findViewById(R.id.submit);
//
//                                    ratingDialog.setCancelable(false);
//
//                                    submit.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//
//                                            DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(vendor).child("Orders");
//
//                                            dref.runTransaction(new Transaction.Handler() {
//                                                @NonNull
//                                                @Override
//                                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                                    double value = 0;
//                                                    if (currentData.getValue() != null) {
//                                                        value = Long.parseLong(currentData.getValue().toString()) + 1;
//                                                        id = Long.parseLong(currentData.getValue().toString()) + 1;
//                                                    }
//                                                    currentData.setValue(value);
//                                                    return Transaction.success(currentData);
//                                                }
//
//                                                @Override
//                                                public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
//
//
//                                                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(vendor).child("Ratings");
//
//                                                    dref.runTransaction(new Transaction.Handler() {
//                                                        @NonNull
//                                                        @Override
//                                                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                                            double value = 0;
//                                                            if (currentData.getValue() != null) {
//                                                                value = Double.parseDouble(currentData.getValue().toString());
//                                                                value = value*(id-1);
//                                                                value+=ratingBar.getRating();
//                                                                value=value/id;
//                                                                value = value*100;
//                                                                value = (double)((int) value);
//                                                                value = value /100;
//                                                                ratings=value;
//                                                            }
//                                                            currentData.setValue(value);
//                                                            return Transaction.success(currentData);
//                                                        }
//
//                                                        @Override
//                                                        public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
//
//
//                                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryStatus").setValue(sessions.getusername()+" 2");
//                                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("PickedDate").setValue(date1);
//                                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("PickedTime").setValue(date2);
//                                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("4");
//
//
//                                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("VendorDelivery").setValue(""+ratingBar.getRating());
//                                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("VendorDeliveryC").setValue(""+comments.getText().toString());
//
//                                                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Vendor").child(vendor).child("TRatings").push();
//                                                            ref.child("PushId").setValue(ref.getKey());
//                                                            ref.child("Comments").setValue(comments.getText().toString());
//                                                            ref.child("Ratings").setValue(""+ratingBar.getRating());
//                                                            ref.child("OrderId").setValue(ordno);
//                                                            ref.child("OrderPushId").setValue(pushid);
//                                                            ratingDialog.dismiss();
//
//
//                                                            SweetAlertDialog dialog= new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
//                                                                    .setTitleText("Good job!")
//                                                                    .setContentText("You have successfully picked the order!")
//                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                                                        @Override
//                                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                                            sweetAlertDialog.dismiss();
//                                                                        }
//                                                                    });
//                                                            dialog.setCancelable(false);
//                                                            dialog.show();
//                                                            picked.setEnabled(true);
//
//                                                        }
//                                                    });
//
//
//
//                                                }
//                                            });
//
//
//                                        }
//                                    });
//
//
//
//
//                                }
//                            })
//                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.cancel();
//                                    picked.setEnabled(true);
//                                }
//                            });
//                    sDialog.setCancelable(false);
//                    sDialog .show();
//
//                }
//                else{
//
//                    picked.setEnabled(false);
//
//
//                    SweetAlertDialog sDialog  = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Are you sure?")
//                            .setContentText("Are you sure u have  arrived the drop location?")
//                            .setCancelText("No")
//                            .setConfirmText("Yes")
//                            .showCancelButton(true)
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                    sweetAlertDialog.dismiss();
//
//                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerStatus").setValue(null);
//                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryStatus").setValue(null);
//                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child(sessions.getusername()).setValue(null);
//                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("5");
//                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveredDate").setValue(date1);
//                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveredDateTime").setValue(date2);
//
//
//
//
//                                                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(sessions.getusername()).child("PendingAmount");
//                                                        dref.runTransaction(new Transaction.Handler() {
//                                                            @NonNull
//                                                            @Override
//                                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                                                double value = 0;
//                                                                if (currentData.getValue() != null) {
//                                                                    value = Double.parseDouble(currentData.getValue().toString()) + charges;
//                                                                    value = value * 100;
//                                                                    value = Math.round(value);
//                                                                    value = value / 100;
//                                                                    dbalance = Double.parseDouble(currentData.getValue().toString()) + charges;
//                                                                    dbalance = dbalance * 100;
//                                                                    dbalance = Math.round(dbalance);
//                                                                    dbalance = dbalance / 100;
//                                                                }
//                                                                currentData.setValue(value);
//
//
//                                                                return Transaction.success(currentData);
//                                                            }
//
//                                                            @Override
//                                                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//                                                                DatabaseReference rref1 = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(sessions.getusername()).child("Transactions").push();
//                                                                Earnings earnings = new Earnings(rref1.getKey(), sessions.getusername(), Double.toString(dbalance), date1, Double.toString(charges), "Online",
//                                                                        "Cr", "Delivery Total", orderid.getText().toString(), "Pending",pushid);
//                                                                rref1.setValue(earnings);
//
//
//                                                                if(getContext()!=null) {
//
//                                                                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
//                                                                            .setTitleText("Good job!")
//                                                                            .setContentText("You order has been successfully delivered!")
//                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                                                                @Override
//                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                                                    sweetAlertDialog.dismiss();
//
//                                                                                    final View ratingDialogView = getLayoutInflater().inflate(R.layout.ratings, null);
//                                                                                    final AlertDialog ratingDialog = new AlertDialog.Builder(getActivity()).create();
//                                                                                    ratingDialog.setView(ratingDialogView);
//                                                                                    ratingDialog.show();
//                                                                                    RotationRatingBar ratingBar = ratingDialogView.findViewById(R.id.ratebar);
//                                                                                    ratingBar.setNumStars(5);
//                                                                                    ratingBar.setStepSize(0.5f);
//                                                                                    ratingBar.setMinimumStars(1);
//
//                                                                                    EditText comments=ratingDialogView.findViewById(R.id.comments);
//                                                                                    Button submit=ratingDialogView.findViewById(R.id.submit);
//
//                                                                                    ratingDialog.setCancelable(false);
//
//                                                                                    submit.setOnClickListener(new View.OnClickListener() {
//                                                                                        @Override
//                                                                                        public void onClick(View view) {
//
//
//
//                                                                                            DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("Orders");
//
//                                                                                            dref.runTransaction(new Transaction.Handler() {
//                                                                                                @NonNull
//                                                                                                @Override
//                                                                                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                                                                                    double value = 0;
//                                                                                                    if (currentData.getValue() != null) {
//                                                                                                        value = Long.parseLong(currentData.getValue().toString()) + 1;
//                                                                                                        id = Long.parseLong(currentData.getValue().toString()) + 1;
//                                                                                                    }
//                                                                                                    currentData.setValue(value);
//                                                                                                    return Transaction.success(currentData);
//                                                                                                }
//
//                                                                                                @Override
//                                                                                                public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
//
//
//                                                                                                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(user).child("Ratings");
//
//                                                                                                    dref.runTransaction(new Transaction.Handler() {
//                                                                                                        @NonNull
//                                                                                                        @Override
//                                                                                                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                                                                                            double value = 0;
//                                                                                                            if (currentData.getValue() != null) {
//                                                                                                                value = Double.parseDouble(currentData.getValue().toString());
//                                                                                                                value = value*(id-1);
//                                                                                                                value+=ratingBar.getRating();
//                                                                                                                value=value/id;
//                                                                                                                value = value*100;
//                                                                                                                value = (double)((int) value);
//                                                                                                                value = value /100;
//                                                                                                                ratings=value;
//                                                                                                            }
//                                                                                                            currentData.setValue(value);
//                                                                                                            return Transaction.success(currentData);
//                                                                                                        }
//
//                                                                                                        @Override
//                                                                                                        public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
//
//
//                                                                                                            ratingDialog.dismiss();
//
//                                                                                                            SweetAlertDialog dialog= new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
//                                                                                                                    .setTitleText("Good job!")
//                                                                                                                    .setContentText("You have successfully delivered the order!")
//                                                                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                                                                                                        @Override
//                                                                                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                                                                                            sweetAlertDialog.dismiss();
//                                                                                                                            if(getActivity()!=null){
//                                                                                                                                getActivity().onBackPressed();
//                                                                                                                            }
//                                                                                                                        }
//                                                                                                                    });
//                                                                                                            dialog.setCancelable(false);
//                                                                                                            dialog.show();
//                                                                                                            picked.setEnabled(true);
//
//                                                                                                        }
//                                                                                                    });
//
//
//
//                                                                                                }
//                                                                                            });
//
//
//                                                                                        }
//                                                                                    });
//                                                                                }
//                                                                            });
//                                                                    sDialog.setCancelable(false);
//                                                                    sDialog.show();
//                                                                    picked.setEnabled(true);
//                                                                }
//
//                                                            }
//                                                        });
//
//
//                                    }
//
//                            })
//                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.cancel();
//                                    picked.setEnabled(true);
//                                }
//                            });
//                    sDialog.setCancelable(false);
//                    sDialog.show();
//
//
//                }
//            }
//        });


        return v;
    }
}
