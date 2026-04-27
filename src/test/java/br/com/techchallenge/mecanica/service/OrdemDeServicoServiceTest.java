package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.service.implementation.OrdemDeServicoServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrdemDeServicoServiceTest {

    @InjectMocks
    private OrdemDeServicoServiceImpl service;

    @Mock
    private OrdemDeServicoRepository ordemRepository;

    @Mock
    private OrdemDeServicoMapper mapper;

    @Test
    void deveLancarExcecaoAoFinalizarOrdemForaDoStatusEmExecucao() {
        // arrange
        OrdemDeServico os = new OrdemDeServico();
        os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);

        UUID id = UUID.randomUUID();

        when(ordemRepository.findById(id))
                .thenReturn(Optional.of(os));

        // act + assert
        assertThrows(RegraNegocioException.class,
                () -> service.finalizar(id));
    }

    @Test
    void deveFinalizarOrdemComStatusEmExecucao() {
        OrdemDeServico os = new OrdemDeServico();
        os.setStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);

        UUID id = UUID.randomUUID();

        when(ordemRepository.findById(id))
                .thenReturn(Optional.of(os));

        service.finalizar(id);

        assertEquals(StatusOrdemDeServicoEnum.FINALIZADA, os.getStatus());
    }
}