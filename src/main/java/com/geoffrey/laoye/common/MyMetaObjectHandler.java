package com.geoffrey.laoye.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());
        if (metaObject.hasSetter("createTime")){
            metaObject.setValue("createTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("createUser")){
            metaObject.setValue("createUser",BaseContext.getCurrentId());

        }
        if (metaObject.hasSetter("updateUser")){
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
        }
        if (metaObject.hasSetter("orderTime")){
            metaObject.setValue("orderTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("checkoutTime")){
            metaObject.setValue("checkoutTime", LocalDateTime.now());
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());
        if (metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("updateUser")){
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
        }
    }
}
