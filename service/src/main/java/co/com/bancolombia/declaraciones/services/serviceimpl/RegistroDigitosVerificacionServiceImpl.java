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

import co.com.bancolombia.declaraciones.libcommons.dto.RegistroDigitosVerificacionDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.ErrorCodes;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import co.com.bancolombia.declaraciones.model.panama.entity.RegistroDigitosVerificacion;
import co.com.bancolombia.declaraciones.model.panama.repository.IRegistroDigitosVerificacionRepository;
import co.com.bancolombia.declaraciones.model.panama.util.mappers.IRegistroDigitosVerificacionMapper;
import co.com.bancolombia.declaraciones.services.service.IRegistroDigitosVerificacionService;

import javax.persistence.QueryTimeoutException;

@Service
public class RegistroDigitosVerificacionServiceImpl implements IRegistroDigitosVerificacionService {

    @Autowired
    private IRegistroDigitosVerificacionRepository registroDigitosVerificacionRepository;
    public static final Logger logger = Logger.getLogger(RegistroDigitosVerificacionServiceImpl.class);

    private static final IRegistroDigitosVerificacionMapper registroDigitosVerificacionMapper = IRegistroDigitosVerificacionMapper.INSTANCE;


    @Override
    public List<RegistroDigitosVerificacionDTO> consultarRegistroDigitosVerificacion(
            Collection<String> numerosCuentasCompensacion) throws TechnicalException, PersistenceException {
        try {
            List<RegistroDigitosVerificacion> list = registroDigitosVerificacionRepository
                    .findAllByNumeroCuentaCompensacionIn(numerosCuentasCompensacion);
            return registroDigitosVerificacionMapper.toDTOs(list);
        } catch (JDBCConnectionException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION: Error en el servicio consultarRegistroDigitosVerificacion(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.DV_ERROR_CONSULTING_CHECK_DIGIT) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);

        }
    }

    /**
     * Consultar registro digitos verificacion page.
     *
     * @param numerosCuentasCompensacion the numero cuenta compensacion
     * @return the page
     */
    @Override
    public Page<RegistroDigitosVerificacionDTO> consultarRegistroDigitosVerificacion(
            Collection<String> numerosCuentasCompensacion, Pageable pageable) throws TechnicalException, PersistenceException {
        try {
            Page<RegistroDigitosVerificacion> registroDigitosVerificacionPage = registroDigitosVerificacionRepository
                    .findAllByNumeroCuentaCompensacionIn(numerosCuentasCompensacion, pageable);
            List<RegistroDigitosVerificacionDTO> listDto = registroDigitosVerificacionPage.getContent().stream()
                    .map(registroDigitosVerificacionMapper::toDTO).collect(Collectors.toList());
            return new PageImpl<>(listDto, pageable, registroDigitosVerificacionPage.getTotalElements());
        } catch (JDBCConnectionException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION: Error en el servicio consultarRegistroDigitosVerificacion(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.DV_ERROR_CONSULTING_CHECK_DIGIT_PAGEABLE) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    /**
     * Guardar registro digitos verificacion registro digitos verificacion dto.
     *
     * @param registroDigitosVerificacionDTO the registro digitos verificacion dto
     * @return the registro digitos verificacion dto
     */
    @Transactional
    @Override
    public RegistroDigitosVerificacionDTO guardarRegistroDigitosVerificacion(
            RegistroDigitosVerificacionDTO registroDigitosVerificacionDTO) throws TechnicalException, PersistenceException {
        try {
            RegistroDigitosVerificacion registroDigitosVerificacion = registroDigitosVerificacionMapper
                    .toEntity(registroDigitosVerificacionDTO);
            RegistroDigitosVerificacion registroDigitosVerificacionDTORes = registroDigitosVerificacionRepository
                    .save(registroDigitosVerificacion);
            return registroDigitosVerificacionMapper.toDTO(registroDigitosVerificacionDTORes);
        } catch (JDBCConnectionException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION: Error en el servicio guardarRegistroDigitosVerificacion(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.DV_ERROR_SAVE_CHECK_DIGIT) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    /**
     * Eliminar registro digitos verificacion.
     *
     * @param id the id
     */
    @Transactional
    @Override
    public Boolean eliminarRegistroDigitosVerificacion(Long id) throws TechnicalException, PersistenceException {
        try {
            registroDigitosVerificacionRepository.deleteById(id);
            return (Boolean.TRUE);
        } catch (JDBCConnectionException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION: Error en el servicio eliminarRegistroDigitosVerificacion(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.DV_ERROR_DELETE_CHECK_DIGIT) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    @Transactional
    @Override
    public RegistroDigitosVerificacionDTO editarRegistroDigitosVerificacion(RegistroDigitosVerificacionDTO registroDigitosVerificacionDTO) throws TechnicalException, PersistenceException {
        try {
            RegistroDigitosVerificacion registroDigitosVerificacion = registroDigitosVerificacionMapper.toEntity(registroDigitosVerificacionDTO);
            RegistroDigitosVerificacion registroDigitoVerificacionRes = null;
            if (registroDigitosVerificacion.getId() != null && registroDigitosVerificacion.getId() > 0) {
                registroDigitoVerificacionRes = registroDigitosVerificacionRepository.save(registroDigitosVerificacion);
            }
            return registroDigitosVerificacionMapper.toDTO(registroDigitoVerificacionRes);
        } catch (JDBCConnectionException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION: Error en el servicio editarRegistroDigitosVerificacion(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.DV_ERROR_EDIT_CHECK_DIGIT) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    /**
     * Consultar registro digitos verificacion
     * dado el NIT de la empresa.
     *
     * @param nit the nit
     * @throws TechnicalException
     */
    @Override
    public RegistroDigitosVerificacionDTO consultarRegistroDigitosVerificacionByNitAndNroCtaCompensacion(String nit, String nroCtaCompensacion) throws TechnicalException, PersistenceException {
        try {
            List<RegistroDigitosVerificacion> registrosDigitosVerificacion = registroDigitosVerificacionRepository.findAllByNitAndNumeroCuentaCompensacion(nit, nroCtaCompensacion);
            if (!registrosDigitosVerificacion.isEmpty()) {
                return registroDigitosVerificacionMapper.toDTOs(registrosDigitosVerificacion).get(0);
            } else {
                return null;
            }
        } catch (JDBCConnectionException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("REG. DIG. VERTIFICACIONREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION: Error en el servicio consultarRegistroDigitosVerificacionByNit(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.DV_ERROR_CONSULTING_CHECK_DIGIT_BY_NIT_AND_NRO) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }
}