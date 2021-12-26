package hurrys.corp.delivery.Models.Orders;

public class Orders {

    public String BillPaymentType;
    public String CAddress;
    public String CName;
    public String CNumber;
    public String Client;
    public String ClientOrderId;
    public String CustomerName;
    public String CustomerNumber;
    public String OrderValue;
    public String RestaurantName;
    public String RestaurantPhone;
    public String SupportNumber;
    public String OrderNo;
    public String OrderDate;
    public String OrderDateTime;
    public String TotalItems;
    public String Status;
    public String DeliveryCustomer;
    public String Pushid;
    public String Rejected;
    public String Qty;

    public  Orders(){}

    public Orders(String BillPaymentType, String CAddress, String CName, String CNumber, String Client, String ClientOrderId, String CustomerName, String CustomerNumber, String OrderValue, String RestaurantName, String RestaurantPhone, String SupportNumber, String OrderNo,
                  String OrderDate, String OrderDateTime, String TotalItems, String Status, String DeliveryCustomer, String Pushid, String Rejected,String Qty){
        this.BillPaymentType = BillPaymentType;
        this.CAddress = CAddress;
        this.CName = CName;
        this.CNumber = CNumber;
        this.Client = Client;
        this.ClientOrderId = ClientOrderId;
        this.CustomerName = CustomerName;
        this.CustomerNumber = CustomerNumber;
        this.OrderValue = OrderValue;
        this.RestaurantName = RestaurantName;
        this.RestaurantPhone = RestaurantPhone;
        this.SupportNumber = SupportNumber;
        this.OrderNo = OrderNo;
        this.OrderDate = OrderDate;
        this.OrderDateTime = OrderDateTime;
        this.TotalItems = TotalItems;
        this.Status = Status;
        this.DeliveryCustomer = DeliveryCustomer;
        this.Pushid = Pushid;
        this.Rejected = Rejected;
        this.Qty = Qty;
    }
}
