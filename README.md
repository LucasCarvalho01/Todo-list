# Todo App

Aplicação full-stack de gerenciamento de tarefas (Todo List).

É possível criar uma conta, fazer login e gerenciar as tarefas - Criar, listar, editar e excluir tarefas.

## Como Executar Localmente

Para executar toda a aplicação localmente, necessário apenas Docker e Docker Compose instalados. 

```bash
docker-compose up --build
```

Esse único comando irá:
- Construir as imagens do frontend e backend
- Configurar o banco de dados PostgreSQL
- Inicializar todos os serviços
- Configurar a rede entre os containers

### Acessos

Após a execução, a aplicação estará disponível em:

- **Frontend**: http://localhost:80
- **Backend**: http://localhost:8080
- **DB**: localhost:5432

## Itens implementados:
- **A\)** Frontend implementado com Angular 20 (versão mais recente)

- **B\)** Backend implementado com Java 21 e Spring Boot 3;
Lombok e MapStruct para redução de boilerplate; <br>

- **C\)** Utilização de Banco de Dados PostgreSQL e Spring Data JPA para persistência de dados
- **D\)** API RESTful com endpoints seguindo o padrão REST
- **E\)** Spring Security e jjwt para autenticação e autorização por meio de tokens JWT; <br>
Autenticação stateless via filtro e CORS configurado, além de rotas públicas mínimas. <br>  
Senha armazenada em banco de dados encriptada com BCrypt. <br>
Configurado interceptor no Angular para adicionar o token JWT em todas as requisições; <br>
Configurado Guard no Angular para verificar se o usuário está autenticado;

- **F\)** Implementação de testes unitários com JUnit e Mockito; <br>
Configurado cobertura de testes com Jacoco; <br>
Configurado GitHub Actions para executar os testes unitários e gerar relatório de cobertura ao abrir uma PR para main; <br>

- **G\)** Configurado Swagger para documentação da API e realizar requisições de forma autenticada; <br>

- **H\)** Aplicação publicada no Render; <br>

- **I\)** Configurado Docker Compose para orquestrar os serviços; <br>
Lógica de auditoria e soft-deletena entidade de Usuários;
Spring Actuator para monitoramento da aplicação;
Interface do frontend 


### Serviços

A aplicação utiliza Docker Compose para orquestrar três serviços principais:

1. **Database (`db`)**
   - Imagem: `postgres:17.5-alpine`
   - Porta: 5432

2. **Backend (`backend`)**
   - Build: `./backend/Dockerfile`
   - Porta: 8080
   - Dependência: aguarda database estar pronto
   - Variáveis de ambiente configuradas

3. **Frontend (`frontend`)**
   - Build: `./frontend/Dockerfile`
   - Porta: 80
   - Dependência: aguarda backend estar pronto
   - Servido via Nginx

## Documentação da API

Com o backend rodando, o Swagger estará disponível em:
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## Estrutura do Projeto (monorepo)

```
todo-app/
├── backend/                 # Spring Boot API
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                # Angular App
│   ├── src/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml       # Orquestração dos serviços
└── README.md               
```

