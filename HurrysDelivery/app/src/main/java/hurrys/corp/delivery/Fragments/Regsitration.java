package hurrys.corp.delivery.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewAnimator;
import com.android.volley.RequestQueue;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.bumptech.glide.Glide;
import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.internal.LatLngAdapter;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tooltip.Tooltip;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Models.Users;
import hurrys.corp.delivery.R;

import static android.app.Activity.RESULT_OK;

public class Regsitration extends Fragment {

    private ImageView doc1,doc2,doc3,doc4,doc5,doc6,doc7;
    private ImageView next,next1,register,close1,close2,close3,close4,close5,close6,close7;
    private TextView previous0,previous,previous1;
    private LinearLayout stage1,stage2,stage3,l1,l2,l3;
    private ProgressBar progressBar;
    private TextView dob;
    private EditText name,email,anumber,address,postcode,bankaccount,bnumber,bcnumber,bsortcode,bankname;
    private TextView comments;
    private Spinner gender;
    private Session session;
    private String path1="",path2="",path3="",path4="",path5="",path6="",path7="";
    private int selection=0;
    private ImageView one,two,three,line1,line2;
    private ArrayList<String> gender1=new ArrayList<String>();
    TextView t1,t2,t3,t4,t5,t6,t7;
    LinearLayout layout;

    CountryCodePicker country;
    private Uri imageUri;
    private Uri imageHoldUri = null;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int SELECT_FILE = 2;
    private final int RESULT_CROP = 400;
    private StorageReference mstorageReference;
    private ProgressBar progressBar2;

    public Regsitration() {
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
        View v=inflater.inflate(R.layout.fragment_regsitration, container, false);

        //status=v.findViewById(R.id.status);
        comments=v.findViewById(R.id.comments);
        previous0=v.findViewById(R.id.previous0);
        previous=v.findViewById(R.id.previous);
        previous1=v.findViewById(R.id.previous1);
        next=v.findViewById(R.id.next);
        next1=v.findViewById(R.id.next1);
        doc1=v.findViewById(R.id.doc1);
        doc2=v.findViewById(R.id.doc2);
        doc3=v.findViewById(R.id.doc3);
        doc4=v.findViewById(R.id.doc4);
        doc5=v.findViewById(R.id.doc5);
        doc6=v.findViewById(R.id.doc6);
        doc7=v.findViewById(R.id.doc7);
        close1=v.findViewById(R.id.close1);
        close2=v.findViewById(R.id.close2);
        close3=v.findViewById(R.id.close3);
        close4=v.findViewById(R.id.close4);
        close5=v.findViewById(R.id.close5);
        close6=v.findViewById(R.id.close6);
        close7=v.findViewById(R.id.close7);
        t1=v.findViewById(R.id.t1);
        t2=v.findViewById(R.id.t2);
        t3=v.findViewById(R.id.t3);
        t4=v.findViewById(R.id.t4);
        t5=v.findViewById(R.id.t5);
        t6=v.findViewById(R.id.t6);
        t7=v.findViewById(R.id.t6);
        l1=v.findViewById(R.id.l1);
        l2=v.findViewById(R.id.l2);
        l3=v.findViewById(R.id.l3);
        one=v.findViewById(R.id.one);
        two=v.findViewById(R.id.two);
        three=v.findViewById(R.id.three);
        line1=v.findViewById(R.id.line1);
        line2=v.findViewById(R.id.line2);
        layout=v.findViewById(R.id.layout);

        country=v.findViewById(R.id.country);

        session = new Session(getActivity());
        register=v.findViewById(R.id.register);
        stage1=v.findViewById(R.id.stage1);
        stage2=v.findViewById(R.id.stage2);
        stage3=v.findViewById(R.id.stage3);
        progressBar=v.findViewById(R.id.progressbar);
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



        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(t1)
                        .setText("Upload a clear image showing profile photo")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.DKGRAY)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                }, 3000);
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(t2)
                        .setText("National ID card/Passport")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.DKGRAY)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                }, 3000);
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(t3)
                        .setText("Any bill dated with in the last 6 months")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.DKGRAY)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                }, 3000);
            }
        });

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(t4)
                        .setText("Statement Dated within last 6 months")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.DKGRAY)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                }, 3000);
            }
        });

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(t5)
                        .setText("Upload a clear image showing current insurance certificate")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.DKGRAY)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                }, 3000);
            }
        });

        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(t6)
                        .setText("Current driving license photo card")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.DKGRAY)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                }, 3000);
            }
        });

        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(t7)
                        .setText("V5 Certificate/Log Book")
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.DKGRAY)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                }, 3000);
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        mstorageReference= FirebaseStorage.getInstance().getReference();
//
//        if(session.getapprovalstatus().equals("Pending"))
//            status.setText("Status : "+session.getapprovalstatus());
//        else {
//            status.setText("Status : " + session.getapprovalstatus());
//            status.setTextColor(Color.RED);
//        }

        comments.setText("Comments : ");

        gender1.add("Select");
        gender1.add("Male");
        gender1.add("Female");

        if(getContext()!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner , gender1);
            gender.setAdapter(adapter);
        }


        name.setText(session.getname());

        register.setVisibility(View.VISIBLE);

        one.setImageResource(R.drawable.sone);
        two.setImageResource(R.drawable.two);
        three.setImageResource(R.drawable.three);
        line1.setImageResource(R.drawable.line);
        line2.setImageResource(R.drawable.line);

        stage1.setVisibility(View.VISIBLE);
        stage2.setVisibility(View.GONE);
        stage3.setVisibility(View.GONE);

