package vn.fpt.diamond_shop.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fpt.diamond_shop.constants.DiamondColorEnum;

import javax.persistence.*;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "ORDER_DETAIL")
@Data
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jewelry_id")
    private Long jewelryId;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "quantity_number")
    private Integer quantityNumber;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;
}
