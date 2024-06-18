package vn.fpt.diamond_shop.service;

public interface MailService {
    void sendOtp(String email, String subject, String otp);
}
