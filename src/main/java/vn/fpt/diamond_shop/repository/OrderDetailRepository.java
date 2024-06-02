package vn.fpt.diamond_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fpt.diamond_shop.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}