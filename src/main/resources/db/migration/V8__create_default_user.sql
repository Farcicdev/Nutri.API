INSERT INTO nutricionistas (
    nome,
    email,
    senha,
    telefone,
    crn,
    especialidade,
    ativo,
    role
)
VALUES (
    'Usuário Teste',
    'teste@gmail.com',
    '$2a$10$i7P9gpp4FYlM1So/.Fl1h./dPZgzSockntttjfrrFoGPY2Hw6gLBS',
    NULL,
    'CRN-TESTE-0001',
    NULL,
    TRUE,
    'NUTRICIONISTA'
)
ON CONFLICT DO NOTHING;
