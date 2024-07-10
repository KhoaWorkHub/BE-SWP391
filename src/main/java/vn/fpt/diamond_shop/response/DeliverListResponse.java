package vn.fpt.diamond_shop.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DELIVER")
@Data
@NoArgsConstructor
public class DeliverListResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "status")
    private String status;

    @Column(name = "total_order")
    private Integer totalOrder;

    @Column(name = "total_order_success")
    private Integer totalOrderSuccess;

    @Column(name = "total_order_fail")
    private Integer totalOrderFail;

    @Column(name = "created_at")
    private Date createdAt;


}
