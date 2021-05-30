package com.company.HW3;

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.Statement;


public class Connection {
    java.sql.Connection c;
    Statement s;

    public Connection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///database_name","username","password");

            s = c.createStatement();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Invalid-connection");
//            System.exit(5);
            e.printStackTrace();
        }
    }
}
