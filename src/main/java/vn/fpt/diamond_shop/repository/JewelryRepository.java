package vn.fpt.diamond_shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.fpt.diamond_shop.model.Jewelry;
import vn.fpt.diamond_shop.response.GetDetailJewelryResponse;
import vn.fpt.diamond_shop.response.GetListJewelryResponse;

import java.util.List;

@Repository
@Transactional
public interface JewelryRepository extends JpaRepository<Jewelry, Long> {

    @Query(value = "select new vn.fpt.diamond_shop.response.GetDetailJewelryResponse("+
            "j.id as id_jewelry,\n" +
            "j.name as jewelry_title,\n" +
            "j.jewelryCode as jewelry_code,\n" +
            "jt.jewelryTypeName as jewelry_type,\n" +
            "jt.id  as jewelry_type_id,\n" +
            "j.quantity as quantity,\n" +
            "j.materialPrices as price,\n" +
            "j.description as description,\n" +
            "j.imageId as image_id,\n" +
            "i.url as url,\n" +
            "jt.idGuide as id_guide, \n" +
            "d.id as diamond_id, \n" +
            "j.goldWeight as gold_weight "+
            ") FROM Jewelry as j left join  JewelryType as jt on (j.jewelryTypeId = jt.id) left join  Diamond as d on (j.idDiamond  = d.id) left join Image as i on (j.imageId  = i.id)"+
            " WHERE 1 = 1 and j.id = :id ")
    GetDetailJewelryResponse getDetailJewelry(@Param("id") Long id);

    @Query(value = "select new vn.fpt.diamond_shop.response.GetListJewelryResponse("+
            "j.id as id_jewelry,\n" +
            "j.name as jewelry_title,\n" +
            "jt.jewelryTypeName as jewelry_type,\n" +
            "jt.id  as jewelry_type_id,\n" +
            "j.quantity as quantity,\n" +
            "j.materialPrices as price,\n" +
            "j.imageId as image_id,\n" +
            "i.url as url\n" +
            ") FROM Jewelry as j left join  JewelryType as jt on (j.jewelryTypeId = jt.id) left join  Diamond as d on (j.idDiamond  = d.id) left join Image as i on (j.imageId  = i.id)"+
            " WHERE 1 = 1 ")
    Page<GetListJewelryResponse> getListJewelry(@Param("id") Long id, Pageable pageable);
}