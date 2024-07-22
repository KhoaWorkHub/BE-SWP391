package vn.fpt.diamond_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fpt.diamond_shop.model.Color;
import vn.fpt.diamond_shop.model.Origin;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {
}