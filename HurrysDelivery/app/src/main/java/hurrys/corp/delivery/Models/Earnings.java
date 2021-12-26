package hurrys.corp.delivery.Models;


public class Earnings {

    public String  PushId;
    public String  UserId;
    public String  UserBalance;
    public String  Date;
    public String  Amount;
    public String  Generated;
    public String  TransactionType;
    public String  TransactionName;
    public String  TransactionId;
    public String  Status;
    public String  OrderPushId;



    public Earnings(){}

    public Earnings(String PushId, String UserId, String UserBalance, String Date, String Amount, String Generated, String TransactionType, String TransactionName, String TransactionId, String Status, String OrderPushId){
        this.PushId=PushId;
        this.UserId=UserId;
        this.UserBalance=UserBalance;
        this.Date=Date;
        this.Amount=Amount;
        this.Generated=Generated;
        this.TransactionType=TransactionType;
        this.TransactionName=TransactionName;
        this.Status=Status;
        this.TransactionId=TransactionId;
        this.OrderPushId=OrderPushId;
    }

}
