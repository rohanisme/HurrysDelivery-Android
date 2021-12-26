package hurrys.corp.delivery.Models;


public class Users {

    public String UserId,Name,Age,Gender,Email,MobileNumber,AlternateNumber,Address,PostCode;
    public String AccountName,AccountNumber,SortCode,BranchName,BranchAddress,FranchiseCode,BankName;
    public String Doc1,Doc2,Doc3,Doc4,Doc5,Doc6,Doc7,Status,ApprovalStatus,Created,IMEI;
    public int Ratings,PendingAmount,Wallet,Cash,Orders;

    public Users(String UserId, String Name, String Age, String Gender, String Email, String MobileNumber, String AlternateNumber, String Address, String PostCode, String AccountName, String AccountNumber, String SortCode, String BranchName, String BranchAddress, String FranchiseCode,String Doc1, String Doc2, String Doc3, String Doc4, String Doc5, String Doc6, String Doc7, String Status, String ApprovalStatus, String Created,String BankName,int Ratings, int PendingAmount, int Wallet,int Cash,int Orders,String IMEI) {
        this.UserId = UserId;
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
        this.Email = Email;
        this.MobileNumber = MobileNumber;
        this.AlternateNumber = AlternateNumber;
        this.Address = Address;
        this.PostCode = PostCode;
        this.AccountName = AccountName;
        this.AccountNumber = AccountNumber;
        this.SortCode = SortCode;
        this.BranchName = BranchName;
        this.BranchAddress = BranchAddress;
        this.FranchiseCode = FranchiseCode;
        this.Doc1 = Doc1;
        this.Doc2 = Doc2;
        this.Doc3 = Doc3;
        this.Doc4 = Doc4;
        this.Doc5 = Doc5;
        this.Doc6 = Doc6;
        this.Doc7 = Doc7;
        this.Status = Status;
        this.ApprovalStatus = ApprovalStatus;
        this.Created = Created;
        this.BankName=BankName;
        this.Ratings = Ratings;
        this.PendingAmount = PendingAmount;
        this.Wallet = Wallet;
        this.Cash=Cash;
        this.Orders=Orders;
        this.IMEI=IMEI;
    }

    public Users(){}



}
