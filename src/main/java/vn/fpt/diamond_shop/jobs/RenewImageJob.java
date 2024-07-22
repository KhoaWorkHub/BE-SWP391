package vn.fpt.diamond_shop.jobs;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.fpt.diamond_shop.model.Image;
import vn.fpt.diamond_shop.repository.ImageRepository;
import vn.fpt.diamond_shop.response.ImageInformation;
import vn.fpt.diamond_shop.service.ImageService;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Log4j2
public class RenewImageJob {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    //run every 4 days
    @Scheduled(cron = "0 0 0 */4 * ?")
    public void renewImageJob() {
        renewImageTask();
    }

    public void renewImageTask() {
        log.info("Start renew image task");
        List<Image> images = imageRepository.findAll();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        images.parallelStream().forEach(e -> {
            try {
                ImageInformation imageInformation = imageService.get(e.getImageName());
                e.setUrl(imageInformation.getUrl());
                e.setUpdateAt(OffsetDateTime.now(ZoneId.of("UTC")));
                atomicInteger.incrementAndGet();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
        imageRepository.saveAll(images);
        log.info("Renew image task completed, total image renewed: {}", atomicInteger.get());
    }

}
