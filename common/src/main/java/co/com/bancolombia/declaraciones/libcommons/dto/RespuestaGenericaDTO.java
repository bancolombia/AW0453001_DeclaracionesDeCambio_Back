package co.com.bancolombia.declaraciones.libcommons.dto;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@ToString

public class RespuestaGenericaDTO implements Serializable {
    private static final long serialVersionUID = 3595048299923055545L;
    private Integer status;
    private String message;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
