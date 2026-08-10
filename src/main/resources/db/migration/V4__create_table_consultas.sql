CREATE TABLE consultas (
    id BIGSERIAL PRIMARY KEY,
    data_consulta TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AGENDADA',
    tipo VARCHAR(30) NOT NULL,
    observacoes VARCHAR(2000),
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP NOT NULL,
    paciente_id BIGINT NOT NULL,
    nutricionista_id BIGINT NOT NULL,
    CONSTRAINT fk_consultas_paciente
        FOREIGN KEY (paciente_id) REFERENCES pacientes (id),
    CONSTRAINT fk_consultas_nutricionista
        FOREIGN KEY (nutricionista_id) REFERENCES nutricionistas (id)
);

CREATE INDEX idx_consultas_paciente_id
    ON consultas (paciente_id);

CREATE INDEX idx_consultas_nutricionista_data
    ON consultas (nutricionista_id, data_consulta);
