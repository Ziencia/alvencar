package es.mde.alvencar;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:config/rest.properties", "classpath:config/jackson.properties",
		"classpath:config/gestionBBDD.properties", "classpath:config/passwordsBD.properties"
})
@EnableJpaRepositories("${misRepositorios}") // leer valor de propiedades? pero solo para las entidades anotadas
@ComponentScan({ "es.mde.repositorios", "es.mde.rest", "es.mde.service" })
public class ConfiguracionPorJava {

	@Value("${misEntidades}")
	String entidades;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env,
			JpaVendorAdapter vendorAdapter) {

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		// JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter(); // O
		// pedirlo como parametro y que haga el Autowired
		em.setJpaVendorAdapter(vendorAdapter);

		em.setPackagesToScan(entidades); // leer valor de propiedades? pero solo para las entidades anotadas
		em.setMappingResources("jpa/Factura.orm.xml","jpa/Oferta.orm.xml", "jpa/Cliente.orm.xml",
								"jpa/Vehiculo.orm.xml","jpa/Transaccion.orm.xml");
		Properties jpaProperties = new Properties();
		Arrays.asList("dialect", "show_sql", "hbm2ddl.auto", "enable_lazy_load_no_trans") // leer valor de para las
																							// entidades anotadas
				.stream().map(s -> "hibernate." + s)
				.map(p -> new AbstractMap.SimpleEntry<String, String>(p, env.getProperty(p)))
				.filter(e -> e.getValue() != null).forEach(e -> jpaProperties.put(e.getKey(), e.getValue()));
		em.setJpaProperties(jpaProperties);

		return em;
	}

	@Bean
	public EntityManager entityManager(EntityManagerFactory emf) {
		return emf.createEntityManager();
	}
}
