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
import org.springframework.web.bind.annotation.*;
import co.com.bancolombia.declaraciones.libcommons.securitytools.StringEncrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.com.bancolombia.declaraciones.libcommons.dto.CuentaCompensacionDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.PaginatorDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.RegistroDigitosVerificacionDTO;
import co.com.bancolombia.declaraciones.libcommons.dto.RespuestaGenericaDTO;
import co.com.bancolombia.declaraciones.libcommons.exceptions.ErrorCodes;
import co.com.bancolombia.declaraciones.libcommons.securitytools.GenerateKey;
import co.com.bancolombia.declaraciones.libcommons.securitytools.JwtDecode;
import co.com.bancolombia.declaraciones.services.service.IRegistroDigitosVerificacionService;
import io.jsonwebtoken.Claims;


@RestController
@RequestMapping("/bancolombia/declaraciones/")
public class RegistroDigitosVerificacionController {

    @Autowired
    private IRegistroDigitosVerificacionService registroDigitosVerificacionService;
    public static final Logger logger = Logger.getLogger(RegistroDigitosVerificacionController.class);

    @PostMapping("registro-digitos-verificacion/list")
    public ResponseEntity<Object> getRegistroDigitosVerificacion(
            @RequestHeader(value = "Authorization") String authorization, @RequestBody PaginatorDTO paginable) {

        Pageable pageable = new PageRequest(0, 10);
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        if (paginable != null)
            pageable = new PageRequest(paginable.getPage(), paginable.getSize());
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<CuentaCompensacionDTO> cuentasCompensacion = mapper.convertValue(claims.get("cuentasCompensacion"),
                    new TypeReference<List<CuentaCompensacionDTO>>() {
                    });
            List<String> numerosCuentasCompensacion = cuentasCompensacion
                    .stream().map(CuentaCompensacionDTO::getCodigoBancoRepublica)
                    .collect(Collectors.toList());
            Page<RegistroDigitosVerificacionDTO> list = registroDigitosVerificacionService
                    .consultarRegistroDigitosVerificacion(numerosCuentasCompensacion, pageable);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION CONTROLLER: " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operacion: getRegistroDigitosVerificacion."
                    + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("registro-digitos-verificacion/crear")
    public ResponseEntity<Object> saveVerificationDigit(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody RegistroDigitosVerificacionDTO encryption
    ) {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        try {
            RegistroDigitosVerificacionDTO registroDigitosVerificacionDTO = encryption;
            registroDigitosVerificacionService.guardarRegistroDigitosVerificacion(registroDigitosVerificacionDTO);
            respuestaGenericaDTO.setStatus(200);
            respuestaGenericaDTO.setMessage("se creo correctamente");
            logger.info("REG. DIG. VERIFICACION CONTROLLER: Digito de verificación creado correctamente.");
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION CONTROLLER: " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operacion: saveVerificationDigit." + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("registro-digitos-verificacion/eliminar")
    public ResponseEntity<Object> deleteVerificationDigit(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody String idDigitoVerificacion
    ) throws Exception {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        try {
            String id = stringEncrypt.decrypt(idDigitoVerificacion);
            //RegistroDigitosVerificacionDTO registroDigitosVerificacionDTO = stringEncrypt.decrypt(encryption, RegistroDigitosVerificacionDTO.class);
            registroDigitosVerificacionService.eliminarRegistroDigitosVerificacion(Long.parseLong(id));
            respuestaGenericaDTO.setStatus(200);
            respuestaGenericaDTO.setMessage("se elimino correctamente");
            logger.info("REG. DIG. VERIFICACION CONTROLLER: Digito de verificación eliminado correctamente.");
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION CONTROLLER: " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operacion: deleteVerificationDigit." + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("registro-digitos-verificacion/editar")
    public ResponseEntity<Object> editVerificationDigit(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody RegistroDigitosVerificacionDTO encryption
    ) {
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();
        try {
            //RegistroContrapartesDTO registroContrapartesDTO = stringEncrypt.decrypt(encryption, RegistroContrapartesDTO.class);
            RegistroDigitosVerificacionDTO registroDigitosVerificacionDTO = encryption;
            registroDigitosVerificacionService.editarRegistroDigitosVerificacion(registroDigitosVerificacionDTO);
            respuestaGenericaDTO.setStatus(200);
            respuestaGenericaDTO.setMessage("se edit�o correctamente");
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION CONTROLLER: " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.TECNICAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operación: editVerificationDigit." + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("registro-digitos-verificacion/obtener")
    public ResponseEntity<Object> getRegistroDigitoVerificacion(
            @RequestHeader(value = "Authorization") String authorization, @RequestBody RegistroDigitosVerificacionDTO digVer) {
        GenerateKey generateKey;
        String token;
        Claims claims;
        RespuestaGenericaDTO respuestaGenericaDTO = new RespuestaGenericaDTO();

        try {
            generateKey = GenerateKey.getSingletonInstance();
            token = authorization.replace("Bearer ", "");
            claims = jwtDecode.decodeToken(generateKey.getPublicKey(), token);
            ObjectMapper mapper = new ObjectMapper();

            RegistroDigitosVerificacionDTO registro = registroDigitosVerificacionService
                    .consultarRegistroDigitosVerificacionByNitAndNroCtaCompensacion(digVer.getNit(), digVer.getNumeroCuentaCompensacion());
            return new ResponseEntity<>(registro, HttpStatus.OK);
        } catch (PersistenceException e) {
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("REG. DIG. VERIFICACION CONTROLLER: " + e.getCause());
            respuestaGenericaDTO.setStatus(ErrorCodes.INTERNAL_ERROR.getCode());
            respuestaGenericaDTO.setMessage("Fallo en la operacion: getRegistroDigitoVerificacion" + e.getMessage());
            return new ResponseEntity<>(respuestaGenericaDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
