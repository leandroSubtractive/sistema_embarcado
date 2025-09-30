# Desenvolvimento de Drivers e Integração com Hardware

## Objetivo

- Simular a leitura de dados de sensores do veículo (ex: velocidade,
temperatura) e a interação com o sistema embarcado através de um “driver” em user-
space.
- Criar uma simulação conceitual da comunicação CAN, demonstrando o
envio e recebimento de mensagens e sua aplicação no controle do equalizador.

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

Link da Aplicação: [CANSimulationBUS](https://github.com/leandroSubtractive/sistema_embarcado/tree/devel/aosp/src/apps/CANSimulationBUS)

## Entregáveis

- Código-fonte do driver simulado (VehicleSensorSimulator.kt).
- Código-fonte da simulação CAN (CanMessage.kt, VehicleCanBusSimulator.kt).
- Guia explicativo sobre a integração do driver e da comunicação CAN (este e-book).

## Estrutura de Pastas do Aplicativo

```bash
.
└── com
    └── leandromendes
        └── cansimulationbus
            ├── data
            │   └── model
            │       └── CanMessage.kt
            ├── MainActivity.kt
            └── util
                ├── VehicleCanBusSimulator.kt
                └── VehicleSensorSimulator.kt
```

**CanMessage.kt:** Classe de dados que representar uma mensagem CAN.

**VehicleCanBusSimulator.kt:** Esta classe simulará o barramento CAN do veículo, permitindo o envio e recebimento
de mensagens. Ela também terá um mecanismo para notificar a MainActivity sobre
mensagens recebidas, como um novo nível de volume.

Detalhes:

- Usando kotlinx.coroutines e Channel para simular um fluxo assíncrono de mensagens, como ocorreria em um barramento CAN real.
- sendMessage simula o envio de uma mensagem para o barramento.
- O bloco init lança uma corrotina que “escuta” o canal de mensagens, simulando um receptor CAN. Quando uma mensagem é recebida, ela é processada (ex: extraindo o volume) e também emitida através de um MutableSharedFlow(_canMessageFlow).
- O MutableSharedFlow permite que múltiplos observadores (como a
MainActivity) recebam as mensagens CAN sem que o simulador precise saber quem são esses observadores. Isso é um padrão de design robusto para comunicação assíncrona.

**VehicleSensorSimulator.kt:** A classe VehicleSensorSimulator simula um sensor veicular genérico. O método readSensorData() retorna um valor aleatório baseado no tipo de sensor, e calibrateSensor() simula uma operação de calibração.

## Resultados

<p style="text-align:center">
    <img src=imgs/Screenshot_20250929_202341.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 1:</strong> Tela inicial do aplicativo</figcaption>
</p>

<p style="text-align:center">
    <img src=imgs/Screenshot_20250929_202454.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 2:</strong> Tela do aplicativo após apertar os botões</figcaption>
</p>

<p style="text-align:center">
    <img src=imgs/log_speed.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 3:</strong> Log de execução da mensagem de velocidade</figcaption>
</p>

<p style="text-align:center">
    <img src=imgs/log_volume.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 4:</strong> Log de execução da mensagem de alteração de volume.</figcaption>
</p>
