package com.araouf.server.service;

public interface EmailService {
    public void send(String to, String email);
    public String getVerficationEmailTemplate(String name, String otp) ;
}
