package com.wendo.bank.interceptor;

import com.wendo.bank.enitity.MetaEntity;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class CustomJPAInterceptor extends EmptyInterceptor {

    @Value("${spring.jackson.time-zone}")
    private String zone;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if(entity instanceof MetaEntity) {
            MetaEntity obj = (MetaEntity) entity;
            if(obj.getId() == null) {
                obj.setCreatedTimestamp(ZonedDateTime.now(ZoneId.of(zone)));
                obj.setUpdatedTimestamp(ZonedDateTime.now(ZoneId.of(zone)));
            } else {
                obj.setUpdatedTimestamp(ZonedDateTime.now(ZoneId.of(zone)));
            }
        }
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object [] previousState,
                                String[] propertyNames, Type[] types) {

        if (entity instanceof MetaEntity) {
            ((MetaEntity) entity).setUpdatedTimestamp(ZonedDateTime.now(ZoneId.of(zone)));
        }
        return super.onFlushDirty(entity, id, currentState,
                previousState, propertyNames, types);
    }
}