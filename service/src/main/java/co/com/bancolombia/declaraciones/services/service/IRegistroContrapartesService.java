package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.dto.RegistroContrapartesDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Collection;
import java.util.List;


public interface IRegistroContrapartesService {


	Page<RegistroContrapartesDTO> consultarRegistrosContrapartes(Collection<String> numerosCuentasCompensacion, Pageable pageable) throws PersistenceException, TechnicalException;

	List<RegistroContrapartesDTO> consultarRegistrosContrapartes(Collection<String> numerosCuentasCompensacion) throws PersistenceException,TechnicalException;

	RegistroContrapartesDTO guardarRegistrosContrapartes(RegistroContrapartesDTO registroContrapartesDTO) throws PersistenceException, TechnicalException;

	Boolean eliminarRegistrosContrapartes(Long id) throws PersistenceException, TechnicalException;

	RegistroContrapartesDTO editarRegistrosContrapartes(RegistroContrapartesDTO registroContrapartesDTO) throws PersistenceException, TechnicalException;

}
