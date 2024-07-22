package vn.fpt.diamond_shop.constants;

import org.checkerframework.checker.units.qual.K;

public enum DiamondColorEnum {
    D(17),

    E(18),

    F(19),

    G(20),

    H(21),

    I(22),

    J(23),

    K(24),

    M(25),

    L(26);

    final long id;

    DiamondColorEnum(long id) {
        this.id = id;
    }

    public static DiamondColorEnum of(Long id) {
        for (DiamondColorEnum value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }
}
