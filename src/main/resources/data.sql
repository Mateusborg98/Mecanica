INSERT INTO cliente (id, nome, cpf_cnpj, contato, email)
VALUES (
  gen_random_uuid(),
  'João da Silva',
  '12345678901',
  '11999999999',
  'joao@email.com'
)
ON CONFLICT (cpf_cnpj) DO NOTHING;

INSERT INTO veiculo (id, placa, marca, modelo, ano, cliente_id)
VALUES (
  gen_random_uuid(),
  'ABC1D23',
  'Toyota',
  'Corolla',
  2020,
  (SELECT id FROM cliente WHERE cpf_cnpj = '12345678901')
)
ON CONFLICT (placa) DO NOTHING;

INSERT INTO peca (id, nome, marca, preco)
VALUES
(gen_random_uuid(), 'Bateria', 'Moura', 500.00),
(gen_random_uuid(), 'Filtro de Óleo', 'Tecfil', 40.00);

INSERT INTO estoque (id, peca_id, quantidade)
VALUES
(
  gen_random_uuid(),
  (SELECT id FROM peca WHERE nome = 'Bateria' LIMIT 1),
  10
),
(
  gen_random_uuid(),
  (SELECT id FROM peca WHERE nome = 'Filtro de Óleo' LIMIT 1),
  30
)
ON CONFLICT (peca_id) DO NOTHING;

INSERT INTO ordem_de_servico (
  id,
  status,
  dt_inicio_os,
  dt_fim_os,
  cliente_id,
  veiculo_id
)
VALUES (
  gen_random_uuid(),
  'EM_EXECUCAO',
  NOW() - INTERVAL '2 hours',
  NULL,
  (SELECT id FROM cliente WHERE cpf_cnpj = '12345678901'),
  (SELECT id FROM veiculo WHERE placa = 'ABC1D23')
);

INSERT INTO servico (
  id,
  descricao,
  preco,
  status,
  dt_inicio,
  dt_fim,
  ordem_de_servico_id
)
VALUES
(
  gen_random_uuid(),
  'Troca de óleo',
  120.00,
  'FINALIZADO',
  NOW() - INTERVAL '45 minutes',
  NOW() - INTERVAL '5 minutes',
  (SELECT id FROM ordem_de_servico LIMIT 1)
),
(
  gen_random_uuid(),
  'Troca de pneu',
  300.00,
  'FINALIZADO',
  NOW() - INTERVAL '90 minutes',
  NOW() - INTERVAL '30 minutes',
  (SELECT id FROM ordem_de_servico LIMIT 1)
);

INSERT INTO item_ordem_de_servico (
  id,
  ordem_de_servico_id,
  peca_id,
  quantidade,
  valor_unitario
)
VALUES
(
  gen_random_uuid(),
  (SELECT id FROM ordem_de_servico LIMIT 1),
  (SELECT id FROM peca WHERE nome = 'Bateria' LIMIT 1),
  1,
  500.00
);

INSERT INTO operador (id, nome, matricula, email, contato)
VALUES (
  gen_random_uuid(),
  'Carlos Operador',
  12345,
  'operador@email.com',
  '11988887777'
)
ON CONFLICT (matricula) DO NOTHING;
