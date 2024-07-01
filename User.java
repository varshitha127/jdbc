package BankingManagementSystem;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con,Scanner sc) {
        this.con = con;
        this.sc = sc;
    }
    public void register() {
        sc.nextLine();
        System.out.println("enter your Full name");
        String full_name =sc.nextLine();
        System.out.println("enter your email");
        String email= sc.nextLine();
        System.out.println("enter your password");
        String password = sc.nextLine();
        if(user_exist(email)){
            System.out.println("User already exists for this email Address!");
            return;
        }
        String register_query ="INSERT INTO users(full_name,email,password)values(?,?,?)";
        try{
            PreparedStatement pstmt=con.prepareStatement(register_query);
            pstmt.setString(1,full_name);
            pstmt.setString(2,email);
            pstmt.setString(3,password);
            int affectedrows = pstmt.executeUpdate();
            if (affectedrows >0){
                System.out.println("Registration successful");
            } else{
                System.out.println("Registration Failed");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String login() {
        sc.nextLine();
        System.out.print("enter your email");
        String email=sc.nextLine();
        System.out.println("enter your password");
        String password =sc.nextLine();

        String login_query ="SELECT * from users where email=? AND password=?";

        try{
            PreparedStatement pstmt = con.prepareStatement(login_query);
            pstmt.setString(1,email);
            pstmt.setString(2,password);
            ResultSet rs =pstmt.executeQuery();
            if(rs.next()){
                return email;
            }else {
                return null;
            }
        }catch(SQLException e) {
             e.printStackTrace();
        }
          return null;
    }

    public boolean user_exist(String email) {
        String query= "SELECT *from users where email=?";
        try{
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1,email);
            ResultSet rs=pstmt.executeQuery();
            if(rs.next()){
                return true;
            } else{
                return false;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}

