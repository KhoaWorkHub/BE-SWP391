package vn.fpt.diamond_shop.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fpt.diamond_shop.constants.DiamondColorEnum;

import javax.persistence.*;
import java.sql.Date;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "customer_id")
    private Long customerId;

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
