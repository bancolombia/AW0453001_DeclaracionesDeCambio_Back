package co.com.bancolombia.declaraciones.model.panama.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.com.bancolombia.declaraciones.model.panama.entity.RegistroDigitosVerificacion;


@Repository
public interface IRegistroDigitosVerificacionRepository
        extends JpaRepository<RegistroDigitosVerificacion, String> {


    List<RegistroDigitosVerificacion> findAllByNumeroCuentaCompensacionIn(Collection<String> numerosCuentasCompensacion);

    Page<RegistroDigitosVerificacion> findAllByNumeroCuentaCompensacionIn(Collection<String> numerosCuentasCompensacion, Pageable pageable);

    void deleteById(Long id);

    List<RegistroDigitosVerificacion> findAllByNitAndNumeroCuentaCompensacion(String nit , String nroCtaCompensacion);

}
