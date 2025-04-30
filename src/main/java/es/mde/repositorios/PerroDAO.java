package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.mde.externas.Perro;

@Repository
public interface PerroDAO extends JpaRepository<Perro, Long> {

}

