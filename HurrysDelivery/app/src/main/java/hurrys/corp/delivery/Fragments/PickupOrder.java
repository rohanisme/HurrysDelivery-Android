package hurrys.corp.delivery.Fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.PhoneNumberUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.OrderDetails.OrderDetails;
import hurrys.corp.delivery.Models.OrderDetails.OrderDetails1;
import hurrys.corp.delivery.Models.OrderDetails.ViewHolder;
import hurrys.corp.delivery.R;


public class PickupOrder extends Fragment {


    private TextView sname,cmobilenumber,orderid,qty,help;
    private Button scall;
    private Button accept;

    private String pushid="";
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session session;
    ProgressBar progressBar;
    int temp = 0;

    TextView paymenttype,cash;

    public PickupOrder() {
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
        View v=inflater.inflate(R.layout.fragment_pickup_order, container, false);


        session=new Session(getActivity());

        sname=v.findViewById(R.id.sname);
        cmobilenumber=v.findViewById(R.id.cmobilenumber);

        scall=v.findViewById(R.id.scall);
        accept=v.findViewById(R.id.accept);
        orderid=v.findViewById(R.id.orderid);
        qty=v.findViewById(R.id.qty);
        help=v.findViewById(R.id.help);
        cash=v.findViewById(R.id.cash);
        paymenttype=v.findViewById(R.id.paymenttype);
        progressBar=v.findViewById(R.id.progressbar);

        mRecyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        shimmer_view_container=v.findViewById(R.id.shimmer_view_container);
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        shimmer_view_container.startShimmerAnimation();

        progressBar.setVisibility(View.VISIBLE);

        pushid=session.getstagepushid();

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

        sname.setText("Receive Order Items");
        cmobilenumber.setText(session.getdnumber());
        orderid.setText("Order Id:#"+session.getorderid());
        qty.setText(session.gettotalitems()+" Items");
        paymenttype.setText(session.getpaymenttype());

        if(!session.getpaymenttype().equals("ONLINE")){
            cash.setText("CASH TO BE COLLECTED : \u00a3"+session.getordervalue());
        }
        else{
            cash.setText("CASH TO BE COLLECTED : \u00a30.00");
        }

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

        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("OrderType").getValue().toString().equals("Others")){
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
                                                progressBar.setVisibility(View.GONE);
                                            }

                                        };

                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                            }
                            else{

                                mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");
                                FirebaseRecyclerAdapter<OrderDetails1, ViewHolder> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<OrderDetails1, ViewHolder>(
                                                OrderDetails1.class,
                                                R.layout.orders_details_row,
                                                ViewHolder.class,
                                                mref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(ViewHolder viewHolder, OrderDetails1 orderDetails, int position) {
                                                viewHolder.setDetails2(getContext(),orderDetails.Name,orderDetails.Price,orderDetails.Type,orderDetails.Qty,orderDetails.Image);
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
                                                progressBar.setVisibility(View.GONE);
                                            }

                                        };

                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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
                            .setTitleText("Are you sure you have picked  the order!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryStatus").setValue(session.getusername()+" 2");
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("PickedDate").setValue(date1);
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("PickedTime").setValue(date2);
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("4");

                                    SweetAlertDialog dialog= new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Good job!")
                                            .setContentText("You have successfully picked the order!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();

                                                    session.setstage("Delivery");
                                                    session.setstagepushid(pushid);
                                                    Fragment fragment = new OrderDelivery();
                                                    Bundle bundle=new Bundle();
                                                    bundle.putString("pushid",pushid);
                                                    fragment.setArguments(bundle);
                                                    if(getActivity()!=null) {
                                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                        fragmentManager.beginTransaction()
                                                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                                    }

                                                }
                                            });
                                    dialog.setCancelable(false);
                                    dialog.show();

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


        scall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + cmobilenumber.getText().toString()));
                startActivity(intent);
            }
        });

        return v;
    }

    private void openWhatsApp() {
        String smsNumber = "919686222139";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(getContext(), "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
