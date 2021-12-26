package hurrys.corp.delivery.Configurations;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusername(String usename)
    {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getusername() {
        String usename = prefs.getString("usename","");
        return usename;
    }


    public void setrole(String usename)
    {
        prefs.edit().putString("role", usename).commit();
    }

    public String getrole() {
        String usename = prefs.getString("role","");
        return usename;
    }


    public void setinstructions(String usename)
    {
        prefs.edit().putString("setintrstuction", usename).commit();
    }

    public String getinstructions() {
        String usename = prefs.getString("setintrstuction","");
        return usename;
    }

    public void setflat(String usename)
    {
        prefs.edit().putString("setflat", usename).commit();
    }

    public String getflat() {
        String usename = prefs.getString("setflat","");
        return usename;
    }


    public void setname(String usename)
    {
        prefs.edit().putString("name", usename).commit();
    }

    public String getname() {
        String usename = prefs.getString("name","");
        return usename;
    }

    public void setemail(String usename)
    {
        prefs.edit().putString("email", usename).commit();
    }

    public String getemail() {
        String usename = prefs.getString("email","");
        return usename;
    }


    public void setpassword(String usename)
    {
        prefs.edit().putString("pass", usename).commit();
    }

    public String getpassword() {
        String usename = prefs.getString("pass","");
        return usename;
    }


    public void setlocation(String usename)
    {
        prefs.edit().putString("location", usename).commit();
    }

    public String getlocation() {
        String usename = prefs.getString("location","");
        return usename;
    }



    public void setpp(String usename)
    {
        prefs.edit().putString("pp", usename).commit();
    }

    public String getpp() {
        String usename = prefs.getString("pp","");
        return usename;
    }


    public void setdaname(String username) {
        prefs.edit().putString("daname", username).commit();
    }

    public String getdaname() {
        String username = prefs.getString("daname", "");
        return username;
    }


    public void setdaaddress(String username) {
        prefs.edit().putString("daaddress", username).commit();
    }

    public String getdaaddress() {
        String username = prefs.getString("daaddress", "");
        return username;
    }

    public void setdaf(String username) {
        prefs.edit().putString("daf", username).commit();
    }

    public String getdaf() {
        String username = prefs.getString("daf", "");
        return username;
    }


    public void setdal(String username) {
        prefs.edit().putString("dal", username).commit();
    }

    public String getdal() {
        String username = prefs.getString("dal", "");
        return username;
    }

    public void setdaloc(String username) {
        prefs.edit().putString("daloc", username).commit();
    }

    public String getdaloc() {
        String username = prefs.getString("daloc", "");
        return username;
    }

    public void setdadist(String username) {
        prefs.edit().putString("dadist", username).commit();
    }

    public String getdadist() {
        String username = prefs.getString("dadist", "");
        return username;
    }


    public void setisfirsttime(String username) {
        prefs.edit().putString("first", username).commit();
    }

    public String getisfirsttime() {
        String username = prefs.getString("first", "");
        return username;
    }


    public void setcart(String username) {
        prefs.edit().putString("cart", username).commit();
    }

    public String getcart() {
        String username = prefs.getString("cart", "");
        return username;
    }



    public void settoken(String username) {
        prefs.edit().putString("token", username).commit();
    }

    public String gettoken() {
        String username = prefs.getString("token", "");
        return username;
    }

    public void setsub(String username) {
        prefs.edit().putString("sub", username).commit();
    }

    public String getsub() {
        String username = prefs.getString("sub", "");
        return username;
    }



    public void setrange(String username) {
        prefs.edit().putString("range", username).commit();
    }

    public String getrange() {
        String username = prefs.getString("range", "");
        return username;
    }



    public void setpincode(String username) {
        prefs.edit().putString("pincode", username).commit();
    }

    public String getpincode() {
        String username = prefs.getString("pincode", "");
        return username;
    }


    public void setnumber(String username) {
        prefs.edit().putString("setnumber", username).commit();
    }

    public String getnumber() {
        String username = prefs.getString("setnumber", "");
        return username;
    }



    public void setextras(String username) {
        prefs.edit().putString("esetextras", username).commit();
    }

    public String getextras() {
        String username = prefs.getString("esetextras", "");
        return username;
    }


    public void setcommision(String username) {
        prefs.edit().putString("commision", username).commit();
    }

    public String getcommision() {
        String username = prefs.getString("commision", "");
        return username;
    }


    public void setstatus(String username) {
        prefs.edit().putString("status", username).commit();
    }

    public String getstatus() {
        String username = prefs.getString("status", "");
        return username;
    }


    public void setaddress(String username) {
        prefs.edit().putString("address", username).commit();
    }

    public String getaddress() {
        String username = prefs.getString("address", "");
        return username;
    }


    public void setcity(String username) {
        prefs.edit().putString("city", username).commit();
    }

    public String getcity() {
        String username = prefs.getString("city", "");
        return username;
    }


    public void setlocality(String username) {
        prefs.edit().putString("locality", username).commit();
    }

    public String getlocality() {
        String username = prefs.getString("locality", "");
        return username;
    }


    public void setreferral(String username) {
        prefs.edit().putString("referral", username).commit();
    }

    public String getreferral() {
        String username = prefs.getString("referral", "");
        return username;
    }


    public void setstorename(String username) {
        prefs.edit().putString("storename", username).commit();
    }

    public String getstorename() {
        String username = prefs.getString("storename", "");
        return username;
    }



    public void setsubmitted(String username) {
        prefs.edit().putString("submitted", username).commit();
    }

    public String getsubmitted() {
        String username = prefs.getString("submitted", "");
        return username;
    }


    public void setapprovalstatus(String username) {
        prefs.edit().putString("approvalstatus", username).commit();
    }

    public String getapprovalstatus() {
        String username = prefs.getString("approvalstatus", "");
        return username;
    }


    public void settemp(String username) {
        prefs.edit().putString("temp", username).commit();
    }

    public String gettemp() {
        String username = prefs.getString("temp", "");
        return username;
    }


    public void setapprovalfirst(String username) {
        prefs.edit().putString("approvalfirst", username).commit();
    }

    public String getapprovalfirst() {
        String username = prefs.getString("approvalfirst", "");
        return username;
    }

    public void setstage(String username) {
        prefs.edit().putString("stage", username).commit();
    }

    public String getstage() {
        String username = prefs.getString("stage", "");
        return username;
    }

    public void setstagepushid(String username) {
        prefs.edit().putString("stagepushid", username).commit();
    }

    public String getstagepushid() {
        String username = prefs.getString("stagepushid", "");
        return username;
    }


    public void setpickup(String username) {
        prefs.edit().putString("setpickup", username).commit();
    }

    public String getpickup() {
        String username = prefs.getString("setpickup", "");
        return username;
    }

    public void setdelivery(String username) {
        prefs.edit().putString("setdelivery", username).commit();
    }

    public String getdelivery() {
        String username = prefs.getString("setdelivery", "");
        return username;
    }

    public void setcname(String username) {
        prefs.edit().putString("setcname", username).commit();
    }

    public String getcname() {
        String username = prefs.getString("setcname", "");
        return username;
    }

    public void setcaddress(String username) {
        prefs.edit().putString("setcaddress", username).commit();
    }

    public String getcaddress() {
        String username = prefs.getString("setcaddress", "");
        return username;
    }

    public void setcnumber(String username) {
        prefs.edit().putString("setcnumber", username).commit();
    }

    public String getcnumber() {
        String username = prefs.getString("setcnumber", "");
        return username;
    }

    public void setdname(String username) {
        prefs.edit().putString("setdname", username).commit();
    }

    public String getdname() {
        String username = prefs.getString("setdname", "");
        return username;
    }

    public void setdaddress(String username) {
        prefs.edit().putString("setdaddress", username).commit();
    }

    public String getdaddress() {
        String username = prefs.getString("setdaddress", "");
        return username;
    }

    public void setdnumber(String username) {
        prefs.edit().putString("setdnumber", username).commit();
    }

    public String getdnumber() {
        String username = prefs.getString("setdnumber", "");
        return username;
    }

    public void setordervalue(String username) {
        prefs.edit().putString("setordervalue", username).commit();
    }

    public String getordervalue() {
        String username = prefs.getString("setordervalue", "");
        return username;
    }

    public void setpaymenttype(String username) {
        prefs.edit().putString("setpaymenttype", username).commit();
    }

    public String getpaymenttype() {
        String username = prefs.getString("setpaymenttype", "");
        return username;
    }

    public void setorderid(String username) {
        prefs.edit().putString("setorderid", username).commit();
    }

    public String getorderid() {
        String username = prefs.getString("setorderid", "");
        return username;
    }

    public void settotalitem(String username) {
        prefs.edit().putString("settotalitem", username).commit();
    }

    public String gettotalitems() {
        String username = prefs.getString("settotalitem", "");
        return username;
    }






}
