package co.com.bancolombia.declaraciones.libcommons.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@ToString

public class Formularios1DTO implements Serializable {
    private static final long serialVersionUID = -8721968908141965408L;
    private String consecutivoCompuesto;
    private Integer consecutivo;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // 2019-08-14T21:38:30.912Z
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fechaCreacion;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // 2019-08-14T21:38:30.912Z
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fechaUltimaModificacion;
    private Short tipoOperacion;
    private Integer idCiudadInicial;
    private Integer idCuentaCompensacionInicial;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // 2019-08-14T21:38:30.912Z
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fechaInicial;
    private Integer idCuentaCompensacionAnterior;
    private Integer consecutivoAnterior;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // 2019-08-14T21:38:30.912Z
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fechaAnterior;
    private Integer idTipoIdentificacionImportador;
    private Long numrIdentificadorImportador;
    private Integer digitoVerificacionImportador;
    private String nombreImportador;
    private Integer idCiudadImportador;
    private String condicionesPago;
    private String condicionesDespacho;
    private String observaciones;
    private Short estado;
    private Short estadoF10;
    private String firma;
    private String cuentaCompensacion;
    private String numeroCuentaCompensacionInicial;
    private String numeroCuentaCompensacionAnterior;
    private String numeroIdentificacionCliente;
    private String nombreDeclarante;
    private Long numeroIdentificacionDeclarante;
    private String infoAduanera;
    private Collection<OperacionesFormulario1DTO> operacionesFormulario1Collection;
    private Collection<DocumentosTransporteDTO> documentosTransporteCollection;
    private Collection<DocumentosImportacionDTO> documentosImportacionCollection;
    private List<MovimientosDTO> movimiento;
    private List<DetalleMovimientoDTO> detalleMovimiento;
    private boolean isMovenment;
	public String getConsecutivoCompuesto() {
		return consecutivoCompuesto;
	}
	public void setConsecutivoCompuesto(String consecutivoCompuesto) {
		this.consecutivoCompuesto = consecutivoCompuesto;
	}
	public Integer getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public LocalDateTime getFechaUltimaModificacion() {
		return fechaUltimaModificacion;
	}
	public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}
	public Short getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(Short tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public Integer getIdCiudadInicial() {
		return idCiudadInicial;
	}
	public void setIdCiudadInicial(Integer idCiudadInicial) {
		this.idCiudadInicial = idCiudadInicial;
	}
	public Integer getIdCuentaCompensacionInicial() {
		return idCuentaCompensacionInicial;
	}
	public void setIdCuentaCompensacionInicial(Integer idCuentaCompensacionInicial) {
		this.idCuentaCompensacionInicial = idCuentaCompensacionInicial;
	}
	public LocalDateTime getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(LocalDateTime fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public Integer getIdCuentaCompensacionAnterior() {
		return idCuentaCompensacionAnterior;
	}
	public void setIdCuentaCompensacionAnterior(Integer idCuentaCompensacionAnterior) {
		this.idCuentaCompensacionAnterior = idCuentaCompensacionAnterior;
	}
	public Integer getConsecutivoAnterior() {
		return consecutivoAnterior;
	}
	public void setConsecutivoAnterior(Integer consecutivoAnterior) {
		this.consecutivoAnterior = consecutivoAnterior;
	}
	public LocalDateTime getFechaAnterior() {
		return fechaAnterior;
	}
	public void setFechaAnterior(LocalDateTime fechaAnterior) {
		this.fechaAnterior = fechaAnterior;
	}
	public Integer getIdTipoIdentificacionImportador() {
		return idTipoIdentificacionImportador;
	}
	public void setIdTipoIdentificacionImportador(Integer idTipoIdentificacionImportador) {
		this.idTipoIdentificacionImportador = idTipoIdentificacionImportador;
	}
	public Long getNumrIdentificadorImportador() {
		return numrIdentificadorImportador;
	}
	public void setNumrIdentificadorImportador(Long numrIdentificadorImportador) {
		this.numrIdentificadorImportador = numrIdentificadorImportador;
	}
	public Integer getDigitoVerificacionImportador() {
		return digitoVerificacionImportador;
	}
	public void setDigitoVerificacionImportador(Integer digitoVerificacionImportador) {
		this.digitoVerificacionImportador = digitoVerificacionImportador;
	}
	public String getNombreImportador() {
		return nombreImportador;
	}
	public void setNombreImportador(String nombreImportador) {
		this.nombreImportador = nombreImportador;
	}
	public Integer getIdCiudadImportador() {
		return idCiudadImportador;
	}
	public void setIdCiudadImportador(Integer idCiudadImportador) {
		this.idCiudadImportador = idCiudadImportador;
	}
	public String getCondicionesPago() {
		return condicionesPago;
	}
	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}
	public String getCondicionesDespacho() {
		return condicionesDespacho;
	}
	public void setCondicionesDespacho(String condicionesDespacho) {
		this.condicionesDespacho = condicionesDespacho;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Short getEstado() {
		return estado;
	}
	public void setEstado(Short estado) {
		this.estado = estado;
	}
	public Short getEstadoF10() {
		return estadoF10;
	}
	public void setEstadoF10(Short estadoF10) {
		this.estadoF10 = estadoF10;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	public String getCuentaCompensacion() {
		return cuentaCompensacion;
	}
	public void setCuentaCompensacion(String cuentaCompensacion){
		this.cuentaCompensacion = cuentaCompensacion;
	}
	public String getNumeroCuentaCompensacionInicial() {
		return numeroCuentaCompensacionInicial;
	}
	public void setNumeroCuentaCompensacionInicial(String numeroCuentaCompensacionInicial) {
		this.numeroCuentaCompensacionInicial = numeroCuentaCompensacionInicial;
	}
	public String getNumeroCuentaCompensacionAnterior() {
		return numeroCuentaCompensacionAnterior;
	}
	public void setNumeroCuentaCompensacionAnterior(String numeroCuentaCompensacionAnterior) {
		this.numeroCuentaCompensacionAnterior = numeroCuentaCompensacionAnterior;
	}
	public String getNumeroIdentificacionCliente() {
		return numeroIdentificacionCliente;
	}
	public void setNumeroIdentificacionCliente(String numeroIdentificacionCliente) {
		this.numeroIdentificacionCliente = numeroIdentificacionCliente;
	}
	public String getNombreDeclarante() {
		return nombreDeclarante;
	}
	public void setNombreDeclarante(String nombreDeclarante) {
		this.nombreDeclarante = nombreDeclarante;
	}
	public Long getNumeroIdentificacionDeclarante() {
		return numeroIdentificacionDeclarante;
	}
	public void setNumeroIdentificacionDeclarante(Long numeroIdentificacionDeclarante) {
		this.numeroIdentificacionDeclarante = numeroIdentificacionDeclarante;
	}
	public String getInfoAduanera() {
		return infoAduanera;
	}
	public void setInfoAduanera(String infoAduanera) {
		this.infoAduanera = infoAduanera;
	}
	public Collection<OperacionesFormulario1DTO> getOperacionesFormulario1Collection() {
		return operacionesFormulario1Collection;
	}
	public void setOperacionesFormulario1Collection(
			Collection<OperacionesFormulario1DTO> operacionesFormulario1Collection) {
		this.operacionesFormulario1Collection = operacionesFormulario1Collection;
	}
	public Collection<DocumentosTransporteDTO> getDocumentosTransporteCollection() {
		return documentosTransporteCollection;
	}
	public void setDocumentosTransporteCollection(Collection<DocumentosTransporteDTO> documentosTransporteCollection) {
		this.documentosTransporteCollection = documentosTransporteCollection;
	}
	public Collection<DocumentosImportacionDTO> getDocumentosImportacionCollection() {
		return documentosImportacionCollection;
	}
	public void setDocumentosImportacionCollection(Collection<DocumentosImportacionDTO> documentosImportacionCollection) {
		this.documentosImportacionCollection = documentosImportacionCollection;
	}
	public List<DetalleMovimientoDTO> getDetalleMovimiento() {
		return detalleMovimiento;
	}
	public void setDetallesMovimientos(List<DetalleMovimientoDTO> detalleMovimiento) {
		this.detalleMovimiento = detalleMovimiento;
	}
	public boolean isMovenment() {
		return isMovenment;
	}
	public void setMovenment(boolean isMovenment) {
		this.isMovenment = isMovenment;
	}
	public List<MovimientosDTO> getMovimiento() {
		return movimiento;
	}
	public void setMovimiento(List<MovimientosDTO> movimiento) {
		this.movimiento = movimiento;
	}
}
