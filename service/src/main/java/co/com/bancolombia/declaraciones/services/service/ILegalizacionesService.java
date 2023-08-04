package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import org.springframework.data.domain.Pageable;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFPeriodoActivoDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFRequestLegalizacionDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;

public interface ILegalizacionesService {

    ResumenFPeriodoActivoDTO findLegalizacionByFilter(String numeroIdentificacionCliente,
                                                      ResumenFRequestLegalizacionDTO filters, Pageable pageable) throws BusinessException, TechnicalException, PersistenceException;

    short countLegalizacionesPendientes(String numeroIdentificacionCliente) throws TechnicalException, BusinessException, PersistenceException;

}
