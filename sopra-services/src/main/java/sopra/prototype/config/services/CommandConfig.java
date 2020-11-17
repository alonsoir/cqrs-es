package sopra.prototype.config.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@ConfigurationProperties()
@PropertySource({"classpath:application-command.properties"})
//TODO no se porqué, pero no estoy cargando este properties, es como si al no encontrarlo, spring cargara un
// application.properties por defecto y yo quiero poder cargar este application-command.properties...
// confirmado, está cargando el application.properties que está en sopra-spring.
// Carga el que está en sopra-spring y tambien carga este. Si encuentra primero una propiedad en el sopra-spring,
// carga ese valor, aunque haya uno en el application-command.properties.
// Voy a desactivar todas las propiedades de bases de datos del general para que los valores útiles sean de
// application-command.properties.
@EnableJpaRepositories(
        basePackages = "sopra.prototype.repositories",
        entityManagerFactoryRef = "userCommandEntityManager",
        transactionManagerRef = "userCommandTransactionManager"
)
@ComponentScan("sopra.prototype.user.services")
@ComponentScan("sopra.prototype.command.services")
public class CommandConfig {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userCommandEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(userCommandDataSource());
        //"sopra.prototype.repositories"
        em.setPackagesToScan(new String[] { "sopra.prototype.vo","sopra.prototype.repositories"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource userCommandDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.commands.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.commands.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.commands.password"));

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager userCommandTransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(userCommandEntityManager().getObject());
        return transactionManager;
    }
}
