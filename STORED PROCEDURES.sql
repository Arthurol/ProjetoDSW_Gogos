--
-- TIPO: 0 (venda), 1 (compra)
--
-- OPERACAO: 0 (crédito), 1 (débito), 2 (bloqueio), 3 (desbloqueio)
--
-- STATUS: 0 (aberta), 1 (liquidada), 2 (cancelada)
--


--
-- SP PARA DEPURAÇÃO DE OUTRAS STORED PROCEDURES
--
DROP PROCEDURE IF EXISTS Debug;
DELIMITER //
CREATE PROCEDURE Debug(msg VARCHAR(255), Condicao BIT)
BEGIN
	IF not Condicao THEN
		INSERT INTO DebugMessages(message) VALUES(msg);
	END IF;
END //
DELIMITER ;


--
-- SP PARA INSERIR USUARIOS
--
DROP PROCEDURE IF EXISTS InserirUsuario;
DELIMITER //
CREATE PROCEDURE InserirUsuario(vNome VARCHAR(80), vTelefone VARCHAR(20), vCpf VARCHAR(14), vEmail VARCHAR(40), vSenha VARCHAR(255))
BEGIN
	
	INSERT INTO usuarios (nome, telefone, cpf, email, senha, administrator, numeroLogins)
	VALUES (vNome, vTelefone, vCpf, vEmail, vSenha, 0, 0);
	
END //
DELIMITER ;

--
-- SP PARA REMOVER TODOS OS USUARIOS
--
DROP PROCEDURE IF EXISTS RemoverUsuarios;
DELIMITER //
CREATE PROCEDURE RemoverUsuarios()
BEGIN
	SET FOREIGN_KEY_CHECKS = 0;
	SET SQL_SAFE_UPDATES = 0;
	DELETE FROM usuarios;
	SET SQL_SAFE_UPDATES = 1;
	SET FOREIGN_KEY_CHECKS = 1;
	
END //
DELIMITER ;

--
-- SP PARA INSERIR TOKEN
--
DROP PROCEDURE IF EXISTS InserirToken;
DELIMITER //
CREATE PROCEDURE InserirToken(vIdUsuario INT, vToken INT, vDataValidade DATETIME)
BEGIN

	INSERT INTO tokens (idUsuario, token, validade)
	VALUES (vIdUsuario, vToken, vDataValidade);
	
END //
DELIMITER ;


--
-- SP PARA INCREMENTAR O Nº DE LOGINS FALHOS FEITOS PELO USUÁRIO
--
DROP PROCEDURE IF EXISTS IndicarLoginFalha;
DELIMITER //
CREATE PROCEDURE IndicarLoginFalha(vIdUsuario INT)
BEGIN

	UPDATE usuarios
	SET numeroLogins = numeroLogins + 1
	WHERE id = vIdUsuario;
	
END //
DELIMITER ;

--
-- SP PARA INDICAR SUCESSO NO LOGIN, ZERANDO LOGINS FALHOS
--
DROP PROCEDURE IF EXISTS IndicarLoginSucesso;
DELIMITER //
CREATE PROCEDURE IndicarLoginSucesso(vIdUsuario INT )
BEGIN
	DECLARE lTime DATETIME;
	SET lTime = now();
	
	UPDATE usuarios
	SET numeroLogins = 0, ultimoLogin = lTime
	WHERE id = vIdUsuario;
	
END //
DELIMITER ;


--
-- SP PARA EDITAR USUÁRIO
--
DROP PROCEDURE IF EXISTS EditarUsuario;
DELIMITER //
CREATE PROCEDURE EditarUsuario(vIdUsuario INT, vNome VARCHAR(80), vTelefone VARCHAR(20), vCpf VARCHAR(14), vFoto BLOB)
BEGIN
	
	UPDATE usuarios
	SET nome = vNome, telefone = vTelefone, cpf = vCpf, foto = vFoto
	WHERE id = vIdUsuario;
	
END //
DELIMITER ;


