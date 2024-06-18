package vn.edu.fpt.diamondshopbeapi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.fpt.diamondshopbeapi.config.AppConfig;

@Service("mysql")
public class MysqlData implements Database {
    @Autowired
    AppConfig appConfig;

    @Override
    public void createDatabase() {
        appConfig.getAbc();
    }

    @Override
    public void createTable() {

    }

    @Override
    public void deleteTable() {

    }
}
