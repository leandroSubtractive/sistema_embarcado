# Customização do AOSP

Relatório descrevendo os passos utilizados para a customização do Android OSP.

## CURSO

FORMAÇÃO EM SISTEMAS EMBARCADOS

LEANDRO MENDES DOS SANTOS

## Links

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

Link do Vídeo: [Vídeo](https://drive.google.com/drive/folders/1xURd7VnulzM-5QFO18Kk7mN_jLk9DK0C)

## Objetivos De Aprendizagem

- Configurar um dispositivo virtual, como o "Pixel 5", com a API adequada e personalizar
configurações, como resolução e armazenamento, para garantir simulações precisas
- Definir os principais subsistemas do sistema embarcado, como a interface do usuário, além de
desenvolver um diagrama claro que mostre os fluxos de dados e interações entre os
subsistemas.

## Sumário

1. [Momento 1: Configurar o ambiente no Android Studio](#1-momento-1-configurar-o-ambiente-no-android-studio)
    - 1.1. [Android Studio](#11-android-studio)
        - 1.1.1 [Instalação](#111-instalação)
        - 1.1.2 [Emulador](#112-emulador)
        - 1.1.3. [Extra](#113-extra)
            - 1.1.3.1. [Adicionando Play Store](#1131-adicionando-playstore)
            - 1.1.3.2. [Alterando o tamanho da RAM](#1132-alterando-a-ram)
        - 1.1.4. [Instalando APP no Dispositivo Virtual](#114-instalando-app-no-emulador)
2. [Momento 2: Planejar a Arquitetura do Sistema Embarcado](#momento-2-planejar-a-arquitetura-do-sistema-embarcado)
3. [Referências](#3-referências)

## 1. Momento 1: Configurar o ambiente no Android Studio

Toda a configuração da máquina já foi realizada na atividade anterior [(Implementação de um dispositivo com sistema
operacional Android)](https://github.com/leandroSubtractive/sistema_embarcado/blob/devel/readme_files/DC1%5BHandsOn_UA1-UA2%5D%20Implementa%C3%A7%C3%A3o%20de%20um%20dispositivo%20com%20sistema%20operacional%20Android.md#implementa%C3%A7%C3%A3o-de-um-dispositivo-com-sistema-operacional-android), inclusive a instalação do Android Studio. Neste documento, faremos uma revisão e atualização de alguns pontos, caso necessário.

### 1.1. Android Studio

#### 1.1.1. Instalação

O processo de instalação do Android Studio escolhido foi o manual[[1]](https://developer.android.com/studio/install?authuser=2&%3Bhl=pt-br&hl=pt-br&_gl=1*6t7xu0*_up*MQ..&gclid=Cj0KCQiAvbm7BhC5ARIsAFjwNHsGfZbB6VwAez5eR4Q9N3bVmn-8JY4GTpa_tPP0lZi0HPCPOu7TgqIaAhOYEALw_wcB&gclsrc=aw.ds#linux), abaixo seguem os passos utilizados:

- **Bibliotecas necessárias para máquinas de 64 bits**

```bash
sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386
```

- **Donwload**

Para realizar o download do Android Studio, basta acessar o [site oficial[2]](https://developer.android.com/studio?hl=pt-br#downloads) da IDE e fazer o download da versão que escolher. Nós escolhemos a versão mais recente.

<p style="text-align:center">
    <img src=imgs/as_donwloads.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 1:</strong> Android Studio donwload</figcaption>
</p>

A versão mais recente e a que estou usando é `Android Studio Narwhal`. Adicionei uma etapa de atualização com o processo que realizei para atualizar minha antiga versão.

```bash
Android Studio Narwhal 3 Feature Drop | 2025.1.3
Build #AI-251.26094.121.2513.14007798, built on August 28, 2025
Runtime version: 21.0.7+-13880790-b1038.58 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Toolkit: sun.awt.X11.XToolkit
Linux 6.8.0-84-generic
Ubuntu 22.04.5 LTS; glibc: 2.35
Kotlin plugin: K2 mode
GC: G1 Young Generation, G1 Concurrent GC, G1 Old Generation
Memory: 2968M
Cores: 12
Registry:
  ide.experimental.ui=true
Current Desktop: ubuntu:GNOME
```

- **Instalação**

Abrindo o terminal dentro do diretório onde foi realizado o download, basta digitar os comandos abaixo:

```bash
# Extrai os dados
$ tar -zxvf android-studio-2024.2.1.12-linux.tar.gz android-studio/
# Move a pasta para o diretório de instalação
$ sudo mv android-studio /opt/
# Cria link simbolico do binario do android studio
$ sudo ln -sf /opt/android-studio/bin/studio /bin/android-studio
```

- **Atualizção**

```bash
# Extrai os dados
$ tar -zxvf android-studio-2025.1.3.7-linux.tar.gz android-studio/
# Remova a instalação antiga
$ sudo rm -rf /opt/android-studio
# Move a pasta para o diretório de instalação
$ sudo mv android-studio /opt/
# Cria link simbolico do binario do android studio
$ sudo ln -sf /opt/android-studio/bin/studio /bin/android-studio
```

- **Criação de atalho da aplicação**

```bash
# Cria arquivo de inicialização do android studio via interface gráfica
sudo nano /usr/share/applications/android-studio.desktop
```

> **:memo: Nota:** Se estiver atualizando uma versão existente não precisa atualizar este arquivo.

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
    <img src=imgs/menu.png alt style="width:90%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 1:</strong> Menu de Aplicativos</figcaption>
</p>

Para continuar o processo de instalação, basta clicar no ícone do Android Studio e, se tudo estiver correto, as telas abaixo aparecerão.

Nesta tela, o Android Studio não achou nenhum SDK instalado, basta apertar em `Next` e seguir para a tela de instalação.

<p style="text-align:center">
    <img src=imgs/as_s_1.png alt style="width:90%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 2:</strong> Tela inicial de instalação</figcaption>
</p>

---

Na próxima tela, pode deixar as configurações padrão, nesta tela é feita a escolha da versão do SDK do Android. `Next`

<p style="text-align:center">
    <img src=imgs/as_s_2.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 3:</strong> Escolha do SDK</figcaption>
</p>

---

Tela de verificação das configurações, estando tudo certo, basta apertar em `Next`.

<p style="text-align:center">
    <img src=imgs/as_s_3.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 4:</strong> Validação das configurações</figcaption>
</p>

---

Tela de aceite dos termos de licenciamento. Se concorda, basta apertar `Finish`.

<p style="text-align:center">
    <img src=imgs/as_s_4.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 5:</strong> Termos e condições</figcaption>
</p>

---

Tela de Download

<p style="text-align:center">
    <img src=imgs/as_s_5.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 6:</strong> Donwloads</figcaption>
</p>

Tela de instalação

<p style="text-align:center">
    <img src=imgs/as_s_6.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 7:</strong> Instalação</figcaption>
</p>

---

Instalação finalizada

<p style="text-align:center">
    <img src=imgs/as_end.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 8:</strong> Tela inicial do Android Studio</figcaption>
</p>

A imagem abaixo exibe os pacotes instalados do SDK Tools. O HAXM é um recurso que não é utilizado no sistema operacional Linux, ficando mais a cargo do KVM. Além disso, segundo a própria [Google](https://developer.android.com/studio/run/emulator-acceleration?hl=pt-br#haxm-uninstall), o HAXM não é mais recomendado e foi descontinuado pela Intel.

<p style="text-align:center">
    <img src=imgs/sdk_tools.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 9:</strong> Tela do SDK Tools do Android Studio</figcaption>
</p>
---

#### 1.1.2. Emulador

- Criando um dispositivo virtual.

Abrindo o gerenciador de dispositivos virtuais.

<p style="text-align:center">
    <img src=imgs/avd_1.png alt style="width:75%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 10:</strong> Configuração: Virtual Devices Manager</figcaption>
</p>

---

Nesta tela, basta apertar em `Criar dispositivo virtual`.

<p style="text-align:center">
    <img src=imgs/avd_2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 11:</strong> Virtual Devices Manager</figcaption>
</p>

Escolhendo a categoria e o modelo do dispositivo a ser criado.

<p style="text-align:center">
    <img src=imgs/avd_3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 12:</strong> Seleção do Hardware</figcaption>
</p>

---

Escolhendo a imagem do sistema a ser baixada, a versão escolhida foi a mesma do AOSP baixado na seção [Android Open Source Project](https://github.com/leandroSubtractive/sistema_embarcado/blob/devel/readme_files/DC1%5BHandsOn_UA1-UA2%5D%20Implementa%C3%A7%C3%A3o%20de%20um%20dispositivo%20com%20sistema%20operacional%20Android.md#53-sincroniza%C3%A7%C3%A3o-do-c%C3%B3digo-fonte).

<p style="text-align:center">
    <img src=imgs/avd_4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 13:</strong> Selecão da imagem do sistema</figcaption>
</p>

---

Nesta parte é feito o ajuste das configurações do dispositivo virtual, foi realizada a alteração do tamanho da RAM e da quantidade de núcleos do processador.

<p style="text-align:center">
    <img src=imgs/avd_5.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 14:</strong> Configuração do AVD</figcaption>
</p>

---

Dispositivo criado, dando play e verificando o funcionamento.

<p style="text-align:center">
    <img src=imgs/test_end_avd.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 15:</strong> Teste do dispositivo Virtual</figcaption>
</p>

---

> **:bulb: Dica:** [_Documentação oficial_[3]](https://developer.android.com/studio/run/managing-avds?hl=pt-br#workingavd) _com mais detalhes sobre os dispositivos virtuais._

#### 1.1.3. Extra

##### 1.1.3.1. Adicionando PlayStore

Como é possível notar, alguns dispositivos virtuais não têm a Play Store instalada na imagem do sistema, uma maneira simples de realizar esse procedimento é executar os passos a seguir:

- Liste as configurações do dispositivo no conforme a figura abaixo.

Está pasta foi criada a partir do dispositivo criado na seção anterior.

<p style="text-align:center">
    <img src=imgs/avd_ps_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 16:</strong> Configurações do dispositivo virtual</figcaption>
</p>

Entre na pasta e liste todos os arquivos conforma a figura a serguir.

<p style="text-align:center">
    <img src=imgs/avd_ps_2.png alt style="width:95%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 17:</strong> Arquivos de configurações do dispositivo virtual</figcaption>
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
    <figcaption style="text-align:center"><strong>Figura 18:</strong> Configuração do dispositivo</figcaption>
</p>

- **Teste no dispositivo**

<p style="text-align:center">
    <img src=imgs/PlayStore.png alt style="width:90%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 19:</strong> Configuração do dispositivo</figcaption>
</p>

##### 1.1.3.2. Alterando a RAM

Alguns perfis de dispositivos, principalmente os que têm a Play Store na pré-instalada, não permitem a alteração das configurações de desempenho. Conforme a figura abaixo.

Uma forma simples de realizar essas alterações é criando um clone do perfil e alterar as configurações de desempenho do clone.

<p style="text-align:center">
    <img src=imgs/config_blocked.png alt style="width:90%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 20:</strong> Configuração do dispositivo bloqueadas</figcaption>
</p>

- Criando um dispositivo clone

As imagens abaixo mostram o passo a passo para o procedimento.

Para clonar o dispositivo basta escolher e aperta no botão em destaque da imagem a baixo.

<p style="text-align:center">
    <img src=imgs/clone.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 21:</strong> Clonando dispositivo</figcaption>
</p>

---

Dispositivo clonado aparecendo na lista de dispositivos

<p style="text-align:center">
    <img src=imgs/clone_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 22:</strong> Dispositivo clonado</figcaption>
</p>

Agora as configurações podem ser alteradas, caso queira, pode adicionar novamente a Play Store no dispositivo conforme o tópico anterior.

<p style="text-align:center">
    <img src=imgs/clone_1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 23:</strong> Configurando dispositivo clonado</figcaption>
</p>

---

> **:bulb: Dica:** _A RAM também pode ser alterada direto no arquivo de configuração do dispositivo, este_ [_link_[4]](https://wazeem.com/76/how-to-increase-ram-in-android-emulators-with-google-play-images/) _mostra o procedimento, muito semelhante ao de adicionar a Play Store._

#### 1.1.4. Instalando APP no Emulador

O processo de instalação escolhido para teste foi relativamente simples, apenas arrastando e soltando o arquivo[[5]](https://developer.android.com/studio/run/emulator-install-add-files?hl=pt-br) _".apk"_, gerado de um exemplo de `Hello Word`, na tela do emulador, depois foi clicado no arquivo e feita a visualização da execução do APP.

<p style="text-align:center">
    <img src=imgs/Install.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 25:</strong> Aplicativo Gretting Card Instalado</figcaption>
</p>

---

Rodando aplicativo

<p style="text-align:center">
    <img src=imgs/app_run.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 26:</strong> Rodando aplicativo Gretting Card</figcaption>
</p>

---

> **:memo: Nota:** [_Link_[6]](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-1-pathway-2#0) _do exemplo utilizado para criar o APP.

## Momento 2: Planejar a Arquitetura do Sistema Embarcado

O diagrama da figura 27 apresenta um paralelo com as camadas do Android Automotive[[7]](https://proandroiddev.com/mastering-android-automotive-os-architecture-expert-guide-33b7fa73bf8c). Explicando de maneira objetiva, o sistema proposto possui uma interface de comunicação com o usuário que recebe inputs (Pensando no cenário automotivo: toques na tela). Nesta mesma interface, o usuário também recebe feedbacks do sistema.
Todos esses estímulos enviados e recebidos do sistema ocorrem através da camada de comunicação, que tem por função fundamental se comunicar com as camadas mais inferiores do sistema de maneira transparente, expondo APIs e serviços que possibilitem a manipulação de outros dispositivos.
Nas camadas mais inferiores ocorre de fato o tratamento do dado que é recebido e enviado à interface do sistema.
Fazendo uma comparação com o aplicativo de perfis de equalização, as mensagens CAN enviadas das camadas de comunicação são processadas pelo sistema de áudio do veículo.

<p style="text-align:center">
    <img src=imgs/Diagrama.drawio.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 27:</strong> Arquitetura do Sistema Embarcado</figcaption>
</p>

## 3. Referências

1. **[Instalar o Android Studio no Linux](https://developer.android.com/studio/install?authuser=2&%3Bhl=pt-br&hl=pt-br&_gl=1*6t7xu0*_up*MQ..&gclid=Cj0KCQiAvbm7BhC5ARIsAFjwNHsGfZbB6VwAez5eR4Q9N3bVmn-8JY4GTpa_tPP0lZi0HPCPOu7TgqIaAhOYEALw_wcB&gclsrc=aw.ds#linux)**

2. **[Android Studio Download](https://developer.android.com/studio?hl=pt-br#downloads)**

3. **[Editar AVDs já existentes](https://developer.android.com/studio/run/managing-avds?hl=pt-br#workingavd)**

4. **[How to Increase RAM in Android Emulators with Google Play Images](https://wazeem.com/76/how-to-increase-ram-in-android-emulators-with-google-play-images/)**

5. **[Instalar e adicionar arquivos no AVD](https://developer.android.com/studio/run/emulator-install-add-files?hl=pt-br)**

6. **[Create your first Android app](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-1-pathway-2#0)**

7. **[Android Automotive OS Architecture: Definitive Guide](https://proandroiddev.com/mastering-android-automotive-os-architecture-expert-guide-33b7fa73bf8c)**
