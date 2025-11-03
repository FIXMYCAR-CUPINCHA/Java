package br.com.fiap.mottu.dto;

public record PatioDTO(Long id, String nome, String endereco, String complemento, java.math.BigDecimal areaM2, Long idLocalidade, java.util.List<MotoDTO> motos) {

}
