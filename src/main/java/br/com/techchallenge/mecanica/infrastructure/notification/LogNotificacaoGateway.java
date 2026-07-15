package br.com.techchallenge.mecanica.infrastructure.notification;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.application.gateway.NotificacaoGateway;

@Component
public class LogNotificacaoGateway implements NotificacaoGateway {

    private static final Logger logger =
            LoggerFactory.getLogger(LogNotificacaoGateway.class);

    @Override
    public void notificarAlteracaoStatusOrdem(
            UUID ordemDeServicoId,
            String status,
            String mensagem) {

        logger.info(
                "Simulando envio de e-mail | ordemDeServicoId={} | status={} | mensagem={}",
                ordemDeServicoId,
                status,
                mensagem);
    }
}