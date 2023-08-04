package co.com.bancolombia.declaraciones.rest.controller;


import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.com.bancolombia.declaraciones.libcommons.dto.PaginatorDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.RespuestaGenericaDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFPeriodoActivoDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.ResumenFRequestLegalizacionDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.BusinessException;
import co.com.bancolombia.declaraciones.libcommons.exceptions.ErrorCodes;
import co.com.bancolombia.declaraciones.libcommons.exceptions.TechnicalException;
import co.com.bancolombia.declaraciones.libcommons.securitytools.GenerateKey;
import co.com.bancolombia.declaraciones.libcommons.securitytools.JwtDecode;
import co.com.bancolombia.declaraciones.services.service.ILegalizacionesService;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/bancolombia/declaraciones/")
public class LegalizacionesController {

    @Autowired
    private ILegalizacionesService legalizacionService;

    public static final Logger logger = Logger.getLogger(LegalizacionesController.class);

    @PostMapping("legalizacion/list")
    public ResponseEntity<Object> consultarLegalizaciones(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody ResumenFRequestLegalizacionDTO resumenFReq) {

        Pageable pageable = new PageRequest(0, 10);
        PaginatorDTO paginable = resumenFReq.getPaginator();
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();

        if (paginable != null)
            pageable = new PageRequest(paginable.getPage(), paginable.getSize());

        try {
            ResumenFPeriodoActivoDTO list = legalizacionService
                    .findLegalizacionByFilter(numeroIdentificacionCliente, resumenFReq, pageable);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (BusinessException e) {
            logger.error("LEGALIZACIONES CONTROLLER: Error en el servicio consultarLegalizaciones. BusinessException: " + e.getCause());
            respuestaGenericaDTO.setStatus(e.getErrorCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (TechnicalException e) {
            logger.error("LEGALIZACIONES CONTROLLER: Error en el servicio consultarLegalizaciones(). TechnicalException: " + e.getCause());
            respuestaGenericaDTO.setStatus(e.getErrorCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("LEGALIZACIONES CONTROLLER: Error en el servicio consultarLegalizaciones. Exception: " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operacion: " + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("legalizacion/countLegalizacionesPendientes")
    public ResponseEntity<Object> countLegalizacionesPendientes(
            @RequestHeader(value = "Authorization") String authorization) {

        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        try {

            long pendientes = legalizacionService
                    .countLegalizacionesPendientes(numeroIdentificacionCliente);
            return new ResponseEntity<>(pendientes, HttpStatus.OK);
        } catch (TechnicalException e) {
            logger.error("LEGALIZACIONES CONTROLLER: Error en el servicio countLegalizacionesPendientes(). TechnicalException: " + e.getCause());
            respuestaGenericaDTO.setStatus(e.getErrorCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("LEGALIZACIONES CONTROLLER: Error en el servicio countLegalizacionesPendientes(). " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Error en countLegalizacionesPendientes: " + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}