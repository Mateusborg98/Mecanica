# Sistema de Oficina Mecânica – Tech Challenge

## Descrição
API backend para gestão de ordens de serviço de uma oficina mecânica.

## Funcionalidades
- Gestão de clientes
- Gestão de veículos
- Gestão de serviços
- Gestão de peças e estoque
- Ordem de serviço com controle de status
- Orçamento automático
- Aprovação de orçamento
- Acompanhamento de OS

## Tecnologias
- Java
- Spring Boot
- PostgreSQL
- H2
- Docker
- JWT
- Swagger

## Segurança
As APIs administrativas são protegidas por autenticação JWT.

## Banco de Dados
O projeto utiliza PostgreSQL como banco de dados principal, por ser um banco relacional amplamente utilizado em ambientes produtivos, com suporte robusto a transações, integridade referencial e escalabilidade, atendendo bem aos requisitos de um sistema de gestão de ordens de serviço.
Para os testes unitários e de integração, foi utilizado o banco H2 em memória, permitindo maior velocidade na execução dos testes, isolamento do ambiente e facilidade de configuração, sem dependência de infraestrutura externa.
Essa abordagem possibilita conciliar realismo arquitetural em produção com eficiência no desenvolvimento e garantia da qualidade de software.

### Acesso ao banco de dados
Utilizar essa credenciais:
```bash
Username: postgres
Password: admin
Url: //localhost:3354/mecanica
```

## Execução do Projeto

### Pré-requisitos
- Docker
- Docker Compose
### Subindo a aplicação
```bash
docker-compose up --build
```
## Testes
Cobertura superior a 80%, incluindo testes unitários e de integração.

## Documentação
- Domain Storytelling
- Event Storming
- Diagramas DDD
