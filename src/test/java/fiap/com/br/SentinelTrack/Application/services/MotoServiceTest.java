package fiap.com.br.SentinelTrack.Application.services;

import fiap.com.br.SentinelTrack.Api.exception.DuplicatePlacaException;
import fiap.com.br.SentinelTrack.Api.exception.PatioNotFoundException;
import fiap.com.br.SentinelTrack.Application.dto.CreateMotoDTO;
import fiap.com.br.SentinelTrack.Application.dto.MotoDTO;
import fiap.com.br.SentinelTrack.Application.mapper.MotoMapper;
import fiap.com.br.SentinelTrack.Domain.models.Moto;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.MotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para MotoService
 * Demonstra cobertura completa de testes com boas práticas
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MotoService - Testes Unitários")
class MotoServiceTest {

    @Mock
    private MotoRepository motoRepository;

    @Mock
    private PatioService patioService;

    @Mock
    private MotoMapper mapper;

    @InjectMocks
    private MotoService motoService;

    private Moto motoEntity;
    private MotoDTO motoDTO;
    private CreateMotoDTO createMotoDTO;
    private Patio patioEntity;

    @BeforeEach
    void setUp() {
        // Arrange - Dados de teste
        patioEntity = new Patio();
        patioEntity.setId(1L);
        patioEntity.setNome("Pátio Central");

        motoEntity = new Moto();
        motoEntity.setId(1L);
        motoEntity.setModelo("Honda CG 160");
        motoEntity.setPlaca("ABC1234");
        motoEntity.setStatus("DISPONIVEL");
        motoEntity.setDataEntrada(new Date());
        motoEntity.setPatio(patioEntity);

        motoDTO = new MotoDTO();
        motoDTO.setId(1L);
        motoDTO.setModelo("Honda CG 160");
        motoDTO.setPlaca("ABC1234");
        motoDTO.setStatus("DISPONIVEL");
        motoDTO.setDataEntrada(new Date());
        motoDTO.setIdPatio(1L);
        motoDTO.setNomePatio("Pátio Central");

        createMotoDTO = new CreateMotoDTO();
        createMotoDTO.setModelo("Honda CG 160");
        createMotoDTO.setPlaca("ABC1234");
        createMotoDTO.setStatus("DISPONIVEL");
        createMotoDTO.setDataEntrada(new Date());
        createMotoDTO.setIdPatio(1L);
    }

    @Test
    @DisplayName("Deve listar todas as motos com sucesso")
    void deveListarTodasAsMotos() {
        // Arrange
        List<Moto> motos = Arrays.asList(motoEntity);
        when(motoRepository.findAll()).thenReturn(motos);
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        List<MotoDTO> resultado = motoService.listarTodas();

        // Assert
        assertThat(resultado)
            .isNotNull()
            .hasSize(1)
            .containsExactly(motoDTO);

        verify(motoRepository).findAll();
        verify(mapper).toDTO(motoEntity);
    }

    @Test
    @DisplayName("Deve buscar moto por ID com sucesso")
    void deveBuscarMotoPorId() {
        // Arrange
        Long id = 1L;
        when(motoRepository.findById(id)).thenReturn(Optional.of(motoEntity));
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        Optional<MotoDTO> resultado = motoService.buscarPorId(id);

        // Assert
        assertThat(resultado)
            .isPresent()
            .contains(motoDTO);

        verify(motoRepository).findById(id);
        verify(mapper).toDTO(motoEntity);
    }

    @Test
    @DisplayName("Deve buscar moto por placa com sucesso")
    void deveBuscarMotoPorPlaca() {
        // Arrange
        String placa = "ABC1234";
        when(motoRepository.findByPlaca(placa)).thenReturn(Optional.of(motoEntity));
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        Optional<MotoDTO> resultado = motoService.buscarPorPlaca(placa);

        // Assert
        assertThat(resultado)
            .isPresent()
            .contains(motoDTO);

        verify(motoRepository).findByPlaca(placa);
    }

