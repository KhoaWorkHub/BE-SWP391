package vn.fpt.diamond_shop.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "INVOICE")
@Data
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "content")
    @Lob
    private String htmlContent;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Invoice(String orderId, String invoiceHtml) {
        this.orderId = orderId;
        this.htmlContent = invoiceHtml;
        this.createdAt = new Date();
    }
}
