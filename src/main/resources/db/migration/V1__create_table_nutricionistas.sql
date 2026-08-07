CREATE TABLE nutricionistas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    crn VARCHAR(30) NOT NULL UNIQUE,
    especialidade VARCHAR(120)
);