--
-- SP PARA EDITAR SENHA DO USUÁRIO
--
DROP PROCEDURE IF EXISTS TrocarSenha;
DELIMITER //
CREATE PROCEDURE TrocarSenha(idUsuario INT, vSenha VARCHAR(255))
BEGIN
	
	UPDATE usuarios
	SET senha = vSenha
	WHERE id = vIdUsuario;
	
END //
DELIMITER ;


--
-- SP PARA REGISTRAR TRANSFERÊNCIA
--
DROP PROCEDURE IF EXISTS RegistrarTransferencia;
DELIMITER //
CREATE PROCEDURE RegistrarTransferencia(vIdUsuario INT, vBanco VARCHAR(3), vAgencia VARCHAR(6), vConta VARCHAR(10), vValor FLOAT)
BEGIN

	DECLARE lTime DATETIME;

	SET lTime = now();
	
	INSERT INTO transferencias (idUsuario, data, numeroBanco, numeroAgencia, numeroConta, valor)
	VALUES (vIdUsuario, lTime, vBanco, vAgencia, vConta, vValor);
	
	INSERT INTO lancamentosDinheiro (data, idUsuario, operacao, historico, valor)
	VALUES (lTime, vIdUsuario, 0, CONCAT("Reg Cred Usuario ", vIdUsuario, " Valor R$ ", vValor, ",00"), vValor);
	
END //
DELIMITER ;


--
-- SP PARA REGISTRAR CRÉDITO DE PERSONAGEM
--
DROP PROCEDURE IF EXISTS AdicionarPersonagem;
DELIMITER //
CREATE PROCEDURE AdicionarPersonagem(vIdUsuario INT, vIdPersonagem INT, vQuantidade INT)
BEGIN
	-- Variáveis
	DECLARE lTime INT;
	DECLARE lPrecoUnitario INT;
	
	SET lTime = now();
	SET lPrecoUnitario = 0.0;
		
	
	INSERT INTO lancamentosPersonagem (idPersonagem, idUsuario, operacao, data, historico, quantidade, precoUnitario)
	VALUES (vIdPersonagem, vIdUsuario, 0, lTime, CONCAT("Reg Credito Personagem #", vIdPersonagem), vQuantidade, lPrecoUnitario);
	
END //
DELIMITER ;


--
-- SP PARA REGISTRAR DÉBITO DE PERSONAGEM
--
DROP PROCEDURE IF EXISTS RemoverPersonagem;
DELIMITER //
CREATE PROCEDURE RemoverPersonagem(vIdUsuario INT, vIdPersonagem INT, vQuantidade INT, OUT oErro INT)
BEGIN
	-- Variáveis
	DECLARE lTime INT;
	DECLARE lSaldoDisponivel INT;
	DECLARE lPrecoUnitario INT;	
	
	SET lTime = now();
	SET lPrecoUnitario = 0.0;
	
	CALL CalculaSaldoDisponivelPersonagem(vIdUsuario, vIdPersonagem, lSaldoDisponivel);
	
	IF lSaldoDisponivel >= vQuantidade THEN
	
	INSERT INTO lancamentosPersonagem (idPersonagem, idUsuario, operacao, data, historico, quantidade, precoUnitario)
	VALUES (vIdPersonagem, vIdUsuario, 1, lTime, CONCAT("Reg Debito Personagem #", idPersonagem), vQuantidade, lPrecoUnitario);
	
	ELSE
		SET oErro = 1;
	END IF;
	
END //
DELIMITER ;


