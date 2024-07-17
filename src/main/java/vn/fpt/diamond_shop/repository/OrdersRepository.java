package vn.fpt.diamond_shop.repository;

import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.fpt.diamond_shop.model.Orders;
import vn.fpt.diamond_shop.response.OrdersListAllUser;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {


    Page<Orders> findAllOrderByOrderByCreatedAtDesc(Pageable pageable);

    Page<Orders> findAllOrderByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    List<Orders> findAllByOrderByCreatedAtDesc();

    Page<Orders> findAllOrderByCustomerIdAndStatusOrderByCreatedAtDesc(Long customerId, String status, Pageable pageable);

    Page<Orders> findAllOrderByCustomerIdOrderByCreatedAtDesc(Long customerId,  Pageable pageable);

    @Query("SELECT SUM(o.totalPrice) FROM Orders o WHERE o.customerId = :customerId AND o.status = :status")
    Long getCustomerAmount(Long customerId, String status);

    List<Orders> findAllOrderByStatusOrderByCreatedAtDesc(String status);

    @Query("SELECT SUM(o.totalPrice) FROM Orders o WHERE  1= 1 AND (:status is null or o.status = :status)")
    Long getTotalStatusAmount(String status);

    Optional<Orders> findByUniqueOrderId(String uniqueOrderId);

    @Query(value = "SELECT new vn.fpt.diamond_shop.response.OrdersListAllUser(" +
            "o.id as id," +
            "o.uniqueOrderId as uniqueOrderId," +
            "o.orderDate as orderDate," +
            "o.status as status," +
            "o.customerId as customerId," +
            "o.totalPrice as totalPrice," +
            "o.createdAt as createdAt," +
            "o.updatedAt as updatedAt," +
            "eu.phoneNumber as phoneNumber  " +
            ")FROM Orders as o left join EndUser as eu on (o.customerId = eu.accountId)" +
            " WHERE  1= 1 " +
            "AND (:status is null or o.status = :status)" +
            "AND (:phoneNumber is null or eu.phoneNumber = :phoneNumber)" +
            "AND (:uniqueOrderId is null or o.uniqueOrderId = :uniqueOrderId)")
    List<OrdersListAllUser> searchAllOrders(
            @Param("status") String status,
            @Param("uniqueOrderId") String uniqueOrderId,
            @Param("phoneNumber") String phoneNumber
    );
    @Query(value  = "SELECT new vn.fpt.diamond_shop.response.OrdersListAllUser(" +
            " o.id as id," +
            "o.uniqueOrderId as uniqueOrderId," +
            "o.orderDate as orderDate," +
            "o.status as status," +
            "o.customerId as customerId," +
            "o.totalPrice as totalPrice," +
            "o.createdAt as createdAt," +
            "o.updatedAt as updatedAt," +
            "eu.phoneNumber as phoneNumber  " +
            ") FROM Orders as o left join EndUser as eu on (o.customerId = eu.accountId)" +
            " WHERE  1= 1 " +
            "AND (:status is null or o.status = :status)" +
            "AND (:phoneNumber is null or eu.phoneNumber = :phoneNumber)" +
            "AND (:uniqueOrderId is null or o.uniqueOrderId = :uniqueOrderId)")
    Page<OrdersListAllUser> searchAllOrders(
            @Param("status") String status,
            @Param("uniqueOrderId") String uniqueOrderId,
            @Param("phoneNumber") String phoneNumber,
            Pageable pageable
    );
}
