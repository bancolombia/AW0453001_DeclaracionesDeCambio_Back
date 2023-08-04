package co.com.bancolombia.declaraciones.services.serviceimpl;

import co.com.bancolombia.declaraciones.libcommons.dto.*;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import co.com.bancolombia.declaraciones.libcommons.helpers.PdfHeaderEventHandler;
import co.com.bancolombia.declaraciones.model.panama.entity.administracion.Monedas;
import co.com.bancolombia.declaraciones.model.panama.entity.administracion.Numerales;
import co.com.bancolombia.declaraciones.model.panama.entity.administracion.TiposIdentificacion;
import co.com.bancolombia.declaraciones.model.panama.repository.IMonedasRepository;
import co.com.bancolombia.declaraciones.model.panama.repository.INumeralesRepository;
import co.com.bancolombia.declaraciones.model.panama.repository.ITiposIdentificacionRepository;
import co.com.bancolombia.declaraciones.services.service.*;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Configuration
@EnableTransactionManagement(proxyTargetClass = false)
public class MassiveLoadServiceImpl implements IMassiveLoadService {
    private XSSFWorkbook workbook;
    private List<Object> registers;
    private List<MassiveLoadErrorDTO> errors;
    private IFormularios1ExcelProcessorService formularios1ExcelProcessorService;
    private IFormularios2ExcelProcessorService formularios2ExcelProcessorService;
    private IFormularios3ExcelProcessorService formularios3ExcelProcessorService;

    @Autowired
    public void setFormularios3ExcelProcessorService(IFormularios3ExcelProcessorService formularios3ExcelProcessorService) {
        this.formularios3ExcelProcessorService = formularios3ExcelProcessorService;
    }

    @Autowired
    private ITiposIdentificacionRepository tiposIdentificacionRepository;

    @Autowired
    private IMonedasRepository monedasRepository;

    @Autowired
    private INumeralesRepository numeralesRepository;

    @Autowired
    private IFormularios1Service formularios1Service;

    @Autowired
    public void setFormularios1ExcelProcessorService(IFormularios1ExcelProcessorService formularios1ExcelProcessorService) {
        this.formularios1ExcelProcessorService = formularios1ExcelProcessorService;
    }

    @Autowired
    public void setFormularios2ExcelProcessorService(IFormularios2ExcelProcessorService formularios2ExcelProcessorService) {
        this.formularios2ExcelProcessorService = formularios2ExcelProcessorService;
    }

    @Override
    public String getNameOfSheet() {
        return workbook.getSheetName(0);
    }

    @Override
    public List<MassiveLoadErrorDTO> getListOfError() {
        return this.errors;
    }