//        l1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stage1.setVisibility(View.VISIBLE);
//                stage2.setVisibility(View.GONE);
//                stage3.setVisibility(View.GONE);
//            }
//        });
//
//        l2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stage1.setVisibility(View.GONE);
//                stage2.setVisibility(View.VISIBLE);
//                stage3.setVisibility(View.GONE);
//            }
//        });
//
//        l3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stage1.setVisibility(View.GONE);
//                stage2.setVisibility(View.GONE);
//                stage3.setVisibility(View.VISIBLE);
//            }
//        });



        close1.setVisibility(View.GONE);
        close2.setVisibility(View.GONE);
        close3.setVisibility(View.GONE);
        close4.setVisibility(View.GONE);
        close5.setVisibility(View.GONE);
        close6.setVisibility(View.GONE);
        close7.setVisibility(View.GONE);

        doc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=1;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        doc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=2;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        doc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=3;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        doc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=4;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        doc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=5;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        doc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=6;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        doc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=7;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    path1="";
                    doc1.setImageResource(R.drawable.addimage);
                    close1.setVisibility(View.GONE);
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path2="";
                doc2.setImageResource(R.drawable.addimage);
                close2.setVisibility(View.GONE);
            }
        });

        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path3="";
                doc3.setImageResource(R.drawable.addimage);
                close3.setVisibility(View.GONE);
            }
        });

        close4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path4="";
                doc4.setImageResource(R.drawable.addimage);
                close4.setVisibility(View.GONE);
            }
        });

        close5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path5="";
                doc5.setImageResource(R.drawable.addimage);
                close5.setVisibility(View.GONE);
            }
        });

        close6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path6="";
                doc6.setImageResource(R.drawable.addimage);
                close6.setVisibility(View.GONE);
            }
        });

        close7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path7="";
                doc7.setImageResource(R.drawable.addimage);
                close7.setVisibility(View.GONE);
            }
        });


        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(getContext(), new DatePickerPopWin.OnDatePickedListener() {
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
                .maxYear(2025) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose("2000-01-01") // date chose when init popwindow
                .build();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerPopWin.showPopWin(getActivity());
            }
        });

        FirebaseDatabase.getInstance().getReference().child("DeliveryPartner")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Users users=dataSnapshot.getValue(Users.class);

                            if(dataSnapshot.child("Submitted").exists()){
                                if(dataSnapshot.child("Submitted").getValue().toString().equals("Yes")){
                                    one.setImageResource(R.drawable.sone);
                                    two.setImageResource(R.drawable.stwo);
                                    three.setImageResource(R.drawable.sthree);
                                    line1.setImageResource(R.drawable.sline);
                                    line2.setImageResource(R.drawable.sline);
                                }

                            }

                            if(!TextUtils.isEmpty(users.Name)&&!TextUtils.isEmpty(users.Age)&&!TextUtils.isEmpty(users.Gender)&&!TextUtils.isEmpty(users.Email)&&!TextUtils.isEmpty(users.Address)&&!TextUtils.isEmpty(users.PostCode)){
                                one.setImageResource(R.drawable.sone);
                                line1.setImageResource(R.drawable.sline);
                            }

                            if(!TextUtils.isEmpty(users.AccountName)&&!TextUtils.isEmpty(users.AccountNumber)&&!TextUtils.isEmpty(users.SortCode)&&!TextUtils.isEmpty(users.BankName)){
                                two.setImageResource(R.drawable.stwo);
                                line2.setImageResource(R.drawable.sline);
                            }

                            name.setText(users.Name);
                            dob.setText(users.Age);

                            if(gender1.contains(users.Gender))
                                gender.setSelection(gender1.indexOf(users.Gender));
                            else
                                gender.setSelection(0);

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
                            int count=0;
                            if(!TextUtils.isEmpty(users.Doc1)) {
                                count++;
                                if (getContext() != null)
                                    Glide.with(getContext())
                                            .load(users.Doc1)
                                            .centerCrop()
                                            .into(doc1);
                                close1.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(users.Doc2)){
                                count++;
                                if(getContext()!=null)
                                    Glide.with(getContext())
                                            .load(users.Doc2)
                                            .centerCrop()
                                            .into(doc2);
                                    close2.setVisibility(View.VISIBLE);
                                }
                            if(!TextUtils.isEmpty(users.Doc3)) {
                                count++;
                                if (getContext() != null)
                                    Glide.with(getContext())
                                            .load(users.Doc3)
                                            .centerCrop()
                                            .into(doc3);
                                close3.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(users.Doc4)) {
                                count++;
                                if (getContext() != null)
                                    Glide.with(getContext())
                                            .load(users.Doc4)
                                            .centerCrop()
                                            .into(doc4);
                                close4.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(users.Doc5)) {
                                count++;
                                if (getContext() != null)
                                    Glide.with(getContext())
                                            .load(users.Doc5)
                                            .centerCrop()
                                            .into(doc5);
                                close5.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(users.Doc6)) {
                                count++;
                                if (getContext() != null)
                                    Glide.with(getContext())
                                            .load(users.Doc6)
                                            .centerCrop()
                                            .into(doc6);
                                close6.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(users.Doc7)) {
                                count++;
                                if (getContext() != null)
                                    Glide.with(getContext())
                                            .load(users.Doc7)
                                            .centerCrop()
                                            .into(doc7);
                                close7.setVisibility(View.VISIBLE);
                            }


                            if(count==7){
                                three.setImageResource(R.drawable.sthree);
                            }

                            progressBar.setVisibility(View.GONE);
                            if(dataSnapshot.child("Comments").exists()) {
                                comments.setText(dataSnapshot.child("Comments").getValue().toString());
                                comments.setVisibility(View.VISIBLE);
                            }
                            else
                                comments.setVisibility(View.GONE);



                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(name.getText().toString())){
                    name.setError("Enter Name");
                    name.requestFocus();
                    return;
                }

                if(gender.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getContext(),"Select Gender",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(anumber.getText().toString())){
                    anumber.setError("Enter Alternate Number");
                    anumber.requestFocus();
                    return;
                }

                if(anumber.getText().toString().length()!=10){
                    anumber.setError("Enter 10 Digit Alternate Number");
                    anumber.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Enter Email Id");
                    email.requestFocus();
                    return;
                }

