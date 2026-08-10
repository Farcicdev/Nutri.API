CREATE TABLE avaliacoes_antropometricas (
    id BIGSERIAL PRIMARY KEY,
    peso NUMERIC(6, 2) NOT NULL,
    altura NUMERIC(4, 2) NOT NULL,
    data_avaliacao TIMESTAMP NOT NULL,
    cintura NUMERIC(6, 2),
    quadril NUMERIC(6, 2),
    peitoral NUMERIC(6, 2),
    tricipital NUMERIC(6, 2),
    subescapular NUMERIC(6, 2),
    bicipital NUMERIC(6, 2),
    suprailiaca NUMERIC(6, 2),
    abdominal NUMERIC(6, 2),
    coxa NUMERIC(6, 2),
    axilar_media NUMERIC(6, 2),
    protocolo_dobras VARCHAR(40) NOT NULL,
    formula_conversao VARCHAR(20) NOT NULL,
    versao_formula INTEGER NOT NULL DEFAULT 1,
    idade_utilizada_calculo INTEGER NOT NULL,
    sexo_utilizado_calculo VARCHAR(20) NOT NULL,
    soma_dobras NUMERIC(7, 2) NOT NULL,
    densidade_corporal NUMERIC(7, 5),
    imc NUMERIC(6, 2) NOT NULL,
    percentual_gordura NUMERIC(5, 2) NOT NULL,
    massa_gorda NUMERIC(6, 2) NOT NULL,
    massa_magra NUMERIC(6, 2) NOT NULL,
    observacoes VARCHAR(1000),
    consulta_id BIGINT NOT NULL,
    CONSTRAINT fk_avaliacoes_antropometricas_consulta
        FOREIGN KEY (consulta_id) REFERENCES consultas (id),
    CONSTRAINT ck_avaliacoes_antropometricas_peso CHECK (peso > 0),
    CONSTRAINT ck_avaliacoes_antropometricas_altura CHECK (altura > 0),
    CONSTRAINT ck_avaliacoes_antropometricas_idade CHECK (idade_utilizada_calculo >= 0)
);

CREATE INDEX idx_avaliacoes_antropometricas_consulta_id
    ON avaliacoes_antropometricas (consulta_id);
