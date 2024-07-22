package vn.fpt.diamond_shop.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fpt.diamond_shop.model.Diamond;

import java.util.List;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class GIAInfoResponse {

    private Long invoiceId;

    private String shapeCut;

    private String nameDiamond;

    private int carat;

    private String color;

    private String clarity;

    private String cut;

    private Diamond diamond;
}