--
-- SP PARA REGISTRAR ORDENS DE VENDA
--
DROP PROCEDURE IF EXISTS RegistraOrdemVenda;
DELIMITER //
CREATE PROCEDURE RegistraOrdemVenda(vIdUsuario INT, vIdPersonagem INT, vQuantidade INT, vPrecoUnitario FLOAT, OUT oErro INT)
BEGIN
	-- Variáveis
	DECLARE lIdOrdemVenda INT;
	DECLARE lQuantidadeVenda INT;
	DECLARE lIdOrdemCompra INT;
	DECLARE lIdUsuarioComprador INT;
	DECLARE lQuantidadeCompra INT;
	DECLARE lPrecoCompra FLOAT;
	DECLARE lIdRestante INT;
	DECLARE lSaldoDisponivel INT;
	DECLARE lAgora DATETIME;
	
	-- Controle de transação
	-- DECLARE CONTINUE HANDLER FOR SQLEXCEPTION ROLLBACK;
	-- START TRANSACTION;

	-- Pega a data de agora
	SET lAgora = now();
	
	-- Calcula o saldo disponível do personagem
	CALL CalculaSaldoDisponivelPersonagem(vIdUsuario, vIdPersonagem, lSaldoDisponivel);

	-- Verifica se o saldo disponível permite a venda
	IF lSaldoDisponivel >= vQuantidade THEN
		
		-- Registra a oferta de venda
		INSERT INTO ofertas (data, tipo, idUsuario, idPersonagem, quantidade, quantidadeOriginal, precoUnitario, status)
		VALUES (lAgora, 0, vIdUsuario, vIdPersonagem, vQuantidade, vQuantidade, vPrecoUnitario, 0);
	
		-- Pega o identificador da oferta de venda
		SET lIdOrdemVenda = LAST_INSERT_ID();

		-- Bloqueio da quantidade de personagens no vendedor
		INSERT INTO lancamentosPersonagem (data, idUsuario, idPersonagem, operacao, historico, quantidade, precoUnitario)
		VALUES (lAgora, vIdUsuario, vIdPersonagem, 2, CONCAT("Reg Oferta Venda #", lIdOrdemVenda), vQuantidade, vPrecoUnitario);
		
		-- Guarda a quantidade vendida
		SET lQuantidadeVenda = vQuantidade;
	
		-- Se houver oferta de compra com preço maior que a venda, executa
		REPEAT
			SET lIdOrdemCompra = NULL;
			
			SELECT id, idUsuario, quantidade, precoUnitario
			INTO lIdOrdemCompra, lIdUsuarioComprador, lQuantidadeCompra, lPrecoCompra
			FROM ofertas
			WHERE status = 0
			AND tipo = 1
			AND idUsuario <> vIdUsuario
			AND idPersonagem = vIdPersonagem 
			AND precoUnitario >= vPrecoUnitario
			ORDER BY precoUnitario DESC, data ASC, id ASC
			LIMIT 1;
			
			IF NOT lIdOrdemCompra IS NULL THEN
				CALL ExecutaOrdens(lAgora, vIdPersonagem, lIdOrdemCompra, lIdUsuarioComprador, lQuantidadeCompra, lIdOrdemVenda, vIdUsuario, lQuantidadeVenda, vPrecoUnitario, lPrecoCompra, lIdRestante);
				IF lIdRestante <> 0 THEN SET lIdOrdemVenda = lIdRestante; END IF;  
			END IF;
	
		UNTIL (lQuantidadeVenda = 0) OR (lIdOrdemCompra IS NULL) END REPEAT;
	ELSE
		SET oErro = 1;
	END IF;
 
	-- COMMIT;
END //
DELIMITER ;


--
-- SP PARA REGISTRAR OFERTAS DE COMPRA
--

