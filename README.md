# :mortar_board: Formação em Sistemas Embarcados

Repositório dedicado a todas as atividades do curso de formação em sistemas embarcados.

## :books: Disciplinas

* [G95140|EXT-ADS-037|Fundamentos do Android](#-fundamentos-do-android)
  * [[DC-1] UA1 e UA2 - Hands On](#-dc-1-ua1-e-ua2---hands-on)
  * [[DC-1] UA3 e UA4 - Hands On](#-dc-1-ua3-e-ua4---hands-on)
* [G95140|EXT-ADS-038|Programação Orientada a Objetos para Android](#-programação-orientada-a-objetos-para-android)
  * [[DC-2] UA1 e UA2 - Hands On](#-dc-2-ua1-e-ua2---hands-on)
  * [[DC-2] UA3 e UA4 - Hands On](#-dc-2-ua3-e-ua4---hands-on)
* [G95140|EXT-ADS-039|Interface e Gerenciamento de Serviços no Android](#-interface-e-gerenciamento-de-serviços-no-android)
  * [[DC-3] UA1 e UA2 - Hands On](#-dc-3-ua1-e-ua2---hands-on)
  * [[DC-3] UA3 e UA4 - Hands On](#-dc-3-ua3-e-ua4---hands-on)
* [G95140|EXT-ADS-040|Serviços Avançados e Integração Nativa no Android](#-serviços-avançados-e-integração-nativa-no-android)
  * [[DC-4] UA1 e UA2 - Hands On](#-dc-4-ua1-e-ua2---hands-on)
  * [[DC-4] UA3 e UA4 - Hands On](#-dc-4-ua3-e-ua4---hands-on)
* [G95140|EXT-ADS-041|Desenvolvimento e Integração de HAL no Android](#-desenvolvimento-e-integração-de-hal-no-android)
  * [[DC-5] UA1 e UA2 - Hands On](#-dc-5-ua1-e-ua2---hands-on)
  * [[DC-5] UA3 e UA4 - Hands On](#-dc-5-ua3-e-ua4---hands-on)
* [G95140|EXT-ADS-042|Programação de Sistemas Embarcados com Linux](#-programação-de-sistemas-embarcados-com-linux)
  * [[DC-6] UA1 e UA2 - Hands On](#-dc-6-ua1-e-ua2---hands-on)
  * [[DC-6] UA3 e UA4 - Hands On](#-dc-6-ua3-e-ua4---hands-on)

## :book: Fundamentos do Android

### :white_check_mark: [DC-1] UA1 e UA2 - Hands On

* Descrever a arquitetura do sistema operacional Android;
* Configurar o ambiente Linux para sincronizar o Android Open Source Project (AOSP);
* Validar a funcionalidade básica do ambiente configurado.

:bookmark_tabs: Relátorio da atividade: [Ambiente de desenvolvimento Android](readme_files/DC1[HandsOn_UA1-UA2]%20Implementação%20de%20um%20dispositivo%20com%20sistema%20operacional%20Android.md)

:arrow_forward:  Video da atividade: [Vídeo](https://drive.google.com/drive/folders/1xURd7VnulzM-5QFO18Kk7mN_jLk9DK0C)

### :white_check_mark: [DC-1] UA3 e UA4 - Hands On

* Configurar um dispositivo virtual, como o "Pixel 5", com a API adequada e personalizar
configurações, como resolução e armazenamento, para garantir simulações precisas;
* Definir os principais subsistemas do sistema embarcado, como a interface do usuário, além de
desenvolver um diagrama claro que mostre os fluxos de dados e interações entre os
subsistemas.

:bookmark_tabs: Relátorio da atividade: [Customização do AOSP](readme_files/DC1[HandsOn_UA3-UA4]%20Customização%20do%20AOSP.md)

:arrow_forward:  Video da atividade:[Vídeo](https://drive.google.com/drive/folders/1xURd7VnulzM-5QFO18Kk7mN_jLk9DK0C)

## :computer: Programação Orientada a Objetos para Android

### :white_check_mark: [DC-2] UA1 e UA2 - Hands On

* Explicar o ciclo de vida das Activities;
* Configurar corretamente as permissões de segurança no arquivo Android Manifest;
* Utilizar princípios de Programação Orientada a Objetos no desenvolvimento Android;
* Integrar componentes de interface gráfica com a classe Java ou Kotlin;
* Configurar Intents para navegação entre Activities.

:bookmark_tabs: Relátorio da atividade: [Descrição da Solução](readme_files/DC2[HandsOn_UA1-UA2]%20Aplicativo%20Android%20que%20armazena%20e%20sincroniza%20perfis%20de%20equalização%20de%20som.md)

:arrow_forward: Video da atividade:[Video](https://drive.google.com/drive/folders/1xURd7VnulzM-5QFO18Kk7mN_jLk9DK0C)  

### :white_check_mark: [DC-2] UA3 e UA4 - Hands On

* Implementar o armazenamento de dados com DataStore, Room e Firebase Cloud Firestore
* Utilizar Broadcast Receivers para comunicação assíncrona
* Integrar Content Providers para compartilhamento de dados entre diferentes aplicativos.

:bookmark_tabs: Relátorio da atividade: [Descrição da Solução](readme_files/DC2[HandsOn_UA3-UA4]%20Aplicativo%20Android%20que%20armazena%20e%20sincroniza%20perfis%20de%20equalização%20de%20som.md)

:arrow_forward: Video da atividade:

## :computer: Interface e Gerenciamento de Serviços no Android

### :white_check_mark: [DC-3] UA1 e UA2 - Hands On

* Implementar a estrutura e o funcionamento do Binder no Android;
* Implementar uma interface AIDL em um serviço Android integrada ao Binder.

:bookmark_tabs: Relátorio da atividade: [Interface AIDL](readme_files/DC3[HandsOn_UA1-UA2]%20Implementação%20de%20Comunicação%20IPC%20utilizando%20HIDL%20e%20AIDL:%20Integração%20Básica%20de%20Serviços%20Android.md)

### :white_check_mark: [DC-3] UA3 e UA4 - Hands On

* Explicar as funções principais das Manager Classes no gerenciamento de atividades no Android;
* Implementar a interação entre Manager Classes e System Services no Android
* Utilizar o Espresso e JUnit como ferramentas de automação de testes para componentes gerenciados pelas Manager Classes.

:bookmark_tabs: Relátorio da atividade: [Manager Classes](readme_files/DC3[HandsOn_UA3-UA4]%20Gerenciamento%20Avançado%20de%20Recursos%20com%20Manager%20Classes%20e%20Testes%20Automatizados%20no%20Android.md)

## :computer: Serviços Avançados e Integração Nativa no Android

### :white_check_mark: [DC-4] UA1 e UA2 - Hands On

* Implementar métodos do ciclo de vida dos serviços de áudio no Android;
* Elaborar um serviço de reprodução de áudio no Android;
* Implementar a chamada do serviço de reprodução de áudio no Android.

:bookmark_tabs: Relátorio da atividade: [Native Android Service](readme_files/DC4[HandsOn_UA1-UA2]%20Desenvolvimento%20de%20serviço%20nativo%20Android%20com%20o%20uso%20de%20classes%20de%20serviços.md)

### :white_check_mark: [DC-4] UA3 e UA4 - Hands On

* Implementar um serviço de sistema Android que utilize JNI e JUNIT;
* Implementar testes unitários para verificação do funcionamento do serviço no sistema Android;
* Integrar outros componentes ao serviço do sistema Android;
* Elaborar testes apresentando os resultados obtidos.

:bookmark_tabs: Relátorio da atividade: [Descrição da Solução](readme_files/DC4[HandsOn_UA3-UA4]%20Desenvolvimento%20e%20teste%20de%20serviço%20de%20sistema%20no%20android.md)

## :computer: Desenvolvimento e Integração de HAL no Android

### :white_check_mark: [DC-5] UA1_UA2 e UA3_UA4 - Hands On

* Simular a leitura de dados de sensores do veículo (ex: velocidade,
temperatura) e a interação com o sistema embarcado através de um “driver” em user-
space.
* Criar uma simulação conceitual da comunicação CAN, demonstrando o
envio e recebimento de mensagens e sua aplicação no controle do equalizador.

:bookmark_tabs: Relátorio da atividade: [Descrição da Solução](readme_files/DC5[HandsOn_UA1_2-UA3_4]%20Desenvolvimento%20de%20Drivers%20e%20Integração%20com%20Hardware.md)

## :computer: Programação de Sistemas Embarcados com Linux

### :white_check_mark: [DC-6] UA1 e UA2 - Hands On

* Explicar os conceitos básicos da linha de comando para a manipulação de arquivos;
* Operar arquivos e diretórios no sistema de arquivos do Linux;
* Elaborar scripts de shell que utilizem variáveis, condicionais e laços de repetição para automatizar tarefas;
* Diferenciar as propriedades de arquivos e diretórios no Sistema Operacional Linux.

:bookmark_tabs: Relátorio da atividade: [Descrição da Solução](readme_files/DC6[HandsOn_UA1_UA2]%20linha%20de%20comando,%20sistema%20de%20arquivos%20e%20scripts.md)

### :white_check_mark: [DC-6] UA3 e UA4 - Hands On

* Utilizar ferramentas de automação para implantar aplicativos Android em emuladores ou dispositivos;
* Explicar a configuração do kernel Linux para habilitar o suporte à rede CAN;
* Explicar as principais estratégias de otimização de código em desenvolvimento de software;
* Justificar o uso de estratégias de otimização de código em sistemas embarcados Android automotivos.

:bookmark_tabs: Relátorio da atividade: [Descrição da Solução](readme_files/DC6[HandsOn_UA3_UA4]%20Automação%20e%20otimização%20de%20Sistemas%20Android%20Embarcados.md)

:arrow_forward: Video da atividade:
