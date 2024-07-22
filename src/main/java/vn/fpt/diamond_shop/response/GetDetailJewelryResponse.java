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
@AllArgsConstructor
@NoArgsConstructor
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

    private Float goldWeight;
}
