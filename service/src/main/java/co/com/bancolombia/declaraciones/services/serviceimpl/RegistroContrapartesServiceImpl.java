package co.com.bancolombia.declaraciones.services.serviceimpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import co.com.bancolombia.declaraciones.libcommons.enums.ErrorEnum;
import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.bancolombia.declaraciones.libcommons.dto.RegistroContrapartesDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.ErrorCodes;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import co.com.bancolombia.declaraciones.model.panama.entity.RegistroContrapartes;
import co.com.bancolombia.declaraciones.model.panama.repository.IRegistroContrapartesRepository;
import co.com.bancolombia.declaraciones.model.panama.util.mappers.IRegistroContrapartesMapper;
import co.com.bancolombia.declaraciones.services.service.IRegistroContrapartesService;

import javax.persistence.QueryTimeoutException;

/**
 * The type Registro contrapartes service.
 *
 * @author 1627690
 */
@Service
public class RegistroContrapartesServiceImpl implements IRegistroContrapartesService {

    /**
     * The Registro contrapartes repository.
     */
    @Autowired
    private IRegistroContrapartesRepository registroContrapartesRepository;

    public static final Logger logger = Logger.getLogger(RegistroContrapartesServiceImpl.class);

    private static final IRegistroContrapartesMapper registroContrapartesMapper = IRegistroContrapartesMapper.INSTANCE;


    @Override
    public List<RegistroContrapartesDTO> consultarRegistrosContrapartes(Collection<String> numerosCuentasCompensacion)
            throws PersistenceException, TechnicalException {
        try {
            List<RegistroContrapartes> list = registroContrapartesRepository
                    .findAllByNumeroCuentaCompensacionIn(numerosCuentasCompensacion);
            return registroContrapartesMapper.toDTOs(list);
        } catch (JDBCConnectionException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTES: Error en el servicio consultarRegistrosContrapartes(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.RC_ERROR_CONSULTING_COUNTER_PART) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    /**
     * Consultar registros contrapartes page.
     *
     * @param numerosCuentasCompensacion the numero cuenta compensacion
     * @return the page
     */
    @Override
    public Page<RegistroContrapartesDTO> consultarRegistrosContrapartes(Collection<String> numerosCuentasCompensacion,
                                                                        Pageable pageable) throws PersistenceException, TechnicalException {
        try {
            Page<RegistroContrapartes> registroContrapartesPage = registroContrapartesRepository
                    .findAllByNumeroCuentaCompensacionIn(numerosCuentasCompensacion, pageable);
            List<RegistroContrapartesDTO> listDto = registroContrapartesPage.getContent().stream()
                    .map(registroContrapartesMapper::toDTO).collect(Collectors.toList());
            return new PageImpl<>(listDto, pageable, registroContrapartesPage.getTotalElements());
        } catch (JDBCConnectionException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTES: Error en el servicio consultarRegistrosContrapartes(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.RC_ERROR_CONSULTING_COUNTER_PART_PAGEABLE) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);

        }
    }

    @Transactional
    @Override
    public RegistroContrapartesDTO guardarRegistrosContrapartes(RegistroContrapartesDTO registroContrapartesDTO) throws PersistenceException, TechnicalException {
        try {
            RegistroContrapartes registroContrapartes = registroContrapartesMapper.toEntity(registroContrapartesDTO);
            RegistroContrapartes registroContrapartesRes = registroContrapartesRepository.save(registroContrapartes);
            return registroContrapartesMapper.toDTO(registroContrapartesRes);
        } catch (JDBCConnectionException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTES: Error en el servicio guardarRegistrosContrapartes(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.RC_ERROR_SAVE_COUNTER_PART) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);

        }
    }

    @Transactional
    @Override
    public RegistroContrapartesDTO editarRegistrosContrapartes(RegistroContrapartesDTO registroContrapartesDTO) throws PersistenceException, TechnicalException {
        try {
            RegistroContrapartes registroContrapartes = registroContrapartesMapper.toEntity(registroContrapartesDTO);
            RegistroContrapartes registroContrapartesRes = null;
            if (registroContrapartes.getIdRegistroContraparte() != null && registroContrapartes.getIdRegistroContraparte() > 0) {
                registroContrapartesRes = registroContrapartesRepository.save(registroContrapartes);
            }
            return registroContrapartesMapper.toDTO(registroContrapartesRes);
        } catch (JDBCConnectionException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTES: Error en el servicio editarRegistrosContrapartes(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.RC_ERROR_EDIT_COUNTER_PART) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }


    /**
     * Eliminar registros contrapartes.
     *
     * @param id the id
     */
    @Transactional
    @Override
    public Boolean eliminarRegistrosContrapartes(Long id) throws PersistenceException, TechnicalException {
        try {
            registroContrapartesRepository.deleteByIdRegistroContraparte(id);
            return (Boolean.TRUE);
        } catch (JDBCConnectionException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REGISTROCONTRAPARTEREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTES: Error en el servicio eliminarRegistrosContrapartes(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.RC_ERROR_DELETE_COUNTER_PART) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }
}
