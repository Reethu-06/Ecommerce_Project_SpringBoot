package com.newOne.request;


/**
 * Request class for Admin login.
 * This class holds the data sent by the client during an admin login request.
 */


public class AdminLoginRequest {
    private String email;
    private String password;

    public AdminLoginRequest() {}

    public AdminLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
