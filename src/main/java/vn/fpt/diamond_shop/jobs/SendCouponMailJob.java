package vn.fpt.diamond_shop.jobs;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.fpt.diamond_shop.constants.CouponsConditionEnum;
import vn.fpt.diamond_shop.constants.DiscountType;
import vn.fpt.diamond_shop.constants.MailTypeEnum;
import vn.fpt.diamond_shop.constants.StatusOrder;
import vn.fpt.diamond_shop.model.Coupon;
import vn.fpt.diamond_shop.model.EndUser;
import vn.fpt.diamond_shop.repository.*;
import vn.fpt.diamond_shop.request.CouponsMail;
import vn.fpt.diamond_shop.security.model.User;
import vn.fpt.diamond_shop.service.MailService;
import vn.fpt.diamond_shop.util.DateTimeUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class SendCouponMailJob {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailHistoryRepository mailHistoryRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    private String mailSubject = "DIAMOND COUPON";

//    @Scheduled(cron = "0 28 21 * * ?")
    public void checkAndSendCoupon() {
        log.info("Start send coupon mail task");
        Date now = new Date();
        List<EndUser> listAccountId = endUserRepository.findAll();
        List<User> listUser = userRepository.findAllByIdIn(listAccountId.stream().map(EndUser::getAccountId).collect(Collectors.toList()));

        //get all usable coupons by expiration date
        List<Coupon> coupons = couponRepository.findAllByExpirationDateAfter(now);
        coupons.forEach(coupon -> {
            log.info("Starting check coupon: {}", coupon.getCouponsCode());
            //Mail param contents
            CouponsMail mail = new CouponsMail();
            mail.setCode(coupon.getCouponsCode());
            mail.setExpiredDate(DateTimeUtils.format(coupon.getExpirationDate(), "dd/MM/yyyy HH:mm:ss"));
            mail.setPercent(coupon.getDiscountPercent());
            log.info("Coupon detail: {}", coupon.toString());
            //Send mail to all user
            if (coupon.getDiscountType().equals(DiscountType.ALL.name())) {
                log.info("Coupon for ALL");
                listUser.forEach(user -> {
                    log.info("Checking for user: {}", user.getEmail());
                    if (!mailHistoryRepository.existsByMailAndTypeAndValue(user.getEmail(), MailTypeEnum.COUPON.name(), coupon.getCouponsCode())) {
                        log.info("Send coupon to user: {}", user.getEmail());
                        mailService.sendCoupon(user.getEmail(), mailSubject, mail);
                    }
                });
            } else if (coupon.getDiscountType().equals(DiscountType.PERSONAL.name())) {
                log.info("Coupon for PERSONAL");
                listUser.forEach(user -> {
                    if (!mailHistoryRepository.existsByMailAndTypeAndValue(coupon.getType(), MailTypeEnum.COUPON.name(), coupon.getCouponsCode())) {
                        //Send mail to qualified person
                        //Check user's spending money
                        if (coupon.getType().equals(CouponsConditionEnum.CUSTOMERS_LOYAL.name())) {
                            log.info("Condition type: CUSTOMERS_LOYAL, customer need to pay reach {} to get coupon", coupon.getValue());
                            Long customerConsumeAmount = ordersRepository.getCustomerAmount(user.getId(), StatusOrder.DONE.getValue());
                            log.info("Customer {} consume amount: {}", user.getEmail(), customerConsumeAmount);
                            if (customerConsumeAmount >= coupon.getValue()) {
                                log.info("Send coupon to user: {}", user.getEmail());
                                mailService.sendCoupon(user.getEmail(), mailSubject, mail);
                            }
                        }
                        if (coupon.getType().equals(CouponsConditionEnum.ACCOUNT_AGE.name())) {
                            if (user.getCreatedAt() != null) {
                                log.info("Condition type: ACCOUNT_AGE, account age need to reach {} to get coupon", coupon.getValue());

                                LocalDate var1 = Instant.ofEpochMilli(user.getCreatedAt().getTime())
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();

                                LocalDate var2 = Instant.ofEpochMilli(now.getTime())
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();

                                Period period = Period.between(var1, var2);
                                int dayBetween = period.getDays();
                                log.info("Account {} age: {}", user.getEmail(), dayBetween);
                                //Check account age
                                if (dayBetween >= coupon.getValue()) {
                                    log.info("Send coupon to user: {}", user.getEmail());
                                    mailService.sendCoupon(user.getEmail(), mailSubject, mail);
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}