DROP PROCEDURE IF EXISTS RegistraOrdemCompra;
DELIMITER //
CREATE PROCEDURE RegistraOrdemCompra(vIdUsuario INT, vIdPersonagem INT, vQuantidade INT, vPrecoUnitario FLOAT, OUT oErro INT)
lb_registro_ordem_compra: BEGIN
	-- Variáveis
	DECLARE lIdOrdemVenda INT;
	DECLARE lIdOrdemCompra INT;
	DECLARE lQuantidadeCompra INT;
	DECLARE lIdUsuarioVendedor INT;
	DECLARE lQuantidadeVenda INT;
	DECLARE lPrecoVenda FLOAT;
	DECLARE lIdRestante INT;
	DECLARE lAgora DATETIME;
	DECLARE lValorTotal FLOAT;
	DECLARE lSaldoDisponivel INT;

	-- Controle de transação
	-- DECLARE CONTINUE HANDLER FOR SQLEXCEPTION ROLLBACK;
	-- START TRANSACTION;

	-- Inicializacao
	SET oErro = 0;

	-- Pega a data de lAgora
	SET lAgora = now();
	
	-- Calcula o valor total da compra
	SET lValorTotal = vQuantidade * vPrecoUnitario;
	
	-- Verifica se existem recursos financeiros suficientes
	CALL CalculaSaldoDisponivelDinheiro(vIdUsuario, lSaldoDisponivel);
	
	IF lSaldoDisponivel < lValorTotal THEN
		SET oErro = 1;
		LEAVE lb_registro_ordem_compra; 
	END IF;
	
	-- Registra a oferta de compra
	INSERT INTO ofertas (data, tipo, idUsuario, idPersonagem, quantidade, quantidadeOriginal, precoUnitario, status)
	VALUES (lAgora, 1, vIdUsuario, vIdPersonagem, vQuantidade, vQuantidade, vPrecoUnitario, 0);

	-- Pega o identificador da oferta de compra
	SET lIdOrdemCompra = LAST_INSERT_ID();

	-- Bloqueio dos recursos financeiros
	INSERT INTO lancamentosDinheiro (data, idUsuario, operacao, historico, valor)
	VALUES (lAgora, vIdUsuario, 2, CONCAT("Reg Oferta Compra #", lIdOrdemCompra), lValorTotal);
	
	-- Guarda a quantidade comprada
	SET lQuantidadeCompra = vQuantidade;

	-- Se houver oferta de venda com preço menor que a venda, executa
	REPEAT
		SET lIdOrdemVenda = NULL;

		SELECT id, idUsuario, quantidade, precoUnitario
		INTO lIdOrdemVenda, lIdUsuarioVendedor, lQuantidadeVenda, lPrecoVenda
		FROM ofertas
		WHERE status = 0
		AND tipo = 0
		AND precoUnitario <= vPrecoUnitario
		AND idUsuario <> vIdUsuario
		AND idPersonagem = vIdPersonagem
		ORDER BY precoUnitario ASC, data ASC, id ASC
		LIMIT 1;
		
		IF NOT lIdOrdemVenda IS NULL THEN
			CALL ExecutaOrdens(lAgora, vIdPersonagem, lIdOrdemCompra, vIdUsuario, lQuantidadeCompra, lIdOrdemVenda, lIdUsuarioVendedor, lQuantidadeVenda, lPrecoVenda, vPrecoUnitario, lIdRestante);
			IF lIdRestante <> 0 THEN SET lIdOrdemCompra = lIdRestante; END IF;  
		END IF;
	
	UNTIL (lQuantidadeCompra = 0) OR (lIdOrdemVenda IS NULL) END REPEAT;
 
	-- COMMIT;
END //
DELIMITER ;


--
-- SP PARA FAZER O SETTLEMENT ENTRE UMA COMPRA E UMA VENDA DE ACORDO COM SUAS QUANTIDADES
-- A SP não tem tratamento de transações porque deve ser usada apenas por outras SP
-- ou em casos de teste do banco de dados.
--

DROP PROCEDURE IF EXISTS ExecutaOrdens;
DELIMITER //
CREATE PROCEDURE ExecutaOrdens(vAgora DATETIME, vIdPersonagem INT, INOUT vIdCompra INT, vIdUsuarioComprador INT, INOUT vQuantidadeCompra INT, vIdVenda INT, vIdUsuarioVendedor INT, INOUT vQuantidadeVenda INT, vPrecoVenda FLOAT, vPrecoCompra FLOAT, OUT vIdRestante INT)
BEGIN
	IF vQuantidadeCompra = vQuantidadeVenda THEN
		CALL ExecutaCompraIgualVenda(vAgora, vIdPersonagem, vIdCompra, vIdUsuarioComprador, vQuantidadeCompra, vIdVenda, vIdUsuarioVendedor, vQuantidadeVenda, vPrecoVenda, vPrecoCompra);
		SET vQuantidadeVenda = 0; 
		SET vQuantidadeCompra = 0; 
		SET vIdRestante = 0;

	ELSEIF vQuantidadeCompra > vQuantidadeVenda THEN
		CALL ExecutaCompraMaiorVenda(vAgora, vIdPersonagem, vIdCompra, vIdUsuarioComprador, vQuantidadeCompra, vIdVenda, vIdUsuarioVendedor, vQuantidadeVenda, vPrecoVenda, vPrecoCompra, vIdRestante);
		SET vQuantidadeCompra = vQuantidadeCompra - vQuantidadeVenda;
		SET vQuantidadeVenda = 0;
		SET vIdCompra = vIdRestante;
		SET vIdRestante = 0;

	ELSE
		CALL ExecutaVendaMaiorCompra(vAgora, vIdPersonagem, vIdCompra, vIdUsuarioComprador, vQuantidadeCompra, vIdVenda, vIdUsuarioVendedor, vQuantidadeVenda, vPrecoVenda, vPrecoCompra, vIdRestante);
		SET vQuantidadeVenda = vQuantidadeVenda - vQuantidadeCompra; 
		SET vQuantidadeCompra = 0;
	END IF;
