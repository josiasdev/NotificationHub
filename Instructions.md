# NotificationHub — Central de Notificações para Fazenda Inteligente

## 1. Contexto

O **NotificationHub** é uma aplicação para monitoramento de eventos de uma propriedade rural e geração automática de notificações para o produtor.

Uma fazenda pode possuir diferentes sensores e dispositivos conectados, responsáveis por monitorar condições como temperatura, umidade do solo, nível de reservatórios, nível de silos e funcionamento de equipamentos.

O objetivo do sistema é receber dados desses dispositivos, identificar situações que exigem atenção e gerar mensagens predefinidas que possam ser encaminhadas ao produtor.

O sistema deve ser desenvolvido como um **MVP funcional**, simulando um cenário real de integração entre sistemas Web, dispositivos IoT e serviços de mensageria.

A integração real com o **WhatsApp não é obrigatória**.

---

## 2. Objetivo do Desafio

Desenvolver uma aplicação capaz de:

1. receber dados provenientes de dispositivos ou sensores de uma propriedade rural;
2. validar os dados recebidos;
3. avaliar os dados de acordo com um conjunto de regras;
4. identificar situações que devem gerar uma notificação;
5. produzir uma mensagem apropriada para o produtor;
6. encaminhar a mensagem para um mecanismo de envio;
7. manter um histórico dos eventos e notificações processados;
8. disponibilizar uma interface Web que permita acompanhar e simular o funcionamento do sistema.

O desenvolvedor possui liberdade para definir:

* arquitetura;
* tecnologias;
* frameworks;
* banco de dados;
* organização do código;
* estrutura da interface;
* comunicação entre frontend e backend;
* estratégia de testes;
* estratégia de persistência;
* mecanismo utilizado para simular o envio das notificações.

As decisões tomadas devem ser justificadas na documentação do projeto.

---

# 3. Cenário

Considere a seguinte propriedade fictícia:

**Fazenda Boa Esperança**

* Produtor: João Silva
* Identificador: `producer-001`
* Fazenda: `farm-001`
* Telefone para notificações: `+5535999999999`

A propriedade possui sensores e dispositivos distribuídos em diferentes locais.

Para o MVP, devem ser considerados pelo menos os seguintes tipos:

* temperatura do ambiente;
* umidade do ar;
* umidade do solo;
* nível de reservatório de água;
* nível de silo;
* estado de funcionamento de equipamento.

Não é necessário utilizar dispositivos IoT reais.

Os dados podem ser enviados por um **simulador**, pela própria interface Web ou por uma API criada pelo desenvolvedor.

---

# 4. Dados de Entrada

A aplicação deve ser capaz de receber eventos seguindo, no mínimo, as informações descritas abaixo.

Exemplo:

```json
{
  "eventId": "event-001",
  "farmId": "farm-001",
  "deviceId": "sensor-temp-01",
  "type": "AIR_TEMPERATURE",
  "value": 38.5,
  "unit": "C",
  "timestamp": "2026-08-17T14:30:00-03:00"
}
```

Cada evento deve possuir:

| Campo       | Descrição                        |
| ----------- | -------------------------------- |
| `eventId`   | Identificador único do evento    |
| `farmId`    | Identificador da propriedade     |
| `deviceId`  | Identificador do dispositivo     |
| `type`      | Tipo da informação coletada      |
| `value`     | Valor informado pelo dispositivo |
| `unit`      | Unidade de medida                |
| `timestamp` | Data e horário da leitura        |

Para eventos relacionados ao estado de equipamentos, o campo `value` poderá conter um valor textual.

Exemplo:

```json
{
  "eventId": "event-006",
  "farmId": "farm-001",
  "deviceId": "irrigation-pump-01",
  "type": "EQUIPMENT_STATUS",
  "value": "FAILURE",
  "unit": null,
  "timestamp": "2026-08-17T14:35:00-03:00"
}
```

---

# 5. Tipos de Dados Esperados

O MVP deve reconhecer pelo menos os seguintes tipos:

```text
AIR_TEMPERATURE
AIR_HUMIDITY
SOIL_MOISTURE
WATER_RESERVOIR_LEVEL
SILO_LEVEL
EQUIPMENT_STATUS
```

O desenvolvedor poderá adicionar outros tipos de sensores ou eventos caso considere pertinente.

