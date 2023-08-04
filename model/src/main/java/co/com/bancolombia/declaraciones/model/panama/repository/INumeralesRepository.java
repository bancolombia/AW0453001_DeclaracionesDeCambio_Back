package co.com.bancolombia.declaraciones.model.panama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.com.bancolombia.declaraciones.model.panama.entity.administracion.Numerales;
import java.util.List;


@Repository
public interface INumeralesRepository extends JpaRepository<Numerales, Integer> {
    Numerales findById(Integer id);

    List<Numerales> findAllByTransmisionBanRep(Character transmisionBanRep);
    Numerales findByCodigo(Integer codigo);
}