package co.com.bancolombia.declaraciones.services.serviceimpl;

import static co.com.bancolombia.declaraciones.libcommons.helpers.ExtraUtils.genConsecutivoCompuesto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import co.com.bancolombia.declaraciones.libcommons.dto.DocumentosImportacionDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.DocumentosTransporteDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.FiltersDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.Formularios1DTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MovimientoInfoCompletaDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.OperacionesFormulario1DTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFPeriodoActivoDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFResponseDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.dian.Formulario1059DTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.ErrorCodes;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import co.com.bancolombia.declaraciones.model.panama.entity.Formularios1;
import co.com.bancolombia.declaraciones.model.panama.entity.OperacionesFormulario1;
import co.com.bancolombia.declaraciones.model.panama.repository.IConsolidacionesFormulario10Repository;
import co.com.bancolombia.declaraciones.model.panama.repository.IFormatos1066Repository;
import co.com.bancolombia.declaraciones.model.panama.repository.IFormularios1Repository;
import co.com.bancolombia.declaraciones.model.panama.util.mappers.IFormularios1Mapper;
import co.com.bancolombia.declaraciones.model.panama.util.mappers.dian.IFormulario1059Mapper;
import co.com.bancolombia.declaraciones.services.service.IDetalleMovimientoService;
import co.com.bancolombia.declaraciones.services.service.IFormularios1Service;
import co.com.bancolombia.declaraciones.services.service.IFormulariosService;
import co.com.bancolombia.declaraciones.services.service.IMovimientosService;

import javax.persistence.QueryTimeoutException;


@Service
public class Formularios1ServiceImpl implements IFormularios1Service {

    @Autowired
    private IFormulariosService formulariosService;
    @Autowired
    private IMovimientosService movimientosService;
    @Autowired
    private IDetalleMovimientoService dmService;
    @Autowired
    private IFormatos1066Repository repository1066;

    public static final Logger logger = Logger.getLogger(Formularios1ServiceImpl.class);

    @Autowired
    private IFormularios1Repository formularios1Repository;
    @Autowired
    private IConsolidacionesFormulario10Repository consolidacionesRepository;

    private static final IFormularios1Mapper f1Mapper = IFormularios1Mapper.INSTANCE;
    private static final IFormulario1059Mapper f1059 = IFormulario1059Mapper.INSTANCE;

    @Value("${CODIGO.USD}")
    private String codigoUSD;
    @Value("${CODIGO.EUR}")
    private String codigoEUR;
    @Value("${VAL-MSG1-MONTODECLARADOMAYORAPENDIENTE}")
    private String validationAssociation;

