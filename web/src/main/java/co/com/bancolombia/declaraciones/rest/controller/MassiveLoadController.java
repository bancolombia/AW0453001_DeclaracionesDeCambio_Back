package co.com.bancolombia.declaraciones.rest.controller;

import co.com.bancolombia.declaraciones.libcommons.dto.*;
import co.com.bancolombia.declaraciones.libcommons.dto.MLImportacionesDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadDto;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadErrorDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.RespuestaGenericaDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.*;
import co.com.bancolombia.declaraciones.services.serviceimpl.MassiveLoadServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bancolombia/declaraciones/")
public class MassiveLoadController {

    private MassiveLoadServiceImpl massiveLoadService;

    @Autowired
    public void setMassiveLoadService(MassiveLoadServiceImpl massiveLoadService) {
        this.massiveLoadService = massiveLoadService;
    }

    public static final Logger logger = Logger.getLogger(MassiveLoadController.class);

    @PostMapping("process-excel-file")
    public ResponseEntity<?> processExcelFile(@RequestParam("file") MultipartFile file) {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        File temporalFile = null;
        try {
            MassiveLoadDto response = new MassiveLoadDto();
            String formTypeName = "";

            String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
            int lastIndexOfDot = originalFileName.lastIndexOf(".");
            String name = originalFileName.substring(0, lastIndexOfDot);
            String extension = originalFileName.substring(lastIndexOfDot);
            temporalFile = File.createTempFile(name, extension);
            file.transferTo(temporalFile);
            if (this.massiveLoadService.initializeWorkbook(temporalFile)) {
                String sheetName = this.massiveLoadService.getNameOfSheet();
                String formType = "Invalido";
                switch (sheetName.toLowerCase()) {
                    case "f1 importación de bienes":
                        formType = "F1";
                        formTypeName = "Importación de Bienes";
                        response.setFormType(formType);
                        this.massiveLoadService.processData(formType);
                        break;
                    case "f2 exportación de bienes":
                        formType = "F2";
                        formTypeName = "Exportación de Bienes";
                        response.setFormType(formType);
                        this.massiveLoadService.processData(formType);
                        break;
                    case "endeudamiento externo":
                        formType = "F3";
                        formTypeName = "Endeudamiento Externo";
                        response.setFormType(formType);
                        this.massiveLoadService.processData(formType);
                        break;
                    default:
                        throw new MassiveLoadException("El archivo " + originalFileName + " no es una plantilla válida");
                }
                List<MassiveLoadErrorDTO> errors = this.massiveLoadService.getListOfError();
                if (!errors.isEmpty()) {
                    String finalFormTypeName = formTypeName;
                    errors = errors.stream().peek(massiveLoadErrorDTO -> {
                        massiveLoadErrorDTO.setFileName(originalFileName);
                        massiveLoadErrorDTO.setFormName(finalFormTypeName);
                    }).collect(Collectors.toList());
                    List<Object> registers = this.massiveLoadService.getRegisters();
                    response.setRegisters(registers);
                    response.setSuccess(false);
                    response.setDetail("El archivo contiene errores");
                    response.setErrors(errors);
                    response.setFileName(originalFileName);
                    response.setNumberOfRegisters(registers.size());
                } else {
                    List<Object> registers = this.massiveLoadService.getRegisters();
                    response.setRegisters(registers);
                    response.setSuccess(true);
                    response.setDetail("Archivo procesado correctamente");
                    response.setFileName(originalFileName);
                    response.setNumberOfRegisters(registers.size());
                }
            } else {
                throw new MassiveLoadException("Error al intentar inicial el libro de trabajo");
            }
            if (temporalFile.delete()) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                throw new MassiveLoadException("Error al tratar de eliminar el archivo temporal");
            }

        } catch (Exception e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en carga masiva: " + e);
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("process-multiple-files")
    private ResponseEntity<?> uploadMultipleFiles(@RequestParam List<MultipartFile> files) {
        List<MassiveLoadDto> responses = new ArrayList<>();
        List<Object> exceptionList = new ArrayList<>();

        for (MultipartFile file : files) {
            Object responseBody = this.processExcelFile(file).getBody();
            try {
                MassiveLoadDto massiveLoadDto = (MassiveLoadDto) responseBody;
                responses.add(massiveLoadDto);
            } catch (ClassCastException e) {
                exceptionList.add(responseBody);
            }
        }
        if (exceptionList.isEmpty()) {
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } else {
            if (responses.isEmpty()) {
                return new ResponseEntity<>(exceptionList, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                exceptionList.addAll(responses);
                return new ResponseEntity<>(exceptionList, HttpStatus.OK);
            }
        }
    }

    @PostMapping("save-registers")
    private ResponseEntity<?> saveRegisters(@RequestBody List<MassiveLoadDto> registers) {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        registers.forEach(doc -> {
                    switch (doc.getFormType()) {
                        case "F1":
                            ObjectMapper mapper = new ObjectMapper();
                            List<MLImportacionesDTO> mlImportacionesDTO =
                                    ((List<MLImportacionesDTO>) mapper.convertValue(doc.getRegisters(),
                                            new TypeReference<List<MLImportacionesDTO>>() {
                                            }))
                                            .stream().map(element -> (MLImportacionesDTO) element)
                                            .collect(Collectors.toList());
                            try {
                                List<Formularios1DTO> formularios1DTOList
                                        = massiveLoadService.saveListFormularios1(mlImportacionesDTO);

                                if (doc.getRegisters().size() == formularios1DTOList.size()) {
                                    respuestaGenericaDTO.setStatus(200);
                                    respuestaGenericaDTO.setMessage("Listado de importaciones creadas de forma exitosa");
                                }

                            } catch (BusinessException e) {
                                logger.error("MASSIVELOAD CONTROLLER: Error en el servicio saveRegisters(). BusinessException: " + e.getCause());
                                respuestaGenericaDTO.setStatus(e.getErrorCode());
                                respuestaGenericaDTO.setMessage(e.getMessage());
                                //return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.ACCEPTED);
                            } catch (TechnicalException e) {
                                logger.error("MASSIVELOAD CONTROLLER: Error en el servicio saveRegisters(). TechnicalException: " + e.getCause());
                                respuestaGenericaDTO.setStatus(e.getErrorCode());
                                respuestaGenericaDTO.setMessage(e.getMessage());
                                //return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.ACCEPTED);
                            } catch (PersistenceException e) {
                                logger.error("MASSIVELOAD CONTROLLER: Error en el servicio saveRegisters(). PersistenceException: " + e.getCause());
                                respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
                                respuestaGenericaDTO.setMessage(e.getMessage());
                                //return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
                            } catch (Exception e) {
                                logger.error("MASSIVELOAD CONTROLLER: Error en el servicio saveRegisters(). Exception: " + e.getCause());
                                respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
                                respuestaGenericaDTO.setMessage("Error en saveFormOne: " + e.getMessage());
                                //return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            break;
                    }
                }
        );

        return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.OK);
    }

    @PostMapping("generate-errors-pdf")
    public ResponseEntity<?> generateErrorsPDF(@RequestBody List<MassiveLoadErrorDTO> massiveLoadErrorDTOS) {
        String fileName = "Errores Plantillas Carga Masiva";
        String extension = "pdf";
        String dateNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String finalFileName = fileName + "-" + dateNow + "." + extension;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + finalFileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(this.massiveLoadService.generateErrorPdf(massiveLoadErrorDTOS, finalFileName));
    }
}
