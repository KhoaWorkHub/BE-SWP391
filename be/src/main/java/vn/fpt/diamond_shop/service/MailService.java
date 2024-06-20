package vn.fpt.diamond_shop.service;

<<<<<<< HEAD
public interface MailService {
    void sendOtp(String email, String subject, String otp);
=======
import vn.fpt.diamond_shop.request.InvoiceMail;

public interface MailService {
    void sendOtp(String email, String subject, String otp);

    void sendInvoice(String email, String subject, InvoiceMail mail);
>>>>>>> origin/Nhat
}
