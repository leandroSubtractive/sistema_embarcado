# Ambiente de desenvolvimento Android

Roteiro de configuração e preparação do ambiente de trabalho Android em uma máquina com o sistema operacional Linux.

## Objetivo

- Descrever a arquitetura do sistema operacional Android;
- Configurar o ambiente Linux para sincronizar o Android Open Source Project (AOSP);
- Validar a funcionalidade básica do ambiente configurado.

Nas seções a seguir serão descritos os passos para cada configuração.

## Sumário

1. [Estação de Trabalho](#1-estação-de-trabalho)
    - 1.1. [Requisitos de Hardware](#11-requisitos-de-hardware)
    - 1.2. [Requisitos de Software](#12-requisitos-de-software)
    - 1.3. [DualBoot](#13-dualboot)
    - 1.4. [Máquinas Utilizadas](#14-máquinas-utilizadas)
2. [Ferramentas do Sistema](#2-ferramentas-do-sistema)
    - 2.1. [Atualizando Pacotes do Sistema](#21-atualizando-pacotes-do-sistema)
    - 2.2. [Instalação das Ferramentas Necessárias](#22-instalação-das-ferramentas-necessárias)
      - 2.2.1. [Git](#221-git)
      - 2.2.2. [Java](#222-javajdk)
      - 2.2.3. [Python](#223-python)
      - 2.2.4. [Curl](#224-curl)
3. [Instalação do VS Code](#3-instalação-do-vs-code)
4. [Configuração de Espaço Adicional de Memória (Swap)](#4-configuração-de-espaço-adicional-de-memória-swap)
5. [Android Open Source Project](#5-android-open-source-project-aosp)
    - 5.1. [Pacotes Necessários](#51-pacotes-necessários)
    - 5.2. [repo](#52-repo)
    - 5.3. [Sincronização do Código-Fonte](#53-sincronização-do-código-fonte)
    - 5.4. [Compilação do Sistema Operacional](#54-compilação-do-sistema-operacional)
      - 5.4.1. [Configuração das variáveis de ambiente](#541-configuração-das-variáveis-de-ambiente)
      - 5.4.2. [Definição da Plataforma Alvo](#542-definição-da-plataforma-alvo)
      - 5.4.3. [Build](#543-build)
      - 5.4.4. [Teste do Build](#544-teste-do-build)
6. [Android Studio](#6-android-studio)
    - 6.1. [Instalação](#61-instalação)
    - 6.2. [Emulador](#62-emulador)
    - 6.3. [Extra](#63-extra)
      - 6.3.1. [Adicionando Play Store](#631-adicionando-playstore)
      - 6.3.2. [Alterando o tamanho da RAM](#632-alterando-a-ram)
    - 6.4. [Instalando APP no Dispositivo Virtual](#64-instalando-app-no-emulador)
7. [Referências](#7-referências)

## 1. Estação de Trabalho

Nesta etapa, iremos descrever as configurações de hardware recomendadas, além da preparação para a instalação do sistema operacional.

### 1.1. Requisitos de Hardware

A **documentação**[[1]](https://source.android.com/docs/setup/start?hl=pt-br#hardware-requirements) oficial do _`Android Open Source Project(AOSP)`_ recomenda uma máquina com pelo menos `400GB` de espaço livre em disco, sendo 250 GB para verificação + 150 GB para criação. Com o mínimo de `64GB de RAM` e um processador com pelo menos `6 núcleos`.

Estes seriam valores ideias para se ter uma performance satisfatória durante o desenvolvimento com o `Android OSP`, mas vimos que é possível trabalhar normalmente utilizando uma máquina com um pouco menos de recursos computacionais, sendo que o que vai influência de fato, seria o tempo de build e a excursão do emulador(Com o uso da virtualização).

>- **:computer:** Requisitos mínimos de hardware:
>   - Processador Intel ou AMD de 64-bits (4+ CPUs).
>   - 16GB+ de RAM.
>   - 256GB+ de armazenamento disponível.

### 1.2. Requisitos de Software

Com relação aos requisitos de software, a **documentação**[[1]](https://source.android.com/docs/setup/start?hl=pt-br#hardware-requirements) do `Android OSP` pede que seja um sistema operacional `Linux x86_64 bits`. O sistema operacional escolhido para acompanhar o curso foi o **Ubuntu 22.04.5 LTS** por já trabalharmos com ele. Neste [_link_[2]](https://ubuntu.com/download/alternative-downloads#:~:text=Server%2024.04.1%20LTS-,Ubuntu%2022.04.5%20LTS,-Ubuntu%2022.04.5%20Desktop) pode ser baixada a imagem de instalação do sistema operacional Ubuntu.

As etapas de `instalação do sistema operacional` podem ser encontradas neste [_link_[3]](https://ubuntu.com/tutorials/install-ubuntu-desktop#1-overview), está descrito desde a preparação da mídia inicializável até as etapas de particionamento do sistema operacional.

> **:memo: Nota** _Para máquinas com recursos computacionais limitados, a alternativa encontrada foi a instalação e utilização do_ [_Lubuntu 22.04_[4]](https://lubuntu.me/downloads/)_, uma variante mais leve que o Ubuntu._
>
> **:warning: Atenção:** _Ter a instalação nativa do Linux é obrigatória, já que o uso de uma máquina virtual impacta significativamente os builds do Android e a execução do emulador._

### 1.3. DualBoot

Nossas estações de trabalho estão configuradas em `DualBoot` onde o sistema operacional `Ubuntu` e `Windows` foram instalados lado a lado, a escolha de qual sistema operacional será inicializado ocorre no momento de boot com a ferramenta **GRUB**[[5]](https://www.gnu.org/software/grub/) que é responsável por gerenciar a inicialização dos sistemas operacionais.

A Figura 1 abaixo mostra como é a tela do GRUP, a foto foi tirada no momento de boot da máquina.

<p style="text-align:center">
    <img src=imgs/grup_example.jpeg style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 1:</strong> Foto da tela de gerenciamento do GRUP</figcaption>
</p>

[_link_[6]](https://help.ubuntu.com/community/WindowsDualBoot) do processo de instalação do _dual boot_  com **Windows e o Ubuntu.**

> **:memo: Nota:** _Para a montagem da mídia inicializável foi utilizado o software_ [_Rufus_[7]](https://rufus.ie/pt_BR/).
>
> **:bulb: Dica:** _Este vídeo_ [[8]![COMO INSTALAR LINUX UBUNTU JUNTO DO WINDOWS 11 COM DUAL BOOT FÁCIL](imgs/yt.png)](https://www.youtube.com/watch?v=QrsDuBwgF9Y) _descreve bem o passo a passo do processo de instalação, achamos bem didático e objetivo._

### 1.4. Máquinas utilizadas

No Linux existem várias formas de obter informações do sistema, segue abaixo os comandos utilizados e suas respectivas saídas para cada máquina.

Máquina do Leandro:

- Sistema Operacional

```sh
# O comando lsb_release imprime certas informações de LSB (Linux Standard Base) e distribuição.
$ lsb_release -a
No LSB modules are available.
Distributor ID: Ubuntu
Description: Ubuntu 22.04.5 LTS
Release: 22.04
Codename: jammy
```

- Hardware

```sh
# Informações da CPU
$ lscpu                          
Architecture:             x86_64
  CPU op-mode(s):         32-bit, 64-bit
  Address sizes:          39 bits physical, 48 bits virtual
  Byte Order:             Little Endian
CPU(s):                   12
  On-line CPU(s) list:    0-11
Vendor ID:                GenuineIntel
  Model name:             12th Gen Intel(R) Core(TM) i5-12450H
    CPU family:           6
    Model:                154
    Thread(s) per core:   2
    Core(s) per socket:   8
    Socket(s):            1
    Stepping:             3
    CPU max MHz:          4400,0000
    CPU min MHz:          400,0000
...

# Informações da RAM
$ free -h                     
               total        used        free      shared  buff/cache   available
Mem:            23Gi       4,8Gi        12Gi       929Mi       5,4Gi        17Gi
Swap:           14Gi          0B        14Gi

# Informações do Armazenamento
$ df -h
Filesystem      Size  Used Avail Use% Mounted on
...
/dev/nvme1n1p2  457G  413G   22G  96% /
...
```

Máquina do Jorge:

- Sistema Operacional

```sh
# O comando lsb_release imprime certas informações de LSB (Linux Standard Base) e distribuição.
$ lsb_release -a
No LSB modules are available.
Distributor ID: Ubuntu
Description: Ubuntu 22.04.5 LTS
Release: 22.04
Codename: jammy
```

- Hardware

```sh
# Informações da CPU
$ lscpu                          
Architecture:             x86_64
  CPU op-mode(s):         32-bit, 64-bit
  Address sizes:          39 bits physical, 48 bits virtual
  Byte Order:             Little Endian
CPU(s):                   8
  On-line CPU(s) list:    0-7
Vendor ID:                GenuineIntel
  Model name:             Intel(R) Core(TM) i7-7700HQ CPU @ 2.80GHz
    CPU family:           6
    Model:                158
    Thread(s) per core:   2
    Core(s) per socket:   4
    Socket(s):            1
    Stepping:             9
    CPU max MHz:          3800,0000
    CPU min MHz:          800,0000
...

# Informações da RAM
$ free -h                     
               total        used        free      compart.  buff/cache   available
Mem:            15Gi       6,0Gi        763Mi       1,1Gi       8,7Gi        8,1Gi
Swap:           15Gi       6,3B         9,7Gi

# Informações do Armazenamento
$ df -h
Filesystem      Size  Used Avail Use% Mounted on
...
/dev/nvme0n1p5  571G  346G   197G  64% /
...
```

## 2. Ferramentas do Sistema

Antes de baixar e compilar o código-fonte do `Android OSP` é necessário que o sistema contenha algumas ferramentas para o correto funcionamento. Seguindo todos os passos abaixo garantimos que o sistema funcionará corretamente

> **:bulb: Dica:** _Atalho para abrir o terminal no Ubuntu **`Ctrl + Alt + T`**._

### 2.1. Atualizando Pacotes do Sistema

```bash
# Comando
$ sudo apt update && sudo apt upgrade -y
```

<p style="text-align:center">
    <img src=imgs/update.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 2:</strong> Captura da tela após a execução do comando</figcaption>
</p>

### 2.2. Instalação das Ferramentas Necessárias

---

#### 2.2.1. **Git**

Verificando se o Git já está instalado.

```bash
# Retorna a versão do Git caso esteja instalado
$ git --version
```

<p style="text-align:center">
    <img src=imgs/git.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 3:</strong> Captura da tela após a execução do comando, neste caso ferramenta está instalada corretamente</figcaption>
</p>

Caso precise instalar ou atualizar, basta rodar o seguinte comando:

```bash
sudo apt-get install git-all
```

> **:memo: Nota:** _Para mais detalhes sobre o processo de instalação ou sobre o próprio Git, recomendamos este link_ [_Git_[9]](https://git-scm.com/book/pt-br/v2/Come%C3%A7ando-Instalando-o-Git).

---

#### 2.2.2. **Java(JDK)**

O processo se repete. Primeiro verifica-se se a ferramenta já está instalada, caso contrário realiza-se o processo de instalação.

```bash
# Retorna informações do Java caso esteja instalado
$ java --version
```

<p style="text-align:center">
    <img src=imgs/java_v.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 4:</strong> Captura da tela após a execução do comando, ferramenta
    instalada corretamente</figcaption>
</p>

Caso necessário, os passos abaixo orientam o processo de instalação do JDK para Linux:

- [Donwload](https://www.java.com/pt-BR/download/help/linux_x64_install.html#download)
- Vá para o diretório no qual deseja instalar o arquivo. Digite:

```bash
$ cd directory_path_name
# Por exemplo, para instalar o software no diretório /usr/java/, digite:
$ cd /usr/java/
```

- Mova o arquivo binário .tar.gz para o diretório atual.
- Descompacte o tarball (arquivo compactado TAR) e instale o Java

```bash
tar zxvf jre-8u73-linux-x64.tar.gz
```

> **:bulb: Dica:** _Os arquivos Java são instalados em um diretório chamado jre1.8.0_73
no diretório atual. Neste exemplo, ele é instalado no diretório /usr/java/jre1.8.0_73.
Quando a instalação for concluída, você verá a palavra Done._

- Delete o arquivo .tar.gz para economizar espaço em disco.

> **:memo: Nota:** _Todos os passos foram tirados da documentação_ [_oficial._[10]](https://www.java.com/pt-BR/download/help/linux_x64_install.html)

---

#### 2.2.3. **Python**

A biblioteca padrão[[11]](https://docs.python.org/3.10/library/index.html) do `Python` já vem instalada nos sistemas GNU/Linux mais recente
([Python on Ubuntu[12]](https://docs.python.org/3.10/library/index.html)), com o comando abaixo podemos encontrar a versão instalada no sistema.

```bash
# Encontra binario do Python
$ which python3

...
# Versão do Python
$ python3 --version
```

<p style="text-align:center">
    <img src=imgs/python.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 5:</strong> Captura da tela da saída da execução dos comandos citados anteriormente.</figcaption>
</p>

O processo de instalação do Python no Ubuntu é relativamente simples, podendo ser feito através do gerenciador de pacotes do Ubuntu. O comando abaixo demonstra o processo de instalação.

```bash
# Atualiza pacotes
$ sudo apt uddate

...
# Instala o Python (O X deve ser substituido pela a versão especifica de instalação)
$ sudo apt install python3.x
```

> **:bulb: Dica:** _Neste_ [_Link_[13]](https://phoenixnap.com/kb/how-to-install-python-3-ubuntu) _temos mais detalhes sobre o processo de instalação, por exemplo,
 o processo de instalação a parti do código-fonte (**Method 2**)._

---

#### 2.2.4. **Curl**

O processo de instalação da ferramento `curl` é relativamente simples, antes, verifiquemos se a ferramenta já está instalada. Os comandos abaixo foram utilizados para esse procedimento.

```sh
# Verifica versão, se nada for executado basta seguir o processo de instalação
$ curl --version

...
# Atualiza pacotes
$ sudo apt update

...
# Instalação
$ sudo apt install curl
...

```

<p style="text-align:center">
    <img src=imgs/curl.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 6:</strong> Saída da chamada de comando curl no terminal.</figcaption>
</p>

---

## 3. Instalação do VS Code

O `VS Code` é um excelente editor de texto, o seu processo de instalação ocorre através da instalação de um pacote `.deb`. Seguem os comandos abaixo para a instalação.

- O primeiro passo é realizar o download do pacote de instalação através do link: [Download Visual Studio Code[14]](https://code.visualstudio.com/download).
- Abra o terminal no mesmo local do arquivo baixado e digite o comando abaixo:

```sh
$ sudo apt install ./<file>.deb

# Se estiver em uma distribuição Linux mais antiga, precisará executar os comandos abaixo: 
# sudo dpkg -i <file>.deb
# sudo apt-get install -f # Install dependencies

```

As figuras abaixo demontram o processo de instalação.

<p style="text-align:center">
    <img src=imgs/vscode_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 7:</strong> Instalando VS code pelo terminal.</figcaption>
</p>

<p style="text-align:center">
    <img src=imgs/vscode_2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 8:</strong> Permite atualizar o VS Code automaticamente.</figcaption>
</p>

Processo de instalação concluído com sucesso, agora basta abrir o **VS Code pelo** menu de **App**, ou abrir o terminal e digitar `code`.
 A tela da figura abaixo irá aparecer, basta confirmar o tema e pronto, o vs code está pronto para o uso.

<p style="text-align:center">
    <img src=imgs/vscode_3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 9:</strong> configuração inicial do tempo do VS Code.</figcaption>
</p>

Meu VS Code e algumas das extensões instaladas.

<p style="text-align:center">
    <img src=imgs/vscode_4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 10:</strong> Extensões do VS Code.</figcaption>
</p>

## 4. Configuração de Espaço Adicional de Memória (Swap)

Em uma máquina que contenha menos de `32GB de RAM` é recomendado a criação da região de _`SWAP`_ para evitar problemas de
 compilação do `Android OSP`, como foi o caso de nossas máquinas.
Uma máquina possui 24gb de RAM e foi dedicada 15GB de Swap. A outra máquina tem 16GB de RAM e foi dedicada aos mesmos 15GB de Swap.

Abaixo, seguem os comandos utilizados para a criação/aumento da região do Swap:

- Habilitando ou alterando região de **SWAP**

```bash
# Retorna arquivo de Swap caso exista
$ swapon
NAME      TYPE  SIZE USED PRIO
/swapfile file 14,9G   0B   -2
```

<p style="text-align:center">
    <img src=imgs/swap.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 11:</strong> Região de SWAP.</figcaption>
</p>

> **:memo: Nota:** _Valor atual da configuração, antes era pouco mais de 2gb_

- Verificando a região de Swap atual

```bash
# Pré-aloca espaço em disco (16gb no arquivo swapfile)
$ sudo fallocate -l 16G /swapfile
# Da permissão de leitura e escrita para o arquivo swapfile
$ sudo chmod 600 /swapfile
# Cria área de swap no arquivo swapfile
$ sudo mkswap /swapfile
# Habilita região de Swap
$ sudo swapon /swapfile
# "joga" as informações da região de Swap no arquivo de informações do sistema de arquivos
# Esta etapa garante que a região de Swap será habilitada na inicialização da máquina
$ sudo sh -c "echo '/swapfile  none  swap  sw  0  0' >> /etc/fstab"
```

## 5. Android Open Source Project (AOSP)

Algumas etapas e ferramentas são necessárias para o correto funcionamento do Build do Android. A seguir,
 serão mostrados todos os passos executados desde a instalação dos pré-requisitos até a sincronização do repositório.

### 5.1. Pacotes Necessários

Para o Ubuntu 18 ou superior o [Google recomenda[15]](https://source.android.com/docs/setup/start?hl=pt-br#install-packages) os sequintes pacotes:

```bash
sudo apt-get install git-core gnupg flex bison build-essential zip curl zlib1g-dev libc6-dev-i386 x11proto-core-dev libx11-dev lib32z1-dev libgl1-mesa-dev libxml2-utils xsltproc unzip fontconfig
```

---

> **:warning: Atenção:** _Durante a etapa de build, ocorreu erro de falta de pacote **(libncurses5)** para isso, precisou-se apenas instalar
o seguinte pacote_

```bash
sudo apt install libncurses5-dev 
```

### 5.2. repo

Segundo sua **documentação**[[16]](https://gerrit.googlesource.com/git-repo/+/refs/heads/main/README.md), _"Repo é uma ferramenta construída sobre o Git. O Repo ajuda a gerenciar muitos repositórios do Git,
 faz os uploads para sistemas de controle de revisão e automatiza partes do fluxo de trabalho de desenvolvimento."_

O Google utiliza o `repo` para gerenciar o código fonte do `AOSP` e seu processo de instalação foi relativamente simples. Abaixo, segue os comandos utilizados
 para a sua instalação

```bash
# Debian/Ubuntu.
$ sudo apt-get install repo
```

A documentação também da a opção de instalação manual:

```bash
# Instalação manual
$ mkdir -p ~/.bin
$ PATH="${HOME}/.bin:${PATH}"
$ curl https://storage.googleapis.com/git-repo-downloads/repo > ~/.bin/repo
$ chmod a+rx ~/.bin/repo
```

<p style="text-align:center">
    <img src=imgs/repo.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 12:</strong> Saída do comando repo version.</figcaption>
</p>

> **:memo: Nota:** _A saída precisa indicar a versão 2.4 ou mais recente._

### 5.3 Sincronização do Código-Fonte

- Primeiro, foi criada uma pasta local para a sincronização do repositório.

```bash
mkdir -p /home/$USER/Documents/Courses/Embedded_Systems/android-osp
```

- Agora, foi escolhida a branch da versão do Android que iremos trabalhar e foi realizada a sincronização.

A branch escolhida foi a android-13.0.0_r83 do Android 13

```bash
# Inicializando repo apontando para a branch android-13.0.0_r83
$ repo init --partial-clone -b android-13.0.0_r83 -u <https://android.googlesource.com/platform/manifest>

# Iniciando sincronização do repositório
# Este processo pode demorar bastante tempo dependendo da capacidade da máquina e conexção com a internet
# -c: Busca apenas o branch atual do manifesto no servidor
# -j$(nproc): Divide a sincronização entre as threads de execução para que ela seja concluída mais rapidamente
$ repo sync -c -j$(nproc)
```

<p style="text-align:center">
    <img src=imgs/sync.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 13:</strong> Saída do comando repo sync.</figcaption>
</p>

> **:bulb: Dica:** _O comando nproc --all retorna o número de threads disponivel no sistema para execursão._

Abrindo o código-fonte do Android no VSCode, ainda dentro da pasta após sincronizar, basta digitar `code .` e o VS Code será executado a parti do caminho local.

<p style="text-align:center">
    <img src=imgs/sync_vscode.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 14:</strong> Visualização do código-fonte do Android.</figcaption>
</p>

### 5.4. Compilação do Sistema Operacional

#### 5.4.1. Configuração das variáveis de ambiente

Para habilitar as variáveis de ambiente segundo a [documentação do Google[17]](https://source.android.com/docs/setup/build/building?hl=pt-br#initialize), basta entrar na pasta raiz do projeto e rodar o comando abaixo. Se tudo der certo, nenhum erro será gerado. Esse script além de configurar variáveis de ambiente, importa vários comandos que permitem trabalhar com o código-fonte do Android.

```bash
# Cria as variáveis de ambiente
$ source ./build/envsetup.sh
```

<p style="text-align:center">
    <img src=imgs/source.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 15:</strong> Saída da execução do script.</figcaption>
</p>

#### 5.4.2. Definição da Plataforma Alvo

O comando [`lunch`[18]](https://source.android.com/docs/setup/build/building?hl=pt-br#choose-a-target) é o responsável por fazer a definição das configurações de build para a respectiva plataforma, o alvo abaixo foi o escolhido:

```bash
lunch sdk_phone_x86_64
```

<p style="text-align:center">
    <img src=imgs/lunch.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 16:</strong> Saída do comando lunch</figcaption>
</p>

#### 5.4.3. Build

A partir do Android 7.0, o Google trouxe o sistema de [build Soong[19]](https://source.android.com/docs/setup/build?hl=pt-br) para substituir o make. Segundo o Google o make estava se tornando lento e não escalonável, isso fez com que eles trocassem seu sistema de build do Android para o Soong.

Escolhemos utilizar o sistema de build Soong para nosso projeto, abaixo está a saída da execução do comando além do comando utilizado.

```bash
m -j$(nproc)
```

<p style="text-align:center">
    <img src=imgs/m.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 17:</strong> Inicio do build</figcaption>
</p>

<p style="text-align:center">
    <img src=imgs/end_build.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 18:</strong> Fim do Build</figcaption>
</p>

> **:memo: Nota:** _Esse build não foi o primeiro, por isso o tempo de build foi relativamente pequeno se comparado ao inicial que demorou cerca de 3h_

#### 5.4.4. Teste do Build

Um teste simples foi realizado após a build que foi a execução do emulador conforme a figura abaixo:

<p style="text-align:center">
    <img src=imgs/test_emulator_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 19:</strong> Emulator</figcaption>
</p>

## 6. Android Studio

## 6.1. Instalação

O processo de instalação do Android Studio escolhido foi o manual[[20]](https://developer.android.com/studio/install?authuser=2&%3Bhl=pt-br&hl=pt-br&_gl=1*6t7xu0*_up*MQ..&gclid=Cj0KCQiAvbm7BhC5ARIsAFjwNHsGfZbB6VwAez5eR4Q9N3bVmn-8JY4GTpa_tPP0lZi0HPCPOu7TgqIaAhOYEALw_wcB&gclsrc=aw.ds#linux), abaixo seguem os passos utilizados:

- Bibliotecas necessárias para máquinas de 64 bits

```bash
sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386
```

- Donwload

Para realizar o download do Android Studio, basta acessar o [site oficial[21]](https://developer.android.com/studio?hl=pt-br#downloads) da IDE e fazer o download da versão que escolher. Nós escolhemos a versão mais recente.

<p style="text-align:center">
    <img src=imgs/as_donwloads.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 20:</strong> Android Studio donwload</figcaption>
</p>

- Instalação

Abrindo o terminal dentro do diretório onde foi realizado o download, basta digitar os comandos abaixo:

```bash
# Extrai os dados
$ tar -zxvf android-studio-2024.2.1.12-linux.tar.gz android-studio/
# Move a pasta para o diretório de instalação
$ sudo mv android-studio /opt/
# Cria link simbolico do binario do android studio
$ sudo ln -sf /opt/android-studio/bin/studio /bin/android-studio
```

- Criação de atalho da aplicação

```bash
# Cria arquivo de inicialização do android studio via interface gráfica
sudo nano /usr/share/applications/android-studio.desktop
```

Basta copiar e colar as informações abaixo dentro do arquivo criado e pronto. O ícone irá aparecer no menu inicial.

```txt
[Desktop Entry]
Version=1.0
Type=Application
Name=Android Studio
Comment=Android Studio
Exec=/opt/android-studio/bin/studio
Icon=/opt/android-studio/bin/studio.png
Categories=Development;IDE;
Terminal=false
StartupNotify=true
StartupWMClass=jetbrains-android-studio
Name[en_GB]=android-studio.desktop
```

<p style="text-align:center">
    <img src=imgs/menu.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 21:</strong> Menu de Aplicativos</figcaption>
</p>

Para continuar o processo de instalação, basta clicar no ícone do Android Studio e, se tudo estiver correto, as telas abaixo aparecerão.

Nesta tela, o Android Studio não achou nenhum SDK instalado, basta apertar em `Next` e seguir para a tela de instalação.

<p style="text-align:center">
    <img src=imgs/as_s_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 22:</strong> Tela inicial de instalação</figcaption>
</p>

---

Na próxima tela, pode deixar as configurações padrão, nesta tela é feita a escolha da versão do SDK do Android. `Next`

<p style="text-align:center">
    <img src=imgs/as_s_2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 23:</strong> Escolha do SDK</figcaption>
</p>

---

Tela de verificação das configurações, estando tudo certo, basta apertar em `Next`.

<p style="text-align:center">
    <img src=imgs/as_s_3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 24:</strong> Validação das configurações</figcaption>
</p>

---

Tela de aceite dos termos de licenciamento. Se concorda, basta apertar `Finish`.

<p style="text-align:center">
    <img src=imgs/as_s_4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 25:</strong> Termos e condições</figcaption>
</p>

---

Tela de Download

<p style="text-align:center">
    <img src=imgs/as_s_5.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 26:</strong> Donwloads</figcaption>
</p>

Tela de instalação

<p style="text-align:center">
    <img src=imgs/as_s_6.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 27:</strong> Instalação</figcaption>
</p>

---

Instalação finalizada

<p style="text-align:center">
    <img src=imgs/as_end.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 28:</strong> Tela inicial do Android Studio</figcaption>
</p>

---

## 6.2. Emulador

- Criando um dispositivo virtual.

Abrindo o gerenciador de dispositivos virtuais.

<p style="text-align:center">
    <img src=imgs/avd_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 29:</strong> Configuração: Virtual Devices Manager</figcaption>
</p>

---

Nesta tela, basta apertar em `Criar dispositivo virtual`.

<p style="text-align:center">
    <img src=imgs/avd_2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 30:</strong> Virtual Devices Manager</figcaption>
</p>

Escolhendo a categoria e o modelo do dispositivo a ser criado.

<p style="text-align:center">
    <img src=imgs/avd_3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 31:</strong> Seleção do Hardware</figcaption>
</p>

---

Escolhendo a imagem do sistema a ser baixada, a versão escolhida foi a mesma do AOSP baixado na seção [Android Open Source Project](#53-sincronização-do-código-fonte).

<p style="text-align:center">
    <img src=imgs/avd_4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 32:</strong> Selecão da imagem do sistema</figcaption>
</p>

---

Nesta parte é feito o ajuste das configurações do dispositivo virtual, foi realizada a alteração do tamanho da RAM e da quantidade de núcleos do processador.

<p style="text-align:center">
    <img src=imgs/avd_5.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 33:</strong> Configuração do AVD</figcaption>
</p>

---

Dispositivo criado, dando play e verificando o funcionamento.

<p style="text-align:center">
    <img src=imgs/test_end_avd.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 34:</strong> Teste do dispositivo Virtual</figcaption>
</p>

---

> **:bulb: Dica:** [_Documentação oficial_[22]](https://developer.android.com/studio/run/managing-avds?hl=pt-br#workingavd) _com mais detalhes sobre os dispositivos virtuais._

## 6.3. Extra

### 6.3.1. Adicionando PlayStore

Como é possível notar, alguns dispositivos virtuais não têm a Play Store instalada na imagem do sistema, uma maneira simples de realizar esse procedimento é executar os passos a seguir:

- Liste as configurações do dispositivo no conforme a figura abaixo.

Está pasta foi criada a partir do dispositivo criado na seção anterior.

<p style="text-align:center">
    <img src=imgs/avd_ps_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 35:</strong> Configurações do dispositivo virtual</figcaption>
</p>

Entre na pasta e liste todos os arquivos conforma a figura a serguir.

<p style="text-align:center">
    <img src=imgs/avd_ps_2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 36:</strong> Arquivos de configurações do dispositivo virtual</figcaption>
</p>

Faça as seguintes alterações nos respectivos arquivos:

> `config.ini`

```bash
...
# Troce de false para true
PlayStore.enabled = true
# Troque de google_apis para google_apis_playstore
# Android/Sdk/system-images/android-33/
image.sysdir.1 = system-images/android-33/google_apis_playstore/x86_64/
...
```

> `hardware-qemu.ini`

```bash
...
# Troque de google_apis para google_apis_playstore
kernel.path = /home/lmendes/Android/Sdk/system-images/android-33/google_apis_playstore/x86_64//kernel-ranchu
# Troque de google_apis para google_apis_playstore
disk.ramdisk.path = /home/lmendes/Android/Sdk/system-images/android-33/google_apis_playstore/x86_64//ramdisk.img
# Troque de google_apis para google_apis_playstore
disk.systemPartition.initPath = /home/lmendes/Android/Sdk/system-images/android-33/google_apis_playstore/x86_64//system.img
# Troque de google_apis para google_apis_playstore
disk.vendorPartition.initPath = /home/lmendes/Android/Sdk/system-images/android-33/google_apis_playstore/x86_64//vendor.img
# Troce de false para true
PlayStore.enabled = false
...
```

Antes de reiniciar o emulador, apague os dados do dispositivo conforme a imagem abaixo:

<p style="text-align:center">
    <img src=imgs/wipe.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 37:</strong> Configuração do dispositivo</figcaption>
</p>

- Teste no dispositivo

<p style="text-align:center">
    <img src=imgs/PlayStore.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 38:</strong> Configuração do dispositivo</figcaption>
</p>

### 6.3.2. Alterando a RAM

Alguns perfis de dispositivos, principalmente os que têm a Play Store na pré-instalada, não permitem a alteração das configurações de desempenho. Conforme a figura abaixo.

Uma forma simples de realizar essas alterações é criando um clone do perfil e alterar as configurações de desempenho do clone.

<p style="text-align:center">
    <img src=imgs/config_blocked.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 39:</strong> Configuração do dispositivo bloqueadas</figcaption>
</p>

- Criando um dispositivo clone

As imagens abaixo mostram o passo a passo para o procedimento.

Para clonar o dispositivo basta escolher e aperta no botão em destaque da imagem a baixo.

<p style="text-align:center">
    <img src=imgs/clone.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 40:</strong> Clonando dispositivo</figcaption>
</p>

---

Dispositivo clonado aparecendo na lista de dispositivos

<p style="text-align:center">
    <img src=imgs/clone_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 41:</strong> Dispositivo clonado</figcaption>
</p>

Agora as configurações podem ser alteradas, caso queira, pode adicionar novamente a Play Store no dispositivo conforme o tópico anterior.

<p style="text-align:center">
    <img src=imgs/clone_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 42:</strong> Configurando dispositivo clonado</figcaption>
</p>

---

> **:bulb: Dica:** _A RAM também pode ser alterada direto no arquivo de configuração do dispositivo, este_ [_link_[23]](https://wazeem.com/76/how-to-increase-ram-in-android-emulators-with-google-play-images/) _mostra o procedimento, muito semelhante ao de adicionar a Play Store._

## 6.4. Instalando APP no Emulador

O processo de instalação escolhido para teste foi relativamente simples, apenas arrastando e soltando o arquivo[[24]](https://developer.android.com/studio/run/emulator-install-add-files?hl=pt-br) _".apk"_, gerado de um exemplo de `Hello Word`, na tela do emulador, depois foi clicado no arquivo e feita a visualização da execução do APP.

<p style="text-align:center">
    <img src=imgs/Install.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 43:</strong> Aplicativo Gretting Card Instalado</figcaption>
</p>

---

Rodando aplicativo

<p style="text-align:center">
    <img src=imgs/app_run.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 44:</strong> Rodando aplicativo Gretting Card</figcaption>
</p>

---

> **:memo: Nota:** [_Link_[25]](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-1-pathway-2#0) _do exemplo utilizado para criar o APP._

## 7. Referências

1. **[Android OSP Hardware Requirements](https://source.android.com/docs/setup/start?hl=pt-br#hardware-requirements)**

2. **[Ubuntu Desktop 22.04.5 LTS](https://ubuntu.com/download/alternative-downloads#:~:text=Server%2024.04.1%20LTS-,Ubuntu%2022.04.5%20LTS,-Ubuntu%2022.04.5%20Desktop)**

3. **[Instalação do Ubuntu Desktop](https://ubuntu.com/tutorials/install-ubuntu-desktop#1-overview)**

4. **[Sistema Operacional Lubuntu 22.04](https://lubuntu.me/downloads/)**

5. **[Software GRUB](https://www.gnu.org/software/grub/)**

6. **[Windows Dual Boot](https://help.ubuntu.com/community/WindowsDualBoot)**

7. **[Software Rufus](https://rufus.ie/pt_BR/)**

8. **[Como Instalar Linux Ubuntu Junto do Windows 11 Com Dual Boot Fácil](https://www.youtube.com/watch?v=QrsDuBwgF9Y)**

9. **[Ferramenta Git](https://git-scm.com/book/pt-br/v2/Come%C3%A7ando-Instalando-o-Git)**

10. **[Java Documentação Oficial.](https://www.java.com/pt-BR/download/help/linux_x64_install.html)**

11. **[Biblioteca Padrão do Python](https://docs.python.org/3.10/library/index.html)**

12. **[Python on Ubuntu](https://docs.python.org/3.10/library/index.html)**

13. **[How to Install Python 3 on Ubuntu 20.04 or 22.04](https://phoenixnap.com/kb/how-to-install-python-3-ubuntu)**

14. **[Download Visual Studio Code](https://code.visualstudio.com/download)**

15. **[Pacotes Necessários para o Android OSP](https://source.android.com/docs/setup/start?hl=pt-br#install-packages)**

16. **[Repo README](https://gerrit.googlesource.com/git-repo/+/refs/heads/main/README.md)**

17. **[Configurar o ambiente de build (AOSP)](https://source.android.com/docs/setup/build/building?hl=pt-br#initialize)**

18. **[Escolher um destino de Build (AOSP)](https://source.android.com/docs/setup/build/building?hl=pt-br#choose-a-target)**

19. **[Build Soong](https://source.android.com/docs/setup/build?hl=pt-br)**

20. **[Instalar o Android Studio no Linux](https://developer.android.com/studio/install?authuser=2&%3Bhl=pt-br&hl=pt-br&_gl=1*6t7xu0*_up*MQ..&gclid=Cj0KCQiAvbm7BhC5ARIsAFjwNHsGfZbB6VwAez5eR4Q9N3bVmn-8JY4GTpa_tPP0lZi0HPCPOu7TgqIaAhOYEALw_wcB&gclsrc=aw.ds#linux)**

21. **[Android Studio Download](https://developer.android.com/studio?hl=pt-br#downloads)**

22. **[Criar e gerenciar dispositivos virtuais](https://developer.android.com/studio/run/managing-avds?hl=pt-br)**

23. **[How to Increase RAM in Android Emulators with Google Play Images](https://wazeem.com/76/how-to-increase-ram-in-android-emulators-with-google-play-images/)**

24. **[Instalar e adicionar arquivos no AVD](https://developer.android.com/studio/run/emulator-install-add-files?hl=pt-br)**

25. **[Create your first Android app](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-1-pathway-2#0)**
