package BankingManagementSystem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection con;
    private Scanner sc;
    AccountManager(Connection con,Scanner sc) {
        this.con = con;
        this.sc = sc;
    }
    public void credit_money(long account_number)throws SQLException{
      sc.nextLine();
      System.out.println("Enter account number");
      double amount = sc.nextDouble();
      sc.nextLine();
      System.out.println("Enter Security pin:");
      String Security_pin = sc.nextLine();
      try{
          con.setAutoCommit(false);
          if(account_number != 0){
          PreparedStatement pstmt = con.prepareStatement("Select * from account where account_number = ? AND security_pin = ?");
          pstmt.setLong(1, account_number);
          pstmt.setString(2, Security_pin);
          ResultSet rs = pstmt.executeQuery();

          if(rs.next()){
              String credit_query = "Update account set balance = balance + ? where account_number = ?";
              PreparedStatement pstmt1 = con.prepareStatement(credit_query);
              pstmt1.setDouble(1, amount);
              pstmt1.setLong(2, account_number);
              int rowsaffected = pstmt1.executeUpdate();
              if(rowsaffected>0){
                  System.out.println("Rs" +amount+ " Amount credited successfully");
                  con.commit();
                  con.setAutoCommit(true);
                  return;
              }else {
                  System.out.println("Transaction failed");
                  con.rollback();
                  con.setAutoCommit(true);
              }
              }else{
                  System.out.println("Invalid account number");
              }
          }
      }catch(SQLException e){
          e.printStackTrace();
      }
      con.setAutoCommit(true);
    }

    public void debit_money(long account_number)throws SQLException{
        sc.nextLine();
        System.out.println("Enter account number");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security pin:");
        String Security_pin = sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(account_number != 0){}
            PreparedStatement pstmt = con.prepareStatement("Select * from account where account_number = ? AND security_pin = ?");
            pstmt.setLong(1, account_number);
            pstmt.setString(2, Security_pin);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                String debit_query = "Update account set balance = balance - ? where account_number = ?";
                PreparedStatement pstmt1 = con.prepareStatement(debit_query);
                pstmt1.setDouble(1, amount);
                pstmt1.setLong(2, account_number);
                int rowsaffected = pstmt1.executeUpdate();

                if(rowsaffected>0){
                    System.out.println("Rs" +amount+ " Amount credited successfully");
                    con.commit();
                    con.setAutoCommit(true);
                    return;
                }else {
                    System.out.println("Transaction failed");
                    con.rollback();
                    con.setAutoCommit(true);
                }
            }else{
                    System.out.println("Invalid account number");
                }
            }
        catch(SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void transfer_money(long Sender_account_number)throws SQLException {
        sc.nextLine();
        System.out.println("Enter Receiver account number");
        long Receiver_account_number = sc.nextLong();
        System.out.println("Enter amount:");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security pin:");
        String Security_pin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (Sender_account_number != 0 && Receiver_account_number != 0) {
                PreparedStatement pstmt = con.prepareStatement("select * from account where account_number = ? AND security_pin = ?");
                pstmt.setLong(1, Sender_account_number);
                pstmt.setString(2, Security_pin);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    double current_balance = rs.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "Update accounts set balance = balance-? where account_number = ?";
                        String credit_query = "Update accounts set balance = balance+? where account_number = ?";
                        PreparedStatement creditpstmt = con.prepareStatement(credit_query);
                        PreparedStatement debitpstmt = con.prepareStatement(debit_query);
                        creditpstmt.setDouble(1, amount);
                        creditpstmt.setLong(2, Receiver_account_number);
                        debitpstmt.setDouble(1, amount);
                        debitpstmt.setLong(2, Receiver_account_number);
                        int rowsaffected1 = debitpstmt.executeUpdate();
                        int rowsaffected2 = creditpstmt.executeUpdate();
                        if (rowsaffected1 > 0 && rowsaffected2 > 0) {
                            System.out.println("transaction successful");
                            System.out.println("Rs." + amount + "transfered successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("transaction failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("insufficient Balance");
                    }
                } else {
                    System.out.println("invalid security pin");
                }

            }
        }catch(SQLException e){
                    e.printStackTrace();

                }
                con.setAutoCommit(true);
    }

    public void get_balance(long account_number){
       sc.nextLine();
       System.out.println("enter security pin");
       String security_pin = sc.nextLine();
       try{
           PreparedStatement pstmt= con.prepareStatement("Select balance from accounts where account_number= ? and security_pin = ?");
           pstmt.setLong(1,account_number);
           pstmt.setString(2,security_pin);
           ResultSet rs=pstmt.executeQuery();
           if(rs.next()){
               double balance = rs.getDouble("balance");
               System.out.println("balance" +balance);
           }else{
               System.out.println("invalid pin!!");
           }
       }catch(SQLException e){
           e.printStackTrace();
       }
    }
}