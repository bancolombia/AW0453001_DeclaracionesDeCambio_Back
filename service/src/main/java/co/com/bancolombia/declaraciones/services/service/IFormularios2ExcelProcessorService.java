package co.com.bancolombia.declaraciones.services.service;

import co.com.bancolombia.declaraciones.libcommons.dto.MLExportacionesDto;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadErrorDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.List;

public interface IFormularios2ExcelProcessorService {
    void processData(XSSFWorkbook workbook);
    List<MLExportacionesDto> getRegisters();
    List<MassiveLoadErrorDTO> getErrors();
    boolean hasErrors();
}
