# Catalog Admin API

## Sobre
Este é um projeto de estudo que estou utilizando para aprimorar minhas capacidades como desenvolvedor de software. O objetivo é aplicar conceitos de Domain Driven Design, Clean Architecture e boas práticas de desenvolvimento para construir uma aplicação robusta e escalável.


## Domain Driven Design

### Tactical Patterns
- **Identifier**: Identificador único para entidades.
- **Aggregate Root**: Raiz de agregação que define limites de transações.
- **Value Objects**: Objetos imutáveis que representam características de uma entidade.

### Domain Layer
- **Validation Handlers**: Tratamento de validações de dados.
- **Notification Pattern**: Padrão de notificação para comunicação interna.
- **Interface Gateways**: Interface que define a comunicação entre a camada de domínio e a infraestrutura.

## Clean Architecture

### Application Layer

- Camada de aplicação que contém casos de uso (Use Cases).

### Use Cases

- **UseCase**: Caso de uso genérico com entrada e saída.
- **UnaryUseCase**: Caso de uso com uma entrada.
- **NullaryUseCase**: Caso de uso sem entrada.

## Infrastructure

- Implementação de gateways com banco de dados MySql usando docker-compose.
- Tecnologias: Spring Boot, Spring Web (RestControllers), Spring Data JPA.
- Serialização de dados com Jackson.

## Testes

- **JUnit5**: Testes de domínio com asserções.
- **Mockito**: Mocking para testes de casos de uso da aplicação.
- **Testes de Integração**: Utilização de banco de dados H2 para testes integrados.
- **SpringBootTest**: Testes de contexto Spring.
- **Testes de Serialização com Jackson**.
- **Testes End-to-End com TestContainers**: Testes que envolvem a infraestrutura completa.

## Como Executar

Para executar o projeto localmente, siga os passos abaixo:
1. Clone este repositório para o seu ambiente de desenvolvimento:
   `git clone https://github.com/seu-usuario/seu-projeto.git`

2. Navegue até o diretório do projeto:
   `cd seu-projeto`

3. Inicie os serviços necessários usando o docker-compose:
   `docker-compose up -d`

4. Execute o projeto utilizando o Gradle:
   `./gradlew bootRun`

## Licença

Este projeto está licenciado sob a [Licença MIT](https://opensource.org/licenses/MIT).