END //
DELIMITER ;


--
-- SP PARA FAZER O SETTLEMENT ENTRE UMA COMPRA E UMA VENDA COM QUANTIDADES IGUAIS
-- A SP não tem tratamento de transações porque deve ser usada apenas por outras SP
-- ou em casos de teste do banco de dados.
--

DROP PROCEDURE IF EXISTS ExecutaCompraIgualVenda;
DELIMITER //
CREATE PROCEDURE ExecutaCompraIgualVenda(vAgora DATETIME, vIdPersonagem INT, vIdCompra INT, vIdUsuarioComprador INT, vQuantidadeCompra INT, vIdVenda INT, vIdUsuarioVendedor INT, vQuantidadeVenda INT, vPrecoVenda FLOAT, vPrecoCompra FLOAT)
BEGIN
	-- executa a oferta de compra integralmente
	UPDATE ofertas
	SET status = 1
	WHERE id = vIdCompra;

	-- Executa a liquidacao
	CALL ExecutaLiquidacao(vAgora, vIdPersonagem, vIdCompra, vIdUsuarioComprador, vQuantidadeCompra, vPrecoCompra, vIdVenda, vIdUsuarioVendedor, vQuantidadeVenda, vPrecoVenda, vQuantidadeVenda);
	
	-- registra o casamento de ordens
	INSERT INTO casamentosOferta (idOfertaCompra, idOfertaVenda, dataExecucao)
	VALUES (vIdCompra, vIdVenda, vAgora);

	-- executa a oferta de venda integralmente
	UPDATE ofertas
	SET status = 1
	WHERE id = vIdVenda;
END //
DELIMITER ;


--
-- SP PARA FAZER O SETTLEMENT ENTRE UMA COMPRA E UMA VENDA QUANDO A VENDA TIVER QUANTIDADE MAIOR DO QUE A COMPRA
-- A SP não tem tratamento de transações porque deve ser usada apenas por outras SP
-- ou em casos de teste do banco de dados.
--

DROP PROCEDURE IF EXISTS ExecutaVendaMaiorCompra;
DELIMITER //
CREATE PROCEDURE ExecutaVendaMaiorCompra(vAgora DATETIME, vIdPersonagem INT, vIdCompra INT, vIdUsuarioComprador INT, vQuantidadeCompra INT, vIdVenda INT, vIdUsuarioVendedor INT, vQuantidadeVenda INT, vPrecoVenda FLOAT, vPrecoCompra FLOAT, OUT vIdVendaRestante INT)
BEGIN
	DECLARE lQuantidadeRestante INT;
	
	-- executa a oferta de compra integralmente
	UPDATE ofertas
	SET status = 1
	WHERE id = vIdCompra;

	-- Executa a liquidacao
	CALL ExecutaLiquidacao(vAgora, vIdPersonagem, vIdCompra, vIdUsuarioComprador, vQuantidadeCompra, vPrecoCompra, vIdVenda, vIdUsuarioVendedor, vQuantidadeVenda, vPrecoVenda, vQuantidadeCompra);

	-- Calcula a quantidade restante da venda
	SET lQuantidadeRestante = vQuantidadeVenda - vQuantidadeCompra;
	
	-- cria uma nova oferta de venda com a diferenca do que não foi vendido
	INSERT INTO ofertas (data, tipo, idUsuario, idPersonagem, quantidade, quantidadeOriginal, idOrdemOriginal, precoUnitario, status)
	SELECT data, 0, idUsuario, idPersonagem, lQuantidadeRestante, lQuantidadeRestante, vIdVenda, precoUnitario, 0
	FROM ofertas
	WHERE id = vIdVenda;				
	
	-- guarda o identificador da venda que foi criada para o restante
	SET vIdVendaRestante = LAST_INSERT_ID();

	-- Bloqueia os ativos da nova oferta de venda
	INSERT INTO lancamentosPersonagem (data, idUsuario, idPersonagem, operacao, historico, quantidade, precoUnitario)
	VALUES (vAgora, vIdUsuarioVendedor, vIdPersonagem, 2, CONCAT("Reg Oferta Venda #", vIdVendaRestante, " DD"), lQuantidadeRestante, vPrecoVenda);
		
	-- registra o casamento de ordens
	INSERT INTO casamentosOferta (idOfertaCompra, idOfertaVenda, dataExecucao)
	VALUES (vIdCompra, vIdVenda, vAgora);

	-- marca a oferta de venda original como executada
	UPDATE ofertas
	SET status = 1,
	quantidade = vQuantidadeCompra
	WHERE id = vIdVenda;
