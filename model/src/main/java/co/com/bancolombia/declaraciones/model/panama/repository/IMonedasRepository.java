package co.com.bancolombia.declaraciones.model.panama.repository;

import co.com.bancolombia.declaraciones.model.panama.entity.administracion.Monedas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface IMonedasRepository extends JpaRepository<Monedas, Integer> {
    List<Monedas> findAllByOrderByCodigoAsc();

    Monedas findByCodigo(String codigo);
}