    @Override
    public boolean initializeWorkbook(File file) {
        try {
            workbook = new XSSFWorkbook(OPCPackage.open(file));
            registers = new ArrayList<>();
            errors = new ArrayList<>();
            return true;
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void processData(String formType) {
        switch (formType) {
            case "F1":
                this.formularios1ExcelProcessorService.processData(workbook);
                this.errors.addAll(this.formularios1ExcelProcessorService.getErrors());
                this.registers.addAll(this.formularios1ExcelProcessorService.getRegisters());
                break;
            case "F2":
                this.formularios2ExcelProcessorService.processData(workbook);
                this.errors.addAll(this.formularios2ExcelProcessorService.getErrors());
                this.registers.addAll(this.formularios2ExcelProcessorService.getRegisters());
                break;
            case "F3":
                this.formularios3ExcelProcessorService.processData(workbook);
                this.errors.addAll(this.formularios3ExcelProcessorService.getErrors());
                this.registers.addAll(this.formularios3ExcelProcessorService.getRegisters());
                break;
            default:
                System.out.println("Adios mundo");

        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Object> getRegisters() {
        return this.registers;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Formularios1DTO> saveListFormularios1(List<MLImportacionesDTO> importaciones) throws BusinessException, TechnicalException, PersistenceException {
        List<Formularios1DTO> formularios1DTOList = new ArrayList<>();
        String condicionesPago = "No aplica apatir de Fase IV";
        String condicionesDespacho = "No aplica apatir de Fase IV";

        //TODO validar fecha correcta
        LocalDateTime localNow = LocalDateTime.now().atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("America/Bogota"))
                .toLocalDateTime();

        for (MLImportacionesDTO imp : importaciones) {
            Formularios1DTO formularios1DTO = new Formularios1DTO();
            formularios1DTO.setConsecutivoCompuesto(null);
            formularios1DTO.setConsecutivo(Integer.valueOf(imp.getConsecutive()));
            formularios1DTO.setFechaCreacion(localNow);
            formularios1DTO.setFechaUltimaModificacion(localNow);
            formularios1DTO.setTipoOperacion(Short.valueOf(imp.getTypeOperation().substring(0, 1)));
            formularios1DTO.setIdCiudadInicial(0);
            formularios1DTO.setIdCuentaCompensacionInicial(1);
            formularios1DTO.setFechaInicial(convertToLocalDateTime(imp.getFormDate()));
            if (imp.getConsecutiveOld() == null) {
                formularios1DTO.setIdCuentaCompensacionAnterior(0);
                formularios1DTO.setConsecutivoAnterior(null);
                formularios1DTO.setFechaAnterior(null);
            } else {
                formularios1DTO.setIdCuentaCompensacionAnterior(1);
                formularios1DTO.setConsecutivoAnterior(Integer.valueOf(imp.getConsecutiveOld()));
                formularios1DTO.setFechaAnterior(convertToLocalDateTime(imp.getDocumentOldDate()));
            }

            TiposIdentificacion tipoIdentificacion
                    = tiposIdentificacionRepository.findByCodigo(imp.getDocumentType());

            formularios1DTO.setIdTipoIdentificacionImportador(tipoIdentificacion.getId());
            formularios1DTO.setNumrIdentificadorImportador(imp.getDocumentNumber());
            formularios1DTO.setDigitoVerificacionImportador(imp.getValidationDigit());
            formularios1DTO.setNombreImportador(imp.getSocialReasonName());
            formularios1DTO.setIdCiudadImportador(0);
            formularios1DTO.setCondicionesPago(condicionesPago);
            formularios1DTO.setCondicionesDespacho(condicionesDespacho);
            formularios1DTO.setObservaciones(imp.getObservations());
            formularios1DTO.setEstado(Short.valueOf("1"));
            formularios1DTO.setEstadoF10(Short.valueOf("0"));
            formularios1DTO.setCuentaCompensacion(imp.getCompensationAccountCode());
            formularios1DTO.setNumeroCuentaCompensacionInicial(imp.getFinanceEntityAccountNumber());
            if (imp.getConsecutiveOld() == null) {
                formularios1DTO.setNumeroCuentaCompensacionAnterior("0");
            } else {
                formularios1DTO.setNumeroCuentaCompensacionAnterior(imp.getFinanceEntityAccountNumber());
            }
            formularios1DTO.setNumeroIdentificacionCliente(String.valueOf(imp.getDocumentNumber()));
            formularios1DTO.setNombreDeclarante(null);
            formularios1DTO.setNumeroIdentificacionDeclarante(null);
            formularios1DTO.setInfoAduanera(imp.getCustomsOfficerInformation().equals("INCOMPLETA") ? "0" : "1");

            List<OperacionesFormulario1DTO> operaciones = new ArrayList<>();
            //Operacion 1
            OperacionesFormulario1DTO operacion1 = new OperacionesFormulario1DTO();
            operacion1.setIdOperacion(null);

            Monedas moneda1 = monedasRepository.findByCodigo(imp.getGiroCurrencyCode().substring(0, 3));
            operacion1.setIdMonedaGiro(moneda1.getId());

            Numerales numeral1 = numeralesRepository.findByCodigo(imp.getNumeral());
            operacion1.setIdNumeral(numeral1.getId());

            operacion1.setTipoCambioUsd(imp.getTypeChangeToUsd());
            operacion1.setValorMonedaGiro(imp.getGiroCurrencyValue());
            operacion1.setValorUsd(imp.getUsdValue());
            operacion1.setIdFormulario1(null);
            operaciones.add(operacion1);

            //Operacion 2
            if (imp.getNumeralTwo() != 0) {
                OperacionesFormulario1DTO operacion2 = new OperacionesFormulario1DTO();
                operacion2.setIdOperacion(null);

                Monedas moneda2 = monedasRepository.findByCodigo(imp.getGiroCurrencyCodeTwo().substring(0, 3));
                operacion2.setIdMonedaGiro(moneda2.getId());

                Numerales numeral2 = numeralesRepository.findByCodigo(imp.getNumeralTwo());
                operacion2.setIdNumeral(numeral2.getId());

                operacion2.setTipoCambioUsd(imp.getTypeChangeToUsdTwo());
                operacion2.setValorMonedaGiro(imp.getGiroCurrencyValueTwo());
                operacion2.setValorUsd(imp.getUsdValueTwo());
                operacion2.setIdFormulario1(null);
                operaciones.add(operacion2);
            }

            formularios1DTO.setOperacionesFormulario1Collection(operaciones);

            List<DocumentosTransporteDTO> documentosTransporteDTO = new ArrayList<>();
            formularios1DTO.setDocumentosTransporteCollection(documentosTransporteDTO);

            List<DocumentosImportacionDTO> documentosImportacion = new ArrayList<>();

            if (imp.getCustomsOfficerNumberDocument() != null) {
                DocumentosImportacionDTO documentosImportacionDTO = new DocumentosImportacionDTO();
                documentosImportacionDTO.setIdDocumento(null);
                documentosImportacionDTO.setAnho("0");
                documentosImportacionDTO.setNumero(imp.getCustomsOfficerNumberDocument());
                documentosImportacionDTO.setValorUsd(imp.getUsdFobValue());
                documentosImportacionDTO.setIdFormulario1(null);
                documentosImportacion.add(documentosImportacionDTO);
            }
            formularios1DTO.setDocumentosImportacionCollection(documentosImportacion);

            formularios1DTO.setMovimiento(null);
            formularios1DTO.setDetallesMovimientos(null);
            formularios1DTO.setMovenment(false);

            formularios1DTOList.add(formularios1DTO);

        }

        List<Formularios1DTO> listToSave = groupingTemplateInfo(formularios1DTOList);
        for (Formularios1DTO formToSave : listToSave) {
            formularios1Service.saveFormOne(formToSave);
        }

        return formularios1DTOList;
    }

    public List<Formularios1DTO> groupingTemplateInfo(List<Formularios1DTO> formularios1DTOList) {
        //TODO validar el completo o imcompleto
        //TODO validar no hay mas de 2 numerales
        Map<String, Map<Integer, Map<LocalDateTime, List<Formularios1DTO>>>> multipleFieldsMap = formularios1DTOList.stream()
                .collect(
                        Collectors.groupingBy(Formularios1DTO::getCuentaCompensacion,
                                Collectors.groupingBy(Formularios1DTO::getConsecutivo,
                                        Collectors.groupingBy(Formularios1DTO::getFechaInicial))));


        List<Formularios1DTO> listToSave = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Map<LocalDateTime, List<Formularios1DTO>>>> entry : multipleFieldsMap.entrySet()) {
            for (Map.Entry<Integer, Map<LocalDateTime, List<Formularios1DTO>>> entry2 : entry.getValue().entrySet()) {
                for (Map.Entry<LocalDateTime, List<Formularios1DTO>> entry3 : entry2.getValue().entrySet()) {

                    List<Formularios1DTO> listF1 = entry3.getValue();

                    List<OperacionesFormulario1DTO> operToSave = new ArrayList<>();
                    List<DocumentosImportacionDTO> documentsToSave = new ArrayList<>();
                    for (int i = 0; i < listF1.size(); i++) {
                        List<OperacionesFormulario1DTO> operTmp = (List<OperacionesFormulario1DTO>) listF1.get(i).getOperacionesFormulario1Collection();

                        if (operTmp.size() > 1) {
                            if (operTmp.get(0).getIdNumeral().equals(operTmp.get(1).getIdNumeral())) {
                                BigDecimal sumMonedaGiro = operTmp.get(0).getValorMonedaGiro().add(operTmp.get(1).getValorMonedaGiro());

                                operTmp.get(0).setValorMonedaGiro(sumMonedaGiro.setScale(2, RoundingMode.HALF_UP));
                                operToSave.add(operTmp.get(0));
                            }
                            {
                                operToSave.add(operTmp.get(0));
                                operToSave.add(operTmp.get(1));
                            }

                        } else {
                            operToSave.add(operTmp.get(0));
                        }

                        for (DocumentosImportacionDTO docsTmp : listF1.get(i).getDocumentosImportacionCollection()) {
                            documentsToSave.add(docsTmp);
                        }

                    }

                    Map<OperacionesFormulario1DTO, List<OperacionesFormulario1DTO>> operToSaveGroup = new HashMap<>();
                    operToSaveGroup = operToSave.stream().collect(Collectors.groupingBy(OperacionesFormulario1DTO::getIdNumeral))
                            .entrySet().stream()
                            .collect(Collectors.toMap(x -> {
                                BigDecimal sumValorMonedaGiro = x.getValue().stream().map(OperacionesFormulario1DTO::getValorMonedaGiro).reduce(BigDecimal.ZERO, BigDecimal::add);
                                BigDecimal sumValorUsd = x.getValue().stream().map(OperacionesFormulario1DTO::getValorUsd).reduce(BigDecimal.ZERO, BigDecimal::add);

                                OperacionesFormulario1DTO o = new OperacionesFormulario1DTO();
                                o.setIdOperacion(null);
                                o.setIdMonedaGiro(x.getValue().get(0).getIdMonedaGiro());
                                o.setIdNumeral(x.getValue().get(0).getIdNumeral());
                                o.setTipoCambioUsd(x.getValue().get(0).getTipoCambioUsd());
                                o.setValorMonedaGiro(sumValorMonedaGiro);
                                o.setValorUsd(sumValorUsd);
                                o.setIdFormulario1(null);
                                return o;

                            }, Map.Entry::getValue));


                    List<OperacionesFormulario1DTO> operToSaveGroupFinal = new ArrayList<>();
                    for (Map.Entry<OperacionesFormulario1DTO, List<OperacionesFormulario1DTO>> mEntry : operToSaveGroup.entrySet()) {
                        operToSaveGroupFinal.add(mEntry.getKey());
                    }

                    listF1.get(0).setOperacionesFormulario1Collection(operToSaveGroupFinal);
                    listF1.get(0).setDocumentosImportacionCollection(documentsToSave);
                    listToSave.add(listF1.get(0));
                }
            }

        }

        return listToSave;

    }

    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("America/Bogota"))
                .toLocalDateTime();
    }

    @Override
    public byte[] generateErrorPdf(List<MassiveLoadErrorDTO> errorDTOS, String file) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream,  new WriterProperties().setCompressionLevel(0)));
            Document document = new Document(pdfDocument, PageSize.A4);
            PdfHeaderEventHandler headerEventHandler = new PdfHeaderEventHandler(document);
            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerEventHandler);
            document.setMargins(headerEventHandler.getTableHeight(), 30, 30, 30);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.setFont(font);

            String reportDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String pdfTitle = "Informe Detallado de errores identificados \n" +
                    "en el procesamiento dfe la información \n" +
                    "Carga Masiva \n \n";

            Paragraph titleParagraph = new Paragraph().add(new Text(pdfTitle)).setFontSize(13).setBold();
            document.add(titleParagraph);
            Paragraph dateParagraph = new Paragraph().add(new Text("Fecha de reporte: ").setFontSize(13).setBold())
                    .add(new Text(reportDate)).setFontSize(12)
                    .add("\n");

            document.add(dateParagraph);
            String documentDescription = "¡Hola! \n" +
                    "Identificamos algunos errores en tus plantillas. Para terminar el proceso de carga debes " +
                    "corregirlos y volver a subir los documentos. Encuentra en este reporte la lista de errores que " +
                    "debes cambiar. \n \n";

            document.add(new Paragraph(new Text(documentDescription)));

            Table table = new Table(UnitValue.createPercentArray(new float[]{20, 25, 15, 40}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setSpacingRatio(30f);

            Color grayColor = new DeviceRgb(224, 224, 224);
            //Headers de la tabla
            Cell headerCell = new Cell().add(new Paragraph("Tipo de Plantilla").setBold()).setBackgroundColor(grayColor)
                    .setBorder(Border.NO_BORDER);
            table.addHeaderCell(headerCell);
            headerCell = new Cell().add(new Paragraph("Nombre del archivo").setBold()).setBackgroundColor(grayColor)
                    .setBorder(Border.NO_BORDER);
            table.addHeaderCell(headerCell);
            headerCell = new Cell().add(new Paragraph("Celda").setBold().setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(grayColor)
                    .setBorder(Border.NO_BORDER);
            table.addHeaderCell(headerCell);
            headerCell = new Cell().add(new Paragraph("Descripción").setBold()).setBackgroundColor(grayColor)
                    .setBorder(Border.NO_BORDER);
            table.addHeaderCell(headerCell);

            for (MassiveLoadErrorDTO massiveLoadErrorDTO : errorDTOS) {
                // Se añaden los registros
                table.addCell(new Cell().add(new Paragraph(massiveLoadErrorDTO.getFormName()).setFontSize(9f)).setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph(massiveLoadErrorDTO.getFileName()).setFontSize(10f)).setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph(massiveLoadErrorDTO.getCellDescription()).setFontSize(12f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph(massiveLoadErrorDTO.getErrorDescription())).setBorder(Border.NO_BORDER));

            }

            document.add(table);
            document.close();

            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }

    }

}
