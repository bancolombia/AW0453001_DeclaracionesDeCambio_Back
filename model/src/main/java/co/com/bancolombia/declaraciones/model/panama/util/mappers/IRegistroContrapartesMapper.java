package co.com.bancolombia.declaraciones.model.panama.util.mappers;

import co.com.bancolombia.declaraciones.libcommons.dto.RegistroContrapartesDTO;
import co.com.bancolombia.declaraciones.libcommons.mappers.IEntityMapper;
import co.com.bancolombia.declaraciones.model.panama.entity.RegistroContrapartes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface IRegistroContrapartesMapper
        extends IEntityMapper<RegistroContrapartes, RegistroContrapartesDTO> {

    IRegistroContrapartesMapper INSTANCE = Mappers.getMapper(IRegistroContrapartesMapper.class);


@Override
    RegistroContrapartes toEntity(RegistroContrapartesDTO dto);

@Override
    RegistroContrapartesDTO toDTO(RegistroContrapartes entity);
}
