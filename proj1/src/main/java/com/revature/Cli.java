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
                request();
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
    private static void request(){
        System.out.println("please input your reimbursement request information");
        System.out.println("id?");
        String id = scan.nextLine();
    }
    private static void login(){
        System.out.println("please input your username");
        String username = scan.nextLine();
        System.out.println("please input your password");
        String password = scan.nextLine();

        StringBuilder passwordHidden = new StringBuilder();
        for (int i = 0; i<password.length(); i++) {
            passwordHidden.append('*');
        }
        System.out.println("Username: "+username+"\nPassword: "+passwordHidden);
    }
}