package vn.edu.fpt.diamondshopbeapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseResponse {
    private int code;

    private boolean isOk;

    private Object data;

    public BaseResponse( Object data) {
        this.code = 200;
        this.isOk = true;
        this.data = data;
    }
}
