-- =========================================================
-- CLIENTES
-- =========================================================
INSERT INTO clienteJpaEntity (id, nome, cpf_cnpj, contato, email) VALUES
(gen_random_uuid(), 'João da Silva',      '12345678900', '11999999999', 'joao@email.com'),
(gen_random_uuid(), 'Maria Oliveira',     '98765432100', '11988888888', 'maria@email.com'),
(gen_random_uuid(), 'Carlos Pereira',     '45678912300', '11977777777', 'carlos@email.com'),
(gen_random_uuid(), 'Fernanda Souza',     '74185296300', '11966666666', 'fernanda@email.com'),
(gen_random_uuid(), 'Ricardo Mendes',     '85274196300', '11955555555', 'ricardo@email.com'),
(gen_random_uuid(), 'Patricia Lima',      '96385274100', '11944444444', 'patricia@email.com');

-- =========================================================
-- VEÍCULOS
-- =========================================================
INSERT INTO veiculoJpaEntity (id, placa, marca, modelo, ano, cliente_id) VALUES
(gen_random_uuid(), 'ABC1D23', 'Honda',      'Civic',       2020,
 (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '12345678900')),

(gen_random_uuid(), 'XYZ9Z99', 'Volkswagen', 'Gol',         2019,
 (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '98765432100')),

(gen_random_uuid(), 'BRA2E45', 'Toyota',     'Corolla',     2022,
 (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '45678912300')),

(gen_random_uuid(), 'CAR5F67', 'Chevrolet',  'Onix',        2021,
 (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '74185296300')),

(gen_random_uuid(), 'MEC7G89', 'Hyundai',    'HB20',        2023,
 (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '85274196300')),

(gen_random_uuid(), 'TOP1H11', 'Fiat',       'Argo',        2018,
 (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '96385274100'));

-- =========================================================
-- OPERADORES
-- =========================================================
INSERT INTO operadorJpaEntity (
    id,
    nome,
    matricula,
    email,
    contato
)
VALUES
(gen_random_uuid(), 'Administrador', 1, 'admin@email.com',     '11999999999'),
(gen_random_uuid(), 'Carlos Mecânico', 2, 'carlos@oficina.com', '11911111111'),
(gen_random_uuid(), 'Marcos Técnico', 3, 'marcos@oficina.com',  '11922222222'),
(gen_random_uuid(), 'Ana Diagnóstico', 4, 'ana@oficina.com',    '11933333333');

-- =========================================================
-- PEÇAS (CATÁLOGO)
-- =========================================================
INSERT INTO pecaJpaEntity (id, nome, marca, preco) VALUES
(gen_random_uuid(), 'Filtro de óleo',          'Bosch',       50.00),
(gen_random_uuid(), 'Pneu Aro 16',             'Michelin',   450.00),
(gen_random_uuid(), 'Pastilha de freio',       'Fras-le',    180.00),
(gen_random_uuid(), 'Bateria 60Ah',            'Moura',      550.00),
(gen_random_uuid(), 'Amortecedor dianteiro',   'Monroe',     380.00),
(gen_random_uuid(), 'Correia dentada',         'Contitech',  220.00),
(gen_random_uuid(), 'Velas de ignição',        'NGK',        120.00),
(gen_random_uuid(), 'Filtro de ar',            'Mahle',       65.00),
(gen_random_uuid(), 'Óleo 5W30',               'Mobil',       45.00);

-- =========================================================
-- ESTOQUE
-- =========================================================
INSERT INTO estoqueJpaEntity (id, peca_id, quantidade) VALUES
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Filtro de óleo'), 50),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Pneu Aro 16'), 20),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Pastilha de freio'), 35),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Bateria 60Ah'), 15),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Amortecedor dianteiro'), 18),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Correia dentada'), 25),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Velas de ignição'), 40),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Filtro de ar'), 45),
(gen_random_uuid(), (SELECT id FROM pecaJpaEntity WHERE nome = 'Óleo 5W30'), 120);

-- =========================================================
-- SERVIÇOS (CATÁLOGO)
-- =========================================================
INSERT INTO servicoJpaEntity (id, descricao, preco) VALUES
(gen_random_uuid(), 'Troca de óleo',              120.00),
(gen_random_uuid(), 'Troca de pneu',              300.00),
(gen_random_uuid(), 'Alinhamento',                150.00),
(gen_random_uuid(), 'Balanceamento',              120.00),
(gen_random_uuid(), 'Revisão completa',           950.00),
(gen_random_uuid(), 'Troca de bateria',           180.00),
(gen_random_uuid(), 'Diagnóstico eletrônico',     220.00),
(gen_random_uuid(), 'Troca de correia dentada',   600.00),
(gen_random_uuid(), 'Troca de freios',            450.00);