//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+";
//                if (!email.getText().toString().matches(emailPattern))
//                {
//                    email.setError("Enter Valid EmailId");
//                    email.requestFocus();
//                    return;
//                }

                if(TextUtils.isEmpty(address.getText().toString())){
                    address.setError("Enter Address");
                    address.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(postcode.getText().toString())){
                    postcode.setError("Enter Post Code");
                    postcode.requestFocus();
                    return;
                }

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername());
                mref.child("UserId").setValue(session.getusername());
                mref.child("Name").setValue(name.getText().toString());
                mref.child("Age").setValue(dob.getText().toString());
                mref.child("Gender").setValue(gender.getSelectedItem().toString());
                mref.child("Email").setValue(email.getText().toString());
                mref.child("MobileNumber").setValue(session.getnumber());
                mref.child("AlternateNumber").setValue(country.getSelectedCountryCodeWithPlus()+anumber.getText().toString());
                mref.child("Address").setValue(address.getText().toString());
                mref.child("PostCode").setValue(postcode.getText().toString());

//                e1.setImageResource(R.drawable.ic_success);
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);

                one.setImageResource(R.drawable.sone);
                two.setImageResource(R.drawable.two);
                three.setImageResource(R.drawable.three);
                line1.setImageResource(R.drawable.sline);
                line2.setImageResource(R.drawable.line);

            }
        });


        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(TextUtils.isEmpty(bankaccount.getText().toString())){
                    bankaccount.setError("Enter Bank Account Name");
                    bankaccount.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(bnumber.getText().toString())){
                    bnumber.setError("Enter Bank Account Number");
                    bnumber.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(bcnumber.getText().toString())){
                    bcnumber.setError("Enter Confirm Bank Account Number");
                    bcnumber.requestFocus();
                    return;
                }

                if(bnumber.getText().toString().length()!=8){
                    bnumber.setError("Please enter 8 Digits Account Number");
                    bnumber.requestFocus();
                    return;
                }


                if(!bnumber.getText().toString().equals(bcnumber.getText().toString())){
                    bcnumber.setError("Account Number and Confirm Account Number doesn't match");
                    bcnumber.requestFocus();
                    return;
                }


                if(TextUtils.isEmpty(bsortcode.getText().toString())){
                    bsortcode.setError("Enter Bank Sort Code");
                    bsortcode.requestFocus();
                    return;
                }

                if(bsortcode.getText().toString().length()!=6){
                    bsortcode.setError("Enter 6 Digit Bank Sort Code");
                    bsortcode.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(bankname.getText().toString())){
                    bankname.setError("Enter Bank Name");
                    bankname.requestFocus();
                    return;
                }


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername());
                mref.child("AccountName").setValue( bankaccount.getText().toString());
                mref.child("AccountNumber").setValue(bnumber.getText().toString());
                mref.child("SortCode").setValue(bsortcode.getText().toString());
                mref.child("BankName").setValue(bankname.getText().toString());

                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.VISIBLE);
                one.setImageResource(R.drawable.sone);
                two.setImageResource(R.drawable.stwo);
                three.setImageResource(R.drawable.three);
                line1.setImageResource(R.drawable.sline);
                line2.setImageResource(R.drawable.sline);