END //
DELIMITER ;


--
-- SP PARA FAZER O SETTLEMENT ENTRE UMA COMPRA E UMA VENDA QUANDO A COMPRA TIVER QUANTIDADE MAIOR DO QUE A VENDA
-- A SP não tem tratamento de transações porque deve ser usada apenas por outras SP
-- ou em casos de teste do banco de dados.
--

DROP PROCEDURE IF EXISTS ExecutaCompraMaiorVenda;
DELIMITER //
CREATE PROCEDURE ExecutaCompraMaiorVenda(vAgora DATETIME, vIdPersonagem INT, vIdCompra INT, vIdUsuarioComprador INT, vQuantidadeCompra INT, vIdVenda INT, vIdUsuarioVendedor INT, vQuantidadeVenda INT, vPrecoVenda FLOAT, vPrecoCompra FLOAT, OUT vIdCompraRestante INT)
BEGIN
	DECLARE lNovoIdCompra INT;
	DECLARE lQuantidadeRestante INT;
	DECLARE lValorTotal FLOAT;
	
	-- Calcula a quantidade restante da compra
	SET lQuantidadeRestante = vQuantidadeCompra - vQuantidadeVenda;

	-- cria uma nova oferta de compra com a diferença do que não foi vendido
	INSERT INTO ofertas (data, tipo, idUsuario, idPersonagem, quantidade, quantidadeOriginal, idOrdemOriginal, precoUnitario, status)
	SELECT data, 1, idUsuario, idPersonagem, lQuantidadeRestante, lQuantidadeRestante, vIdCompra, precoUnitario, 0
	FROM ofertas
	WHERE id = vIdCompra;
	
	-- Pega o identificador da nova compra
	SET lNovoIdCompra = LAST_INSERT_ID();
	
	-- marca a oferta de compra original como executada
	UPDATE ofertas
	SET status = 1,
	quantidade = vQuantidadeVenda
	WHERE id = vIdCompra;

	-- Executa a liquidacao 
	CALL ExecutaLiquidacao(vAgora, vIdPersonagem, vIdCompra, vIdUsuarioComprador, vQuantidadeCompra, vPrecoCompra, vIdVenda, vIdUsuarioVendedor, vQuantidadeVenda, vPrecoVenda, vQuantidadeVenda);
	
	-- Calcula o valor total da compra residual
	SET lValorTotal = lQuantidadeRestante * vPrecoCompra;

	-- Bloqueia os recursos financeiros referentes ao valor da compra
	INSERT INTO lancamentosDinheiro (data, idUsuario, operacao, historico, valor)
	VALUES (vAgora, vIdUsuarioComprador, 2, CONCAT("Reg Oferta Compra #", lNovoIdCompra, " - Valor DD"), lValorTotal);
		
	-- registra o casamento de ordens
	INSERT INTO casamentosOferta (idOfertaCompra, idOfertaVenda, dataExecucao)
	VALUES (vIdCompra, vIdVenda, vAgora);

	-- executa a oferta de venda integralmente
	UPDATE ofertas
	SET status = 1
	WHERE id = vIdVenda;
	
	-- Muda o ID da compra que está sendo processada
	SET vIdCompraRestante = lNovoIdCompra;
