package vn.fpt.diamond_shop.constants;

import lombok.Getter;

@Getter
public enum DiamondShapeEnum {

    ROUND(10, 1),
    PRINCESS(11, 2),
    OVAL(12, 3),
    MARQUISE(13, 4),
    HEART(14, 5),
    EMERALD(15, 6),
    PEAR(17, 7),
    ASSHER(18, 8),
    CUSHION(19, 9),
    RADIANT(20, 10);

    private final int price;

    private final long id;

    DiamondShapeEnum(int price, long id) {
        this.price = price;
        this.id = id;
    }


    public static DiamondShapeEnum of(Long id) {
        for (DiamondShapeEnum value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

}
