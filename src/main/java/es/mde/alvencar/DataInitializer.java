package es.mde.alvencar;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.mde.entidades.Vehiculo;
import es.mde.entidades.Venta;
import es.mde.entidades.Alquiler;
import es.mde.entidades.Cliente;
import es.mde.entidades.Transaccion;
import es.mde.repositorios.VehiculoDAO;
import es.mde.repositorios.ClienteDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Configuration
public class DataInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    CommandLineRunner initVehiculos(VehiculoDAO vehiculoRepo) {
        return args -> {

            vehiculoRepo.deleteAll();      
            System.out.println("🧹 Datos anteriores eliminados.");
           
            Vehiculo vehiculo1 = new Vehiculo(
                "1234ABC", "BAS12345678", "Toyota", "Corolla", "Rojo",
                LocalDate.of(2020, 5, 20), "Compra"
            );

            Venta venta1 = new Venta(
                4500.0f,
                vehiculo1,
                "Directo",
                LocalDate.of(2026, 5, 20)
            );

            vehiculo1.addTransaccion(venta1);

            Vehiculo vehiculo2 = new Vehiculo(
                "5678XYZ", "BAS87654321", "Ford", "Focus", "Azul",
                LocalDate.of(2019, 7, 15), "Leasing"
            );

            Alquiler alquiler1 = new Alquiler(
                320.0f,
                vehiculo2,
                LocalDateTime.of(2025, 5, 1, 10, 0),
                LocalDateTime.of(2025, 5, 3, 16, 30),
                15000f,
                15540f,
                0.75f,
                0.30f
            );

            vehiculo2.addTransaccion(alquiler1);

            Transaccion trans1 = new Transaccion(150.0f, vehiculo1);
            Transaccion trans2 = new Transaccion(75.5f, vehiculo1);
            Transaccion trans3 = new Transaccion(200.0f, vehiculo2);

            vehiculo1.addTransaccion(trans1);
            vehiculo1.addTransaccion(trans2);
            vehiculo2.addTransaccion(trans3);

            vehiculoRepo.save(vehiculo1);
            vehiculoRepo.save(vehiculo2);

            System.out.println("🚗 Datos de ejemplo cargados correctamente.");
        };
    }

    @Bean
    CommandLineRunner initClientes(ClienteDAO clienteRepo) {
        return args -> {

            clienteRepo.deleteAll();      
            System.out.println("🧹 Datos anteriores de clientes eliminados.");
           
            List<Cliente> clientes = List.of(
                new Cliente("12345678A", "Juan", "Pérez", "García", "Calle Falsa 123", "600123456"),
                new Cliente("87654321B", "Ana", "López", "Martínez", "Av. Principal 45", "600654321"),
                new Cliente("11223344C", "Luis", "Ramírez", "Sánchez", "C/ Mayor 10", "600789012")
            );
            clienteRepo.saveAll(clientes);

            System.out.println("Datos de ejemplo de clientes cargados correctamente.");
        };
    }

}