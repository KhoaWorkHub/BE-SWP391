package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Date;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddOrderResponse {
    private Long id;

    private Long jewelryId;

    private Date orderDate;

    private Integer status;

    private Long customerId;

    private Integer quantityNumber;

    private Long totalPrice;

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;

    private String jewelryTitle;

    private Long priceItems;

    private String imageUrl;
}
