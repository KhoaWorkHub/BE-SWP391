package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.context.annotation.Configuration;
import vn.fpt.diamond_shop.model.Diamond;

import java.util.Date;
import java.util.UUID;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

@NoArgsConstructor
//@RequiredArgsConstructor
public class GetDetailJewelryResponse {
    private Long idJewelry;

    private String jewelryTitle;

    private String jewelryCode;

    private String jewelryType;

    private Long jewelryTypeId;

    private Integer quantity;

    private Long price;

    private String description;

    private Long imageId;

    private String url;

    private Integer idGuide;

    private Long diamondId;

    private String clarityName;

    private String cutName;

    private String polishName;

    private String colorName;

    private String shapeName;

    private String originName;

    private Float goldWeight;
    private Long priceDiamond;
    private Long totalPrice;
    public GetDetailJewelryResponse(Long idJewelry, String jewelryTitle, String jewelryCode, String jewelryType, Long jewelryTypeId, Integer quantity, Long price, Long totalPrice, String description, Long imageId, String url, Integer idGuide, Long diamondId, Float goldWeight) {
        this.idJewelry = idJewelry;
        this.jewelryTitle = jewelryTitle;
        this.jewelryCode = jewelryCode;
        this.jewelryType = jewelryType;
        this.jewelryTypeId = jewelryTypeId;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.description = description;
        this.imageId = imageId;
        this.url = url;
        this.idGuide = idGuide;
        this.diamondId = diamondId;
        this.goldWeight = goldWeight;
    }
}