//                if(docs.getVisibility()==View.VISIBLE) {
//                    e2.setImageResource(R.drawable.ic_success);
//                    stage1.setVisibility(View.GONE);
//                    stage2.setVisibility(View.GONE);
//                    stage3.setVisibility(View.VISIBLE);
//                    register.setVisibility(View.VISIBLE);
//                }
//                else{
//                    blocks.setVisibility(View.VISIBLE);
//                    stage1.setVisibility(View.GONE);
//                    stage2.setVisibility(View.GONE);
//                    stage3.setVisibility(View.GONE);
//                }

            }
        });



        previous0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stage1.setVisibility(View.VISIBLE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);
                one.setImageResource(R.drawable.sone);
                two.setImageResource(R.drawable.two);
                three.setImageResource(R.drawable.three);
                line1.setImageResource(R.drawable.line);
                line2.setImageResource(R.drawable.line);

            }
        });

        previous1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);

                one.setImageResource(R.drawable.sone);
                two.setImageResource(R.drawable.two);
                three.setImageResource(R.drawable.three);
                line1.setImageResource(R.drawable.sline);
                line2.setImageResource(R.drawable.line);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(name.getText().toString())){
                    name.setError("Enter Name");
                    name.requestFocus();
                    return;
                }

                if(gender.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getContext(),"Select Gender",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Enter Email Id");
                    email.requestFocus();
                    return;
                }

