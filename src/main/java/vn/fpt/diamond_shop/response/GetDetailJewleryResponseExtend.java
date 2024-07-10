package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class GetDetailJewleryResponseExtend {
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

    private String diamondColor;

    private String diamondClarity;

    private String diamondCut;

    private String diamondShape;

    private String diamondPolish;

    private String diamondOrigin;

    private int originPrice;

    private int polishedPrice;

    private int cutPrice;

    private int rapaportPrice;
}
