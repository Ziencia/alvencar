package es.mde.alvencar;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.mde.entidades.*;
import es.mde.repositorios.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class DataInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    CommandLineRunner initData(ClienteDAO clienteRepo, VehiculoDAO vehiculoRepo, TransaccionDAO transaccionesRepo) {
    return args -> {

        transaccionesRepo.deleteAll();
        clienteRepo.deleteAll();
        vehiculoRepo.deleteAll();

        Cliente cliente1 = new Cliente("12345678A", "Juan", "Pérez", "García", "Calle Falsa 123", "600123456");
        Cliente cliente2 = new Cliente("87654321B", "Ana", "López", "Martínez", "Av. Principal 45", "600654321");
        Cliente cliente3 = new Cliente("11223344C", "Luis", "Ramírez", "Sánchez", "C/ Mayor 10", "600789012");
    
        Vehiculo vehiculo1 = new Vehiculo("1234ABC", "BAS12345678", "Toyota", "Corolla", "Rojo",
        LocalDate.of(2020, 5, 20), "Compra");
        Vehiculo vehiculo2 = new Vehiculo(
        "5678XYZ", "BAS87654321", "Ford", "Focus", "Azul",
        LocalDate.of(2019, 7, 15), "Leasing");

        List<Vehiculo> vehiculos = List.of(vehiculo1, vehiculo2);
        vehiculoRepo.saveAll(vehiculos);

        List<Cliente> clientes = List.of(cliente1, cliente2, cliente3);
        clienteRepo.saveAll(clientes);

        Transaccion venta1 = new Venta(vehiculo1, cliente1, 4500.0f, LocalDateTime.of(2025, 5, 10, 10, 00), "Directo",
                LocalDateTime.of(2027, 5, 20, 10, 00));
        vehiculo1.addTransaccion(venta1);
        cliente1.addTransaccion(venta1);

        Transaccion alquiler1 = new Alquiler(350f, vehiculo1, cliente1,
                LocalDateTime.of(2025, 5, 12, 10, 00), null, LocalDateTime.of(2025, 5, 14, 10, 00), 100f, 80f, 60f,
                60f);
        Transaccion alquiler2 = new Alquiler(600f, vehiculo1, cliente1,
                LocalDateTime.of(2025, 5, 12, 10, 00), null, LocalDateTime.of(2025, 5, 14, 10, 00), 100f, 80f, 60f,
                60f);
        vehiculo2.addTransaccion(alquiler1);
        vehiculo2.addTransaccion(alquiler2);
        cliente2.addTransaccion(alquiler1);
        cliente2.addTransaccion(alquiler2);

        List<Transaccion> transacciones = List.of(venta1,alquiler1,alquiler2);
        transaccionesRepo.saveAll(transacciones);
    };
}}