# 🎬 Roteiro de Apresentação (Vídeo de 5 min) e Instruções de Envio por E-mail

Este documento contém o **roteiro minucioso para o vídeo de apresentação (máx. 5 minutos)** e o **modelo de e-mail de submissão** exigidos pelo edital do **Cogito Lab**.

---

## 📩 1. Instruções para Envio do E-mail de Entrega

- **Prazo limite:** Até **30/08/2026 às 23:59**.
- **Destinatário:** `gustavovale@ufla.br`
- **Assunto:** `[Seu Nome Completo] - NotificationHub`  
  *(Exemplo: `Josias Martins - NotificationHub`)*

### ✉️ Modelo do E-mail

```text
Assunto: Josias Martins - NotificationHub

Prezado Gustavo e equipe do Cogito Lab,

Submeto a entrega do Projeto Prático NotificationHub referente ao processo seletivo.

📌 Links Importantes:
- Repositório Git (Público): https://github.com/josiasdev/NotificationHub
- Vídeo de Apresentação (YouTube / Google Drive): [INSERIR LINK DO SEU VÍDEO AQUI]
- Swagger UI (com app rodando): http://localhost:8080/swagger-ui.html

📌 Resumo da Solução:
- Arquitetura: Clean Architecture (Hexagonal) em Java 21 e Spring Boot 3.
- Confiabilidade: Garantia dupla de Idempotência (eventId), tratamento de invariantes no domínio e Dead Letter Queue (DLQ) no RabbitMQ.
- Motor de Regras: Padrão Strategy para avaliação extensível dos 6 sensores IoT.
- Frontend: Dashboard moderno em React + TypeScript + Vite com simulador do lote oficial do edital em 1 clique.
- Qualidade: Testes unitários com JUnit 5/Mockito, testes de integração com Testcontainers (MongoDB real + RabbitMQ real) e relatório de cobertura com JaCoCo.
- Registro de IA: Arquivo DEVELOPMENT_LOG.md detalhando o uso crítico de IA generativa.

Atenciosamente,
Josias Martins
```

---

## 📹 2. Roteiro Sugerido para o Vídeo (Máximo 5 Minutos)

O edital exige um vídeo focado na clareza técnica. Recomenda-se gravar a tela usando programas como **OBS Studio**, **Loom** ou **Zoom**.

---

### ⏱️ Cronograma Minuto a Minuto

#### 🟢 0:00 - 0:45 | Introdução e Apresentação do Problema
- **O que falar:**  
  *"Olá! Meu nome é [Seu Nome] e vou apresentar o NotificationHub, desenvolvido para o processo seletivo do Cogito Lab. O sistema atende à Fazenda Boa Esperança do produtor João Silva, ingerindo dados de sensores IoT, aplicando regras de negócio e disparando notificações assíncronas de alerta."*
- **O que mostrar:**  
  Tela inicial do repositório no GitHub ou o diagrama de arquitetura do `README.md`.

---

#### 🟢 0:45 - 2:15 | Demonstração do Sistema Funcionando (MVP ao Vivo)
- **O que falar:**  
  *"Vou demonstrar o MVP em execução. Temos aqui o dashboard construído em React, TypeScript e Vite integrando com a API REST Spring Boot."*
- **Ação prática no vídeo:**
  1. Mostrar os indicadores dos sensores com os valores atuais.
  2. Clicar no botão **"Simular Lote do Edital (7 Eventos)"**.
  3. Mostrar que o sistema ingeriu os 7 eventos:
     - **6 eventos ultrapassaram limiares** e geraram notificações na tabela de alertas (`SENT`).
     - **1 evento com temperatura de 27 °C foi mantido no histórico**, mas não gerou notificação (comportamento correto do edital).

---

#### 🟢 2:15 - 3:30 | Arquitetura, Idempotência e Resiliência
- **O que falar:**  
  *"Sobre a arquitetura: utilizei Clean Architecture (Hexagonal) para isolar o domínio em Java 21 puro, sem acoplamento com frameworks.*
  *Para garantir a **Idempotência**, se o mesmo `eventId` for enviado repetidamente, o MongoDB rejeita via índice único (`@Indexed(unique = true)`), retornando HTTP 409 Conflict sem duplicar alertas.*
  *Para a **Resiliência**, se um envio de mensagem falhar após as retentativas, o RabbitMQ direciona a notificação para uma Dead Letter Queue (`iot.notifications.dlq`), evitando perda de dados."*
- **O que mostrar:**  
  Código no VS Code mostrando o `SensorEvent.java`, `SensorEventDocument.java` e a classe `RabbitMQConfig.java`.

---

#### 🟢 3:30 - 4:30 | Testes Automatizados e Cobertura (Testcontainers & JaCoCo)
- **O que falar:**  
  *"A estratégia de testes conta com testes unitários JUnit 5 para as regras de negócio e Use Cases, e testes de integração de ponta a ponta com **Testcontainers**, subindo contêineres reais do MongoDB 7.0 e RabbitMQ 3.13 durante a execução do Maven.*
  *Também integramos o JaCoCo para geração automatizada do relatório de cobertura em HTML."*
- **O que mostrar:**  
  Executar `mvn test` no terminal mostrando `BUILD SUCCESS` e abrir o arquivo `target/site/jacoco/index.html` ou Swagger UI `/swagger-ui.html`.

---

#### 🟢 4:30 - 5:00 | Uso Crítico de IA e Limitações Conhecidas
- **O que falar:**  
  *"O processo de desenvolvimento com suporte a ferramentas de IA foi rigorosamente registrado no arquivo `DEVELOPMENT_LOG.md`, documentando decisões como a conversão de `OffsetDateTime` e o padrão Strategy.*
  *Como limitação conhecida do MVP, o envio para WhatsApp utiliza uma abstração simulada (`MockWhatsAppNotificationAdapter`), pronta para ser conectada a uma API real como a Z-API ou Twilio em produção.*
  *Muito obrigado!"*

---

## 📊 3. Matriz de Avaliação (60 Pontos) vs. O que Entregamos

| Critério do Edital | Pontos | Como Nós Garantimos a Pontuação Máxima |
| :--- | :---: | :--- |
| **Funcionamento do MVP** | **15 pts** | Sistema funcional completo (API REST Java 21 + Frontend React/Vite + simulador do edital). |
| **Arquitetura e Qualidade** | **10 pts** | Clean Architecture (Hexagonal), Rich Domain Modeling, Strategy Pattern para regras, SOLID. |
| **Estratégia dos Testes** | **15 pts** | JUnit 5/Mockito + Testcontainers (Mongo/RabbitMQ reais) + JaCoCo Report. |
| **Confiabilidade & Falhas**| **5 pts** | Idempotência (`eventId` unique index), Dead Letter Queue (DLQ), validações físicas (umidade 0-100%). |
| **Uso Crítico de IA** | **10 pts** | `DEVELOPMENT_LOG.md` detalhado etapa por etapa com justificativa técnica. |
| **Documentação & Reprodutibilidade** | **5 pts** | `README.md` completo, `STATUS.md`, `docker-compose.yml`, Swagger UI e instruções simples. |
| **TOTAL** | **60 pts** | **100% dos requisitos contemplados com excelência.** |