END //
DELIMITER ;


--
-- SP para liquidacao no mercado 
--

DROP PROCEDURE IF EXISTS ExecutaLiquidacao;
DELIMITER //
CREATE PROCEDURE ExecutaLiquidacao(lAgora DATETIME, vIdPersonagem INT, vIdCompra INT, vIdUsuarioComprador INT, vQuantidadeCompra INT, vPrecoCompra FLOAT, vIdVenda INT, vIdUsuarioVendedor INT, vQuantidadeVenda INT, vPrecoVenda FLOAT, vQuantidadeNegociada INT)
BEGIN
	DECLARE lValorTotalCompra FLOAT;
	DECLARE lValorTotalNegociado FLOAT;
	
	-- Calcula o valor total da compra e da negociacao
	SET lValorTotalCompra = vQuantidadeCompra * vPrecoCompra;
	SET lValorTotalNegociado = vQuantidadeNegociada * vPrecoVenda;
	
	-- Libera os recursos financeiros no comprador
	INSERT INTO lancamentosDinheiro (data, idUsuario, operacao, historico, valor)
	VALUES (lAgora, vIdUsuarioComprador, 3, CONCAT("Exec CP Venda #", vIdVenda, " Compra #", vIdCompra), lValorTotalCompra);

	-- Faz o saque dos recursos financeiros no comprador
	INSERT INTO lancamentosDinheiro (data, idUsuario, operacao, historico, valor)
	VALUES (lAgora, vIdUsuarioComprador, 1, CONCAT("Exec CP Venda #", vIdVenda, " Compra #", vIdCompra), lValorTotalNegociado);

	-- Faz o depósito dos recursos financeiros na conta do vendedor
	INSERT INTO lancamentosDinheiro (data, idUsuario, operacao, historico, valor)
	VALUES (lAgora, vIdUsuarioVendedor, 0, CONCAT("Exec CP Venda #", vIdVenda, " Compra #", vIdCompra), lValorTotalNegociado);

	-- Libera os ativos no vendedor
	INSERT INTO lancamentosPersonagem (data, idUsuario, idPersonagem, operacao, historico, quantidade, precoUnitario)
	VALUES (lAgora, vIdUsuarioVendedor, vIdPersonagem, 3, CONCAT("Exec VN Venda #", vIdVenda, " Compra #", vIdCompra), vQuantidadeVenda, vPrecoVenda);

	-- Faz o saque dos ativos no vendedor
	INSERT INTO lancamentosPersonagem (data, idUsuario, idPersonagem, operacao, historico, quantidade, precoUnitario)
	VALUES (lAgora, vIdUsuarioVendedor, vIdPersonagem, 1, CONCAT("Exec VN Venda #", vIdVenda, " Compra #", vIdCompra), vQuantidadeNegociada, vPrecoVenda);

	-- Faz o depósito do ativo na conta do comprador
	INSERT INTO lancamentosPersonagem (data, idUsuario, idPersonagem, operacao, historico, quantidade, precoUnitario)
	VALUES (lAgora, vIdUsuarioComprador, vIdPersonagem, 0, CONCAT("Exec VN Venda #", vIdVenda, " Compra #", vIdCompra), vQuantidadeNegociada, vPrecoVenda);
END //
DELIMITER ;


--
-- SP PARA CANCELAR ORDENS DE VENDA
--

