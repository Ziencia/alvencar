package es.mde.alvencar;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import es.mde.entidades.*;
import es.mde.repositorios.*;


@SpringBootApplication
public class alvencarApplication {

	private static final Logger log = LoggerFactory.getLogger(alvencarApplication.class);

	public static void main(String[] args) {
		//SpringApplication.run(alvencar.class, args);
		


		ConfigurableApplicationContext context = SpringApplication.run(alvencar.class, args);

		System.err.println("Está funcionando la aplicación");
		log.debug("Está funcionando la aplicación");

		/* 
		// Creando Transacciones
		TransaccionDAO transaccionDAO = context.getBean(TransaccionDAO.class);
		Transaccion transaccion = new Transaccion(100);
		transaccionDAO.save(transaccion);
		List<Transaccion> transacciones = transaccionDAO.findAll();
		transacciones.stream().map(Transaccion::toString).forEach(log::info);

		// Creando Vehiculos
		VehiculoDAO vehiculoDAO = context.getBean(VehiculoDAO.class);
		Vehiculo vehiculo = new Vehiculo("1234ABC","nbastidor","Toyota","Corolla","Rojo",LocalDate.now(),"Nuevo");
		vehiculoDAO.save(vehiculo);
		List<Vehiculo> vehiculos = vehiculoDAO.findAll();
		vehiculos.stream().map(Vehiculo::toString).forEach(log::info);
		*/

		TransaccionDAO transaccionDAO = context.getBean(TransaccionDAO.class);
		transaccionDAO.getTransaccionesDeMatricula("1234ABC").stream().map(Transaccion::toString).forEach(log::error);
		
		System.err.println("Está cerrándose la aplicación");
		//context.close();
	}

}
