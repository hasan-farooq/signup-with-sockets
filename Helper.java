


/*
Wrapper class for the GUI and Server
Makes it easy to initialize and work in a modular waay
 */


package com.company.package_name;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;

public class Helper {


    //    socket that supports sending messages. it takes port number and sends by creating streams inside function
    public Socket send_message(String message, int port, Socket socket) throws IOException {
        socket = new Socket("localhost", port);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(message);
        printWriter.flush();
        return socket;
    }

    public Socket send_message(String message,Socket socket) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(message);
        printWriter.flush();
        return socket;
    }

    //    same as send_message but uses streams to receive requests/messages
    public String receive_message(int port, Socket socket) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String message = bufferedReader.readLine();

        return message;
    }

//    checks for username/password validation
    public boolean is_valid(String type, String text) {
        if (type.equals("password")) {
            if (text.length() > 4 && text.length() < 10) { return true; }
            else{return false;}
        }
        else if (type.equals("username")) {
            if (text.length() > 0 && text.length() < 20) { return true; }
            else{return false;}
        }
        else{return  false;}
    }

//    encrypts/decrypts with XOR bitwise
    public String encrypt_decrypt(String password)
    {
        char xorKey = 'H';
        String changed = "";

        int len = password.length();

        for (int i = 0; i < len; i++)
        {
            changed = changed + Character.toString((char) (password.charAt(i) ^ xorKey));
        }

        return changed;
    }

    public TextField textfield(double x, double y, String placeholder, Pos position ) {
        TextField textField = new TextField();
        textField.setAlignment(position);
        textField.setLayoutY(y);
        textField.setLayoutX(x);
        textField.setPromptText(placeholder);

        return textField;
    }

    public PasswordField passwordfield(double x, double y, String placeholder, Pos position ) {
        PasswordField textField = new PasswordField();
        textField.setAlignment(position);
        textField.setLayoutY(y);
        textField.setLayoutX(x);
        textField.setPromptText(placeholder);

        return textField;
    }

    public Text text(double x, double y, String text, Paint color) {
        Text text1 = new Text();
        text1.setText(text);
        text1.setLayoutY(y);
        text1.setLayoutX(x);
        text1.setFill(color);

        return text1;
    }

    public Button button(double x, double y, String text, Pos position, Cursor cursor) {
        Button button = new Button();
        button.setAlignment(position);
        button.setLayoutY(y);
        button.setLayoutX(x);
        button.setText(text);
        button.setCursor(cursor);

        return button;
    }


}
