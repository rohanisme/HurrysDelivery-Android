package hurrys.corp.delivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;
import com.hbb20.CountryCodePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.Users;
import hurrys.corp.delivery.R;

public class RegisterDetails extends AppCompatActivity {

    ImageView next;
    EditText  name,email,mobilenumber,address,postcode;
    TextView dob;
    CountryCodePicker country;
    Spinner gender;
    ProgressBar progressbar;

    String uniqueid = "HDID", mobile = "";
    long id=0;
    double dbalance=0;
    String android_id="";
    Session session;

    ArrayList<String> gender1=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        next=findViewById(R.id.next);
        mobilenumber=findViewById(R.id.mobilenumber);
        name=findViewById(R.id.name);
        dob=findViewById(R.id.dob);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        postcode=findViewById(R.id.postcode);
        country=findViewById(R.id.country);
        gender=findViewById(R.id.gender);
        progressbar=findViewById(R.id.progressbar);

        session=new Session(this);
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        progressbar.setVisibility(View.GONE);

        gender1.add("Select");
        gender1.add("Male");
        gender1.add("Female");

        String pattern = "yyyy-MM-dd";
        final String dateInString = new SimpleDateFormat(pattern).format(new Date());

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mobile=user.getPhoneNumber();

        int year = Calendar.getInstance().get(Calendar.YEAR) - 18;

        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(RegisterDetails.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                String a[]=dateDesc.split("-");
                dob.setText(a[2]+"/"+a[1]+"/"+a[0]);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1920) //min year in loop
                .maxYear(year) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose("2000-01-01") // date chose when init popwindow
                .build();



        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerPopWin.showPopWin(RegisterDetails.this);
            }
        });

        if(getApplicationContext()!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item , gender1);
            gender.setAdapter(adapter);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next.setEnabled(false);

                if(TextUtils.isEmpty(name.getText().toString())){
                    name.setError("Enter Name");
                    name.requestFocus();
                    next.setEnabled(true);
                    return;
                }

                if(dob.getText().toString().equals("DD/MM/YYYY")){
                    Toast.makeText(getApplicationContext(),"Select DOB",Toast.LENGTH_LONG).show();
                    next.setEnabled(true);
                    return;
                }

                if(getAge(dob.getText().toString())<18){
                    Toast.makeText(getApplicationContext(),"Must be greater than 18 Yrs",Toast.LENGTH_LONG).show();
                    next.setEnabled(true);
                    return;
                }

                if(gender.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getApplicationContext(),"Select Gender",Toast.LENGTH_LONG).show();
                    next.setEnabled(true);
                    return;
                }
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Enter EmailId");
                    email.requestFocus();
                    next.setEnabled(true);
                    return;
                }

//                if (!email.getText().toString().matches(emailPattern))
//                {
//                    email.setError("Enter Valid EmailId");
//                    email.requestFocus();
//                    next.setEnabled(true);
//                    return;
//                }

                if(!TextUtils.isEmpty(mobilenumber.getText().toString())){
                    if(mobilenumber.getText().toString().length()!=10){
                        mobilenumber.setError("Enter 10 Digit Number");
                        mobilenumber.requestFocus();
                        next.setEnabled(true);
                        return;
                    }
                }

                if(TextUtils.isEmpty(address.getText().toString())){
                    address.setError("Enter Address");
                    address.requestFocus();
                    next.setEnabled(true);
                    return;
                }

                if(TextUtils.isEmpty(postcode.getText().toString())){
                    postcode.setError("Enter PostCode");
                    postcode.requestFocus();
                    next.setEnabled(true);
                    return;
                }


                progressbar.setVisibility(View.VISIBLE);


                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartnerId");

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


                        uniqueid+=id;

                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(uniqueid);



                        Users users=new Users(uniqueid,
                                name.getText().toString(),
                                dob.getText().toString(),
                                gender.getSelectedItem().toString(),
                                email.getText().toString(),
                                mobile,
                                country.getSelectedCountryCodeWithPlus()+mobilenumber.getText().toString(),
                                address.getText().toString(),
                                postcode.getText().toString(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "InActive",
                                "Pending",
                                dateInString,
                                "",
                                5,
                                0,
                                0,
                                0,
                                1000,
                                android_id);

                        mref.setValue(users);

                        Intent intent = new Intent(RegisterDetails.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        session = new Session(RegisterDetails.this);
                        session.setusername(users.UserId);
                        session.setnumber(users.MobileNumber);
                        session.setname(users.Name);
                        session.setemail(users.Email);
                        session.setapprovalfirst("Yes");
                        startActivity(intent);
                        finish();

                    }
                });

            }
        });
    }

    private int getAge(String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }
}