-- =========================================================
-- ORDEM DE SERVIÇO 1
-- =========================================================
INSERT INTO ordem_de_servico (
    id,
    cliente_id,
    veiculo_id,
    operador_id,
    status,
    dt_inicio_os,
    dt_fim_os,
    valor_total_os
)
VALUES (
    gen_random_uuid(),
    (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '12345678900'),
    (SELECT id FROM veiculoJpaEntity WHERE placa = 'ABC1D23'),
    (SELECT id FROM operadorJpaEntity WHERE matricula = 1),
    'FINALIZADA',
    NOW() - INTERVAL '5 hours',
    NOW() - INTERVAL '2 hours',
    620.00
);

-- =========================================================
-- ORDEM DE SERVIÇO 2
-- =========================================================
INSERT INTO ordem_de_servico (
    id,
    cliente_id,
    veiculo_id,
    operador_id,
    status,
    dt_inicio_os,
    dt_fim_os,
    valor_total_os
)
VALUES (
    gen_random_uuid(),
    (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '98765432100'),
    (SELECT id FROM veiculoJpaEntity WHERE placa = 'XYZ9Z99'),
    (SELECT id FROM operadorJpaEntity WHERE matricula = 2),
    'EM_EXECUCAO',
    NOW() - INTERVAL '3 hours',
    NULL,
    780.00
);

-- =========================================================
-- ORDEM DE SERVIÇO 3
-- =========================================================
INSERT INTO ordem_de_servico (
    id,
    cliente_id,
    veiculo_id,
    operador_id,
    status,
    dt_inicio_os,
    dt_fim_os,
    valor_total_os
)
VALUES (
    gen_random_uuid(),
    (SELECT id FROM clienteJpaEntity WHERE cpf_cnpj = '45678912300'),
    (SELECT id FROM veiculoJpaEntity WHERE placa = 'BRA2E45'),
    (SELECT id FROM operadorJpaEntity WHERE matricula = 3),
    'AGUARDANDO_APROVACAO',
    NOW() - INTERVAL '1 hour',
    NULL,
    1450.00
);

-- =========================================================
-- PEÇAS DAS OS
-- =========================================================
INSERT INTO peca_ordem_de_servico (
    id,
    ordem_servico_id,
    peca_id,
    quantidade
)
VALUES
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico LIMIT 1),
    (SELECT id FROM pecaJpaEntity WHERE nome = 'Filtro de óleo'),
    1
),
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico LIMIT 1),
    (SELECT id FROM pecaJpaEntity WHERE nome = 'Óleo 5W30'),
    4
),
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico OFFSET 1 LIMIT 1),
    (SELECT id FROM pecaJpaEntity WHERE nome = 'Pastilha de freio'),
    1
),
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico OFFSET 2 LIMIT 1),
    (SELECT id FROM pecaJpaEntity WHERE nome = 'Correia dentada'),
    1
);

-- =========================================================
-- SERVIÇOS EXECUTADOS NAS OS
-- =========================================================
INSERT INTO servico_ordem_de_servico (
    id,
    ordem_servico_id,
    servico_id,
    status,
    dt_inicio,
    dt_fim
)
VALUES
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico LIMIT 1),
    (SELECT id FROM servicoJpaEntity WHERE descricao = 'Troca de óleo'),
    'FINALIZADO',
    NOW() - INTERVAL '4 hours',
    NOW() - INTERVAL '3 hours 20 minutes'
),
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico LIMIT 1),
    (SELECT id FROM servicoJpaEntity WHERE descricao = 'Alinhamento'),
    'FINALIZADO',
    NOW() - INTERVAL '3 hours',
    NOW() - INTERVAL '2 hours 20 minutes'
),
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico OFFSET 1 LIMIT 1),
    (SELECT id FROM servicoJpaEntity WHERE descricao = 'Troca de freios'),
    'EM_EXECUCAO',
    NOW() - INTERVAL '2 hours',
    NULL
),
(
    gen_random_uuid(),
    (SELECT id FROM ordem_de_servico OFFSET 2 LIMIT 1),
    (SELECT id FROM servicoJpaEntity WHERE descricao = 'Troca de correia dentada'),
    'AGUARDANDO',
    NULL,
    NULL
);