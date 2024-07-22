package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse {

    private OrdersData orderInfo;

    private RevenueData revenueData;

    private List<SaleData> saleData;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OrdersData{
        private Integer totalOrder;
        private Integer orderInit;
        private Integer orderWaitPayment;
        private Integer orderDelivery;
        private Integer orderSuccess;
        private Integer orderCancel;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RevenueData{
        private Long totalPrice;
        private Long priceInit;
        private Long priceWaitPayment;
        private Long priceDelivery;
        private Long priceSuccess;
        private Long priceCancel;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaleData{
        private Long saleId;
        private Long totalPrice;
        private Integer totalOrder;
        private Long totalPriceSuccess;
        private Integer totalOrderSuccess;
    }
}
