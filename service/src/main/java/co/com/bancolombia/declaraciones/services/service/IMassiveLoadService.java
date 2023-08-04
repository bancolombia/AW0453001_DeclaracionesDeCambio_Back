package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.dto.Formularios1DTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MLImportacionesDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadErrorDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IMassiveLoadService {

    String getNameOfSheet();

    List<MassiveLoadErrorDTO> getListOfError();

    boolean initializeWorkbook(File file);

    void processData(String formType) throws IOException;

    List<Object> getRegisters();

    List<Formularios1DTO> saveListFormularios1(List<MLImportacionesDTO> importaciones) throws BusinessException, TechnicalException, PersistenceException;

    byte[] generateErrorPdf(List<MassiveLoadErrorDTO> errorDTOS, String file);
}
