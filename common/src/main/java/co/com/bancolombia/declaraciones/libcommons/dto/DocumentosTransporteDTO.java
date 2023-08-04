package co.com.bancolombia.declaraciones.libcommons.dto;


import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@ToString
public class DocumentosTransporteDTO implements Serializable {
    private static final long serialVersionUID = -2676722097069590088L;
    private Integer idDocumento;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fecha;
    private String numero;
    private String idFormulario1;
	public Integer getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getIdFormulario1() {
		return idFormulario1;
	}
	public void setIdFormulario1(String idFormulario1) {
		this.idFormulario1 = idFormulario1;
	}
}
