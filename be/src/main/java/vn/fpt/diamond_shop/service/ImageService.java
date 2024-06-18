package vn.fpt.diamond_shop.service;

import org.springframework.web.multipart.MultipartFile;
import vn.fpt.diamond_shop.response.ImageInformation;

public interface ImageService {
    ImageInformation push(MultipartFile multipartFile);
}
