package vn.fpt.diamond_shop.service.Impl;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
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
            Map<String, String> param = new HashMap<>();
            param.put("otp", content);
            push(email, subject, param);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void push(String email, String subject, Map<String, String> attribute) throws Exception {
        final String templateName = "mail_template.ftl";

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
}
