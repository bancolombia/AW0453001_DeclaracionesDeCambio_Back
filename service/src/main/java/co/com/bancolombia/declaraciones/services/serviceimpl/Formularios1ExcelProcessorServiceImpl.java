package co.com.bancolombia.declaraciones.services.serviceimpl;

import co.com.bancolombia.declaraciones.libcommons.dto.MLImportacionesDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadErrorDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.MassiveLoadException;
import co.com.bancolombia.declaraciones.libcommons.helpers.MassiveLoadExcelProcessorHelper;
import co.com.bancolombia.declaraciones.services.service.IFormularios1ExcelProcessorService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class Formularios1ExcelProcessorServiceImpl implements IFormularios1ExcelProcessorService {
    private List<MassiveLoadErrorDTO> errors;
    private List<MLImportacionesDTO> formOneRegisters;
    private final MassiveLoadExcelProcessorHelper massiveLoadExcelProcessorHelper = new MassiveLoadExcelProcessorHelper();

    @Override
    public void processData(XSSFWorkbook workbook) {
        this.errors = new ArrayList<>();
        this.formOneRegisters = new ArrayList<>();
        XSSFSheet xssfSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = xssfSheet.rowIterator();
        int rowCounter = 0;
        int lineToStart = 4;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell documentTypeCell = row.getCell(0);
            Cell documentNumberCell = row.getCell(1);
            Cell validationDigitCell = row.getCell(2);
            Cell socialReasonNameCell = row.getCell(3);
            Cell compensationAccountCodeCell = row.getCell(4);
            Cell financeEntityAccountNumberCell = row.getCell(5);
            Cell typeOperationCell = row.getCell(6);
            Cell formDateCell = row.getCell(7);
            Cell consecutiveCell = row.getCell(8);
            Cell documentOldDateCell = row.getCell(9);
            Cell consecutiveOldCell = row.getCell(10);
            Cell giroCurrencyCodeCell = row.getCell(11);
            Cell typeChangeToUsdCell = row.getCell(12);
            Cell numeralCell = row.getCell(13);
            Cell giroCurrencyValueCell = row.getCell(14);
            Cell usdValueCell = row.getCell(15);
            Cell giroCurrencyCodeTwoCell = row.getCell(16);
            Cell typeChangeToUsdTwoCell = row.getCell(17);
            Cell numeralTwoCell = row.getCell(18);
            Cell giroCurrencyValueTwoCell = row.getCell(19);
            Cell usdValueTwoCell = row.getCell(20);
            Cell customsOfficerNumberDocumentCell = row.getCell(21);
            Cell usdFobValueCell = row.getCell(22);
            Cell customsOfficerInformationCell = row.getCell(23);
            Cell observationsCell = row.getCell(24);
            if (rowCounter >= lineToStart) {


                try {
                    String documentTypeStr = this.getValueFromCell(documentTypeCell, false);
                    String documentNumberStr = this.getValueFromCell(documentNumberCell, false);
                    if (documentNumberStr == null || documentTypeStr == null || documentNumberStr.isEmpty() || documentTypeStr.isEmpty()) {
                        rowCounter++;
                        continue;
                    }

                    MLImportacionesDTO mlImportacionesDTO = new MLImportacionesDTO();
                    mlImportacionesDTO.setDocumentType(documentTypeStr);


                    mlImportacionesDTO.setDocumentNumber((long) Double.parseDouble(documentNumberStr));

                    String dvStr = this.getValueFromCell(validationDigitCell, false);
                    if (documentTypeStr.equalsIgnoreCase("NI")) {
                        if (dvStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(validationDigitCell.getColumnIndex(), rowCounter)
                                    , "Debes diligenciar el digito de verificacion (DV) cuando el documento es NI");
                        } else {
                            mlImportacionesDTO.setValidationDigit((int) Double.parseDouble(dvStr));
                        }

                    } else {
                        if (!dvStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(validationDigitCell.getColumnIndex(), rowCounter)
                                    , "El Digito de Verificación solo se debe diligenciar cuando el tipo de documento es NI");
                        }
                    }
                    mlImportacionesDTO.setSocialReasonName(this.getValueFromCell(socialReasonNameCell, false));
                    mlImportacionesDTO.setCompensationAccountCode(this.getValueFromCell(compensationAccountCodeCell, false));
                    mlImportacionesDTO.setFinanceEntityAccountNumber(this.getValueFromCell(financeEntityAccountNumberCell, false));
                    String typeOperationStr = this.getValueFromCell(typeOperationCell, false);
                    mlImportacionesDTO.setTypeOperation(typeOperationStr);
                    String formDateStr =  this.getValueFromCell(formDateCell, false);
                    if (formDateStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(formDateCell.getColumnIndex(), rowCounter)
                            , "Debes diligenciar la fecha del formulario ");
                    } else {
                        mlImportacionesDTO.setFormDate(this.massiveLoadExcelProcessorHelper.getDateFromString(formDateStr));
                    }
                    mlImportacionesDTO.setConsecutive(this.getValueFromCell(consecutiveCell, false));

                    String documentOldDateStr = this.getValueFromCell(documentOldDateCell, false);
                    String consecutiveOldStr = this.getValueFromCell(consecutiveOldCell, false);
                    if (typeOperationStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(typeOperationCell.getColumnIndex(), rowCounter), "Debes seleccionar el tipo de operación");
                    } else {
                        if (typeOperationStr.substring(0, 2).equalsIgnoreCase("3.") || typeOperationStr.substring(0, 2).equalsIgnoreCase("4.")) {
                            if (documentOldDateStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                                this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(documentOldDateCell.getColumnIndex(), rowCounter)
                                        , "Debes diligenciar la fecha del documento anterior debido al tipo de operación seleccionada ");
                            } else {
                                mlImportacionesDTO.setDocumentOldDate(this.massiveLoadExcelProcessorHelper.getDateFromString(documentOldDateStr));
                            }
                            if (consecutiveOldStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                                this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(consecutiveOldCell.getColumnIndex(), rowCounter)
                                        , "Debes diligenciar el consecutivo anterior debido al tipo de operación seleccionada ");
                            }
                            mlImportacionesDTO.setConsecutiveOld(consecutiveOldStr);
                        } else {
                            if (!documentOldDateStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                                this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(documentNumberCell.getColumnIndex(), rowCounter)
                                        , "Este campo no se debe diligenciar debido al tipo de operación seleccionado");
                            }
                            if (!documentOldDateStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                                this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(documentNumberCell.getColumnIndex(), rowCounter)
                                        , "Este campo no se debe diligenciar debido al tipo de operación seleccionado");
                            }
                        }
                    }
                    mlImportacionesDTO.setGiroCurrencyCode(this.getValueFromCell(giroCurrencyCodeCell, false));
                    String typeChangeUsdStr = this.getValueFromCell(typeChangeToUsdCell, true);
                    if (typeChangeUsdStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(typeChangeToUsdCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor de cambio usd");
                    } else {
                        mlImportacionesDTO.setTypeChangeToUsd(BigDecimal.valueOf(Double.parseDouble(typeChangeUsdStr)));
                    }

                    String numeralStr = this.getValueFromCell(numeralCell, false);
                    if (numeralStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(numeralCell.getColumnIndex(), rowCounter), "Debes seleccionar el numeral");
                    } else {
                        mlImportacionesDTO.setNumeral((int) Double.parseDouble(numeralStr));
                    }

                    String giroCurrencyValueStr = this.getValueFromCell(giroCurrencyValueCell, true);
                    if (giroCurrencyValueStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(giroCurrencyValueCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor moneda de giro");
                    } else {
                        mlImportacionesDTO.setGiroCurrencyValue(BigDecimal.valueOf(Double.parseDouble(giroCurrencyValueStr)));
                    }

                    String valueUsdStr = this.getValueFromCell(usdValueCell, true);
                    if (valueUsdStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(usdValueCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor de cambio usd");
                    } else {
                        mlImportacionesDTO.setUsdValue(BigDecimal.valueOf(Double.parseDouble(valueUsdStr)));
                    }

                    String currencyCodeTwoStr = this.getValueFromCell(giroCurrencyCodeTwoCell, false);
                    String typeChangeUsdTwoStr = this.getValueFromCell(typeChangeToUsdTwoCell, true);
                    String giroCurrencyValueTwoStr = this.getValueFromCell(giroCurrencyValueTwoCell, true);
                    String numeralTwoStr = this.getValueFromCell(numeralTwoCell, false);
                    String valueUsdTwoStr = this.getValueFromCell(usdValueTwoCell, true);

                    if (!currencyCodeTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        mlImportacionesDTO.setGiroCurrencyCodeTwo(currencyCodeTwoStr);
                        if (typeChangeUsdTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(typeChangeToUsdTwoCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor de cambio usd");
                        } else {
                            mlImportacionesDTO.setTypeChangeToUsdTwo(BigDecimal.valueOf(Double.parseDouble(typeChangeUsdTwoStr)));
                        }
                        if (numeralTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(numeralTwoCell.getColumnIndex(), rowCounter), "Debes seleccionar el numeral");
                        } else {
                            mlImportacionesDTO.setNumeralTwo((int) Double.parseDouble(numeralTwoStr));
                        }
                        if (giroCurrencyValueTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(giroCurrencyValueTwoCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor moneda de giro");
                        } else {
                            mlImportacionesDTO.setGiroCurrencyValueTwo(BigDecimal.valueOf(Double.parseDouble(giroCurrencyValueTwoStr)));
                        }
                        if (valueUsdTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(usdValueTwoCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor de cambio usd");
                        } else {
                            mlImportacionesDTO.setUsdValueTwo(BigDecimal.valueOf(Double.parseDouble(valueUsdTwoStr)));
                        }

                    } else {
                        if (!typeChangeUsdTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(typeChangeToUsdTwoCell.getColumnIndex(), rowCounter), "El valor de cambio usd no debe estar diligenciado");
                        }

                        if (!numeralTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(numeralTwoCell.getColumnIndex(), rowCounter), "No puedes seleccionar el numeral debido a que no seleccionaste el código de moneda de giro 2");
                        }

                        if (!giroCurrencyValueTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(giroCurrencyValueTwoCell.getColumnIndex(), rowCounter), "El valor moneda de giro no debe ser diligenciado");
                        }
                        if (!valueUsdTwoStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(usdValueTwoCell.getColumnIndex(), rowCounter), "El valor de cambio usd no debe ser diligenciado");
                        }
                    }

                    String customsOfficerNumberDocumentStr = this.getValueFromCell(customsOfficerNumberDocumentCell, false);
                    if (!customsOfficerNumberDocumentStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        mlImportacionesDTO.setCustomsOfficerNumberDocument(customsOfficerNumberDocumentStr);

                        String fobValueStr = this.getValueFromCell(usdFobValueCell, true);
                        if (fobValueStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(usdFobValueCell.getColumnIndex(), rowCounter),
                                    "Debes diliegnciar el campo debido a que tienes diligenciado el documento aduanero");
                        } else {
                            mlImportacionesDTO.setUsdFobValue(BigDecimal.valueOf(Double.parseDouble(fobValueStr)));
                        }
                    }
                    mlImportacionesDTO.setCustomsOfficerInformation(this.getValueFromCell(customsOfficerInformationCell, false));
                    mlImportacionesDTO.setObservations(this.getValueFromCell(observationsCell, false));


                    validateLines(mlImportacionesDTO, rowCounter);
                    this.formOneRegisters.add(mlImportacionesDTO);

                } catch (MassiveLoadException e) {
                    MassiveLoadErrorDTO massiveLoadErrorDTO = new MassiveLoadErrorDTO();
                    String cellDescription = this.massiveLoadExcelProcessorHelper.getCellDescription(e.getCellNumber(), rowCounter);

                    massiveLoadErrorDTO.setErrorDescription(e.getMessage());
                    massiveLoadErrorDTO.setCellDescription(cellDescription);
                    this.errors.add(massiveLoadErrorDTO);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            rowCounter++;


        }
    }

    @Override
    public List<MLImportacionesDTO> getRegisters() {
        return this.formOneRegisters;
    }

    @Override
    public List<MassiveLoadErrorDTO> getErrors() {
        return this.errors;
    }

    private void validateLines(MLImportacionesDTO mlImportacionesDTO, int rowCounter) {
        if (mlImportacionesDTO.getDocumentType().equalsIgnoreCase(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(0, rowCounter), "Debes seleccionar el tipo de documento");
        }
        if (mlImportacionesDTO.getSocialReasonName().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(3, rowCounter), "Debes diligenciar el nombre de la razón social");
        }
        if (mlImportacionesDTO.getCompensationAccountCode().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(4, rowCounter), "Debes diligenciar el código de cuenta de compensación");
        }
        if (mlImportacionesDTO.getFinanceEntityAccountNumber().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(5, rowCounter), "Debes diligencia el número de cuenta de la entidad financiera");
        }
        if (mlImportacionesDTO.getConsecutive().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(8, rowCounter), "Debes diligenciar el consecutivo");
        }
        if (mlImportacionesDTO.getGiroCurrencyCode().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(11, rowCounter), "Debes seleccionar el código moneda de giro");
        }

        if (mlImportacionesDTO.getCustomsOfficerInformation().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(23, rowCounter), "Debes seleccionar la información aduanera");
        }
    }

    @Override
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }


    private void addErrorToList(String cellDescription, String errorDescription) {
        this.errors.add(MassiveLoadErrorDTO.builder()
                .cellDescription(cellDescription)
                .errorDescription(errorDescription).build());
    }

    public String getValueFromCell(Cell xssfCell, boolean isDouble) throws MassiveLoadException {
        if (xssfCell != null) {
            switch (xssfCell.getCellType()) {
                case _NONE:
                case STRING:
                    return xssfCell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(xssfCell)) {
                        return String.valueOf(xssfCell.getDateCellValue());
                    } else {
                        if (!isDouble) {
                            long number = (long)xssfCell.getNumericCellValue();
                            return Long.toString(number);
                        } else {
                            return Double.toString(xssfCell.getNumericCellValue());
                        }

                    }
                case FORMULA:
                    switch (xssfCell.getCachedFormulaResultType()) {
                        case _NONE:
                        case STRING:
                            return xssfCell.getRichStringCellValue().getString();
                        case NUMERIC:
                            if(!isDouble) {
                                long number = (long) xssfCell.getNumericCellValue();
                                return Long.toString(number);
                            } else {
                                return Double.toString(xssfCell.getNumericCellValue());
                            }
                    }
                case BLANK:
                    return org.apache.commons.lang3.StringUtils.EMPTY;
                case BOOLEAN:
                    return String.valueOf(xssfCell.getBooleanCellValue());
                case ERROR:
                    throw new MassiveLoadException("Existe un error inesperado en la celda", xssfCell.getColumnIndex());
                default:
                    throw new MassiveLoadException("No se puede identificar el tipo de dato en la celda", xssfCell.getColumnIndex());

            }
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
    }
}
