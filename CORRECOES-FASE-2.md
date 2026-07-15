# Complementos realizados no pacote final

Este pacote foi gerado a partir de `Mecanica-phase-2-final.zip` e complementa a entrega com os seguintes ajustes:

- listagem de ordens de serviço filtrada e ordenada conforme o enunciado;
- data de criação persistida para ordenar as ordens mais antigas primeiro;
- exclusão de ordens `FINALIZADA` e `ENTREGUE` da listagem operacional;
- callbacks públicos de aprovação e recusa de orçamento;
- manutenção das demais rotas administrativas protegidas por JWT;
- persistência do PostgreSQL com `PersistentVolumeClaim`;
- HPA configurado por consumo de CPU e memória;
- imagem Docker padronizada como `mecanica-api:latest`;
- módulo Terraform para criação de cluster Kubernetes Kind local;
- Terraform principal atualizado com PVC e HPA por CPU/memória;
- CI/CD com testes, JaCoCo, Docker, cluster via Terraform, deploy dos YAMLs, banco, API e health check;
- correção da persistência dos itens da ordem de serviço para que os identificadores sejam gerados pelo JPA;
- instalação reproduzível do Metrics Server no Kind, com patch versionado para o certificado local do kubelet;
- roteiro para validar a Metrics API e demonstrar o HPA aumentando as réplicas sob carga;
- ajuste da meta de memória do HPA para `650Mi` e redução estabilizada em 60 segundos, permitindo retornar ao mínimo após a carga;
- documentação e roteiro de vídeo atualizados;
- regras de `.gitignore` para não versionar state, cache ou kubeconfig do Terraform.

## Notificação

Conforme decisão do projeto, a alteração de status continua sendo demonstrada por log. A abstração por gateway foi preservada para permitir uma integração futura com e-mail ou outro provedor sem alterar as regras de negócio.

## Validação

- 91 testes executados;
- 0 falhas e 0 erros;
- 98,58% de cobertura de linhas;
- 88,81% de cobertura de branches;
- regra mínima de 80% do JaCoCo aprovada;
- módulos Terraform `infra` e `infra/cluster` validados;
- 12 arquivos YAML analisados sem erro de sintaxe.
- Metrics API disponível no Kind e HPA validado com escalonamento automático de 1 para 3 réplicas e retorno de 3 para 1 após a carga.

O link definitivo do vídeo e o compartilhamento do repositório com o usuário exigido pela FIAP continuam sendo etapas externas à geração do código.
