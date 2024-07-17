package vn.fpt.diamond_shop.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddDiamondRequest extends BaseRequest{
    private Long idDiamond;
    private String nameDiamond;
    private Long priceDiamond;
    private int carat;
    private Long polish;
    private Long cut;
    private Long clarity;
    private Long color;
    private Long origin;
    private Long shape;
}
