package vn.fpt.diamond_shop.constants;

public enum DiamondClarityEnum {
    IF(20),

    VVS1(21),

    VVS2(22),

    VS1(23),

    VS2(24);

    final long id;

    DiamondClarityEnum(long id) {
        this.id = id;
    }

    public static DiamondClarityEnum of(Long id) {
        for (DiamondClarityEnum value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }
}
