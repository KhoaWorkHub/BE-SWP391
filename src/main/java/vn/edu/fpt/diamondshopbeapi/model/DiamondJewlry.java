package vn.edu.fpt.diamondshopbeapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DIAMOND_JEWLRY")
public class DiamondJewlry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ids", nullable = false)
    private Long id;

    private Long diamondId;

    private Long jewlryId;

    private OffsetDateTime createdAt;

    private OffsetDateTime upDateAt;
}
