package vn.fpt.diamond_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fpt.diamond_shop.model.Cart;
import vn.fpt.diamond_shop.response.ListCartResponse;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "select new vn.fpt.diamond_shop.response.ListCartResponse("+
            "c.id as id,\n" +
            "c.status as status,\n" +
            "c.quantity as quantity_number,\n" +
            "c.createdAt as created_at,\n" +
            "c.createdBy as created_by,\n" +
            "j.name as jewelry_title,\n" +
            "j.materialPrices as price_items,\n" +
            "i.url as image_url " +
            ") FROM \n" +
            "Cart c \n" +
            "left join Jewelry j on\n" +
            "(j.id = c.jewelryId)\n" +
            "left join JewelryType  as jt on\n" +
            "(j.jewelryTypeId = jt.id)\n" +
            "left join Image as i on\n" +
            "(j.imageId = i.id)\n"+
            " WHERE 1 = 1 " +
            "AND c.userId = :userId ")
    List<ListCartResponse> getListCartResponse(@Param("userId") Long userId);
}