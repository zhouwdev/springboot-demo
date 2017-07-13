package springboot.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@MapperScan(basePackages = "com.springboot.core.mapper.1", sqlSessionFactoryRef = "sqlSessionFactory1")
class dataSourceConfig1 {

    @Primary
    @Bean(destroyMethod = "close", name = "datasource1")
    @ConfigurationProperties(prefix = "db.mysql_1")
    DataSource datasource1() {
        return new DruidDataSource();
    }

    @Primary
    @Bean(name = "dataSourceTransactionManager1")
    DataSourceTransactionManager dataSourceTransactionManager1(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "sqlSessionFactory1")
    SqlSessionFactory sqlSessionFactory1(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/1/*Mapper.xml"));
        return sessionFactory.getObject();
    }

}


@MapperScan(basePackages = "com.springboot.core.mapper.2", sqlSessionFactoryRef = "sqlSessionFactory2")
class dataSourceConfig2 {

    @Bean(destroyMethod = "close", name = "datasource2")
    @ConfigurationProperties(prefix = "db.mysql_2")
    DataSource datasource2() {
        return new DruidDataSource();
    }

    @Bean(name = "dataSourceTransactionManager2")
    DataSourceTransactionManager transDataSourceTransactionManager(@Qualifier("datasource2") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactory2")
    SqlSessionFactory sqlSessionFactory2(@Qualifier("datasource2") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/2/*Mapper.xml"));
        return sessionFactory.getObject();
    }

}