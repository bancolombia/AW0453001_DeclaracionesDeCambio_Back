package co.com.bancolombia.declaraciones.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import co.com.bancolombia.declaraciones.libcommons.exceptions.PersistenceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.PutMapping;

import co.com.bancolombia.declaraciones.libcommons.securitytools.StringEncrypt;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.bancolombia.declaraciones.libcommons.dto.CuentaCompensacionDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.PaginatorDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.RegistroContrapartesDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.RespuestaGenericaDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.ErrorCodes;
import co.com.bancolombia.declaraciones.libcommons.securitytools.GenerateKey;
import co.com.bancolombia.declaraciones.libcommons.securitytools.JwtDecode;
import co.com.bancolombia.declaraciones.services.service.IRegistroContrapartesService;
import io.jsonwebtoken.Claims;

/**
 * The type Formularios controller
 *
 * @author 1627690
 */
@RestController
@RequestMapping("/bancolombia/declaraciones/")
public class RegistroContrapartesController {

    @Autowired
    private IRegistroContrapartesService registroContrapartesService;
    public static final String SALTODELINEA = "[\r\n]";
    public static final String FALLOOPE = "Fallo en la operacion";
    public static final Logger logger = Logger.getLogger(RegistroContrapartesController.class);

    @GetMapping("registro-contrapartes/list")
    public ResponseEntity<Object> getRegistroContrapartes(
            @RequestHeader(value = "Authorization") String authorization) {

        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();

        try {

            ObjectMapper mapper = new ObjectMapper();
            List<CuentaCompensacionDTO> cuentasCompensacion =
                    mapper.convertValue(claims.get("cuentasCompensacion"),
                            new TypeReference<List<CuentaCompensacionDTO>>() {
                            }
                    );
            List<String> numerosCuentasCompensacion = cuentasCompensacion
                    .stream().map(CuentaCompensacionDTO::getCodigoBancoRepublica)
                    .collect(Collectors.toList());

            List<RegistroContrapartesDTO> list = registroContrapartesService
                    .consultarRegistrosContrapartes(numerosCuentasCompensacion);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTE CONTROLLER: Error en el servicio getRegistroContrapartes. " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operacion: getRegistroContrapartes()."
                    + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("registro-contrapartes/list")
    public ResponseEntity<Object> getRegistroContrapartes(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody PaginatorDTO paginable) {

        Pageable pageable = new PageRequest(0, 10);
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();

        if (paginable != null)
            pageable = new PageRequest(paginable.getPage(), paginable.getSize());

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<CuentaCompensacionDTO> cuentasCompensacion =
                    mapper.convertValue(claims.get("cuentasCompensacion"),
                            new TypeReference<List<CuentaCompensacionDTO>>() {
                            }
                    );

            List<String> numerosCuentasCompensacion = cuentasCompensacion
                    .stream().map(CuentaCompensacionDTO::getCodigoBancoRepublica)
                    .collect(Collectors.toList());
            Page<RegistroContrapartesDTO> list = registroContrapartesService
                    .consultarRegistrosContrapartes(numerosCuentasCompensacion, pageable);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTE CONTROLLER: Error en el servicio paginable: getRegistroContrapartes()." + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operacion paginable: getRegistroContrapartes()."
                    + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("registro-contrapartes/crear")
    public ResponseEntity<Object> saveCounterParty(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody RegistroContrapartesDTO encryption
    ) {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        try {
            RegistroContrapartesDTO registroContrapartesDTO = encryption;
            registroContrapartesService.guardarRegistrosContrapartes(registroContrapartesDTO);
            respuestaGenericaDTO.setStatus(200);
            respuestaGenericaDTO.setMessage("se creo correctamente");
            logger.info("REGISTRO CONTRAPARTE CONTROLLER: counterParty guardado correctamente.");
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTE CONTROLLER: Error en el servicio saveCounterParty. " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(FALLOOPE + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("registro-contrapartes/eliminar")
    public ResponseEntity<Object> deleteCounterParty(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody String idContraparte
    ) {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        try {
            String id = stringEncrypt.decrypt(idContraparte);
            registroContrapartesService.eliminarRegistrosContrapartes(Long.parseLong(id));
            respuestaGenericaDTO.setStatus(200);
            respuestaGenericaDTO.setMessage("se elimino correctamente");
            logger.info("REGISTRO CONTRAPARTE CONTROLLER: counterParty eliminado correctamente.");
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTE CONTROLLER: Error en el servicio deleteCounterParty " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(FALLOOPE + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("registro-contrapartes/editar")
    public ResponseEntity<Object> editCounterParty(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody RegistroContrapartesDTO encryption
    ) {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        try {
            RegistroContrapartesDTO registroContrapartesDTO = encryption;
            registroContrapartesService.editarRegistrosContrapartes(registroContrapartesDTO);
            respuestaGenericaDTO.setStatus(200);
            respuestaGenericaDTO.setMessage("se edito correctamente");
            logger.info("REGISTRO CONTRAPARTE CONTROLLER: counterParty editado correctamente.");
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            logger.error("REGISTRO CONTRAPARTE CONTROLLER: Error en el servicio editCounterParty " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(FALLOOPE + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}