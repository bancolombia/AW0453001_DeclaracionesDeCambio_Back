package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.dto.MLEndeudamientoDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadErrorDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.List;

public interface IFormularios3ExcelProcessorService {
    void processData(XSSFWorkbook workbook);
    List<MLEndeudamientoDTO> getRegisters();
    List<MassiveLoadErrorDTO> getErrors();
}
