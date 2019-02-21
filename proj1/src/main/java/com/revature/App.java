package com.revature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //  read a hypothetical reimbursement request or login from a client at the console.
        // Scanner scan = new Scanner(System.in);
        // scan.close();
        try(
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
        ){
            System.out.println("please input your reimbursement request information, seperated by \", \" \n(id, money, manager)");
            String input = br.readLine();
            System.out.println("You input: ["+input+"]");
            Scanner scan = new Scanner(input);
            scan.useDelimiter(", ");

            String id = scan.next();
            String money = scan.next();
            String manager = scan.next();

            System.out.println(id +" "+ money + " " + manager);

            System.out.println("please input your username");
            String username = br.readLine();
            System.out.println("please input your password");
            String password = br.readLine();

            StringBuilder passwordHidden = new StringBuilder();
            for (int i = 0; i<password.length(); i++) {
                passwordHidden.append('*');
            }
            System.out.println("Username: "+username+"\nPassword: "+passwordHidden);
        } catch(IOException e){
            e.printStackTrace();
        } catch(NoSuchElementException e){
            e.printStackTrace();
        }
    }
}
