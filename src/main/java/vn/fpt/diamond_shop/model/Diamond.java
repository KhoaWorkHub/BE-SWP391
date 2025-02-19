package vn.fpt.diamond_shop.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "DIAMOND")
@Data
@NoArgsConstructor
public class Diamond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "carat")
    private int carat;
    @Column(name = "price")
    private Long price;
    @Column(name = "clarity_id")
    private Long clarityId;
    @Column(name = "cut_id")
    private Long cutId;
    @Column(name = "polish_id")
    private Long polishId;
    @Column(name = "color_id")
    private Long colorId;
    @Column(name = "shape_id")
    private Long shapeId;

    @Column(name = "origin_id")
    private Long originId;

    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private OffsetDateTime updateAt;

    @Column(name = "name")
    private String name;

    @Column(name = "price_diamond")
    private Long priceDiamond;

    @Column(name = "profit")
    private Long profit;

    // New fields for names
    @Transient
    private String clarityName;
    @Transient
    private String cutName;
    @Transient
    private String polishName;
    @Transient
    private String colorName;
    @Transient
    private String shapeName;
    @Transient
    private String originName;
    public Diamond(int carat, Long clarityId, Long cutId, Long polishId, Long colorId, Long shapeId) {
        this.carat = carat;
        this.clarityId = clarityId;
        this.cutId = cutId;
        this.polishId = polishId;
        this.colorId = colorId;
        this.shapeId = shapeId;
    }
}
