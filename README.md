📦 Codeshop Ecommerce - Backend

Backend de um sistema de e-commerce desenvolvido em Java com Spring Boot, com autenticação JWT, gestão de produtos, pedidos e integração com Stripe.

🛠 <b>Tecnologias</b>: Java 17+, Spring Boot, PostgreSQL (banco de dados), Spring Security + JWT (autenticação), Maven (gerenciamento de dependências), JUnit 5 + Mockito (testes)

⚙️ <b>Funcionalidades</b>: Autenticação de usuários (registro/login) com roles (USER e ADMIN), CRUD de produtos (apenas administradores), Gestão de pedidos (criação, listagem e atualização de status), Validações de dados com Bean Validation, Paginação e ordenação em endpoints (ex: /products), Integração com Stripe (simulação de pagamento)

🏗️ Estrutura do Projeto:

src/  
├── main/  
│   ├── java/  
│   │   └── com/codeshop/  
│   │       ├── controller/    # Endpoints REST  
│   │       ├── dto/           # Objetos de transferência  
│   │       ├── model/         # Entidades JPA  
│   │       ├── repository/    # Interfaces JpaRepository  
│   │       ├── service/       # Lógica de negócio  
│   │       └── security/      # Configurações de JWT  
│   └── resources/  
│       ├── application.properties # Configurações  
│       └── data.sql           # Dados iniciais (opcional)  
├── test/                      # Testes  
└── pom.xml                    # Dependências  

📌 Destaques:
🔹 Clean Architecture: Separação clara entre camadas (DTOs, serviços, entidades).
🔹 Segurança robusta: JWT + Spring Security com controle de roles.
🔹 Tratamento de exceções: Global com @ControllerAdvice.

Nota: O frontend não está incluído neste repositório.

<p align="center"> <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" /> <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" /> <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" /> </p>
