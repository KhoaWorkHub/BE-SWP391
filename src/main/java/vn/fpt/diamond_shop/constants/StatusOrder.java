package vn.fpt.diamond_shop.constants;

public enum StatusOrder {
    INIT("Khởi tạo",0),
    CREATE_PAYMENT("Tạo mã thanh toán",1),
    PAYMENT("Thanh toán",0)
    ;


    private String value;
    private int code;

    StatusOrder(String value,int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}
