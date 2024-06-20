package vn.fpt.diamond_shop.service;

import org.springframework.web.multipart.MultipartFile;
<<<<<<< HEAD
import vn.fpt.diamond_shop.response.ImageInformation;

=======
import vn.fpt.diamond_shop.model.Jewelry;
import vn.fpt.diamond_shop.model.JewelryType;
import vn.fpt.diamond_shop.request.CreateDiamondRequest;
import vn.fpt.diamond_shop.request.GetListJewelryRequest;
import vn.fpt.diamond_shop.response.GetDetailJewelryResponse;
import vn.fpt.diamond_shop.response.ImageInformation;

import java.util.List;

>>>>>>> origin/Khang
public interface ImageService {
    ImageInformation push(MultipartFile multipartFile);

    ImageInformation get(String fileName);
}
