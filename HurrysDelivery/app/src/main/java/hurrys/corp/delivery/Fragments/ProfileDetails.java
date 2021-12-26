package hurrys.corp.delivery.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.Users;
import hurrys.corp.delivery.R;

public class ProfileDetails extends Fragment {

    public ProfileDetails() {
        // Required empty public constructor
    }

    private LinearLayout z1,z2,z3,stage1,stage2,stage3;
    private TextView t1,t2,t3;
    private View s1,s2,s3;

    private Session session;

    private ImageView doc1,doc2,doc3,doc4,doc5,doc6,doc7;
    private TextView dob;
    private TextView name,email,anumber,address,postcode,bankaccount,bnumber,bcnumber,bsortcode,bankname;
    private TextView comments;
    private TextView gender;
    private String path1="",path2="",path3="",path4="",path5="",path6="",path7="";
    private int selection=0;

    private ArrayList<String> gender1=new ArrayList<String>();

    CountryCodePicker country;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile_details, container, false);

        ImageView back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });


        z1=v.findViewById(R.id.z1);
        z2=v.findViewById(R.id.z2);
        z3=v.findViewById(R.id.z3);
        t1=v.findViewById(R.id.t1);
        t2=v.findViewById(R.id.t2);
        t3=v.findViewById(R.id.t3);
        s1=v.findViewById(R.id.s1);
        s2=v.findViewById(R.id.s2);
        s3=v.findViewById(R.id.s3);
        stage1=v.findViewById(R.id.stage1);
        stage2=v.findViewById(R.id.stage2);
        stage3=v.findViewById(R.id.stage3);

        doc1=v.findViewById(R.id.doc1);
        doc2=v.findViewById(R.id.doc2);
        doc3=v.findViewById(R.id.doc3);
        doc4=v.findViewById(R.id.doc4);
        doc5=v.findViewById(R.id.doc5);
        doc6=v.findViewById(R.id.doc6);
        doc7=v.findViewById(R.id.doc7);
        country=v.findViewById(R.id.country);

        name=v.findViewById(R.id.name);
        dob=v.findViewById(R.id.dob);
        email=v.findViewById(R.id.email);
        anumber=v.findViewById(R.id.anumber);
        address=v.findViewById(R.id.address);
        postcode=v.findViewById(R.id.postcode);
        bankaccount=v.findViewById(R.id.bankaccount);
        bnumber=v.findViewById(R.id.bnumber);
        bcnumber=v.findViewById(R.id.bcnumber);
        bsortcode=v.findViewById(R.id.bsortcode);
        gender=v.findViewById(R.id.gender);
        bankname=v.findViewById(R.id.bankname);

        session = new Session(getActivity());

        gender1.add("Select");
        gender1.add("Male");
        gender1.add("Female");

//        if(getContext()!=null){
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner , gender1);
//            gender.setAdapter(adapter);
//        }

        name.setText(session.getname());
        stage1.setVisibility(View.VISIBLE);
        stage2.setVisibility(View.GONE);
        stage3.setVisibility(View.GONE);

        t1.setTextColor(Color.parseColor("#00B246"));
        t2.setTextColor(Color.parseColor("#808080"));
        t3.setTextColor(Color.parseColor("#808080"));
        s1.setBackgroundColor(Color.parseColor("#00B246"));
        s2.setBackgroundColor(Color.parseColor("#eaeaea"));
        s3.setBackgroundColor(Color.parseColor("#eaeaea"));


        z1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.VISIBLE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);

                t1.setTextColor(Color.parseColor("#00B246"));
                t2.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s1.setBackgroundColor(Color.parseColor("#00B246"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));


            }
        });

        z2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);

                t2.setTextColor(Color.parseColor("#00B246"));
                t1.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s2.setBackgroundColor(Color.parseColor("#00B246"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));
            }
        });

        z3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.VISIBLE);

                t3.setTextColor(Color.parseColor("#00B246"));
                t2.setTextColor(Color.parseColor("#808080"));
                t1.setTextColor(Color.parseColor("#808080"));
                s3.setBackgroundColor(Color.parseColor("#00B246"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
            }
        });



        FirebaseDatabase.getInstance().getReference().child("DeliveryPartner")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Users users=dataSnapshot.getValue(Users.class);


                            assert users != null;
                            name.setText(users.Name);
                            dob.setText(users.Age);
//                            if(gender1.indexOf(users.Gender)>-1)
//                               gender.setText(users.Gender);
//                            else
//                                gender.setSelection(0);

                            gender.setText(users.Gender);
                            email.setText(users.Email);
//                            anumber.setText(users.AlternateNumber);

                            int len=users.AlternateNumber.length();
                            if(len==0) {
                                anumber.setText(users.AlternateNumber);
                            }
                            else if(len==13){
                                country.setCountryForPhoneCode(Integer.parseInt(users.AlternateNumber.substring(1,3)));
                                anumber.setText(users.AlternateNumber.substring(3,13));
                            }
                            else if(len==12){
                                country.setCountryForPhoneCode(Integer.parseInt(users.AlternateNumber.substring(1,2)));
                                anumber.setText(users.AlternateNumber.substring(2,12));
                            }



                            address.setText(users.Address);
                            postcode.setText(users.PostCode);

                            bankaccount.setText(users.AccountName);
                            bnumber.setText(users.AccountNumber);
                            bcnumber.setText(users.AccountNumber);
                            bsortcode.setText(users.SortCode);
                            bankname.setText(users.BankName);

                            path1=users.Doc1;
                            path2=users.Doc2;
                            path3=users.Doc3;
                            path4=users.Doc4;
                            path5=users.Doc5;
                            path6=users.Doc6;
                            path7=users.Doc7;
                            if(!TextUtils.isEmpty(users.Doc1))
                                if(getContext()!=null)
                                Glide.with(getContext())
                                        .load(users.Doc1)
                                        .centerCrop()
                                        .into(doc1);
                            if(!TextUtils.isEmpty(users.Doc2))
                                if(getContext()!=null)
                                Glide.with(getContext())
                                        .load(users.Doc2)
                                        .centerCrop()
                                        .into(doc2);
                            if(!TextUtils.isEmpty(users.Doc3))
                                if(getContext()!=null)
                                Glide.with(getContext())
                                        .load(users.Doc3)
                                        .centerCrop()
                                        .into(doc3);
                            if(!TextUtils.isEmpty(users.Doc4))
                                if(getContext()!=null)
                                Glide.with(getContext())
                                        .load(users.Doc4)
                                        .centerCrop()
                                        .into(doc4);
                            if(!TextUtils.isEmpty(users.Doc5))
                                if(getContext()!=null)
                                Glide.with(getContext())
                                        .load(users.Doc5)
                                        .centerCrop()
                                        .into(doc5);
                            if(!TextUtils.isEmpty(users.Doc6))
                                if(getContext()!=null)
                                Glide.with(getContext())
                                        .load(users.Doc6)
                                        .centerCrop()
                                        .into(doc6);
                            if(!TextUtils.isEmpty(users.Doc7))
                                if(getContext()!=null)
                                Glide.with(getContext())
                                        .load(users.Doc7)
                                        .centerCrop()
                                        .into(doc7);



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        return v;
    }
}
