package sv.edu.udb.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;
@Configuration
@EnableTransactionManagement
public class HibernateConnectionConfig {
    private final String dbDriver;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String hibernateDdlAuto;
    private final String hibernateDialect;
    public HibernateConnectionConfig(final @Value("${db.driver}") String dbDriver,
                                     final @Value("${db.url}") String dbUrl,
                                     final @Value("${db.username}") String dbUsername,
                                     final @Value("${db.password}") String dbPassword,
                                     final @Value("${config.hibernate.hbm2ddl.auto}") String
                                             hibernateDdlAuto,
                                     final @Value("${config.hibernate.dialect}") String
                                             hibernateDialect) {
        this.dbDriver = Objects.requireNonNull(dbDriver);
        this.dbUrl = Objects.requireNonNull(dbUrl);
        this.dbUsername = Objects.requireNonNull(dbUsername);
        this.dbPassword = Objects.requireNonNull(dbPassword);
        this.hibernateDdlAuto = Objects.requireNonNull(hibernateDdlAuto);
        this.hibernateDialect = Objects.requireNonNull(hibernateDialect);
    }
    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("sv.edu.udb.repository.domain");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName(this.dbDriver)
                .url(this.dbUrl)
                .username(this.dbUsername)
                .password(this.dbPassword)
                .build();
    }
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        final HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
    private final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", hibernateDdlAuto);
        hibernateProperties.setProperty(
                "hibernate.dialect", hibernateDialect);
        return hibernateProperties;
    }
}
