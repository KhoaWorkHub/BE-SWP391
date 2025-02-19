package vn.fpt.diamond_shop.service.Impl;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.fpt.diamond_shop.request.CouponsMail;
import vn.fpt.diamond_shop.request.SendInvoiceRequest;
import vn.fpt.diamond_shop.service.MailService;

import freemarker.template.Configuration;

import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendOtp(String email, String subject, String content) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("otp", content);
            push(email, subject, param, "otp_mail_template.ftl");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendInvoice(String email, String subject, SendInvoiceRequest mail) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("mail", mail.getMail());
            param.put("orderId", mail.getOrderId());
            param.put("date", mail.getDate());
            param.put("address", mail.getAddress());
            param.put("products", mail.getProducts());
            push(email, subject, param, "invoice_mail_template.ftl");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendCoupon(String email, String subject, CouponsMail mail) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("code", mail.getCode());
            param.put("expiredDate", mail.getExpiredDate());
            param.put("percent", mail.getPercent());
            push(email, subject, param, "coupons_mail_template.ftl");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void push(String email, String subject, Map<String, Object> attribute, String templateName) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        freemarkerConfig.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));
        Template template = freemarkerConfig.getTemplate(templateName);
        StringWriter stringWriter = new StringWriter();
        template.process(attribute, stringWriter);

        String htmlContent = stringWriter.toString();

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }

    public String getHtmlContent(Map<String, Object> attribute, String templateName) {
        try {
            freemarkerConfig.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));
            Template template = freemarkerConfig.getTemplate(templateName);
            StringWriter stringWriter = new StringWriter();
            template.process(attribute, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
