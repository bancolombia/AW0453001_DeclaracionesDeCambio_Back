package co.com.bancolombia.declaraciones.services.serviceimpl;

import co.com.bancolombia.declaraciones.libcommons.dto.MLEndeudamientoDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.MassiveLoadErrorDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.MassiveLoadException;
import co.com.bancolombia.declaraciones.libcommons.helpers.MassiveLoadExcelProcessorHelper;
import co.com.bancolombia.declaraciones.services.service.IFormularios3ExcelProcessorService;
import org.apache.commons.lang3.StringUtils;
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
public class Formularios3ExcelProcessorServiceImpl implements IFormularios3ExcelProcessorService {
    private List<MassiveLoadErrorDTO> errors;
    private List<MLEndeudamientoDTO> formThreeRegisters;
    private final MassiveLoadExcelProcessorHelper massiveLoadExcelProcessorHelper = new MassiveLoadExcelProcessorHelper();


    @Override
    public void processData(XSSFWorkbook workbook) {
        this.errors = new ArrayList<>();
        this.formThreeRegisters = new ArrayList<>();
        XSSFSheet xssfSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = xssfSheet.rowIterator();
        int rowCounter = 0;
        int lineToStart = 4;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell documentTypeCell = row.getCell(0);
            Cell documentNumberCell = row.getCell(1);
            Cell validationDigitCell = row.getCell(2);
            Cell compensationAccountCodeCell = row.getCell(3);
            Cell financeEntityAccountNumberCell = row.getCell(4);
            Cell typeOperationCell = row.getCell(5);
            Cell operationExpenseIncomeCell = row.getCell(6);
            Cell cityCell = row.getCell(7);
            Cell formDateCell = row.getCell(8);
            Cell consecutiveCell = row.getCell(9);
            Cell documentOldDateCell = row.getCell(10);
            Cell consecutiveOldCell = row.getCell(11);
            Cell loanOrGuaranteeNumberCell = row.getCell(12);
            Cell nameDebtorOrCreditorCell = row.getCell(13);
            Cell numeralCell = row.getCell(14);
            Cell contractedCurrencyCell = row.getCell(15);
            Cell contractedCurrencyValueCell = row.getCell(16);
            Cell currencyTradingCell = row.getCell(17);
            Cell typeChangeToUsdCell = row.getCell(18);
            Cell currencyTradingValueCell = row.getCell(19);
            Cell giroCurrencyValueCell = row.getCell(20);
            Cell usdValueCell = row.getCell(21);

            if (rowCounter >= lineToStart) {
                try {
                    String documentTypeStr = this.getValueFromCell(documentTypeCell, false);
                    String documentNumberStr = this.getValueFromCell(documentNumberCell, false);
                    if (documentNumberStr == null || documentTypeStr == null || documentNumberStr.isEmpty() || documentTypeStr.isEmpty()) {
                        rowCounter++;
                        continue;
                    }
                    MLEndeudamientoDTO mlEndeudamientoDTO = new MLEndeudamientoDTO();
                    mlEndeudamientoDTO.setDocumentType(documentTypeStr);


                    mlEndeudamientoDTO.setDocumentNumber((long) Double.parseDouble(documentNumberStr));

                    String dvStr = this.getValueFromCell(validationDigitCell, false);
                    if (documentTypeStr.equalsIgnoreCase("NI")) {
                        if (dvStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(validationDigitCell.getColumnIndex(), rowCounter)
                                    , "Debes diligenciar el digito de verificacion (DV) cuando el documento es NI");
                        } else {
                            mlEndeudamientoDTO.setValidationDigit((int) Double.parseDouble(dvStr));
                        }

                    } else {
                        if (!dvStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(validationDigitCell.getColumnIndex(), rowCounter)
                                    , "El Digito de Verificación solo se debe diligenciar cuando el tipo de documento es NI");
                        }
                    }
                    mlEndeudamientoDTO.setCompensationAccountCode(this.getValueFromCell(compensationAccountCodeCell, false));
                    mlEndeudamientoDTO.setFinanceEntityAccountNumber(this.getValueFromCell(financeEntityAccountNumberCell, false));
                    String typeOperationStr = this.getValueFromCell(typeOperationCell, false);
                    mlEndeudamientoDTO.setTypeOperation(typeOperationStr);
                    mlEndeudamientoDTO.setOperationExpenseIncome(this.getValueFromCell(operationExpenseIncomeCell, false).substring(0, 1));
                    mlEndeudamientoDTO.setCity(this.getValueFromCell(cityCell, false));
                    String dateFormStr = this.getValueFromCell(formDateCell, false);
                    if (dateFormStr.equalsIgnoreCase(StringUtils.EMPTY)){
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(formDateCell.getColumnIndex(), rowCounter)
                                , "Debes diligenciar la fecha del formulario ");
                    }else {
                        mlEndeudamientoDTO.setFormDate(this.massiveLoadExcelProcessorHelper.getDateFromString(dateFormStr));
                    }
                    mlEndeudamientoDTO.setConsecutive(this.getValueFromCell(consecutiveCell, false));

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
                                mlEndeudamientoDTO.setDocumentOldDate(this.massiveLoadExcelProcessorHelper.getDateFromString(documentOldDateStr));
                            }
                            if (consecutiveOldStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                                this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(consecutiveOldCell.getColumnIndex(), rowCounter)
                                        , "Debes diligenciar el consecutivo anterior debido al tipo de operación seleccionada ");
                            }
                            mlEndeudamientoDTO.setConsecutiveOld(consecutiveOldStr);
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
                    String loanOrGuaranteeNumberStr = this.getValueFromCell(loanOrGuaranteeNumberCell, false);
                    if (!loanOrGuaranteeNumberStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        if (loanOrGuaranteeNumberStr.length() != 11) {
                            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(loanOrGuaranteeNumberCell.getColumnIndex(), rowCounter), "El número de prestamo o aval debe tener solo 11 digitos");
                        } else {
                            mlEndeudamientoDTO.setLoanOrGuaranteeNumber(loanOrGuaranteeNumberStr);
                        }
                    }
                    mlEndeudamientoDTO.setNameDebtorOrCreditor(this.getValueFromCell(nameDebtorOrCreditorCell, false));
                    mlEndeudamientoDTO.setContractedCurrency(this.getValueFromCell(contractedCurrencyCell, false));
                    String contractedCurrencyValueStr = this.getValueFromCell(contractedCurrencyValueCell, true);
                    if (contractedCurrencyValueStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(contractedCurrencyValueCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor moneda de reintegro");
                    } else {
                        mlEndeudamientoDTO.setContractedCurrencyValue(BigDecimal.valueOf(Double.parseDouble(contractedCurrencyValueStr)));
                    }

                    String typeChangeUsdStr = this.getValueFromCell(typeChangeToUsdCell, true);
                    if (typeChangeUsdStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(typeChangeToUsdCell.getColumnIndex(), rowCounter), "Debes diligenciar el valor de cambio usd");
                    } else {
                        mlEndeudamientoDTO.setTypeChangeToUsd(BigDecimal.valueOf(Double.parseDouble(typeChangeUsdStr)));
                    }

                    String numeralStr = this.getValueFromCell(numeralCell, false);
                    if (numeralStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(numeralCell.getColumnIndex(), rowCounter), "Debes seleccionar el numeral");
                    } else {
                        mlEndeudamientoDTO.setNumeral((int) Double.parseDouble(numeralStr));
                    }
                    mlEndeudamientoDTO.setCurrencyTrading(this.getValueFromCell(currencyTradingCell, false));
                    String currencyTradingValueStr = this.getValueFromCell(currencyTradingValueCell, true);
                    if (currencyTradingValueStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(currencyTradingValueCell.getColumnIndex(), rowCounter),
                                "Debes diliegnciar el valor moneda de negociación");
                    } else {
                        mlEndeudamientoDTO.setCurrencyTradingValue(BigDecimal.valueOf(Double.parseDouble(currencyTradingValueStr)));
                    }

                    String giroCurrencyValueStr = this.getValueFromCell(giroCurrencyValueCell, true);
                    if (giroCurrencyValueStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(giroCurrencyValueCell.getColumnIndex(), rowCounter),
                                "Debes diliegnciar el valor moneda de giro");
                    } else {
                        mlEndeudamientoDTO.setGiroCurrencyValue(BigDecimal.valueOf(Double.parseDouble(giroCurrencyValueStr)));
                    }

                    String usdValueStr = this.getValueFromCell(usdValueCell, true);
                    if (usdValueStr.equalsIgnoreCase(StringUtils.EMPTY)) {
                        addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(usdValueCell.getColumnIndex(), rowCounter),
                                "Debes diliegnciar el valor USD");
                    } else {
                        mlEndeudamientoDTO.setUsdValue(BigDecimal.valueOf(Double.parseDouble(usdValueStr)));
                    }

                    validateLines(mlEndeudamientoDTO, rowCounter);
                    this.formThreeRegisters.add(mlEndeudamientoDTO);

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
    private void validateLines(MLEndeudamientoDTO mlEndeudamientoDTO, int rowCounter) {
        if (mlEndeudamientoDTO.getDocumentType().equalsIgnoreCase(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(0, rowCounter), "Debes seleccionar el tipo de documento");
        }
        if (mlEndeudamientoDTO.getCompensationAccountCode().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(4, rowCounter), "Debes diligenciar el código de cuenta de compensación");
        }
        if (mlEndeudamientoDTO.getFinanceEntityAccountNumber().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(5, rowCounter), "Debes diligencia el número de cuenta de la entidad financiera");
        }
        if (mlEndeudamientoDTO.getConsecutive().equals(StringUtils.EMPTY)) {
            this.addErrorToList(this.massiveLoadExcelProcessorHelper.getCellDescription(8, rowCounter), "Debes diligenciar el consecutivo");
        }
    }

    @Override
    public List<MLEndeudamientoDTO> getRegisters() {
        return this.formThreeRegisters;
    }

    @Override
    public List<MassiveLoadErrorDTO> getErrors() {
        return this.errors;
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
                            long number = (long) xssfCell.getNumericCellValue();
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
                            if (!isDouble) {
                                long number = (long) xssfCell.getNumericCellValue();
                                return Long.toString(number);
                            } else {
                                return Double.toString(xssfCell.getNumericCellValue());
                            }
                    }
                case BLANK:
                    return StringUtils.EMPTY;
                case BOOLEAN:
                    return String.valueOf(xssfCell.getBooleanCellValue());
                case ERROR:
                    throw new MassiveLoadException("Existe un error inesperado en la celda", xssfCell.getColumnIndex());
                default:
                    throw new MassiveLoadException("No se puede identificar el tipo de dato en la celda", xssfCell.getColumnIndex());

            }
        }
        return StringUtils.EMPTY;
    }
}
