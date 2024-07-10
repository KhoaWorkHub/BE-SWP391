package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;
import vn.fpt.diamond_shop.model.Delivery;
import vn.fpt.diamond_shop.model.OrderDetail;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class OrdersListAllUser {
    private Long id;

    private String uniqueOrderId;

    private Date orderDate;

    private String status;

    private Long customerId;

    private Long totalPrice;

    private Date createdAt;

    private Date updatedAt;

    private String phoneNumber;

    private Delivery deliveryInfo;

    private List<OrderDetail> orderDetails;

    public OrdersListAllUser(Long id, String uniqueOrderId, Date orderDate, String status, Long customerId, Long totalPrice, Date createdAt, Date updatedAt, String phoneNumber) {
        this.id = id;
        this.uniqueOrderId = uniqueOrderId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.phoneNumber = phoneNumber;
    }
}
