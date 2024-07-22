package vn.fpt.diamond_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.fpt.diamond_shop.model.Diamond;

import java.util.UUID;

public interface DiamondRepository extends JpaRepository<Diamond, Long> {

     @Query("SELECT\n" +
             "\tp.addedPrice  + c.addedPrice + c2.addedPrice + c3.addedPrice + o.addedPrice + s.addedPrice  + c.wage  \n" +
             "FROM\n" +
             "\t Diamond d\n" +
             "LEFT JOIN Polish p on (d.polishId = p.id)\n" +
             "LEFT JOIN Cut c  on (d.cutId = c.id)\n" +
             "LEFT JOIN Clarity c2  on (d.clarityId = c2.id)\n" +
             "LEFT JOIN Color c3 on (d.colorId = c3.id)\n" +
             "LEFT JOIN Origin o  on (d.originId  = o.id)\n" +
             "LEFT JOIN Shape s   on (d.shapeId  = s.id)\n" +
             "WHERE d.id = :diamondId")
     Long getPrice(Long diamondId);

     Diamond findAllByName(String name);
}