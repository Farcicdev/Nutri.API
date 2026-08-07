CREATE TABLE pacientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    data_nascimento DATE NOT NULL,
    sexo VARCHAR(20) NOT NULL,
    email VARCHAR(160) NOT NULL,
    telefone VARCHAR(20),
    observacoes VARCHAR(2000),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    nutricionista_id BIGINT NOT NULL,
    CONSTRAINT fk_pacientes_nutricionista
        FOREIGN KEY (nutricionista_id) REFERENCES nutricionistas (id),
    CONSTRAINT uk_pacientes_email_nutricionista
        UNIQUE (nutricionista_id, email)
);

CREATE INDEX idx_pacientes_nutricionista_id
    ON pacientes (nutricionista_id);