---

# 6. Regras Mínimas de Notificação

Para garantir um comportamento mínimo comum entre as diferentes soluções submetidas, considere as seguintes regras.

## Temperatura do ambiente

Se:

```text
AIR_TEMPERATURE > 35 °C
```

gerar uma notificação de temperatura elevada.

Exemplo:

```text
⚠️ Alerta de temperatura: foi registrada temperatura de 38,5 °C pelo sensor sensor-temp-01 na Fazenda Boa Esperança.
```

---

## Umidade do ar

Se:

```text
AIR_HUMIDITY < 30%
```

gerar uma notificação de baixa umidade.

Exemplo:

```text
⚠️ Alerta de umidade: a umidade do ar atingiu 24% na Fazenda Boa Esperança.
```

---

## Umidade do solo

Se:

```text
SOIL_MOISTURE < 20%
```

gerar uma notificação indicando possível necessidade de irrigação.

Exemplo:

```text
💧 Alerta de irrigação: a umidade do solo está em 17%. Verifique a necessidade de irrigação.
```

---

## Reservatório de água

Se:

```text
WATER_RESERVOIR_LEVEL < 15%
```

gerar uma notificação.

Exemplo:

```text
💧 Nível baixo de água: o reservatório está com apenas 12% de sua capacidade.
```

---

## Silo

Se:

```text
SILO_LEVEL < 15%
```

gerar uma notificação.

Exemplo:

```text
⚠️ Nível baixo no silo: o silo monitorado por silo-sensor-01 está com 10% de sua capacidade.
```

---

## Equipamento

Se:

```text
EQUIPMENT_STATUS = FAILURE
```

gerar imediatamente uma notificação.

Exemplo:

```text
🚨 Falha de equipamento: foi detectada uma falha no equipamento irrigation-pump-01.
```

O desenvolvedor poderá melhorar as mensagens e criar classificações como **informativa**, **atenção** ou **crítica**.

---

# 7. Eventos que Não Devem Gerar Alertas

Valores dentro da faixa considerada normal devem ser processados, mas não devem necessariamente gerar notificações.

Por exemplo:

```json
{
  "eventId": "event-007",
  "farmId": "farm-001",
  "deviceId": "sensor-temp-01",
  "type": "AIR_TEMPERATURE",
  "value": 27.0,
  "unit": "C",
  "timestamp": "2026-08-17T15:00:00-03:00"
}
```

Esse evento deve ser considerado válido, mas não deve gerar um alerta.

---

# 8. Interface Web

A aplicação deve fornecer uma interface Web por meio da qual seja possível compreender e demonstrar o funcionamento do NotificationHub.

A interface deve permitir, no mínimo:

* visualizar informações básicas da fazenda;
* visualizar os dispositivos/sensores considerados pelo sistema;
* simular ou inserir novas leituras;
* visualizar eventos recebidos;
* visualizar notificações geradas;
* identificar o conteúdo da mensagem;
* identificar o momento em que ela foi gerada;
* visualizar seu status de envio.

O desenvolvedor poderá definir a organização e o design que considerar mais adequados.

---

# 9. Envio das Notificações

A integração real com WhatsApp **não é obrigatória**.

O sistema deve, entretanto, ser projetado de forma que um serviço real de mensageria possa ser integrado posteriormente.

Uma solução possível é definir uma abstração como:

```text
NotificationProvider
    send(message, recipient)
```

e implementar para o MVP algo equivalente a:

```text
MockWhatsAppProvider
```

O envio simulado pode:

* registrar a mensagem no banco de dados;
* registrar a mensagem em arquivo;
* exibi-la em uma área da aplicação;
* registrar a operação em log;
* utilizar outra abordagem considerada adequada.

A mensagem deverá possuir um estado, por exemplo:

```text
PENDING
SENT
FAILED
```

A utilização de uma API real do WhatsApp ou de outro serviço de mensageria poderá ser realizada pelo desenvolvedor, mas será considerada uma **extensão do MVP e não um requisito obrigatório**.

Nenhum candidato será prejudicado por utilizar exclusivamente um mecanismo simulado.

---

# 10. Histórico

A aplicação deve manter um histórico que permita relacionar:

