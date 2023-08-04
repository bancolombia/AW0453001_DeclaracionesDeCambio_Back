package co.com.bancolombia.declaraciones.libcommons.dto;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.ToString;



@NoArgsConstructor
@ToString

public class RegistroContrapartesDTO
		implements Serializable {
	private static final long serialVersionUID = -5880373302612920521L;
	private Long idRegistroContraparte;
	private String numeroCuentaCompensacion;
	private String nombre;
	private Integer idTipoIdentificacion;
	private String identificacion;
	private Character digitoVerificacion;
	private String codigoBancoRepublica;
	public Long getIdRegistroContraparte() {
		return idRegistroContraparte;
	}
	public void setIdRegistroContraparte(Long idRegistroContraparte) {
		this.idRegistroContraparte = idRegistroContraparte;
	}
	public String getNumeroCuentaCompensacion() {
		return numeroCuentaCompensacion;
	}
	public void setNumeroCuentaCompensacion(String numeroCuentaCompensacion) {
		this.numeroCuentaCompensacion = numeroCuentaCompensacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}
	public void setIdTipoIdentificacion(Integer idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
	}
	public String getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	public Character getDigitoVerificacion() {
		return digitoVerificacion;
	}
	public void setDigitoVerificacion(Character digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}
	public String getCodigoBancoRepublica() {
		return codigoBancoRepublica;
	}
	public void setCodigoBancoRepublica(String codigoBancoRepublica) {
		this.codigoBancoRepublica = codigoBancoRepublica;
	}
}



