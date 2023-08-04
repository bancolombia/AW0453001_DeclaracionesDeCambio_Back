package co.com.bancolombia.declaraciones.libcommons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MassiveLoadDto {
    private boolean success;
    private String fileName;
    private Integer numberOfRegisters;
    private String formType;
    private String detail;
    private List<MassiveLoadErrorDTO> errors;


    private List<Object> registers;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public void setNumberOfRegisters(Integer numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<MassiveLoadErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<MassiveLoadErrorDTO> errors) {
        this.errors = errors;
    }

    public List<Object> getRegisters() {
        return registers;
    }

    public void setRegisters(List<Object> registers) {
        this.registers = registers;
    }
}
