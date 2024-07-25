package vn.fpt.diamond_shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.fpt.diamond_shop.model.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    Page<OrderDetail> findAllByCustomerIdAndStatusOrderByCreatedAtDesc(Long customerId, String status, Pageable pageable);

    List<OrderDetail> findAllByCustomerIdAndStatus(Long customerId, String status);

    Page<OrderDetail> findAllByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);

    List<OrderDetail> findAllByUniqueOrderId(String uniqueOrderId);

    @Query("SELECT SUM(o.totalPrice) FROM OrderDetail o WHERE o.saleId = :customerId AND (:status is null or o.status = :status)")
    Long getSaleInfo(Long customerId, String status);

    @Query("SELECT DISTINCT o.uniqueOrderId FROM OrderDetail o WHERE o.saleId = :customerId AND (:status is null or o.status = :status)")
    List<String> getOrderIdBySaleId(Long customerId, String status);

    @Query("SELECT DISTINCT o.saleId FROM OrderDetail o WHERE 1=1 AND (:customerId is null or o.saleId = :customerId) AND (:status is null or o.status = :status)")
    List<Long> getListSaleId(Long customerId, String status);
}
