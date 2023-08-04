package co.com.bancolombia.declaraciones.model.panama.repository;

import co.com.bancolombia.declaraciones.model.panama.entity.administracion.FormularioPDF;
import co.com.bancolombia.declaraciones.model.panama.entity.administracion.TiposIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITiposIdentificacionRepository
        extends JpaRepository<TiposIdentificacion, Integer> {

    TiposIdentificacion findByCodigo(String codigo);
}
