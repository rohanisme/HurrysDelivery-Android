package hurrys.corp.delivery.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.willy.ratingbar.RotationRatingBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Activities.MainActivity;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.Earnings;
import hurrys.corp.delivery.R;

public class DeliveryRatings extends Fragment {

    public DeliveryRatings() {
        // Required empty public constructor
    }


    private ImageView close;
    private TextView amount,name,earnings;
    private RotationRatingBar r1,r2;
    private EditText f1,f2;
    private Button submit;


    String vendor="",ordno="",user="";
    double id=0,ratings=0,vid=0;
    String pushid="";
    Session sessions;
    double gtot=0,dbalance=0;
    String deliveryid="",vendorid="",earned="";
    String charges="";
    int temp = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_deliveryratings, container, false);


        close=v.findViewById(R.id.close);
        amount=v.findViewById(R.id.amount);
        earnings=v.findViewById(R.id.earnings);
        name=v.findViewById(R.id.name);
        r1=v.findViewById(R.id.r1);
        r2=v.findViewById(R.id.r2);
        f1=v.findViewById(R.id.f1);
        f2=v.findViewById(R.id.f2);
        submit=v.findViewById(R.id.submit);
        sessions=new Session(getActivity());

        if(getArguments()!=null) {
            pushid = getArguments().getString("pushid");
            charges = getArguments().getString("charges");
            amount.setText("\u00a3"+charges);
        }

        final DecimalFormat form = new DecimalFormat("0.00");
        name.setText("Great Delivery "+sessions.getname());

        FirebaseDatabase.getInstance().getReference().child("DeliveryPartner")
                .child(sessions.getusername())
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
                                            sessions.setstage("");
                                            sessions.setstagepushid("");
                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child(sessions.getusername()).setValue(null);
                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerStatus").setValue(null);
                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryStatus").setValue(null);
                                            FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("5");
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(sessions.getusername()).child("Status").setValue("Active");
                                            sessions.setstatus("Active");
                                            sessions.setstage("");
                                            sessions.setstagepushid("");
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


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessions.setstage("");
                sessions.setstagepushid("");
                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child(sessions.getusername()).setValue(null);
                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerStatus").setValue(null);
                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryStatus").setValue(null);
                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("5");
                FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(sessions.getusername()).child("Status").setValue("Active");
                sessions.setstatus("Active");
                sessions.setstage("");
                sessions.setstagepushid("");
                Intent intent = new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        final Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String date1 = df.format(currentDate);

        SimpleDateFormat df1 = new SimpleDateFormat("dd, MMM yyyy  HH:MM");
        final String date2 = df1.format(currentDate);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(r1.getRating()<=0){
                    Toast.makeText(getActivity(),"Select Delivery Ratings",Toast.LENGTH_LONG).show();
                    return;
                }

                if(r2.getRating()<=0){
                    Toast.makeText(getActivity(),"Select Partner Ratings",Toast.LENGTH_LONG).show();
                    return;
                }


                deliveryid = sessions.getusername();


                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(deliveryid).child("Orders");

                dref.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        double value = 0;
                        if (currentData.getValue() != null) {
                            value = Long.parseLong(currentData.getValue().toString()) + 1;
                            id = Long.parseLong(currentData.getValue().toString()) + 1;
                        }
                        currentData.setValue(value);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {


                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(deliveryid).child("Ratings");

                        dref.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                double value = 0;
                                if (currentData.getValue() != null) {
                                    value = Double.parseDouble(currentData.getValue().toString());
                                    value = value*(id-1);
                                    value+=r1.getRating();
                                    value=value/id;
                                    value = value*100;
                                    value = (double)((int) value);
                                    value = value /100;
                                    ratings=value;
                                }
                                currentData.setValue(value);
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {



                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryCustomer").setValue(""+r1.getRating());
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryCustomerC").setValue(""+f1.getText().toString());

                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(deliveryid).child("TRatings").push();
                                ref.child("PushId").setValue(ref.getKey());
                                ref.child("Comments").setValue(f1.getText().toString());
                                ref.child("Ratings").setValue(""+r1.getRating());
                                ref.child("OrderId").setValue(ordno);
                                ref.child("OrderPushId").setValue(pushid);



                            }
                        });



                    }
                });


                DatabaseReference dref1 = FirebaseDatabase.getInstance().getReference().child("Vendor").child(vendorid).child("Orders");

                dref1.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        double value = 0;
                        if (currentData.getValue() != null) {
                            value = Long.parseLong(currentData.getValue().toString()) + 1;
                            vid= Long.parseLong(currentData.getValue().toString()) + 1;
                        }
                        currentData.setValue(value);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {


                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(vendorid).child("Ratings");

                        dref.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                double value = 0;
                                if (currentData.getValue() != null) {
                                    value = Double.parseDouble(currentData.getValue().toString());
                                    value = value*(id-1);
                                    value+=r2.getRating();
                                    value=value/id;
                                    value = value*100;
                                    value = (double)((int) value);
                                    value = value /100;
                                    ratings=value;
                                }
                                currentData.setValue(value);
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {



                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("VendorCustomer").setValue(""+r2.getRating());
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("VendorCustomerC").setValue(""+f2.getText().toString());

                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Vendor").child(vendorid).child("TRatings").push();
                                ref.child("PushId").setValue(ref.getKey());
                                ref.child("Comments").setValue(f2.getText().toString());
                                ref.child("Ratings").setValue(""+r2.getRating());
                                ref.child("OrderId").setValue(ordno);
                                ref.child("OrderPushId").setValue(pushid);

                                sessions.setstage("");
                                sessions.setstagepushid("");
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child(sessions.getusername()).setValue(null);
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerStatus").setValue(null);
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryStatus").setValue(null);
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("5");
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryCustomer").setValue(""+r1.getRating());
                                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryCustomerC").setValue(""+f1.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(sessions.getusername()).child("Status").setValue("Active");
                                sessions.setstatus("Active");

                                Intent intent = new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();

                            }
                        });



                    }
                });

            }
        });

        return v;
    }
}
