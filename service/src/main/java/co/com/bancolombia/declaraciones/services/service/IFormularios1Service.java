package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.dto.FiltersDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.Formularios1DTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFPeriodoActivoDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.dian.Formulario1059DTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface IFormularios1Service {


	ResumenFPeriodoActivoDTO consultarFormularioPeriodoActivo(String numeroIdentificacionCliente, FiltersDTO filters,
			Pageable pageable) throws TechnicalException, BusinessException, PersistenceException;


	Formularios1DTO findByConsecutivoCompuesto(String consecutivoCompuesto)
			throws TechnicalException, BusinessException, PersistenceException;


	Formularios1DTO saveFormOne(Formularios1DTO formulario1dto) throws BusinessException, TechnicalException, PersistenceException;


	Formularios1DTO updateFormOne(Formularios1DTO formulario1dto) throws BusinessException, TechnicalException, PersistenceException;


	Boolean deleteAllByConsecutivosCompuestos(List<String> cuentasCompensacion, String[] consecutivosCompuestos)
			throws TechnicalException, BusinessException, PersistenceException;

	List<Formulario1059DTO> findFormulario1ReportesDian(Short year, Short trimestre, String identificacion)
			throws TechnicalException, PersistenceException;

	List<Formulario1059DTO> findFormulario1ReportesDianAll(Short year, Short trimestre, String identificacion)
			throws TechnicalException, PersistenceException;

	Long getSeqForms() throws TechnicalException;

}