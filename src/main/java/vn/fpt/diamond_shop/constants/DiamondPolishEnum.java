package vn.fpt.diamond_shop.constants;

public enum DiamondPolishEnum {

    EX(1),
    VG(2),
    G(3),
    F(4),
    P(5);

    final long id;


    DiamondPolishEnum(long id) {
        this.id = id;
    }

    public static DiamondPolishEnum of(Long id) {
        for (DiamondPolishEnum value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }
}
