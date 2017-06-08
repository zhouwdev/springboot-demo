package springboot.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by zhouwei on 2017/6/5.
 */

/**
 * 数据库连接池配置
 */

@Configuration
@MapperScan(basePackages = "com.shangying.acctManage.core.mapper")
public class DataSourceConfig {
    @Autowired
    private Environment env;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("db.mysql.url"));
        dataSource.setUsername(env.getProperty("db.mysql.username"));
        dataSource.setPassword(env.getProperty("db.mysql.password"));
        dataSource.setMaxActive(Integer.valueOf(env.getProperty("db.mysql.maxActive")));
        dataSource.setInitialSize(0);
        dataSource.setMinIdle(0);
        dataSource.setMaxWait(600001);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnReturn(false);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(60000l);
        dataSource.setMinEvictableIdleTimeMillis(25200000l);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        dataSource.setLogAbandoned(true);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        //sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml"));
        return sessionFactory.getObject();
    }
}
