package com.revature;

import java.util.Scanner;

public class Cli {
    private static Scanner scan = new Scanner(System.in);
    
    public static void run(){
        //  read a hypothetical reimbursement request or login from a client at the console.
        String input;
        do{
            System.out.println("What do you want to do? q, request, login");
            input = scan.nextLine();
            switch (input){
                case "request":
                if (request())
                	System.out.println("Reimbursment request successful.");
                else
                	System.out.println("FAILURE: Problem with reimbursment request.");
                break;
                
                case "login":
                login();
                break;
                
                default:
                break;
            }
        } while(!input.equals("q"));
    }
    
    // should return a Reimbursement
    private static boolean request(){
        System.out.println("please input your reimbursement request information");
        System.out.println("amount?");
        String amountInput = scan.nextLine();
        // Ideally, user should be already logged in, so the employee_id should just be id of the signed in user
        System.out.println("employee id?");
        String employee_idInput = scan.nextLine();
        
        try {
        	int amount = Integer.parseInt(amountInput);
            int employee_id = Integer.parseInt(employee_idInput);
            Reimbursement reimbursement = new Reimbursement(amount,employee_id);
            //reimbursement.insertSelf(statement);
        }catch(NumberFormatException nfe) {
        	nfe.printStackTrace();
        	return false;
        }
        return true;
    }
    
    private static boolean login(){
        System.out.println("please input your username");
        String username = scan.nextLine();
        System.out.println("please input your password");
        String password = scan.nextLine();

        StringBuilder passwordHidden = new StringBuilder();
        for (int i = 0; i<password.length(); i++) {
            passwordHidden.append('*');
        }
        System.out.println("Username: "+username+"\nPassword: "+passwordHidden);
        return true;
    }
}