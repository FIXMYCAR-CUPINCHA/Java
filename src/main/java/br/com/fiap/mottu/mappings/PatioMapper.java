package br.com.fiap.mottu.mappings;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.fiap.mottu.dto.PatioDTO;
import br.com.fiap.mottu.models.Patio;

@Mapper(componentModel = "spring")
public interface PatioMapper {

    PatioDTO toDTO(Patio patio);

    List<PatioDTO> toDTOList(List<Patio> patios);
}
