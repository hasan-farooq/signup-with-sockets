
/*
Class that implements client side interface
 */

package com.company.package_name;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class GUI extends Application {

    private Socket socket;
    private Helper helper = new Helper();
    private String current_user;
    private String current_password;


//    displays when user is logged in
    public boolean user_screen(Stage stage) {
        Button change_password = helper.button(123, 100, "Change Password", Pos.CENTER, Cursor.HAND);
        Button change_username = helper.button(120, 150, "Change Username", Pos.CENTER, Cursor.HAND);
        Button log_out = helper.button(153, 200, "Log Out", Pos.CENTER, Cursor.HAND);
        Group group = new Group(change_username, change_password,log_out);
        Scene scene = new Scene(group, 400, 400);

        log_out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login_register_screen(stage);
            }
        });

        change_password.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                change_password(stage);
            }
        });

        change_username.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                change_username(stage);
            }
        });
        group.requestFocus();
        stage.setScene(scene);

        return true;
    }

    public boolean change_password(Stage stage) {
        Text text = helper.text(83, 60, "", Color.GREEN);
        PasswordField passwordField = helper.passwordfield(70, 100, "New Password", Pos.CENTER);
        PasswordField old_password = helper.passwordfield(70, 70, "Old Password", Pos.CENTER);
        Button button = helper.button(90, 140, "Change Password", Pos.CENTER, Cursor.HAND);
        Button back = helper.button(130, 170, "Back", Pos.CENTER, Cursor.HAND);
        Group group = new Group(passwordField, button, back, text, old_password);
        Scene scene = new Scene(group, 300, 300);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                user_screen(stage);
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String message = "change-password," + current_password + "," + current_user + "," +
                            passwordField.getText() + "," + old_password.getText();
                    socket = helper.send_message(message, 4000,socket);

                    String str = helper.receive_message(4000,socket);
                    System.out.println(str);

                    if (str.equals("password-changed")) {
                        text.setFill(Color.GREEN);
                        text.setText("Password Changed !!!"); }
                    else {
                        text.setFill(Color.RED);
                        text.setText("Try Again (length 4-10)");
                    }
                } catch (IOException e) { System.out.println("Error"); }
            }
        });
        group.requestFocus();
        stage.setScene(scene);
        return true;
    }

    public boolean change_username(Stage stage) {
        Text text = helper.text(83, 60, "", Color.GREEN);
        PasswordField passwordField = helper.passwordfield(70, 130, "Password", Pos.CENTER);
        TextField old_username = helper.textfield(70, 70, "Old Username", Pos.CENTER);
        TextField textField = helper.textfield(70, 100, "New Username", Pos.CENTER);
        Button button = helper.button(90, 170, "Change Username", Pos.CENTER, Cursor.HAND);
        Button back = helper.button(130, 200, "Back", Pos.CENTER, Cursor.HAND);
        Group group = new Group(textField, button, back, text, old_username, passwordField);
        Scene scene = new Scene(group, 300, 300);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                user_screen(stage);
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String message = "change-username," + current_password + "," + current_user + "," +
                            textField.getText() + "," + old_username.getText() + "," + passwordField.getText();
                    socket = helper.send_message(message, 4000, socket);
                    String str = helper.receive_message(4000, socket);
                    System.out.println(str);

                    if (str.equals("username-changed")) {
                        text.setFill(Color.GREEN);
                        text.setText("Username Changed !!!");
                        current_user = textField.getText();
                    }
                    else if (str.equals("invalid")){
                        text.setFill(Color.RED);
                        text.setText("Try Again ...");
                    }
                    else {
                        text.setFill(Color.RED);
                        text.setText("Already Taken...");
                    }

                } catch (IOException e) {
                    System.out.println("Error");
//                    e.printStackTrace();
                }
            }
        });
        group.requestFocus();
        stage.setScene(scene);

        return true;
    }

    public boolean login_register_screen(Stage stage){
        TextField username_textField = helper.textfield(120, 100, "username", Pos.CENTER);
        PasswordField password_textField = helper.passwordfield(120, 150, "password", Pos.CENTER);
        Text text = helper.text(122, 280, "", Color.BLACK);
        Button login_button = helper.button(175, 200, "Login", Pos.CENTER, Cursor.HAND);
        Button register_button = helper.button(164.5, 235, "Register", Pos.CENTER, Cursor.HAND);
        Group group = new Group(username_textField,password_textField,register_button,login_button,text);
        Scene scene = new Scene(group,400,400);
        scene.setFill(Color.color(196/255, 207/255, 255/255,0.1));
        group.requestFocus();

        login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String message = "login," + username_textField.getText() + "," + password_textField.getText();
                    socket = helper.send_message(message,4000,socket);
                    String str = helper.receive_message(4000, socket);
                    if (str.equals("login-true")) {
                        user_screen(stage);
                    } else {
                        text.setFill(Color.RED);
                        text.setLayoutX(133);
                        text.setText("Invalid Credentials !!!");
                    }

                } catch (IOException e) {
                    System.out.println("Error");
//                    e.printStackTrace();
                }
                current_password = password_textField.getText();
                current_user = username_textField.getText();
            }
        });

        register_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String message = "register," + username_textField.getText() + "," + password_textField.getText();
                    socket = helper.send_message(message, 4000, socket);

                    String str = helper.receive_message(4000, socket);
                    System.out.println(str);

                    if (str.equals("registered")) {
                        text.setFill(Color.GREEN);
                        text.setLayoutX(122);
                        text.setText("Registered Successfully !!!");
                    } else if (str.equals("invalid")) {
                        text.setFill(Color.RED);
                        text.setLayoutX(95);
                        text.setText("Password (4-10) | Username (0-20)");
                    } else {
                        text.setFill(Color.RED);
                        text.setLayoutX(122);
                        text.setText("Username Already Exists !!!");
                    }

                } catch (IOException e) {
                    System.out.println("Error");
//                    e.printStackTrace();
                }
            }
        });

        stage.setScene(scene);
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        login_register_screen(primaryStage);
        primaryStage.show();
    }
}
