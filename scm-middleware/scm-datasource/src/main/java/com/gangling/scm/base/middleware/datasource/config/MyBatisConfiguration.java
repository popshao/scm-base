package com.gangling.scm.base.middleware.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties({DruidDataSourcePropertiesConfig.class})
public class MyBatisConfiguration implements EnvironmentAware {

    private Environment environment;

    @Autowired
    private DruidDataSourcePropertiesConfig properties;

    @Value("${publicKey}")
    private String publicKey;

    @Bean
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setKeepAlive(true);
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());
        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setMaxActive(properties.getMaxActive());
        druidDataSource.setMaxWait(properties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(properties.getValidationQuery());
        druidDataSource.setTestWhileIdle(properties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(properties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(properties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
        Properties prop = new Properties();
        prop.setProperty("config.decrypt", "true");
        prop.setProperty("config.decrypt.key", publicKey);
        druidDataSource.setConnectProperties(prop);
        try {
            druidDataSource.setFilters(properties.getFilters());
            druidDataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }
//
//    @Bean
//    public DataSourceTransactionManager dataSourceTransactionManager(
//            DataSource dataSource) {
//        DataSourceTransactionManager manager = new DataSourceTransactionManager();
//        manager.setDataSource(dataSource);
//        return manager;
//    }

    /**
     * 也可以写在 application.yml 里面或者配置中心写
     * mybatis:
     * mapper-locations:
     * - mappers/ProductMapper.xml
     * config-location:
     * classpath:mybatis-config.xml
     * configuration:
     * interceptors:
     * - com.*.MybatisTraceSelectInterceptor
     * 放到application.yml中可能要写多份
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
        sfb.setDataSource(dataSource);
        sfb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(environment.getProperty("mybatis.mapper-locations", "classpath:mapper/**/*.xml")));
        org.apache.ibatis.session.Configuration configuration = sfb.getObject().getConfiguration();
        configuration.setDefaultFetchSize(1000);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        return sfb.getObject();
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
