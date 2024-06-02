package vn.fpt.diamond_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fpt.diamond_shop.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}