package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.dto.RegistroDigitosVerificacionDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Collection;
import java.util.List;


public interface IRegistroDigitosVerificacionService {


    Page<RegistroDigitosVerificacionDTO> consultarRegistroDigitosVerificacion(Collection<String> numerosCuentasCompensacion, Pageable pageable) throws TechnicalException,PersistenceException;

    List<RegistroDigitosVerificacionDTO> consultarRegistroDigitosVerificacion(Collection<String> numerosCuentasCompensacion) throws TechnicalException, PersistenceException, BusinessException;

    RegistroDigitosVerificacionDTO guardarRegistroDigitosVerificacion(RegistroDigitosVerificacionDTO registroDigitosVerificacionDTO) throws TechnicalException, PersistenceException;

    Boolean eliminarRegistroDigitosVerificacion(Long id) throws TechnicalException, PersistenceException;

    RegistroDigitosVerificacionDTO editarRegistroDigitosVerificacion(RegistroDigitosVerificacionDTO registroDigitosVerificacionDTO) throws TechnicalException, PersistenceException;

    RegistroDigitosVerificacionDTO consultarRegistroDigitosVerificacionByNitAndNroCtaCompensacion(String nit, String nroCtaCompensacion) throws TechnicalException, PersistenceException;

}
