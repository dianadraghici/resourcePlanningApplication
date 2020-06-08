package dania.app.web.integrationTests.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@TestPropertySource("classpath:application-test.properties")
@EnableJpaRepositories(basePackages = "dania.app.web.repository")
@EnableTransactionManagement
@EnableSpringDataWebSupport
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class TestDatabaseConfig {

    @Resource
    private Environment environment;

    private DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("test-db-schema.sql")
                .build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        return getEMFactoryBean(getDataSource(), TestConstants.ENTITIES_PACKAGE);
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private LocalContainerEntityManagerFactoryBean getEMFactoryBean(DataSource dataSource, String... packagesToScan) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactoryBean.setPackagesToScan(packagesToScan);
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaPropertyMap(getJPAProperties());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean;
    }

    private Map<String, Object> getJPAProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(TestConstants.HIBERNATE_SHOW_SQL, getEnvironmentProperty(TestConstants.HIBERNATE_SHOW_SQL));
        properties.put(TestConstants.HIBERNATE_DIALECT, getEnvironmentProperty(TestConstants.HIBERNATE_DIALECT));
        properties.put(TestConstants.HIBERNATE_HBM2DDL_AUTO,
                getEnvironmentProperty(TestConstants.HIBERNATE_HBM2DDL_AUTO));

        return properties;
    }

    private String getEnvironmentProperty(String key) {
        return environment.getProperty(key);
    }
}