    @Override
    @Transactional
    public ResumenFPeriodoActivoDTO consultarFormularioPeriodoActivo(String numeroIdentificacionCliente,
                                                                     FiltersDTO filters, Pageable pageable) throws TechnicalException, BusinessException, PersistenceException {
        ResumenFPeriodoActivoDTO resumenFPeriodoActivoDTO = new ResumenFPeriodoActivoDTO();
        ResumenFResponseDTO f1ResponseDTO;
        Formularios1 f1;
        BigDecimal valorUsdOpf1;
        BigDecimal valorTotalUsdOpf1 = BigDecimal.valueOf(0);
        BigDecimal valorTotalEurOpf1 = BigDecimal.valueOf(0);

        try {
            Page<Object[]> filterResPage = formularios1Repository
                    .findF1PeriodoActivoByFilters(numeroIdentificacionCliente, filters, pageable);
            List<Object[]> totales = formularios1Repository.findF1ValorTotalUsd(numeroIdentificacionCliente, filters);
            for (Object[] o : totales) {
                if (o[1].equals(codigoUSD)) {
                    valorTotalUsdOpf1 = (BigDecimal) o[0];
                } else if (o[1].equals(codigoEUR)) {
                    valorTotalEurOpf1 = (BigDecimal) o[0];
                }
            }
            List<ResumenFResponseDTO> resDTOList = new ArrayList<>();
            for (Object[] objs : filterResPage.getContent()) {
                f1 = (Formularios1) objs[0];

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                if (f1.getFechaCreacion() != null && f1.getFechaInicial() != null) {
                    Date fecCreacionFormat = formatter.parse(f1.getFechaCreacion().toString().substring(0, 10));
                    Timestamp timeStampConvertFecCreacion = new Timestamp(fecCreacionFormat.getTime());
                    f1.setFechaCreacion(timeStampConvertFecCreacion);

                    Date fecInicialFormat = formatter.parse(f1.getFechaInicial().toString().substring(0, 10));
                    Timestamp timeStampConvertFecInicial = new Timestamp(fecInicialFormat.getTime());
                    f1.setFechaInicial(timeStampConvertFecInicial);
                }


                valorUsdOpf1 = (BigDecimal) objs[1];
                f1ResponseDTO = f1Mapper.toResponseDTO(f1);
                if (f1.getOperacionesFormulario1Collection() != null
                        && !f1.getOperacionesFormulario1Collection().isEmpty()) {
                    String tipoMoneda = f1.getOperacionesFormulario1Collection()
                            .toArray(new OperacionesFormulario1[0])[0].getIdMonedaGiro().getCodigo();
                    f1ResponseDTO.setTipoValorDeclarado(!tipoMoneda.equals("0") ? tipoMoneda : null);
                }
                if (valorUsdOpf1 != null)
                    f1ResponseDTO.setValorDeclarado(String.valueOf(valorUsdOpf1));
                f1ResponseDTO.setFormularioConsolidado(consolidacionesRepository
                        .countByConsecutivoFormularioAndCodigoFormulario(f1ResponseDTO.getConsecutivoCompuesto(), "F1")
                        .shortValue());
                resDTOList.add(f1ResponseDTO);
            }

            Page<ResumenFResponseDTO> resDTOPage = new PageImpl<>(resDTOList, pageable,
                    filterResPage.getTotalElements());
            resumenFPeriodoActivoDTO.setValorTotalUsd(valorTotalUsdOpf1);
            resumenFPeriodoActivoDTO.setValorTotalEur(valorTotalEurOpf1);
            resumenFPeriodoActivoDTO.setResumenFormularios(resDTOPage);

            return resumenFPeriodoActivoDTO;
        } catch (BusinessException | TechnicalException | PersistenceException e) {
            throw e;

        } catch (JDBCConnectionException e) {
            logger.error("FORMULARIOSREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("FORMULARIOSREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("FORMULARIOSREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("FORMULARIOSREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("FORMULARIOSREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio consultarFormularioPeriodoActivo(). " + e.getCause());

            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CONSULTING_ACTIVE_PERIOD) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    @Override
    @Transactional
    public Formularios1DTO findByConsecutivoCompuesto(String consecutivoCompuesto)
            throws TechnicalException, BusinessException, PersistenceException {
        Formularios1 formularios1;
        Formularios1DTO formularios1DTO;
        if (consecutivoCompuesto == null)
            throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CONSULTING_BY_CONSECUTIVE_COMPOSED), ErrorCodes.VALIDATION_ERROR);
        try {
            formularios1 = formularios1Repository.findByConsecutivoCompuesto(consecutivoCompuesto);
            formularios1DTO = f1Mapper.toDTO(formularios1);
            return formularios1DTO;
        } catch (JDBCConnectionException e) {
            logger.error("FORMULARIOSREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("FORMULARIOSREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("FORMULARIOSREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("FORMULARIOSREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("FORMULARIOSREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio findByConsecutivoCompuesto(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CONSULTING_DATA) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Formularios1DTO saveFormOne(Formularios1DTO formularios1DTO) throws BusinessException, TechnicalException, PersistenceException {
        Formularios1 formularios1;
        String genId;
        MovimientoInfoCompletaDTO movCompletaDTO = null;

        try {
            if (formularios1DTO == null)
                throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_SAVING_FORM_NULL), ErrorCodes.VALIDATION_ERROR);
            logger.info("FORMULARIOS1: save f1DTO entrante: " + formularios1DTO);
            genId = genConsecutivoCompuesto(formularios1DTO.getConsecutivo(), formularios1DTO.getFechaInicial(),
                    formularios1DTO.getNumeroIdentificacionCliente());

            //validaciones del negocio
            List<Integer> lstNumerales = new ArrayList<>();
            lstNumerales.add(1);
            lstNumerales.add(2);
            lstNumerales.add(3);
            lstNumerales.add(4);
            lstNumerales.add(191);
            lstNumerales.add(192);
            lstNumerales.add(193);
            lstNumerales.add(194);
            lstNumerales.add(175);
            if (formularios1DTO.getTipoOperacion() != null &&
                    (formularios1DTO.getTipoOperacion() == 3 || formularios1DTO.getTipoOperacion() == 4)
                    && (formularios1DTO.getConsecutivoAnterior() == null || formularios1DTO.getFechaAnterior() == null)) {
                throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_OPERATION_NULL_REQUIRED_VALUES), ErrorCodes.VALIDATION_CONTROL_ERROR);
            }
            if (formularios1DTO.getOperacionesFormulario1Collection() != null) {
                for (OperacionesFormulario1DTO operacion : formularios1DTO.getOperacionesFormulario1Collection()) {
                    if (operacion.getIdMonedaGiro() == 1 &&
                            (operacion.getValorMonedaGiro().compareTo(operacion.getValorUsd()) > 0)) {
                        throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_TYPE_MONEY_VALIDATION), ErrorCodes.VALIDATION_CONTROL_ERROR);
                    }
                    if (!lstNumerales.contains(operacion.getIdNumeral())) {
                        throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CORRUPTED_DATA), ErrorCodes.VALIDATION_CONTROL_ERROR);
                    }
                }
            }

            //-------

            if (formularios1DTO.getConsecutivoCompuesto() == null) {
                logger.info("FORMULARIOS1: el genId generado: " + genId);
                if (Boolean.TRUE.equals(formulariosService.existsAnyFormularioById(genId))) {
                    throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.ERROR_WRONG_GENERATED_ID), ErrorCodes.VALIDATION_ERROR);
                }

                formularios1DTO.setConsecutivoCompuesto(genId);
            } else { // Viene de Editar
                logger.info("FORMULARIOS1: ES FORMULARIO EDITAR!!!");
                logger.info("FORMULARIOS1: CONSECUTIVOCOMPUESTO FROM REQ: " + formularios1DTO.getConsecutivoCompuesto());
                // NEW MODIFICATION TO HANDLE F.INICIAL CONSECUTIVOCOMPUESTO
                if (!genId.equals(formularios1DTO.getConsecutivoCompuesto())) {
                    // Si no son iguales es porque cambia la f.Inicial en el editar
                    if (Boolean.TRUE.equals(formulariosService.existsAnyFormularioById(genId))) {
                        throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.ERROR_WRONG_GENERATED_ID), ErrorCodes.VALIDATION_ERROR);
                    }
                    logger.info("FORMULARIOS1: SE ELIMINARAI FORM CON CONSECUTIVOPUESTO: " + formularios1DTO.getConsecutivoCompuesto());
                    formularios1Repository.deleteByConsecutivoCompuesto(formularios1DTO.getConsecutivoCompuesto());
                    formularios1DTO.setConsecutivoCompuesto(genId);
                    logger.info("FORMULARIOS1: SE ELIMINO CON VIEJO ID, EL QUE SE CREARAA CON NUEVO ID: " + formularios1DTO);
                }
            }

			if (formularios1DTO.getOperacionesFormulario1Collection() != null) {
				logger.info("FORMULARIOS1: TIENE OPERACIONESF1 COLLECTION: "
						+ Arrays.toString(formularios1DTO.getOperacionesFormulario1Collection().toArray()));
				final boolean itIsAssociated = dmService.isAssociatedFormWithAnyMovimiento(
						formularios1DTO.getCuentaCompensacion(), String.valueOf(formularios1DTO.getConsecutivo()),
						formularios1DTO.getFechaInicial());
				logger.info("FORMULARIOS1: itIsAssociated?: " + itIsAssociated);

				if (itIsAssociated) {
					itIsAssociated(formularios1DTO);
				} else if (formularios1DTO.isMovenment()) {
					movCompletaDTO = new MovimientoInfoCompletaDTO();
					movCompletaDTO.setDetallesMovimientos(formularios1DTO.getDetalleMovimiento() != null
							? formularios1DTO.getDetalleMovimiento() : null);
					movCompletaDTO.setLstMovimientos(
							formularios1DTO.getMovimiento() != null ? formularios1DTO.getMovimiento() : null);
					movimientosService.saveMovimientoComplete(movCompletaDTO);					
				}
				processOperaciones(formularios1DTO, genId);
			}
            processDocumentosCollection(formularios1DTO, genId);

            logger.info("FORMULARIOS1: F1DTO seteado antes de guardar: " + formularios1DTO);
            formularios1 = f1Mapper.toEntity(formularios1DTO);
            logger.info("FORMULARIOS1: POS EN MEMORIA F1 ENTITY antes de guardar: " + formularios1);
            logger.info("FORMULARIOS1: CONSECUTIVOCOMPUESTO F1 ENTITY antes de guardar: " + formularios1.getConsecutivoCompuesto());
            logger.info("FORMULARIOS1: FECHA CREACION F1 ENTITY antes de guardar: " + formularios1.getFechaCreacion());
            logger.info(
                    "FORMULARIOS1: FECHA CREACION STRING F1 ENTITY antes de guardar: " + formularios1.getFechaCreacion().toString());
            return f1Mapper.toDTO(formularios1Repository.save(formularios1));
        } catch (BusinessException | TechnicalException e) {
            throw e;
        } catch (JDBCConnectionException e) {
            logger.error("FORMULARIOSREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("FORMULARIOSREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("FORMULARIOSREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("FORMULARIOSREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("FORMULARIOSREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio saveFormOne(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_SAVING_DATA) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }

    }

    private void itIsAssociated(Formularios1DTO formularios1DTO) throws BusinessException, TechnicalException {
        final int ONE = 1;
        BigDecimal montoDeclaradoFormNew = new BigDecimal(0);

        for (OperacionesFormulario1DTO opF1 : formularios1DTO.getOperacionesFormulario1Collection())
            montoDeclaradoFormNew = montoDeclaradoFormNew.add(opF1.getValorMonedaGiro());

        final List<Object[]> movPendientesPorDeclarar = movimientosService.getPendientesPorDeclararOfAForm(
                formularios1DTO.getCuentaCompensacion(), String.valueOf(formularios1DTO.getConsecutivo()),
                formularios1DTO.getFechaInicial());

        final List<Object[]> movsAActualizar = new ArrayList<>();
        for (Object[] obj : movPendientesPorDeclarar) {

            final String idMov = (String) obj[0];
            final BigDecimal montoMov = (BigDecimal) obj[2];

            final BigDecimal montosDeclaradosTotales = dmService.getSumMontosDeclaradosTotales(idMov);

            final BigDecimal montoDeclaradoFormOld = dmService.getSumMontosDeclaradosOfForm(idMov,
                    String.valueOf(formularios1DTO.getConsecutivo()), formularios1DTO.getFechaInicial());

            final BigDecimal montoDeclarado = montosDeclaradosTotales.subtract(montoDeclaradoFormOld)
                    .add(montoDeclaradoFormNew);

            final int compareToRes = montoDeclarado.compareTo(montoMov);

            if (compareToRes > 0)
                throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_WRONG_MOVEMENT_VALUE), ErrorCodes.VALIDATION_ERROR);

            final Object[] paramsToUpdate = new Object[2];
            paramsToUpdate[0] = idMov;
            paramsToUpdate[1] = montoDeclaradoFormNew;
            movsAActualizar.add(paramsToUpdate);
        }
        for (Object[] obj : movsAActualizar) {
            final String idMov = (String) obj[0];
            final BigDecimal montoDeclarado = (BigDecimal) obj[1];
            dmService.updateMontoDeclarado(montoDeclarado, idMov, String.valueOf(formularios1DTO.getConsecutivo()), formularios1DTO.getFechaInicial());
            movimientosService.updateMontoPendiente(idMov);
        }

    }

    private void processOperaciones(Formularios1DTO formularios1DTO, String genId) {
        int i = 0;
        for (OperacionesFormulario1DTO opF1 : formularios1DTO.getOperacionesFormulario1Collection()) {
            opF1.setIdFormulario1(genId);
            if (opF1.getIdOperacion() == null) {
                formularios1DTO.getOperacionesFormulario1Collection().toArray()[i] = opF1;
            }
            i++;
        }
    }

    private void processDocumentosCollection(Formularios1DTO formularios1DTO, String genId) {
        if (formularios1DTO.getDocumentosImportacionCollection() != null) {
            int i = 0;
            for (DocumentosImportacionDTO di : formularios1DTO.getDocumentosImportacionCollection()) {
                di.setIdFormulario1(genId);
                if (di.getIdDocumento() == null) {
                    formularios1DTO.getDocumentosImportacionCollection().toArray()[i] = di;
                }
                i++;
            }
        }
        if (formularios1DTO.getDocumentosTransporteCollection() != null) {
            int i = 0;
            for (DocumentosTransporteDTO dt : formularios1DTO.getDocumentosTransporteCollection()) {
                dt.setIdFormulario1(genId);
                if (dt.getIdDocumento() == null) {
                    formularios1DTO.getDocumentosTransporteCollection().toArray()[i] = dt;
                }
                i++;
            }
        }
    }

    @Override
    @Transactional
    public Formularios1DTO updateFormOne(Formularios1DTO formularios1DTO) throws BusinessException, TechnicalException, PersistenceException {
        if (formularios1DTO == null)
            throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_SAVING_FORM_NULL), ErrorCodes.VALIDATION_ERROR);
        Formularios1 formularios1;
        try {
            formularios1 = f1Mapper.toEntity(formularios1DTO);
            if (formularios1Repository.findByConsecutivoCompuesto(formularios1.getConsecutivoCompuesto()) == null)
                throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CONSULTING_DATA), ErrorCodes.VALIDATION_ERROR);
            // validaciones del negocio
            List<Integer> lstNumerales = new ArrayList<>();
            lstNumerales.add(1);
            lstNumerales.add(2);
            lstNumerales.add(3);
            lstNumerales.add(4);
            lstNumerales.add(191);
            lstNumerales.add(192);
            lstNumerales.add(193);
            lstNumerales.add(194);
            lstNumerales.add(175);
            if (formularios1.getTipoOperacion() != null &&
                    (formularios1.getTipoOperacion() == 3 || formularios1.getTipoOperacion() == 4)
                    && (formularios1.getConsecutivoAnterior() != null || formularios1.getFechaAnterior() != null)) {
                throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_OPERATION_NULL_REQUIRED_VALUES), ErrorCodes.VALIDATION_CONTROL_ERROR);
            }
            if (formularios1.getOperacionesFormulario1Collection() != null) {
                for (OperacionesFormulario1 operacion : formularios1.getOperacionesFormulario1Collection()) {
                    if (operacion.getIdMonedaGiro().getId() == 1 &&
                            (operacion.getValorMonedaGiro().compareTo(operacion.getValorUsd()) > 0)) {
                        throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_TYPE_MONEY_VALIDATION), ErrorCodes.VALIDATION_CONTROL_ERROR);
                    }
                    if (!lstNumerales.contains(operacion.getIdNumeral().getCodigo())) {
                        throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CORRUPTED_DATA), ErrorCodes.VALIDATION_CONTROL_ERROR);
                    }
                }
            }

            //-------
            return f1Mapper.toDTO(formularios1Repository.save(formularios1));
        } catch (BusinessException e) {
            throw e;

        } catch (JDBCConnectionException e) {
            logger.error("FORMULARIOSREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("FORMULARIOSREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("FORMULARIOSREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("FORMULARIOSREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("FORMULARIOSREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio updateFormOne(). " + e.getCause());

            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_UPDATING_DATA) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAllByConsecutivosCompuestos(List<String> cuentasCompensacion,
                                                     String[] consecutivosCompuestos) throws TechnicalException, BusinessException, PersistenceException {
        Formularios1 formulario1;
        boolean control = true;
        try {
            for (String item : consecutivosCompuestos) {
                formulario1 = formularios1Repository.findByConsecutivoCompuesto(item);
                if (formulario1 == null) {
                    logger.info("FORMULARIOS1: El formulario no existe en base de datos");
                    throw new BusinessException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_DELETING_DATA),
                            ErrorCodes.VALIDATION_ERROR);
                }
                Formularios1DTO f1DTO = f1Mapper.toDTO(formulario1);
                // boolean eliminatedAssociation = movimientosService.deleteMovimientos(cuentasCompensacion,
                // 		String.valueOf(f1DTO.getConsecutivo()), f1DTO.getFechaInicial());
                boolean eliminatedAssociation = movimientosService.deleteMovimientos2(cuentasCompensacion,
                        String.valueOf(f1DTO.getConsecutivo()), f1DTO.getFechaInicial(), "1");
                if (eliminatedAssociation) {
                    Optional<BigDecimal> existeF1066F1067 = repository1066.existF1oF2InF1066oF1067(item);
                    if (existeF1066F1067.isPresent() && existeF1066F1067.get().longValue() == 1) {
                        control = false;
                    } else {
                        formularios1Repository.deleteByConsecutivoCompuesto(formulario1.getConsecutivoCompuesto());
                    }
                    logger.info("FORMULARIOS1: F1 con id: " + f1DTO.getConsecutivoCompuesto()
                            + " se elimina asociacion con un(os) movimiento(s)");
                } else {
                    throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.ERROR_DELETING_ASSOCIATED_MOVS),
                            ErrorCodes.VALIDATION_ERROR);
                }
            }
            return control;
        } catch (TechnicalException | BusinessException e) {
            throw e;
        } catch (JDBCConnectionException e) {
            logger.error("FORMULARIOSREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("FORMULARIOSREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("FORMULARIOSREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("FORMULARIOSREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("FORMULARIOSREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio f1 deleteAllByConsecutivosCompuestos(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.ERROR_DELETING_FORM_DOES_NOT_EXIST + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    @Override
    @Transactional
    public List<Formulario1059DTO> findFormulario1ReportesDian(Short year, Short trimestre, String identificacion)
            throws TechnicalException, PersistenceException {
        try {
            List<Formulario1059DTO> l = new ArrayList<>();
            List<Formularios1> lista = formularios1Repository.findFormulario1ReportesDian(year, trimestre,
                    identificacion);
            for (Formularios1 f : lista) {
                Formulario1059DTO fdto = f1059.toDTO(f);
                l.add(fdto);
            }
            return l;
        } catch (JDBCConnectionException e) {
            logger.error("FORMULARIOSREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("FORMULARIOSREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("FORMULARIOSREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("FORMULARIOSREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("FORMULARIOSREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio findFormulario1ReportesDian(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CONSULTING_DATA) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    @Override
    @Transactional
    public List<Formulario1059DTO> findFormulario1ReportesDianAll(Short year, Short trimestre, String identificacion)
            throws TechnicalException, PersistenceException {
        try {
            List<Formulario1059DTO> l = new ArrayList<>();
            List<Formularios1> lista = formularios1Repository.findFormulario1ReportesDianAll(year, trimestre,
                    identificacion);
            for (Formularios1 f : lista) {
                Formulario1059DTO fdto = f1059.toDTO(f);
                l.add(fdto);
            }
            return l;
        } catch (JDBCConnectionException e) {
            logger.error("FORMULARIOSREPOSITORY: Error de conexión" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_JDBC_ERROR_CONNECTION), e.getCause());
        } catch (ConstraintViolationException e) {
            logger.error("FORMULARIOSREPOSITORY: Violacion de integridad" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_DUPLICATE_KEY_ON_DATABASE), e.getCause());
        } catch (QueryTimeoutException e) {
            logger.error("FORMULARIOSREPOSITORY: Tiempo agotado para procesar el query" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_QUERY_TIMEOUT), e.getCause());
        } catch (SQLGrammarException e) {
            logger.error("FORMULARIOSREPOSITORY: SQL error gramatical" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_SQL_GRAMMAR_ERROR), e.getCause());
        } catch (GenericJDBCException e) {
            logger.error("FORMULARIOSREPOSITORY: error generico de JDBC" + e.getCause());
            throw new PersistenceException(ErrorEnum.getCompleteMessage(ErrorEnum.PE_GENERIC_JDBC_ERROR), e.getCause());
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio findFormulario1ReportesDian(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_CONSULTING_DATA) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }

    @Override
    @Transactional
    public Long getSeqForms() throws TechnicalException {
        try {
            return formularios1Repository.getNextSeriesId();
        } catch (Exception e) {
            logger.error("FORMULARIOS1: Error en el servicio getSeqForms(). " + e.getCause());
            throw new TechnicalException(ErrorEnum.getCompleteMessage(ErrorEnum.F1_ERROR_GETTING_NEXT_SERIES) + ": " + e.getMessage(), e.getCause(), ErrorCodes.TECNICAL_ERROR);
        }
    }
}