DROP PROCEDURE IF EXISTS CancelaOrdemVenda;
DELIMITER //
CREATE PROCEDURE CancelaOrdemVenda(vIdVenda INT)
BEGIN
	DECLARE lAgora DATETIME;
	DECLARE lIdUsuario INT;
	DECLARE lIdPersonagem INT;
	DECLARE lQuantidade INT;
	DECLARE lPrecoUnitario FLOAT;

	-- Controle de transação
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION ROLLBACK;
	START TRANSACTION;
	
	--	Pega a data de lAgora
	SET lAgora = now();

	-- Captura os dados da oferta de venda
	SELECT idUsuario, idPersonagem, quantidade, precoUnitario
	INTO lIdUsuario, lIdPersonagem, lQuantidade, lPrecoUnitario
	FROM ofertas
	WHERE id = vIdVenda;

	-- Liberacao da quantidade de ativos no vendedor
	INSERT INTO lancamentosPersonagem (data, idUsuario, idPersonagem, operacao, historico, quantidade, precoUnitario)
	VALUES (lAgora, lIdUsuario, lIdPersonagem, 3, CONCAT("Canc Oferta Venda #", vIdVenda), lQuantidade, lPrecoUnitario);
	
	--	Marca a oferta de venda como cancelada
	UPDATE ofertas
	SET status = 2
	WHERE id = vIdVenda;

	COMMIT;
END //
DELIMITER ;


--
-- SP PARA CANCELAR OFERTAS DE COMPRA
--

DROP PROCEDURE IF EXISTS CancelaOrdemCompra;
DELIMITER //
CREATE PROCEDURE CancelaOrdemCompra(vIdCompra INT)
BEGIN
	DECLARE lAgora DATETIME;
	DECLARE lIdUsuario INT;
	DECLARE lQuantidade INT;
	DECLARE lPrecoUnitario FLOAT;
	DECLARE lValorTotal FLOAT;

	-- Controle de transação
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION ROLLBACK;
	START TRANSACTION;
	
	--	Pega a data de lAgora
	SET lAgora = now();

	-- Captura os dados da oferta de compra
	SELECT idUsuario, idPersonagem, quantidade, precoUnitario
	INTO lIdUsuario, lQuantidade, lPrecoUnitario
	FROM ofertas
	WHERE id = vIdCompra;

	-- Calcula o valor total da compra
	SET lValorTotal = lQuantidade * lPrecoUnitario;
	
	-- Libera os recursos financeiros no comprador	
	INSERT INTO lancamentosDinheiro (data, idUsuario, operacao, historico, valor)
	VALUES (lAgora, lIdUsuario, 3, CONCAT("Canc Oferta Compra #", vIdCompra), lValorTotal);
	
	--	Marca a oferta de compra como cancelada
	UPDATE ofertas
	SET status = 2
	WHERE id = vIdCompra;

	COMMIT;
END //
DELIMITER ;


--
-- SP PARA CALCULAR O SALDO DISPONÍVEL EM UMA CONTA CORRENTE DE PERSONAGENS
-- 

DROP PROCEDURE IF EXISTS CalculaSaldoDisponivelPersonagem;
DELIMITER //
CREATE PROCEDURE CalculaSaldoDisponivelPersonagem(vIdUsuario INT, vIdPersonagem INT, OUT vSaldo INT)
BEGIN
	SELECT SUM(CASE operacao WHEN 0 THEN quantidade WHEN 1 THEN -quantidade WHEN 2 THEN -quantidade WHEN 3 THEN quantidade END)
	INTO vSaldo
	FROM lancamentosPersonagem
	WHERE idUsuario = vIdUsuario
	AND idPersonagem = vIdPersonagem;
	
	IF vSaldo IS NULL THEN SET vSaldo = 0.0; END IF;
END //
DELIMITER ;


--
-- SP PARA CALCULAR O SALDO DISPONÍVEL EM UMA CONTA CORRENTE DE DINHEIRO
-- 

DROP PROCEDURE IF EXISTS CalculaSaldoDisponivelDinheiro;
DELIMITER //
CREATE PROCEDURE CalculaSaldoDisponivelDinheiro(vIdUsuario INT, OUT vSaldo INT)
BEGIN
	SELECT SUM(CASE operacao WHEN 0 THEN valor WHEN 1 THEN -valor WHEN 2 THEN -valor WHEN 3 THEN valor END)
	INTO vSaldo
	FROM lancamentosDinheiro
	WHERE idUsuario = vIdUsuario;
	
	IF vSaldo IS NULL THEN SET vSaldo = 0.0; END IF;
END //
DELIMITER ;
