package vn.fpt.diamond_shop.third_party.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.fpt.diamond_shop.constants.PayTypeEnum;
import vn.fpt.diamond_shop.third_party.payload.CreatePaymentRequest;
import vn.fpt.diamond_shop.third_party.payload.CreatePaymentResponse;

@Log4j2
@Component
public class CreatePaymentRefundApi {
    private final String url = "http://localhost:8081/v1/payment/stripe/refund";

    public void refund(String orderId) {
        try {
            HttpResponse<JsonNode> response = Unirest
                    .put(url)
                    .queryString("orderId", orderId).asJson();
            if (response.getStatus() != 200) {
                log.error("Refund payment failed");
                throw new RuntimeException("Refund payment failed");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