```text
Evento recebido
       ↓
Regra avaliada
       ↓
Notificação gerada
       ↓
Tentativa de envio
       ↓
Resultado
```

O histórico deve permitir compreender pelo menos:

* qual evento foi recebido;
* quando ocorreu;
* qual dispositivo o originou;
* se alguma regra foi acionada;
* qual mensagem foi produzida;
* se houve tentativa de envio;
* resultado dessa tentativa.

---

# 11. Validação dos Dados

O sistema deve lidar adequadamente com entradas inválidas.

Alguns exemplos são:

* `eventId` ausente;
* dispositivo ausente;
* tipo de sensor desconhecido;
* valor incompatível com o tipo;
* unidade incompatível;
* timestamp inválido;
* valores fisicamente incompatíveis, quando pertinente.

Por exemplo:

```json
{
  "eventId": "event-invalid-01",
  "farmId": "farm-001",
  "deviceId": "sensor-humidity-01",
  "type": "AIR_HUMIDITY",
  "value": 130,
  "unit": "%",
  "timestamp": "2026-08-17T16:00:00-03:00"
}
```

Uma umidade de `130%` deverá ser tratada como entrada inválida.

A forma como esses erros serão apresentados e registrados fica a critério do desenvolvedor.

---

# 12. Eventos Duplicados

Considere que sistemas IoT podem retransmitir uma leitura caso não recebam confirmação de processamento.

Consequentemente, o mesmo `eventId` poderá eventualmente ser recebido mais de uma vez.

Por exemplo, o seguinte evento pode chegar duas vezes:

```json
{
  "eventId": "event-100",
  "farmId": "farm-001",
  "deviceId": "sensor-temp-01",
  "type": "AIR_TEMPERATURE",
  "value": 39,
  "unit": "C",
  "timestamp": "2026-08-17T17:00:00-03:00"
}
```

O sistema deve evitar que a duplicação acidental desse evento resulte no envio de múltiplas notificações idênticas ao produtor.

A estratégia utilizada fica a critério do desenvolvedor.

---

# 13. Dados para Demonstração

A solução deve fornecer uma forma simples de executar o sistema utilizando dados previamente preparados.

Um conjunto mínimo de demonstração pode conter:

```json
[
  {
    "eventId": "event-001",
    "farmId": "farm-001",
    "deviceId": "sensor-temp-01",
    "type": "AIR_TEMPERATURE",
    "value": 38.5,
    "unit": "C",
    "timestamp": "2026-08-17T14:30:00-03:00"
  },
  {
    "eventId": "event-002",
    "farmId": "farm-001",
    "deviceId": "sensor-humidity-01",
    "type": "AIR_HUMIDITY",
    "value": 24,
    "unit": "%",
    "timestamp": "2026-08-17T14:31:00-03:00"
  },
  {
    "eventId": "event-003",
    "farmId": "farm-001",
    "deviceId": "sensor-soil-01",
    "type": "SOIL_MOISTURE",
    "value": 17,
    "unit": "%",
    "timestamp": "2026-08-17T14:32:00-03:00"
  },
  {
    "eventId": "event-004",
    "farmId": "farm-001",
    "deviceId": "reservoir-sensor-01",
    "type": "WATER_RESERVOIR_LEVEL",
    "value": 12,
    "unit": "%",
    "timestamp": "2026-08-17T14:33:00-03:00"
  },
  {
    "eventId": "event-005",
    "farmId": "farm-001",
    "deviceId": "silo-sensor-01",
    "type": "SILO_LEVEL",
    "value": 10,
    "unit": "%",
    "timestamp": "2026-08-17T14:34:00-03:00"
  },
  {
    "eventId": "event-006",
    "farmId": "farm-001",
    "deviceId": "irrigation-pump-01",
    "type": "EQUIPMENT_STATUS",
    "value": "FAILURE",
    "unit": null,
    "timestamp": "2026-08-17T14:35:00-03:00"
  },
  {
    "eventId": "event-007",
    "farmId": "farm-001",
    "deviceId": "sensor-temp-01",
    "type": "AIR_TEMPERATURE",
    "value": 27,
    "unit": "C",
    "timestamp": "2026-08-17T14:36:00-03:00"
  }
]
```

Os seis primeiros eventos devem gerar notificações.

O último representa uma situação normal e não deve gerar alerta.

---