//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+";
//                if (!email.getText().toString().matches(emailPattern))
//                {
//                    email.setError("Enter Valid EmailId");
//                    email.requestFocus();
//                    return;
//                }

                if(TextUtils.isEmpty(address.getText().toString())){
                    address.setError("Enter Address");
                    address.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(postcode.getText().toString())){
                    postcode.setError("Enter Post Code");
                    postcode.requestFocus();
                    return;
                }


                if(TextUtils.isEmpty(bankaccount.getText().toString())){
                    bankaccount.setError("Enter Bank Account Name");
                    bankaccount.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(bnumber.getText().toString())){
                    bnumber.setError("Enter Bank Account Number");
                    bnumber.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(bcnumber.getText().toString())){
                    bcnumber.setError("Enter Confirm Bank Account Number");
                    bcnumber.requestFocus();
                    return;
                }

                if(bnumber.getText().toString().length()!=8){
                    bnumber.setError("Please enter 8 Digits Account Number");
                    bnumber.requestFocus();
                    return;
                }


                if(!bnumber.getText().toString().equals(bcnumber.getText().toString())){
                    bcnumber.setError("Account Number and Confirm Account Number doesn't match");
                    bcnumber.requestFocus();
                    return;
                }


                if(TextUtils.isEmpty(bsortcode.getText().toString())){
                    bsortcode.setError("Enter Bank Sort Code");
                    bsortcode.requestFocus();
                    return;
                }

                if(bsortcode.getText().toString().length()!=6){
                    bsortcode.setError("Enter 6 Digit Bank Sort Code");
                    bsortcode.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(path1)){
                    Toast.makeText(getContext(),"Upload Photo",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(path2)){
                    Toast.makeText(getContext(),"Upload ID Photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(path3)){
                    Toast.makeText(getContext(),"Upload Address Photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(path4)){
                    Toast.makeText(getContext(),"Upload Bank Statement",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(path5)){
                    Toast.makeText(getContext(),"Upload Insurance",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(path6)){
                    Toast.makeText(getContext(),"Upload Vehicle Registration Certificate",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(path7)){
                    Toast.makeText(getContext(),"Upload Driving License",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure? Once submitted cannot be changed!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismiss();


//                                    e3.setImageResource(R.drawable.ic_success);
                                        String pattern = "yyyy-MM-dd";
                                        final String dateInString = new SimpleDateFormat(pattern).format(new Date());

                                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername());
                                        mref.child("UserId").setValue(session.getusername());
                                        mref.child("Name").setValue(name.getText().toString());
                                        mref.child("Age").setValue(dob.getText().toString());
                                        mref.child("Gender").setValue(gender.getSelectedItem().toString());
                                        mref.child("Email").setValue(email.getText().toString());
                                        mref.child("MobileNumber").setValue(session.getnumber());
                                        mref.child("AlternateNumber").setValue(anumber.getText().toString());
                                        mref.child("Address").setValue(address.getText().toString());
                                        mref.child("PostCode").setValue(postcode.getText().toString());
                                        mref.child("AccountName").setValue( bankaccount.getText().toString());
                                        mref.child("AccountNumber").setValue(bnumber.getText().toString());
                                        mref.child("BankName").setValue(bankname.getText().toString());
                                        mref.child("SortCode").setValue(bsortcode.getText().toString());
                                        mref.child("Doc1").setValue(path1);
                                        mref.child("Doc2").setValue(path2);
                                        mref.child("Doc3").setValue(path3);
                                        mref.child("Doc4").setValue(path4);
                                        mref.child("Doc5").setValue(path5);
                                        mref.child("Doc6").setValue(path6);
                                        mref.child("Doc7").setValue(path7);
                                        mref.child("Status").setValue("Active");
                                        mref.child("ApprovalStatus").setValue("Pending");
                                        mref.child("Updated").setValue(dateInString);
                                        mref.child("Submitted").setValue("Yes");

                                        session.setsubmitted("yes");

                                        if(getContext()!=null) {
                                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Submitted Successfully!")
                                                    .setContentText("Hurrys will contact you within 24hours!")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismiss();
                                                            if(getActivity()!=null)
                                                                getActivity().onBackPressed();
                                                        }
                                                    })
                                                    .show();
                                        }



                                        stage1.setVisibility(View.GONE);
                                        stage2.setVisibility(View.GONE);
                                        stage3.setVisibility(View.VISIBLE);
                                        one.setImageResource(R.drawable.sone);
                                        two.setImageResource(R.drawable.stwo);
                                        three.setImageResource(R.drawable.sthree);
                                        line1.setImageResource(R.drawable.sline);
                                        line2.setImageResource(R.drawable.sline);

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

    private void galleryIntent() {
        //CHOOSE IMAGE FROM GALLERY
//        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void cameraIntent() {

        requestMultiplePermissions();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        stage1.setVisibility(View.GONE);
        stage2.setVisibility(View.GONE);
        stage3.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);

        //SAVE URI FROM GALLERY
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageHoldUri = data.getData();

            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                File f = new File(String.valueOf(imageHoldUri));
                String imageName = f.getName();

                StorageReference riversRef = mstorageReference.child("Documents/" + c + ".jpg");
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Updating....!");
                progressDialog.show();
                progressDialog.setCancelable(false);
                riversRef.putFile(imageHoldUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                final String[] u = new String[1];
                                storageRef.child("Documents/" +c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        if(selection==1) {
                                            path1 = u[0];
                                            if(getContext()!=null)
                                             Glide.with(getContext()).load(path1).centerCrop().into(doc1);
                                           close1.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc1").setValue(path1);
                                        }
                                        else  if(selection==2) {
                                            path2 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path2).centerCrop().into(doc2);
                                            close2.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc2").setValue(path2);
                                        }
                                        else  if(selection==3) {
                                            path3 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path3).centerCrop().into(doc3);
                                            close3.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc3").setValue(path3);
                                        }
                                        else  if(selection==4) {
                                            path4 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path4).centerCrop().into(doc4);
                                            close4.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc4").setValue(path4);
                                        }
                                        else  if(selection==5) {
                                            path5 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path5).centerCrop().into(doc5);
                                            close5.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc5").setValue(path5);
                                        }
                                        else  if(selection==6) {
                                            path6 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path6).centerCrop().into(doc6);
                                            close6.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc6").setValue(path6);
                                        }
                                        else  if(selection==7) {
                                            path7 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path7).centerCrop().into(doc7);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc7").setValue(path7);
                                            close7.setVisibility(View.VISIBLE);
                                        }


                                        stage1.setVisibility(View.GONE);
                                        stage2.setVisibility(View.GONE);
                                        stage3.setVisibility(View.VISIBLE);
                                        register.setVisibility(View.VISIBLE);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });

            } else {
                Toast.makeText(getContext(), "File Path Null", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            stage1.setVisibility(View.GONE);
            stage2.setVisibility(View.GONE);
            stage3.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);

            imageHoldUri = imageUri;

            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                File f = new File(String.valueOf(imageHoldUri));
                String imageName = f.getName();

                StorageReference riversRef = mstorageReference.child("Documents/" + c + ".jpg");
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Updating....!");
                progressDialog.show();
                progressDialog.setCancelable(false);
                riversRef.putFile(imageHoldUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                final String[] u = new String[1];
                                storageRef.child("Documents/" +c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        stage1.setVisibility(View.GONE);
                                        stage2.setVisibility(View.GONE);
                                        stage3.setVisibility(View.VISIBLE);
                                        register.setVisibility(View.VISIBLE);

                                        u[0] = uri.toString();
                                        if(selection==1) {
                                            path1 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path1).centerCrop().into(doc1);
                                            close1.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc1").setValue(path1);
                                        }
                                        else  if(selection==2) {
                                            path2 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path2).centerCrop().into(doc2);
                                            close2.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc2").setValue(path2);
                                        }
                                        else  if(selection==3) {
                                            path3 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path3).centerCrop().into(doc3);
                                            close3.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc3").setValue(path3);
                                        }
                                        else  if(selection==4) {
                                            path4 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path4).centerCrop().into(doc4);
                                            close4.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc4").setValue(path4);
                                        }
                                        else  if(selection==5) {
                                            path5 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path5).centerCrop().into(doc5);
                                            close5.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc5").setValue(path5);
                                        }
                                        else  if(selection==6) {
                                            path6 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path6).centerCrop().into(doc6);
                                            close6.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc6").setValue(path6);
                                        }
                                        else  if(selection==7) {
                                            path7 = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path7).centerCrop().into(doc7);
                                            close7.setVisibility(View.VISIBLE);
                                            FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(session.getusername()).child("Doc7").setValue(path7);
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });

            } else {
                Toast.makeText(getContext(), "File Path Null", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
