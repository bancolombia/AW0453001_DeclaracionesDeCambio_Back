package co.com.bancolombia.declaraciones.model.panama.repository;

import co.com.bancolombia.declaraciones.model.panama.entity.RegistroContrapartes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;


@Repository
public interface IRegistroContrapartesRepository
        extends JpaRepository<RegistroContrapartes, String> {


    List<RegistroContrapartes> findAllByNumeroCuentaCompensacionIn(Collection<String> numerosCuentasCompensacion);

    Page<RegistroContrapartes> findAllByNumeroCuentaCompensacionIn(Collection<String> numerosCuentasCompensacion, Pageable pageable);

    void deleteByIdRegistroContraparte(Long idRegistroContraparte);


}
