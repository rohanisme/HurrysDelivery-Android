package hurrys.corp.delivery.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.R;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG ="LOGIN DATA" ;
    protected boolean _active = true;
    protected int _splashTime = 3000;
    private Session session;

    Animation anim;
    ImageView imageView;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseApp.initializeApp(getApplicationContext());

        setContentView(R.layout.activity_splash_screen);

        session = new Session(SplashScreen.this);

        LottieAnimationView animation_view=findViewById(R.id.animation_view);

        animation_view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                database = FirebaseDatabase.getInstance().getReference();
                if(TextUtils.isEmpty(session.getisfirsttime()))
                {
                    FirebaseDynamicLinks.getInstance()
                            .getDynamicLink(getIntent())
                            .addOnSuccessListener(SplashScreen.this, new OnSuccessListener<PendingDynamicLinkData>() {
                                @Override
                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                    // Get deep link from result (may be null if no link is found)
                                    Uri deepLink = null;
                                    if (pendingDynamicLinkData != null) {
                                        deepLink = pendingDynamicLinkData.getLink();
                                    }

                                    if (deepLink != null
                                            && deepLink.getBooleanQueryParameter("invitedby", false)) {
                                        String referrerUid = deepLink.getQueryParameter("invitedby");
                                        session.setreferral(referrerUid);

                                        startActivity(new Intent(SplashScreen.this,
                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }
                                    else{
                                        startActivity(new Intent(SplashScreen.this,
                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }



                                }
                            });
                }
                else
                {
                    if (session.getusername() != "") {
                        startActivity(new Intent(SplashScreen.this,
                                MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreen.this,
                                Login.class));
                        finish();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


//        WebView webView=findViewById(R.id.webView);
//
//        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
//
//
//        webView.setWebViewClient(new WebViewClient() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
//            }
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });
//
//        webView.loadUrl("https://hurrys.co.uk/splash.html");
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                database = FirebaseDatabase.getInstance().getReference();
//                if(TextUtils.isEmpty(session.getisfirsttime()))
//                {
//                    FirebaseDynamicLinks.getInstance()
//                            .getDynamicLink(getIntent())
//                            .addOnSuccessListener(SplashScreen.this, new OnSuccessListener<PendingDynamicLinkData>() {
//                                @Override
//                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                                    // Get deep link from result (may be null if no link is found)
//                                    Uri deepLink = null;
//                                    if (pendingDynamicLinkData != null) {
//                                        deepLink = pendingDynamicLinkData.getLink();
//                                    }
//
//                                    if (deepLink != null
//                                            && deepLink.getBooleanQueryParameter("invitedby", false)) {
//                                        String referrerUid = deepLink.getQueryParameter("invitedby");
//                                        session.setreferral(referrerUid);
//
//                                        startActivity(new Intent(SplashScreen.this,
//                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                        finish();
//                                    }
//                                    else{
//                                        startActivity(new Intent(SplashScreen.this,
//                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                        finish();
//                                    }
//
//
//
//                                }
//                            });
//                }
//                else
//                {
//                    if (session.getusername() != "") {
//                        startActivity(new Intent(SplashScreen.this,
//                                MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                        finish();
//                    } else {
//                        startActivity(new Intent(SplashScreen.this,
//                                Login.class));
//                        finish();
//                    }
//                }
//            }
//        }, 14000);



    }
}
