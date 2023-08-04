package co.com.bancolombia.declaraciones.services.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.com.bancolombia.declaraciones.libcommons.enums.ErrorEnum;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFPeriodoActivoDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFRequestLegalizacionDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFResponseDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.ErrorCodes;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import co.com.bancolombia.declaraciones.libcommons.mappers.IGMTBogotaMapper;
import co.com.bancolombia.declaraciones.model.panama.repository.ILegalizacionesRepository;
import co.com.bancolombia.declaraciones.services.service.ILegalizacionesService;

import javax.persistence.QueryTimeoutException;

@Service
public class LegalizacionesServiceImpl implements ILegalizacionesService {

    @Autowired
    private ILegalizacionesRepository repository;

    private static final Logger logger = Logger.getLogger(LegalizacionesServiceImpl.class);

    @Override
    public ResumenFPeriodoActivoDTO findLegalizacionByFilter(String numeroIdentificacionCliente,
                                                             ResumenFRequestLegalizacionDTO filters, Pageable pageable) throws BusinessException, TechnicalException, PersistenceException {
        ResumenFPeriodoActivoDTO resumenFPeriodoActivoDTO = new ResumenFPeriodoActivoDTO();
        List<ResumenFResponseDTO> resDTOList = new ArrayList<>();
        IGMTBogotaMapper o = new IGMTBogotaMapper() {
        };
        try {
            Page<Object[]> filterResPage = repository.findLegalizacionByFilter(numeroIdentificacionCliente, filters,
                    pageable);
            if (filterResPage != null) {
                for (Object[] objs : filterResPage.getContent()) {
                    ResumenFResponseDTO l = new ResumenFResponseDTO();
                    l.setConsecutivoCompuesto(objs[0] != null ? (String) objs[0] : "");
                    l.setFechaCreacion(objs[1] != null ? o.toLocalDateLegalization((Date) objs[1]) : null);
                    l.setFechaInicial(objs[2] != null ? o.toLocalDateLegalization((Date) objs[2]) : null);
                    l.setCodigoCuentaCompensacion(objs[3] != null ? (String) objs[3] : "");
                    l.setConsecutivo(objs[4] != null ? objs[4].toString() : "");
                    l.setTipoOperacion(objs[5] != null ? (String) objs[5] : "");
                    l.setTipoFormulario(objs[6] != null ? (String) objs[6] : "");
                    l.setEstadoFormulario(objs[7] != null ? (String) objs[7] : "");
                    l.setValorDeclarado(objs[8] != null ? objs[8].toString() : "");
                    l.setPendienteLegalizar(objs[10] != null ? objs[10].toString() : "");
                    l.setIdEstadoF10(Short.valueOf(objs[11] != null ? objs[11].toString() : ""));
                    resDTOList.add(l);
                    Page<ResumenFResponseDTO> resDTOPage = new PageImpl<>(resDTOList, pageable,
                            filterResPage.getTotalElements());
                    resumenFPeriodoActivoDTO.setResumenFormularios(resDTOPage);
                }
            }
            return resumenFPeriodoActivoDTO;
        } catch (BusinessException | PersistenceException e) {
            throw e;

        } catch (JDBCConnectionException e) {
            logger.error("LEGALIZACIONESREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("LEGALIZACIONESREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("LEGALIZACIONESREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("LEGALIZACIONESREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("LEGALIZACIONESREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("LEGALIZACIONES: Error en el servicio findLegalizacionByFilter(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.L_ERROR_FIND_BY_FILTER) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);

        }
    }

    @Override
    public short countLegalizacionesPendientes(String numeroIdentificacionCliente) throws TechnicalException, PersistenceException {
        try {
            return repository.countLegalizacionesPendientes(numeroIdentificacionCliente);
        } catch (JDBCConnectionException e) {
            logger.error("LEGALIZACIONESREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("LEGALIZACIONESREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("LEGALIZACIONESREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("LEGALIZACIONESREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("LEGALIZACIONESREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());

        } catch (Exception e) {
            logger.error("LEGALIZACIONES: Error en el servicio countLegalizacionesPendientes(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.L_ERROR_COUNT_PENDING_LEGALIZATIONS) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

}
