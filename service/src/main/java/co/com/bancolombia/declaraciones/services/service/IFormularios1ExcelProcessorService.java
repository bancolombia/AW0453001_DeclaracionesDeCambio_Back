package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.dto.Formularios1DTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MLImportacionesDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadErrorDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.List;

public interface IFormularios1ExcelProcessorService {

    void processData(XSSFWorkbook workbook);
    List<MLImportacionesDTO> getRegisters();
    List<MassiveLoadErrorDTO> getErrors();
    boolean hasErrors();
}
