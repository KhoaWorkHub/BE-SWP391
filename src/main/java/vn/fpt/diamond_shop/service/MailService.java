package vn.fpt.diamond_shop.service;

import vn.fpt.diamond_shop.request.InvoiceMail;

public interface MailService {
    void sendOtp(String email, String subject, String otp);

    void sendInvoice(String email, String subject, InvoiceMail mail);
}
