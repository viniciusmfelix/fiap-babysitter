
DELETE FROM execucao;
COMMIT;
DELETE FROM acao;
COMMIT;

DROP SEQUENCE acao_seq;
DROP SEQUENCE execucao_seq;

CREATE SEQUENCE acao_seq START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE NOCYCLE NOCACHE;
CREATE SEQUENCE execucao_seq INCREMENT BY 1 START WITH 1 NOMAXVALUE MINVALUE 1 NOCYCLE NOCACHE;

INSERT INTO acao (id, nome, descricao, ativo) VALUES (acao_seq.nextval, 'Ir ao mercado', 'Ir ao mercado para comprar leite para o bebe as quartas.', 1);
INSERT INTO acao (id, nome, descricao, ativo) VALUES (acao_seq.nextval, 'Trocar fralda', 'Trocar fralda do bebe de 5 em 5h.', 1);
INSERT INTO acao (id, nome, descricao, ativo) VALUES (acao_seq.nextval, 'Preparar mamadeira', 'Preparar mamadeira de leite morna.', 1);
INSERT INTO acao (id, nome, descricao, ativo) VALUES (acao_seq.nextval, 'Passeio', 'Passear com o bebe as manhas.', 0);
INSERT INTO acao (id, nome, descricao, ativo) VALUES (acao_seq.nextval, 'Dar banho', 'Banhar o bebe apenas durante o horario da tarde a cada dois dias.', 1);

INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 1);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 5);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 5);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 3);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 3);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 1);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 3);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 2);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 2);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 1);
INSERT INTO execucao (id, data_exec, acao_id) VALUES (execucao_seq.nextval, SYSDATE, 3);