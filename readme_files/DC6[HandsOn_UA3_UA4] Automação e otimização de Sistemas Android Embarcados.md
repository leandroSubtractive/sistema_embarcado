# Automação e otimização de Sistemas Android Embarcados

## Objetivo

- Utilizar ferramentas de automação para implantar aplicativos Android em emuladores ou dispositivos;
- Explicar a configuração do kernel Linux para habilitar o suporte à rede CAN;
- Explicar as principais estratégias de otimização de código em desenvolvimento de software;
- Justificar o uso de estratégias de otimização de código em sistemas embarcados Android automotivos.

## Reposítorio

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

Link da Aplicação: [VehicleEqualizerFull](https://github.com/leandroSubtractive/sistema_embarcado/tree/devel/aosp/src/apps/VehicleEqualizerFull)

Link do Vídeo: [Vídeo](https://drive.google.com/drive/folders/1xURd7VnulzM-5QFO18Kk7mN_jLk9DK0C)

## Sumário

1. [Ambiente de desenvolvimento](#1-ambiente-de-desenvolvimento)
    - 1.1. [Sistema Operacional](#11-sistema-operacional)
    - 1.2. [Java(JDK)](#12-javajdk)
    - 1.3. [Android Studio](#13-android-studio)
    - 1.4. [Git](#14-git)
    - 1.5. [Emulador Android](#15-emulador-android)
2. [Aplicativos Desenvolvidos Durante o Curso](#2-aplicativos-desenvolvidos-durante-o-curso)

## 1. Ambiente de Desenvolvimento

### 1.1. Sistema Operacional

```sh
# O comando lsb_release imprime certas informações de LSB (Linux Standard Base) e distribuição.
$ lsb_release -a
No LSB modules are available.
Distributor ID: Ubuntu
Description: Ubuntu 22.04.5 LTS
Release: 22.04
Codename: jammy
```

### 1.2. Java(JDK)

```bash
# Retorna informações do Java caso esteja instalado
$ java --version
```

<p style="text-align:center">
    <img src=imgs/java_v.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 1:</strong> Captura da tela após a execução do comando, ferramenta
    instalada corretamente</figcaption>
</p>

### 1.3. Android Studio

```bash
# Informações gerais do Android Studio
Android Studio Narwhal 3 Feature Drop | 2025.1.3
Build #AI-251.26094.121.2513.14007798, built on August 28, 2025
Runtime version: 21.0.7+-13880790-b1038.58 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Toolkit: sun.awt.X11.XToolkit
Linux 6.8.0-83-generic
Ubuntu 22.04.5 LTS; glibc: 2.35
Kotlin plugin: K2 mode
GC: G1 Young Generation, G1 Concurrent GC, G1 Old Generation
Memory: 2968M
Cores: 12
Registry:
  ide.experimental.ui=true
Current Desktop: ubuntu:GNOME

```

### 1.4. Git

```bash
# Retorna a versão do Git caso esteja instalado
$ git --version
```

<p style="text-align:center">
    <img src=imgs/git.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 2:</strong> Captura da tela após a execução do comando, neste caso ferramenta está instalada corretamente</figcaption>
</p>

### 1.5. Emulador Android

O Emulador Android utilizado é o mesmo da atividade anterior, não foi realiza que já foi entregue e descrita na sessão [1.5 Emulador Android](https://github.com/leandroSubtractive/sistema_embarcado/blob/devel/readme_files/AIDL_dc3_u1_u2.md#15-emulador-android) do relatório [Interface e Gerenciamento de Serviços no Android](https://github.com/leandroSubtractive/sistema_embarcado/blob/devel/readme_files/AIDL_dc3_u1_u2.md#interface-e-gerenciamento-de-servi%C3%A7os-no-android).

## 2. Aplicativos Desenvolvidos Durante o Curso


<p style="text-align:center">
    <img src=imgs/All_Apps.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 3:</strong> Aplicativos agrupados por disciplinas.</figcaption>
</p>