# 14. Qualidade, Testes e Confiabilidade

A solução deverá possuir testes automatizados.

O desenvolvedor tem liberdade para definir:

* estratégia de testes;
* ferramentas utilizadas;
* níveis de teste;
* organização dos testes;
* meta de cobertura.

Entretanto, a solução deve fornecer:

* instruções para execução dos testes;
* relatório de cobertura ou mecanismo reproduzível para sua geração;
* explicação resumida da estratégia adotada.

Durante a avaliação, serão considerados não apenas os percentuais de cobertura, mas principalmente se os testes exercitam comportamentos relevantes.

Exemplos de cenários que podem ser considerados incluem:

* processamento de uma leitura normal;
* geração de cada tipo de alerta;
* valores exatamente nos limites das regras;
* dados inválidos;
* eventos duplicados;
* falha durante o envio da notificação;
* indisponibilidade de algum componente;
* persistência correta do histórico.

O desenvolvedor poderá identificar e testar outros cenários que considere relevantes.

---

# 15. Uso de Inteligência Artificial

O uso de ferramentas de Inteligência Artificial é **permitido e encorajado**.

O desenvolvedor poderá utilizar, por exemplo:

* ChatGPT;
* GitHub Copilot;
* Claude;
* Gemini;
* Cursor;
* outras ferramentas de IA generativa.

A utilização dessas ferramentas deverá ser registrada no projeto.

O objetivo é avaliar não somente a capacidade de utilizar IA para produzir software, mas também a capacidade de:

* avaliar criticamente suas sugestões;
* identificar resultados incorretos;
* revisar código gerado;
* melhorar soluções propostas;
* validar o comportamento por meio de testes e outras evidências.

---

# 16. Registro do Processo de Desenvolvimento

O repositório deverá conter um arquivo:

```text
DEVELOPMENT_LOG.md
```

Esse documento deverá registrar os principais passos do desenvolvimento.

Para interações relevantes com ferramentas de IA, deve-se registrar:

* ferramenta utilizada;
* objetivo da interação;
* prompt utilizado;
* resumo da resposta obtida, quando necessário;
* decisão tomada a partir da resposta;
* indicação se a sugestão foi aceita, parcialmente aceita ou rejeitada;
* alterações realizadas pelo desenvolvedor.

Não é necessário documentar interações triviais de autocomplete.

O documento deverá registrar também decisões relevantes, tais como:

* escolha das tecnologias;
* definição da arquitetura;
* modelagem dos dados;
* estratégia utilizada para processar eventos;
* estratégia de notificações;
* abordagem utilizada para evitar duplicidades;
* estratégia de testes;
* decisões relacionadas à confiabilidade;
* mudanças relevantes realizadas durante o desenvolvimento.

O objetivo é permitir compreender **como a solução evoluiu e quais evidências e decisões levaram ao resultado final**.

---

# 17. Entrega Esperada

A entrega deverá incluir, no mínimo:

* código-fonte;
* repositório Git;
* histórico de commits;
* `README.md`;
* `DEVELOPMENT_LOG.md`;
* instruções para execução;
* dados de exemplo;
* testes automatizados;
* instruções para execução dos testes;
* informação sobre cobertura dos testes;
* descrição resumida da arquitetura;
* aplicação funcional.

O desenvolvedor poderá incluir diagramas, documentação adicional, CI/CD, containers ou outros recursos que considere úteis.

---

# 18. Escopo do MVP

Não são requisitos obrigatórios:

* dispositivos IoT reais;
* hardware;
* integração real com WhatsApp;
* conta no WhatsApp Business;
* contratação de serviços externos;
* infraestrutura em nuvem;
* aplicação móvel nativa;
* utilização de Inteligência Artificial dentro do NotificationHub.

Nenhuma funcionalidade do MVP deve depender da contratação de um serviço pago.

O foco deve estar na construção de uma solução que represente adequadamente o fluxo:

```text
Sensor / Dispositivo
        ↓
      Evento
        ↓
 NotificationHub
        ↓
 Validação e Regras
        ↓
    Notificação
        ↓
 Serviço de Mensagem
        ↓
      Produtor
```

A prioridade é entregar um **MVP funcional, testável, confiável e bem estruturado**, demonstrando capacidade de transformar um problema aberto em uma solução de software.
