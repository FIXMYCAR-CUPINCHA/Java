package br.com.fiap.mottu.dto;

public record MotoDTO(Long id, String modelo, String placa, String status, PatioWithoutMotoDTO patio,java.util.Date dataEntrada) {

}
