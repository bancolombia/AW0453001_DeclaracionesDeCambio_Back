package co.com.bancolombia.declaraciones.model.panama.util.mappers;


import co.com.bancolombia.declaraciones.libcommons.dto.RegistroDigitosVerificacionDTO;
import co.com.bancolombia.declaraciones.libcommons.mappers.IEntityMapper;
import co.com.bancolombia.declaraciones.model.panama.entity.RegistroDigitosVerificacion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Registro digitos verificacion mapper.
 *
 * @author 1627690
 */
@Mapper
public interface IRegistroDigitosVerificacionMapper extends
        IEntityMapper<RegistroDigitosVerificacion, RegistroDigitosVerificacionDTO> {
    /**
     * The constant INSTANCE.
     */
    IRegistroDigitosVerificacionMapper INSTANCE = Mappers.getMapper(IRegistroDigitosVerificacionMapper.class);

    @Override
    RegistroDigitosVerificacion toEntity(RegistroDigitosVerificacionDTO dto);

    @Override
    RegistroDigitosVerificacionDTO toDTO(RegistroDigitosVerificacion entity);
}
