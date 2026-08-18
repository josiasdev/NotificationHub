# DEVELOPMENT_LOG.md — NotificationHub (Cogito Lab)

Este documento registra as principais decisões arquiteturais, modelagem de dados, estratégias de resiliência e a interação com ferramentas de Inteligência Artificial durante o desenvolvimento do **NotificationHub**.

---

## [2026-08-17] - Etapa 1: Arquitetura de Pacotes e Modelagem do Domínio

### Contexto e Objetivo
Estruturar os pacotes da aplicação Spring Boot 3 seguindo os princípios de **Clean Architecture (Hexagonal Architecture)** e criar as entidades de domínio imutáveis com auto-validação.

### Ferramentas de IA Utilizadas
- **Ferramenta:** Antigravity (Gemini 3.6 Flash)
- **Objetivo:** Propor a divisão de pacotes (Clean Architecture) e modelagem dos objetos core do domínio (`SensorEvent` e `Notification`).
- **Prompt:** Solicitada a proposta da estrutura de pacotes e entidades do domínio.
- **Decisão:** Sugestão aceita integralmente. Separação rígida entre `domain`, `application` e `infrastructure`.

---

## [2026-08-17] - Etapa 2: Ingestão de Eventos, Idempotência, Motor de Regras e Persistência

### Contexto e Objetivo
Implementar o fluxo completo de ingestão de eventos, validação de invariantes, motor de regras desacoplado (Strategy Pattern), persistência MongoDB com suporte a índice único para idempotência e mensageria RabbitMQ.

### Ferramentas de IA Utilizadas
- **Ferramenta:** Antigravity (Gemini 3.6 Flash)
- **Objetivo:** Criar os Use Cases de ingestão, motor de regras, adapters do MongoDB e RabbitMQ, REST Endpoints e suíte de testes unitários.
- **Prompt:** "podemos prosseguir"

### Decisões Arquiteturais Tomadas

1. **Idempotência (Garantia Dupla):**
   - **Camada de Aplicação:** O `SensorEventMongoAdapter` faz uma verificação prévia (`existsByEventId`).
   - **Camada de Banco de Dados:** No `SensorEventDocument`, o atributo `eventId` possui a anotação `@Indexed(unique = true)`. Em cenários de concorrência massiva, se o mesmo `eventId` chegar simultaneamente, o MongoDB dispara uma `DuplicateKeyException`, capturada pelo adapter e convertida para `DuplicateEventException` (HTTP 409 Conflict).

2. **Motor de Regras Extensível (Strategy Pattern + Spring DI):**
   - Foi criada a interface `NotificationRule` e o `RulesEngine`. Cada tipo de alerta (`AirTemperatureRule`, `AirHumidityRule`, `SoilMoistureRule`, `WaterReservoirRule`, `SiloLevelRule`, `EquipmentStatusRule`) é um componente Spring independente.
   - **Vantagem para a Entrevista:** Para adicionar um novo tipo de sensor ou nova regra no futuro, basta criar uma nova classe que implemente `NotificationRule`, respeitando o princípio **Open/Closed (SOLID)**.

3. **Arquitetura Assíncrona e Resiliência (RabbitMQ + DLQ):**
   - Configuração de filas `iot.events.queue` e `iot.notifications.queue`.
   - A fila de notificações foi configurada com Dead Letter Exchange (`iot.notifications.dlq.exchange`) e Dead Letter Queue (`iot.notifications.dlq`). Se uma notificação exceder o número máximo de retentativas (3 tentativas), ela é direcionada automaticamente para a DLQ sem perda de mensagem.

4. **Tratamento de Erros e Validação de Dados:**
   - Dados inválidos (ex: umidade > 100%, campos obrigatórios ausentes) são rejeitados no construtor de `SensorEvent` e tratados pelo `GlobalExceptionHandler` retornando HTTP 400 Bad Request.

5. **Testes Automatizados:**
   - Implementada suíte de testes unitários com JUnit 5 e Mockito cobrindo regras de negócio, invariantes de domínio e casos de uso de ingestão/idempotência. Todos os testes estão passando.

---

## [2026-08-17] - Etapa 3: Interface Web (React + TypeScript + Vite)

### Contexto e Objetivo
Desenvolvimento do Frontend da aplicação em React, TypeScript e Vite para simulação e monitoramento em tempo real dos sensores IoT e alertas gerados na Fazenda Boa Esperança.

