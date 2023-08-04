package co.com.bancolombia.declaraciones.libcommons.dto;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@ToString
public class CuentaCompensacionDTO implements Serializable {
    private static final long serialVersionUID = 8959062544752411325L;
    private String numeroCuentaCompensacion;
    private String codigoBancoRepublica;
    private String sucursal;
	public String getNumeroCuentaCompensacion() {
		return numeroCuentaCompensacion;
	}
	public void setNumeroCuentaCompensacion(String numeroCuentaCompensacion) {
		this.numeroCuentaCompensacion = numeroCuentaCompensacion;
	}
	public String getCodigoBancoRepublica() {
		return codigoBancoRepublica;
	}
	public void setCodigoBancoRepublica(String codigoBancoRepublica) {
		this.codigoBancoRepublica = codigoBancoRepublica;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
}