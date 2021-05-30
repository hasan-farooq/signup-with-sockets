package com.company.package_name;

import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandler implements Runnable {

    Helper helper = new Helper();
    Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private boolean register_user(String username, String password) {
        if (helper.is_valid("username", username) && helper.is_valid("password", password)) {
            Connection connection = new Connection();
            String temp = helper.encrypt_decrypt(password);
            String query1 = "insert into users values('" + username + "','" + temp + "')";
            try {
                connection.s.executeUpdate(query1);   //executes the query
            } catch (Exception excp) {
                System.out.println("Error Occurred while Adding User");
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean login_user(String username, String password) throws SQLException {
        Connection connection = new Connection();
        String temp = helper.encrypt_decrypt(password);
        String query = "select * from users where username = '" + username + "' AND password = '" + temp + "' ";

        ResultSet resultSet = null;
        try {
            resultSet = connection.s.executeQuery(query);
        } catch (SQLException throwables) {
            System.out.println("Error in username exists");
        }
        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean username_exists(String username) throws SQLException {
        Connection connection = new Connection();
        String query = "select * from users where username = '" + username + "'";

        ResultSet resultSet = null;
        try {
            resultSet = connection.s.executeQuery(query);
        } catch (SQLException throwables) {
            System.out.println("Error in username exists");
        }
        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void run() {
        {
            String str = null;
            try {
                str = helper.receive_message(4000, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] args = str.split(",");   // mesage coming from client

            try {
                if (args[0].equals("register")) {
                    if (!username_exists(args[1])) {
                        if (helper.is_valid("password", args[2]) && helper.is_valid("username", args[1])) {
                            if (register_user(args[1], args[2])) {
                                socket = helper.send_message("registered", socket);
                            } else {
                                socket = helper.send_message("invalid", socket);
                            }
                        } else {
                            socket = helper.send_message("invalid", socket);
                        }
                    } else {
                        socket = helper.send_message("not registered", socket);
                    }
                }
                else if (args[0].equals("login")) {
                    if (login_user(args[1], args[2])) {
                        socket = helper.send_message("login-true", socket);
                    } else {
                        socket = helper.send_message("login-fail", socket);
                    }
                }
                else if (args[0].equals("change-password")) {
                    if (login_user(args[2], args[4]) && helper.is_valid("password", args[3])) {
                        Connection connection = new Connection();
                        String temp = helper.encrypt_decrypt(args[3]);
                        String query = "update users set password = '" + temp + "' where username = '" + args[2] + "'";
                        connection.s.executeUpdate(query);

                        socket = helper.send_message("password-changed", socket);

                    } else {
                        socket = helper.send_message("change-fail", socket);
                    }
                }
                else if (args[0].equals("change-username")) {
                    if (login_user(args[4], args[5])) {
                        if (!username_exists(args[3]) && helper.is_valid("username", args[3])) {
                            Connection connection = new Connection();
                            String query = "update users set username = '" + args[3] + "' where username = '" + args[2] + "'";
                            connection.s.executeUpdate(query);
                            socket = helper.send_message("username-changed", socket);
                        } else {
                            socket = helper.send_message("change-fail", socket);
                        }
                    } else {
                        socket = helper.send_message("invalid", socket);
                    }
                }
            } catch (Exception e) {
                System.out.println("Client Handler Error");
            }
        }
    }
}