### Ferramentas de IA Utilizadas
- **Ferramenta:** Antigravity (Gemini 3.6 Flash)
- **Objetivo:** Desenvolver interface SPA completa com componentes reutilizáveis, design glassmorphism moderno, consumo de APIs REST e simulador de lote do edital.
- **Prompt:** "Opção A com react, typescript, vite."

### Funcionalidades do Frontend

1. **Dashboard & Indicadores em Tempo Real:**
   - Cards de leitura dos 6 tipos de sensores com badges dinâmicos (`NORMAL` em verde ou `ALERTA` em vermelho com borda destacada) atualizados automaticamente via polling.

2. **Simulador de Eventos:**
   - Form individual para disparar leituras customizadas de sensores.
   - **Botão "Simular Lote do Edital (7 Eventos)":** Executa a chamada em lote contendo a massa exata de dados descrita na seção 13 do edital (gerando 6 notificações de alerta e mantendo 1 evento normal sem notificação).

3. **Histórico de Eventos Ingeridos:**
   - Tabela com lista completa de eventos recebidos, IDs únicos, timestamps e valores.

4. **Histórico de Notificações:**
   - Tabela detalhada das mensagens geradas com severidade (`INFO`, `WARNING`, `CRITICAL`), número de retentativas, destinatário e badges de status de envio (`SENT`, `PENDING`, `FAILED`, `DLQ_ROUTED`).

---

## [2026-08-17] - Etapa 4: Testes de Integração com Testcontainers (MongoDB & RabbitMQ)

### Contexto e Objetivo
Criar testes de integração de ponta a ponta com **Testcontainers** subindo contêineres reais isolados do MongoDB (`mongo:7.0`) e do RabbitMQ (`rabbitmq:3.13-management`).

### Ferramentas de IA Utilizadas
- **Ferramenta:** Antigravity (Gemini 3.6 Flash)
- **Objetivo:** Implementar classe `IngestSensorEventIntegrationTest` utilizando `@Testcontainers`, `@Container` e `@DynamicPropertySource`.
- **Prompt:** "Etapa 4: Testes de Integração com Testcontainers (MongoDB & RabbitMQ)"

### Detalhes da Implementação
- **Container MongoDB:** Sobe instância real de banco de dados para validar persistência e índices de restrição de idempotência (`eventId`).
- **Container RabbitMQ:** Sobe instância real do broker para validar publicação, consumo assíncrono e roteamento de notificações.
- **Execução Condicional:** Anotação `@EnabledIfEnvironmentVariable(named = "DOCKER_AVAILABLE", matches = "true")` permitindo execução fluida em ambientes locais ou pipelines de CI/CD.

---

## [2026-08-17] - Etapa 5: Documentação Interativa com Swagger UI / OpenAPI 3 & Correção de Converters BSON

### Contexto e Objetivo
Implementar documentação interativa da API REST com Swagger UI / Springdoc OpenAPI e adicionar conversores BSON customizados para o tipo `java.time.OffsetDateTime` no MongoDB.

### Ferramentas de IA Utilizadas
- **Ferramenta:** Antigravity (Gemini 3.6 Flash)
- **Objetivo:** Configurar `OpenApiConfig`, anotações Swagger nos Controllers e resolver o `CodecConfigurationException` de `OffsetDateTime` no MongoDB.
- **Prompt:** "vamos atualizar ele e fazer o swagger ui também."

### Ajustes Técnicos Realizados
1. **Swagger UI / OpenAPI 3 Integration:**
   - Adicionada a dependência `springdoc-openapi-starter-webmvc-ui` (v2.6.0).
   - Documentação interativa disponível em `http://localhost:8080/swagger-ui.html` e especificação JSON em `http://localhost:8080/v3/api-docs`.

2. **MongoDB Custom Converters (`MongoConfig.java`):**
   - Resolução da falha de codec BSON com `OffsetDateTimeToDateConverter` e `DateToOffsetDateTimeConverter`, permitindo persistência transparente de datas com timezone no MongoDB.

3. **Tratamento de Rotas HTTP & Exception Handling:**
   - Adicionado `HomeController` mapeado na raiz `/` retornando status de saúde da aplicação e mapa dos endpoints REST.
   - Atualizado `GlobalExceptionHandler` para responder com HTTP 404 em `NoResourceFoundException` sem gerar poluição de logs.

---
