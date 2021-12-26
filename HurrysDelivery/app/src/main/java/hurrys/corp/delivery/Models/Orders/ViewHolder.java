package hurrys.corp.delivery.Models.Orders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.willy.ratingbar.RotationRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import hurrys.corp.delivery.Activities.MainActivity;
import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.Fragments.AcceptOrder;
import hurrys.corp.delivery.R;


public  class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    private ClickListener mClickListener;

    public ViewHolder(View itemView) {

        super(itemView);
        mView = itemView;
        //Item Click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //Item Long Click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx,String OrderNo,String ItemDetails,String Pushid,String OrderDateTime,String ChefTotal,String Status,String ChefCommision,String Subtotal,String DeliveryCharges,String Seller,String OrderType,String DeliveryPrice){
        TextView orderid,items,pushid,date,total,status,type;
        ImageView statusimage;
        RotationRatingBar ratebar = mView.findViewById(R.id.ratebar);
        ratebar.setVisibility(View.GONE);
        orderid = mView.findViewById(R.id.orderid);
        date = mView.findViewById(R.id.date);
        total = mView.findViewById(R.id.total);
        pushid = mView.findViewById(R.id.pushid);
        items = mView.findViewById(R.id.items);
        status = mView.findViewById(R.id.status);
//        type = mView.findViewById(R.id.type);
//        statusimage = mView.findViewById(R.id.statusimage);



//        double price = Double.parseDouble(Subtotal);
//        double del = Double.parseDouble(DeliveryCharges);
//        double com = Double.parseDouble(ChefCommision);
//
//        double tot = price * (com/100.0);
//        double gst = tot*0.18;
//        double gst1 = del*0.18;
//
//        double gtot = price - tot  - gst - del - gst1;

        DecimalFormat form=new DecimalFormat("0.00");

        pushid.setText(Pushid);
        orderid.setText(OrderNo);
        date.setText(OrderDateTime);
        total.setText("\u00a3"+form.format(Double.parseDouble(DeliveryPrice)));
        items.setText(ItemDetails);
        status.setText(Status);
//        type.setText(OrderType);

        Session session=new Session(ctx);
        if(Status.equals(session.getusername()+" 1")){
//            statusimage.setImageResource(R.drawable.neworder);
        }
        else{
//            statusimage.setImageResource(R.drawable.ongoing);
        }




    }


    public void setDetailsPending(Context ctx, String OrderNo, String Qty, String Pushid, String OrderDateTime, String OrderValue, String Status, String CName, String Rejected,String DeliveryCustomer){

        LinearLayout linearLayout;
        ImageView categoryimage;
        TextView time,items,rate,amount,orderid,pushid;
        Session session;
        ScaleRatingBar r1;
        session = new Session(ctx);

        orderid = mView.findViewById(R.id.orderid);
        categoryimage = mView.findViewById(R.id.categoryimage);
        time = mView.findViewById(R.id.time);
        items = mView.findViewById(R.id.items);
        rate = mView.findViewById(R.id.rate);
        amount = mView.findViewById(R.id.amount);
        pushid = mView.findViewById(R.id.pushid);
        linearLayout = mView.findViewById(R.id.linearLayout);
        r1 = mView.findViewById(R.id.r1);

        DecimalFormat form = new DecimalFormat("0.00");
        pushid.setText(Pushid);
        orderid.setText("Order Id :" +OrderNo);
        time.setText(OrderDateTime);
        amount.setText("   \u00a3");
        amount.setVisibility(View.GONE);
        items.setText(Qty+" items from "+CName);

        rate.setVisibility(View.GONE);
        r1.setVisibility(View.GONE);
//            if(TextUtils.isEmpty(DeliveryCustomer)) {
//                rate.setVisibility(View.VISIBLE);
//            }
//            else
//                rate.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(Rejected)){
            if(Rejected.equals(session.getusername())){
                linearLayout.setVisibility(View.GONE);
                linearLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        MainActivity activity = (MainActivity)ctx;
        Bundle bundle = new Bundle();
        Fragment fragment = new AcceptOrder();
        bundle.putString("pushid", pushid.getText().toString());
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_container, fragment).commit();


    }

    public void setDetails1(Context ctx, String OrderNo, String Qty, String Pushid, String OrderDateTime, String OrderValue, String Status, String CName, String DeliveryCustomer){

        LinearLayout linearLayout;
        ImageView categoryimage;
        TextView time,items,rate,amount,orderid,pushid;
        ScaleRatingBar r1;


        orderid = mView.findViewById(R.id.orderid);
        categoryimage = mView.findViewById(R.id.categoryimage);
        time = mView.findViewById(R.id.time);
        items = mView.findViewById(R.id.items);
        rate = mView.findViewById(R.id.rate);
        amount = mView.findViewById(R.id.amount);
        pushid = mView.findViewById(R.id.pushid);
        linearLayout = mView.findViewById(R.id.linearLayout);
        r1 = mView.findViewById(R.id.r1);


        if(!Status.equals("1")&&!Status.equals("2")&&!Status.equals("3")&&!Status.equals("4")) {
            DecimalFormat form = new DecimalFormat("0.00");
            pushid.setText(Pushid);
            orderid.setText("Order Id :" +OrderNo);
            time.setText(OrderDateTime);
//            amount.setText("   \u00a3" + form.format(Double.parseDouble(DeliveryPrice)));
            amount.setVisibility(View.GONE);
            items.setText(Qty+" items from "+CName);

            if(TextUtils.isEmpty(DeliveryCustomer)) {
                rate.setVisibility(View.VISIBLE);
                r1.setVisibility(View.GONE);
            }
            else {
                rate.setVisibility(View.GONE);
                r1.setVisibility(View.VISIBLE);
                r1.setRating(Float.parseFloat(DeliveryCustomer));
                r1.setEnabled(false);
                r1.setClickable(false);
            }

        }
        else{
            linearLayout.setVisibility(View.GONE);
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }


    }

    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }
}
