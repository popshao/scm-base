package com.gangling.scm.base.middleware.datasource.interceptor;

import org.apache.ibatis.session.SqlSessionFactory;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InterceptorListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private QueryVersionInterceptor queryWorldVersionInterceptor;
    @Autowired
    private UpdateVersionInterceptor updateWorldVersionInterceptor;
    @Autowired
    private UpdateStockInterceptor updateStockInterceptor;
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(updateStockInterceptor);
            sqlSessionFactory.getConfiguration().addInterceptor(queryWorldVersionInterceptor);
            sqlSessionFactory.getConfiguration().addInterceptor(updateWorldVersionInterceptor);
        }
    }
}
