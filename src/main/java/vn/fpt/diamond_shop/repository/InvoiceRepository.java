package vn.fpt.diamond_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fpt.diamond_shop.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Invoice findByOrderId(String orderId);
}