package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fpt.diamond_shop.model.*;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class ListAllDiamondResponse {
    private List<Clarity> clarities;
    private List<Polish> polishes;
    private List<Color> colors;
    private List<Origin> origins;
    private List<Shape> shapes;
    private List<Cut> cuts;
}
