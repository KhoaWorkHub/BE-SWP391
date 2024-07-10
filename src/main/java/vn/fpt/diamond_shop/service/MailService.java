package vn.fpt.diamond_shop.service;

import vn.fpt.diamond_shop.request.CouponsMail;
import vn.fpt.diamond_shop.request.InvoiceMail;
import vn.fpt.diamond_shop.request.SendInvoiceRequest;

public interface MailService {
    void sendOtp(String email, String subject, String otp);

    void sendInvoice(String email, String subject, SendInvoiceRequest mail);

    void sendCoupon(String email, String subject, CouponsMail couponMail);
}
