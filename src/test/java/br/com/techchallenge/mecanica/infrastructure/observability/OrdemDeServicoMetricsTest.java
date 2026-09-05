package br.com.techchallenge.mecanica.infrastructure.observability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

class OrdemDeServicoMetricsTest {

    private SimpleMeterRegistry registry;
    private AtomicLong nanoTime;
    private OrdemDeServicoMetrics metrics;

    @BeforeEach
    void preparar() {
        registry = new SimpleMeterRegistry();
        nanoTime = new AtomicLong();
        metrics = new OrdemDeServicoMetrics(registry, nanoTime::get);
    }

    @Test
    void deveContabilizarCriacaoETransicoes() {
        UUID ordemId = UUID.randomUUID();

        metrics.registrarStatus(ordemId, "RECEBIDA");
        metrics.registrarStatus(ordemId, "EM_DIAGNOSTICO");

        assertEquals(1, registry.counter(
                "mecanica.ordens.servico.criadas").count());
        assertEquals(1, registry.counter(
                "mecanica.ordens.servico.status.transicoes",
                "status", "EM_DIAGNOSTICO").count());
    }

    @Test
    void deveMedirDuracaoDasTresEtapas() {
        UUID ordemId = UUID.randomUUID();

        metrics.registrarStatus(ordemId, "EM_DIAGNOSTICO");
        nanoTime.set(2_000_000_000L);
        metrics.registrarStatus(ordemId, "AGUARDANDO_APROVACAO");

        metrics.registrarStatus(ordemId, "EM_EXECUCAO");
        nanoTime.set(5_000_000_000L);
        metrics.registrarStatus(ordemId, "FINALIZADA");
        nanoTime.set(9_000_000_000L);
        metrics.registrarStatus(ordemId, "ENTREGUE");

        assertEquals(2, registry.timer(
                "mecanica.ordens.servico.status.duracao",
                "etapa", "diagnostico").totalTime(java.util.concurrent.TimeUnit.SECONDS));
        assertEquals(3, registry.timer(
                "mecanica.ordens.servico.status.duracao",
                "etapa", "execucao").totalTime(java.util.concurrent.TimeUnit.SECONDS));
        assertEquals(4, registry.timer(
                "mecanica.ordens.servico.status.duracao",
                "etapa", "finalizacao").totalTime(java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test
    void deveIgnorarFimSemInicioEStatusNaoCronometrado() {
        UUID ordemId = UUID.randomUUID();

        metrics.registrarStatus(ordemId, "AGUARDANDO_APROVACAO");
        metrics.registrarStatus(ordemId, "DESCONHECIDO");

        assertEquals(0, registry.timer(
                "mecanica.ordens.servico.status.duracao",
                "etapa", "diagnostico").count());
    }

    @Test
    void deveContabilizarFalhaDeProcessamento() {
        metrics.registrarFalha(
                "/ordens-servico/70000000-0000-0000-0000-000000000001/finalizar",
                422);

        assertEquals(1, registry.counter(
                "mecanica.ordens.servico.processamento.falhas",
                "path", "/ordens-servico/{id}/finalizar",
                "status", "422").count());
    }
}
