package com.wendo.bank.config;

import com.wendo.bank.interceptor.CustomJPAInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyInterceptorRegistration implements HibernatePropertiesCustomizer {

    @Autowired
    private CustomJPAInterceptor customJPAInterceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", customJPAInterceptor);
    }
}
