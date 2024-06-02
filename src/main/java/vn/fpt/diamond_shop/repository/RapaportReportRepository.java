package vn.fpt.diamond_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.fpt.diamond_shop.model.RapaportReport;

import java.util.UUID;

public interface RapaportReportRepository extends JpaRepository<RapaportReport, Long> {

    @Query(value = "SELECT rr.percent FROM RapaportReport rr JOIN Clarity c JOIN Color cc WHERE " +
            "(:weight >= rr.caraF" +
            " AND :weight <= rr.caraT)" +
            " AND :#{c.clarity.name()} = :clarity" +
            " AND :#{cc.color.name()} = :color")
    Double findPricePercent(@Param("weight") Double weight,
                            @Param("color") String color,
                            @Param("clarity") String clarity);
}