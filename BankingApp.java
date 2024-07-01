package BankingManagementSystem;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import static java.lang.Class.forName;
public class BankingApp {
    private static final String url="jdbc:mysql://localhost:3306/banking_system";
    private static final String username="root";
    private static final String password="root";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
        }catch(ClassNotFoundException e){
            System.out.println("Driver not found");
        }
        try{
            Connection con = DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            User user= new User(con,sc);
            Accounts accounts = new Accounts(con,sc);
            AccountManager accountManager= new AccountManager(con,sc);

            String email;
            long account_number;

            while(true){
                System.out.println("Welcome to the Banking System ");
                System.out.println();
                System.out.println("1 . Register ");
                System.out.println("2 . Login ");
                System.out.println("3 . Exit ");
                System.out.println("Enter your choice ");
                int choice1 = sc.nextInt();
                switch(choice1){
                    case 1:
                        user.register();
                        System.out.println("\033[H\033[2J]");
                        System.out.println();
                        break;
                    case 2:
                        email= user.login();
                        if(email==null){
                            System.out.println();
                            System.out.println("user logged in!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1.open a new account");
                                System.out.println("2.exit");
                                if(sc.nextInt()==1){
                                    account_number= accounts.open_account(email);
                                    System.out.println("Account opened!");
                                    System.out.println("your account Number is "+account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number=accounts.getAccount_number(email);
                            int choice2= 0;
                            while(choice2 != 5){
                                System.out.println();
                                System.out.println("1.Debit card");
                                System.out.println("2.Credit card");
                                System.out.println("3.Transfer Money");
                                System.out.println("4.Check Balance");
                                System.out.println("5.Logout");
                                System.out.println("Enter your choice ");
                                choice2 = sc.nextInt();
                                switch(choice2){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.get_balance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid choice");
                                        break;
                                }
                            }
                        }else {
                            System.out.println("incorrect email or password!");
                        }
                case 3:
                    System.out.println("thank you for using banking system!!");
                    System.out.println("existing system.....");
                    return;
                    default:
                        System.out.println("Enter valid choice");
                        break;

                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}