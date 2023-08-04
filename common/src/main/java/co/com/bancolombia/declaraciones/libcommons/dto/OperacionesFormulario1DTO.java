package co.com.bancolombia.declaraciones.libcommons.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@ToString

public class OperacionesFormulario1DTO implements Serializable {
    private static final long serialVersionUID = -7873409532269820069L;
    private Integer idOperacion;
    private Integer idMonedaGiro;
    private Integer idNumeral;
    private BigDecimal tipoCambioUsd;
    private BigDecimal valorMonedaGiro;
    private BigDecimal valorUsd;
    private String idFormulario1;
	public Integer getIdOperacion() {
		return idOperacion;
	}
	public void setIdOperacion(Integer idOperacion) {
		this.idOperacion = idOperacion;
	}
	public Integer getIdMonedaGiro() {
		return idMonedaGiro;
	}
	public void setIdMonedaGiro(Integer idMonedaGiro) {
		this.idMonedaGiro = idMonedaGiro;
	}
	public Integer getIdNumeral() {
		return idNumeral;
	}
	public void setIdNumeral(Integer idNumeral) {
		this.idNumeral = idNumeral;
	}
	public BigDecimal getTipoCambioUsd() {
		return tipoCambioUsd;
	}
	public void setTipoCambioUsd(BigDecimal tipoCambioUsd) {
		this.tipoCambioUsd = tipoCambioUsd;
	}
	public BigDecimal getValorMonedaGiro() {
		return valorMonedaGiro;
	}
	public void setValorMonedaGiro(BigDecimal valorMonedaGiro) {
		this.valorMonedaGiro = valorMonedaGiro;
	}
	public BigDecimal getValorUsd() {
		return valorUsd;
	}
	public void setValorUsd(BigDecimal valorUsd) {
		this.valorUsd = valorUsd;
	}
	public String getIdFormulario1() {
		return idFormulario1;
	}
	public void setIdFormulario1(String idFormulario1) {
		this.idFormulario1 = idFormulario1;
	}
}
