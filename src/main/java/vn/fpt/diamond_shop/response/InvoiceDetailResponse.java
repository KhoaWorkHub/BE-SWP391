package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fpt.diamond_shop.model.Jewelry;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceDetailResponse {

    private Date orderDate;

    private String customerName;

    private String phoneNumber;

    private String orderId;

    private List<Product> listOfProduct;


    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product{
        private String jewelryName;
        private String jewelryCode;
        private Integer quantity;
    }

}
