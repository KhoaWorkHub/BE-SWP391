package vn.fpt.diamond_shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
import vn.fpt.diamond_shop.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import vn.fpt.diamond_shop.service.MailService;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableCaching
public class SpringSocialApplication implements CommandLineRunner {

    @Autowired
    private MailService mailService;

    public static void main(String[] args) {
        SpringApplication.run(SpringSocialApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        mailService.sendOtp("thanhpdhe141032@gmail.com", "Diamond Shop OTP", "123456");
    }
}
