package vn.edu.fpt.diamondshopbeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.diamondshopbeapi.model.Diamond;

import java.util.Optional;

@Repository
public interface DiamondRepository extends JpaRepository<Diamond, Long> {

    Optional<Diamond> findDiamondByIdIsNotNullAndNameIs(String name);
}
