package vn.edu.fpt.diamondshopbeapi.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vn.edu.fpt.diamondshopbeapi.exception.AppException;
import vn.edu.fpt.diamondshopbeapi.model.Diamond;
import vn.edu.fpt.diamondshopbeapi.repository.DiamondRepository;
import vn.edu.fpt.diamondshopbeapi.service.Database;
import vn.edu.fpt.diamondshopbeapi.service.IDiamondService;

import java.util.Optional;

@Service
public class DiamondService implements IDiamondService {

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    @Qualifier("posgresData")
    private Database database;

    @Override
    public Diamond getById(Long id) {
        try {
            Optional<Diamond> diamond = diamondRepository.findById(id);
            diamond.orElse(new Diamond());
            boolean isNull = diamond.isPresent();
            if (!isNull) {
                throw new RuntimeException("Diamond not found");
            }
        } catch (RuntimeException exception) {
            //logic
            //logging
            //call api noti
            throw new AppException("Diamond not found");
        }

        return null;
    }


}
