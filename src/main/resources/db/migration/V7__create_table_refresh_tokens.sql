CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    nutricionista_id BIGINT NOT NULL REFERENCES nutricionistas(id) ON DELETE CASCADE,
    persistente BOOLEAN NOT NULL,
    expira_em TIMESTAMP WITH TIME ZONE NOT NULL,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL,
    revogado_em TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_refresh_tokens_nutricionista_id
    ON refresh_tokens (nutricionista_id);