    @Test
    @DisplayName("Deve criar nova moto com sucesso")
    void deveCriarNovaMotoComSucesso() {
        // Arrange
        when(patioService.buscarEntidadePorId(1L)).thenReturn(Optional.of(patioEntity));
        when(motoRepository.findByPlaca(createMotoDTO.getPlaca())).thenReturn(Optional.empty());
        when(mapper.toEntity(createMotoDTO, patioEntity)).thenReturn(motoEntity);
        when(motoRepository.save(motoEntity)).thenReturn(motoEntity);
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        MotoDTO resultado = motoService.criar(createMotoDTO);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .isEqualTo(motoDTO);

        verify(patioService).buscarEntidadePorId(1L);
        verify(motoRepository).findByPlaca(createMotoDTO.getPlaca());
        verify(motoRepository).save(motoEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar moto com pátio inexistente")
    void deveLancarExcecaoAoCriarMotoComPatioInexistente() {
        // Arrange
        when(patioService.buscarEntidadePorId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> motoService.criar(createMotoDTO))
            .isInstanceOf(PatioNotFoundException.class)
            .hasMessageContaining("Pátio não encontrado com ID: 1");

        verify(patioService).buscarEntidadePorId(1L);
        verifyNoInteractions(motoRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar moto com placa duplicada")
    void deveLancarExcecaoAoCriarMotoComPlacaDuplicada() {
        // Arrange
        when(patioService.buscarEntidadePorId(1L)).thenReturn(Optional.of(patioEntity));
        when(motoRepository.findByPlaca(createMotoDTO.getPlaca())).thenReturn(Optional.of(motoEntity));

        // Act & Assert
        assertThatThrownBy(() -> motoService.criar(createMotoDTO))
            .isInstanceOf(DuplicatePlacaException.class)
            .hasMessageContaining("Já existe uma moto cadastrada com a placa: ABC1234");

        verify(patioService).buscarEntidadePorId(1L);
        verify(motoRepository).findByPlaca(createMotoDTO.getPlaca());
        verify(motoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar moto existente com sucesso")
    void deveAtualizarMotoExistente() {
        // Arrange
        Long id = 1L;
        when(motoRepository.findById(id)).thenReturn(Optional.of(motoEntity));
        when(motoRepository.findByPlaca(createMotoDTO.getPlaca())).thenReturn(Optional.of(motoEntity));
        when(patioService.buscarEntidadePorId(1L)).thenReturn(Optional.of(patioEntity));
        when(motoRepository.save(motoEntity)).thenReturn(motoEntity);
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        Optional<MotoDTO> resultado = motoService.atualizar(id, createMotoDTO);

        // Assert
        assertThat(resultado)
            .isPresent()
            .contains(motoDTO);

        verify(motoRepository).findById(id);
        verify(mapper).updateEntity(motoEntity, createMotoDTO, patioEntity);
        verify(motoRepository).save(motoEntity);
    }

    @Test
    @DisplayName("Deve deletar moto existente com sucesso")
    void deveDeletarMotoExistente() {
        // Arrange
        Long id = 1L;
        when(motoRepository.existsById(id)).thenReturn(true);

        // Act
        boolean resultado = motoService.deletar(id);

        // Assert
        assertThat(resultado).isTrue();
        verify(motoRepository).existsById(id);
        verify(motoRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve retornar false ao tentar deletar moto inexistente")
    void deveRetornarFalseAoDeletarMotoInexistente() {
        // Arrange
        Long id = 999L;
        when(motoRepository.existsById(id)).thenReturn(false);

        // Act
        boolean resultado = motoService.deletar(id);

        // Assert
        assertThat(resultado).isFalse();
        verify(motoRepository).existsById(id);
        verify(motoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve buscar motos por status")
    void deveBuscarMotosPorStatus() {
        // Arrange
        String status = "DISPONIVEL";
        List<Moto> motos = Arrays.asList(motoEntity);
        when(motoRepository.findByStatus(status)).thenReturn(motos);
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        List<MotoDTO> resultado = motoService.buscarPorStatus(status);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .hasSize(1)
            .containsExactly(motoDTO);

        verify(motoRepository).findByStatus(status);
    }

    @Test
    @DisplayName("Deve buscar motos por pátio")
    void deveBuscarMotosPorPatio() {
        // Arrange
        Long idPatio = 1L;
        List<Moto> motos = Arrays.asList(motoEntity);
        when(motoRepository.findByPatioId(idPatio)).thenReturn(motos);
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        List<MotoDTO> resultado = motoService.buscarPorPatio(idPatio);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .hasSize(1);

        verify(motoRepository).findByPatioId(idPatio);
    }

    @Test
    @DisplayName("Deve buscar motos por modelo")
    void deveBuscarMotosPorModelo() {
        // Arrange
        String modelo = "Honda";
        List<Moto> motos = Arrays.asList(motoEntity);
        when(motoRepository.findByModeloContainingIgnoreCase(modelo)).thenReturn(motos);
        when(mapper.toDTO(motoEntity)).thenReturn(motoDTO);

        // Act
        List<MotoDTO> resultado = motoService.buscarPorModelo(modelo);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .hasSize(1);

        verify(motoRepository).findByModeloContainingIgnoreCase(modelo);
    }
}
