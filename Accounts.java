package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection con;
    private Scanner sc;
    public Accounts(Connection con,Scanner sc) {
        this.con = con;
        this.sc = sc;
    }
    public long open_account(String email) {
      if(!account_exist(email)){
         String open_account_query= "INSERT INTO accounts(account_number,full_name,email,balance,security_pin) values (?,?,?,?,?)";
         sc.nextLine();
         System.out.println("enter your full_name");
         String full_name =sc.nextLine();
         System.out.println("enter your initial Amount");
         double balance = sc.nextDouble();
         sc.nextLine();
         System.out.println("enter Security pin");
         String security_pin = sc.nextLine();

         try{
           long account_number = generateAccountNumber();
             PreparedStatement pstmt=con.prepareStatement(open_account_query);
             pstmt.setLong(1,account_number);
             pstmt.setString(2,full_name);
             pstmt.setString(3,email);
             pstmt.setDouble(4,balance);
             pstmt.setString(5,security_pin);
             int affectedrows = pstmt.executeUpdate();
             if (affectedrows >0){
                 return account_number;
             }else{
                 throw new RuntimeException("account creation failed !!");
             }
         }catch(SQLException e){
             e.printStackTrace();
         }
      }
      throw new RuntimeException("account already exists");
    }

    public long getAccount_number(String email) {
       String query =" SELECT account_number from accounts where email=? ";
       try{
          PreparedStatement pstmt =con.prepareStatement(query);
          pstmt.setString(1,email);
          ResultSet rs= pstmt.executeQuery();
          if (rs.next()){
              return rs.getLong("account_number");
          }
       }catch(SQLException e){
           e.printStackTrace();
       }
       throw new RuntimeException("account number doesn't exists");
    }

    private long generateAccountNumber() {
     try{
         Statement stmt= con.createStatement();
         ResultSet rs= stmt.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC limit 1");
         if(rs.next()){
             long last_account_number = rs.getLong("account_number");
             return last_account_number+1;
         }else{
             return 10000100;
         }
     }catch(SQLException e){
         e.printStackTrace();
     }
     return 10000100;
    }

    public boolean account_exist(String email) {
       String query="SELECT account_number from Account where email=?";
       try{
           PreparedStatement pstmt = con.prepareStatement(query);
           pstmt.setString(1,email);
           ResultSet rs=pstmt.executeQuery();
           if(rs.next()){
               return true;
           }else{
               return false;
           }
       }catch(SQLException e){
           e.printStackTrace();
       }
       return false;
    }

}