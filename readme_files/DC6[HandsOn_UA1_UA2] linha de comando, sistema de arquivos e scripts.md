# Ambiente Linux: linha de comando, sistema de arquivos e scripts

---

## CURSO

FORMAÇÃO EM SISTEMAS EMBARCADOS

LEANDRO MENDES DOS SANTOS

---

## Links

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

---

## OBJETIVOS DE APRENDIZAGEM

- Explicar os conceitos básicos da linha de comando para a manipulação de arquivos;
- Operar arquivos e diretórios no sistema de arquivos do Linux;
- Elaborar scripts de shell que utilizem variáveis, condicionais e laços de repetição para automatizar tarefas;
- Diferenciar as propriedades de arquivos e diretórios no Sistema Operacional Linux.

---

## Sumário

1. [Laboratórios](#1-laboratórios)
    - 1.1. [Laboratório 5](#11-laboratório-5)
    - 1.2. [Laboratório 7](#12-laboratório-7)
    - 1.3. [Laboratório 8](#13-laboratório-8)
    - 1.4. [laboratório 17](#14-laboratório-17)
    - 1.5. [Laboratório 11](#15-laboratório-11)

## 1. Laboratórios

This lab has two user accounts (username :: password )

   root :: netlab123
   sysadmin :: netlab123

### 1.1. Laboratório 5

---
***Command Line Skills***

> 5.2 Files and Directories

Nesta seção, foi apresentado o comando `ls`. Esse comando lista os arquivos e diretórios presentes no diretório atual do usuário.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_ls.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 1:</strong> Comando ls</figcaption>
</p>

> 5.2.1 Step 1

Nesta seção, o comando `ls` foi adicionando a opção `-l` que fornece informações adicionais sobre os arquivos localizados no diretório de trabalho atual.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_ls_l.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 2:</strong> Comando ls -l</figcaption>
</p>

> 5.2.2 Step 2

Nesta seção, é adicionado o argumento `/home` que é um diretório, sendo assim o comando `ls -l /home` irá listar as informações de dentro do diretório.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_ls_l_home.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 3:</strong> Comando ls -l /home</figcaption>
</p>

> 5.2.3 Step 3

Nesta seção, o comando whoami é utilizado para exibir o nome de usuário do usuário atual.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_whoami.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 4:</strong> Comando whoami</figcaption>
</p>

> 5.2.4 Step 4

Nesta seção, o comando exibe informações sobre o sistema atual, nome do kernel.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_uname.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 5:</strong> Comando uname</figcaption>
</p>

O mesmo comando com a opção `-n` ou `--nodename` exibirá o nome do host do nó de rede, também encontrado no prompt.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_uname_n.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 6:</strong> Comando uname -n ou --nodename</figcaption>
</p>

> 5.2.5 Step 5

Nesta seção, O comando `pwd` é usado para exibir sua `localização` atual ou diretório `de trabalho` atual.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_pwd.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 7:</strong> Comando pwd</figcaption>
</p>

---
***Command History***

> 5.3.1 Step 1

Nesta seção, O comando `history` serve para exibir uma lista dos comandos já executados no terminal.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_history.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 8:</strong> Comando history</figcaption>
</p>

> 5.3.2 Step 2

Nesta seção, O comando `history` é utilizado com o argumento 5 que indica uma limitação na lista de resultado dos comandos (Retorna os 5 últimos comandos).

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_history_5.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 9:</strong> Comando history 5</figcaption>
</p>

> 5.3.3 Step 3

Nesta seção, veremos que para executar um comando novamente, basta digitar o ponto de exclamação e o número da lista do histórico.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_!.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 10:</strong> Comando !</figcaption>
</p>

---
***Shell Variables***

> 5.4.1 Step 1

Nesta seção, veremos o comando `echo` que pode ser usado para imprimir texto e o valor de uma variável, e para mostrar como o ambiente do shell expande meta caracteres.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_echo.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 11:</strong> Comando echo</figcaption>
</p>

> 5.4.2 Step 2

Nesta seção, veremos o comando `echo` sendo utilizado para exibir o valor de uma variável de sistema. A variável `HISTSIZE` define quantos comandos anteriores devem ser armazenados na lista de histórico.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_echo_histsize.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 12:</strong> Comando echo $HISTSIZE</figcaption>
</p>

> 5.4.3 Step 3

O comando `echo $PATH` exibe o valor da variavel PATH.

Essa variável é usada para localizar comandos. Cada um dos diretórios listados acima é pesquisado quando você executa um comando. Por exemplo, se você tentar executar o comando date, o shell primeiro procurará o comando no diretório `/home/sysadmin/bin` e, em seguida, no diretório `/usr/local/sbin` e assim por diante. Quando o comando date é encontrado, o shell o `executa`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_echo_path.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 13:</strong> Comando echo $PATH</figcaption>
</p>

> 5.4.4 Step 4

Nesta seção, o comando `which` é usado para determinar se existe um arquivo executável, neste caso chamado date, localizado em um diretório listado no valor `PATH`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_which.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 14:</strong> Comando which date</figcaption>
</p>

---
***Command Types***

> 5.5.1 Step 1

Nesta seção, o comando `type` é usado para determinar informações sobre o tipo de comando.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_type.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 15:</strong> Comando type cd</figcaption>
</p>

> 5.5.2 Step 2

Usar a opção `-a` do comando `type` exibe todos os locais que contêm o comando

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_type_a.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 16:</strong> Comando type -a ls</figcaption>
</p>

> 5.5.3 Step 3

Nesta seção, o comando `alias` é usado para determinar quais aliases estão definidos no shell atual. Os aliases podem ser usados para mapear comandos mais longos para sequências de caracteres mais curtas.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_alias.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 17:</strong> Comando alias</figcaption>
</p>

---
***Quoting***

> 5.6.1 Step 1

Nesta seção, vemos que é possível utilizar (` `) para executar comandos dentro de outro comando, abaixo o exemplo de uso do comando `date` rodando dentro do `echo`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_date_.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 17:</strong> Comando `date`</figcaption>
</p>

> 5.6.2 Step 2

Outra forma de realizar a execução de comandos dentro de outros comandos é com o uso do $(), que diferente da (``) torna mais explicito a execução.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_$.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 17:</strong> Comando $</figcaption>
</p>

> 5.6.3 Step 3

Caso as aspas invertidas não sejam usadas para executar um comando, é necessário colocar aspas simples ao redor delas.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command__.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 18:</strong> Comando ''</figcaption>
</p>

> 5.6.4 Step 4

Outro ponto que é mostrado nessa seção é que a barra invertida (\) tem a função semelhante das aspas simples ('').

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_invert.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 19:</strong> Comando \</figcaption>
</p>

> 5.6.5 Step 5

Uma observação importante é que os caracteres de aspas duplas " não têm qualquer efeito sobre os caracteres de aspas invertidas. O shell continuará a utilizá-los como substituição de comando.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_aspas.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 20:</strong> Comando "</figcaption>
</p>

> 5.6.6 Step 6

Nesta seção, vemos que os caracteres de aspas duplas afetam os caracteres curingas, desativando seu significado especial.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_aspas_d.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 21:</strong> Comando "</figcaption>
</p>

---
***Control Statements***

> 5.7.1 Step 1

Nesta seção, será mostrado como executar mais de um comando utilizando separadores: ;, &&, ||.

Para o separador ;, os comandos são executados um após o outro independente do resultado de execução do comando anterior.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_ponto_e_v.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 22:</strong> Comando ;</figcaption>
</p>

> 5.7.2 Step 2

Nesta seção, vemos o exemplo que, embora o primeiro comando "falhe" os demais são executados de maneira independente.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_pev.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 23:</strong> Comando ;</figcaption>
</p>

> 5.7.3 Step 3

O uso de && define que o comando a direito só é executado se o comando a esquerda for executado com sucesso.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_and.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 24:</strong> Comando &&</figcaption>
</p>

> 5.7.4 Step 4

Causando uma falha na execução de comandos, é possível observar que o comando à direita do que falhou não é executado.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_and_f.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 25:</strong> Comando &&</figcaption>
</p>

> 5.7.5 Step 5

Nesta seção é demonstrado o uso do comando ||, em resumo, o primeiro comando executado com sucesso faz com que os demais não precisem ser executados.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_ou.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 26:</strong> Comando ||</figcaption>
</p>

### 1.2. Laboratório 7

---
***Navigating the Filesystem***

Algumas seções são revisões de comandos já demonstrados em seções anteriores, apenas os comandos e informações novas foram colocados neste relatório, embora todas as atividades tenham sido feitas.

> 7.2.2 Step 2

Nesta seção é demonstrado o uso do comando `cd`. O comando cd (**C**hange **D**irectory) é utilizado para acessar diretorios a parti do diretorio atual, ou mudar o diretorio atual.  

A seguir, o comando cd é usado com um caminho para um diretório alterando o diretório atual.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_cd_pwd.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 27:</strong> Comando cd pwd</figcaption>
</p>

> 7.2.3 Step 3

O comando `cd` sem paramentro volta para o home.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_cd_pwd_h.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 28:</strong> Comando cd pwd</figcaption>
</p>

> 7.2.4 Step 4

Nesta seção, vemos o uso mais comum do comando `cd` utilizado para acessar um diretório específico.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/command_cd_home.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 29:</strong> Comando cd</figcaption>
</p>

> 7.2.5 Step 5

Nesta seção, vemos que o uso do comando `cd` com o ~ faz com que volte para o diretório home que está logado no termina, qualquer nome colocado depois do `~` formará um caminho absoluto para um diretório home diferente do padrão.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/cd_pwd.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 30:</strong> Comando cd</figcaption>
</p>

> 7.2.6 Step 6

Exemplo de uso do `~`:

Captura de tela:

<p style="text-align:center">
    <img src="imgs/7.2.6 Step 6.png" alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 31:</strong> Comando cd</figcaption>
</p>

7.2.10 Step 10 e 7.2.11 Step 11

Nesta seção veremos a diferença entre caminhos absolutos e relativos.

Nesta seção veremos a diferença entre caminhos absolutos e relativos.

Para acessar o caminho absoluto como, por exemplo: `cd /usr/share/doc`
Após entrar nesse diretório, caso queira acessar a pasta `bash`, ao invés de usar o caminho absoluto `cd /usr/share/doc/bash` basta digitar o caminho relativo `cd bash/`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.2.10-Step-10.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 32:</strong> Comando cd</figcaption>
</p>

> 7.2.12 Step 12

O argumento `..` é utilizado para subir um nível acima com o comando `cd`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.2.12-Step-12.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 33:</strong> Comando cd</figcaption>
</p>

> 7.2.13 Step 13

Outro uso do paramento `..` é para acessar caminhos relativos a parti de um diretório qualquer. Por exemplo, se fizermos de dentro do diretório `docs`acessar `dict` que está no mesmo nível de `docs`basta executar cd `../dict`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.2.13-Step-13.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 34:</strong> Comando cd</figcaption>
</p>

---
***Listing Files and Directories***

> 7.3.1 Step 1

Como já mencionado em seções anteriores, o comando `ls`serve para listar o conteúdo de um diretório.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.2.1-Step-1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 35:</strong> Comando ls</figcaption>
</p>

| Cor               | Tipo de Arquivo   |
| :--------------- | --------------- |
| Preto ou branco | Arquivo regular |
| Azul            | Arquivo de diretório|
| Cyan            | Arquivo de link simbólico (um arquivo que aponta para outro arquivo)|
| Verde           | Arquivo executável (um programa) |

> 7.3.2 Step 2

Nesta seção vemos o uso do argumento `-a`. O argumento `-a` junto do comando `ls`é utilizado para exibir também os arquivos ocultos.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.3.2-Step-2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 36:</strong> Comando ls</figcaption>
</p>

Os arquivos ocultos começam com `.`.

> 7.3.3 Step 3

Utilizando o argumento `-l` para exibir informações detalhadas sobre um arquivo.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.3.3-Step-3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 37:</strong> Comando ls</figcaption>
</p>

|  |   |
| :---------------------- | :---------------------- |
| `-` | O primeiro caractere, um - no exemplo anterior, indica que tipo de “arquivo” é esse. Um caractere - é para um arquivo simples, enquanto um caractere d seria para um diretório. |
|   `rw-r--r--`         | Isso representa as permissões do arquivo. As permissões serão discutidas em um laboratório posterior.|
|       `1`           | Isso representa algo chamado contagem de links físicos (discutido mais adiante). |
|     `root`            | O usuário proprietário do arquivo.|
|     `root`            | O proprietário do grupo do arquivo. |
|      `150`            | O tamanho do arquivo em bytes |
| `Jan 22 15:18`        | The date/time when the file was last modified. |

> 7.3.4 Step 4

Nesta seção vemos como acessar o conteúdo dos subdistritos dentro do diretório atual, para isso, acrescenta-se o argumento `-R` de recursivo.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.3.4-Step-4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 38:</strong> Comando ls</figcaption>
</p>

> 7.3.5 Step 5

Nesta seção, vemos como lista arquivos e diretórios que começam com a letra `s`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.3.5-Step-5.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 39:</strong> Comando ls</figcaption>
</p>

**OBS:** Observe que a opção `-d` impede que os arquivos dos subdiretórios sejam exibidos. Ela deve sempre ser usada com o comando ls quando você estiver usando globbing de arquivos.

> 7.3.6 Step 6

O caractere `?` pode ser usado para corresponder exatamente a um caractere em um nome de arquivo. Ou seja, se executamos um comando com 4 `?`será listado todos os arquivos com 4 caracteres.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.3.6-Step-6.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 40:</strong> Comando ls</figcaption>
</p>

> 7.3.7 Step 7

Utilizando o `[]`podemos exibir apenas os arquivos que começam com qualquer uma das letras contidas dentro de `[]`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/7.3.7-Step-7.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 40:</strong> Comando ls</figcaption>
</p>

### 1.3. Laboratório 8

---
***Managing Files and Directories***

> 8.2.1 Step 1

O asterisco * corresponde a “zero ou mais” caracteres em um nome de arquivo. Segui demonstrarei todos os casos de uso do `*` com outros comandos.

uso do `*`com o echo exibe todos os nomes dos diretórios.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.2.1-Step-1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 41:</strong> Comando echo</figcaption>
</p>

> 8.2.2 Step 2

Exibindo diretórios dentro do diretório atual que começam com as letras `D`e `P`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.2.2-Step-2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 42:</strong> Comando echo</figcaption>
</p>

> 8.2.3 Step 3

Nesta seção vemos o contrário da seção anterior, agora filtrando os arquivos terminados em `s`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.2.3-Step-3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 43:</strong> Comando echo</figcaption>
</p>

> 8.2.4 Step 4 a seção 8.2.9 Step 9

Nestas seção são explorados o uso de filtros e caracteres especiais para listar arquivos e diretórios, a imagem a segui demontrarar o uso de cada um deles.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.2.9-Step-9.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 44:</strong> Comando echo</figcaption>
</p>

**OBS:** O que decide quais letras vêm entre D e P é a sua posição na tabela [ASCII](https://www.ime.usp.br/~kellyrb/mac2166_2015/tabela_ascii.html), isso se aplica a qualquer intervalo, letras e letras, número e letras, caracteres especiais e letras.

---
***Copying, Moving and Renaming Files and Directories***

> 8.3.1 Step 1

Nesta seção vemos o uso do comando `cp` utilizado para realiza a copia de arquivos ou diretórios por linha de comando.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.3.1-Step-1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 45:</strong> Comando cp</figcaption>
</p>

> 8.3.2 Step 2

Agora irei repetir o processo utilizando a opção `-v`ou `-verbose` que tem por objetivo exibir o passo a passo do comando. Um novo comando é mostrado nesta seção `rm`que apaga o arquivo.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.3.2-Step-2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 46:</strong> Comando cp</figcaption>
</p>

```bash
# Source ---->--- Target
'/etc/hosts' -> 'hosts'
```

**OBS:** Se o diretório alvo não for determinado e `.`for colocado no lugar, o arquivo será copiado para o diretório local

> 8.3.4 Step 4

O uso do atributo `-p` faz com que durante uma copía os atributos do arquivo sejam presevados.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.3.4-Step-4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 47:</strong> Comando cp</figcaption>
</p>

> 8.3.6 Step 6

Para copia todos os arquivo de um diretorio, basta adicionar a flag `-R`. Nesta seção um novo comando foi apresentado o `mkdir`que cria um diretório.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.3.6-Step-6.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 48:</strong> Comando cp</figcaption>
</p>

> 8.3.7 Step 7

Para realizar a remoção de maneira recursiva, é apresentado a flag `-r`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.3.7-Step-7.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 49:</strong> Comando cp</figcaption>
</p>

> 8.3.8 Step 8

Nesta seção é apresentado o comando `mv` que é utilizado para mover arquivos e diretorios.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/8.3.8-Step-8.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 50:</strong> Comando mv</figcaption>
</p>

```bash
touch premove	# Creates an empty file called premove
mv              # premove postmove	This command “cuts” the premove file and “pastes” it to a file called postmove
rm              #postmove	Removes postmove file
```

### 1.4. Laboratório 17

---
***Ownerships and Permissions***

> **File Permissions**

Nas próximas seções, mostrarei a manipulação de permissões em arquivos.

> 17.2.1 Step 1

Preparação do ambiente de estudo.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.1-Step-1.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 51:</strong> Preparação do ambiente</figcaption>
</p>

> 17.2.2 Step 2

Listando as propriedades dos diretórios.

Uso do comando `ls -l`

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.2-Step-2.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 52:</strong> Preparação do ambiente</figcaption>
</p>

## Detalhando as Permissões (`-rw-rw-r--`)

O bloco de permissões é composto por **10 caracteres** e segue a seguinte ordem: `[Tipo][Usuário][Grupo][Outros]`.

### 1. Tipo de Arquivo (1º Caractere)

| Caractere | Tipo de Objeto |
| :---: | :--- |
| **`-`** | Arquivo regular. |
| **`d`** | Diretório (pasta). |
| **`l`** | Link simbólico. |
| **`c`** | Arquivo de dispositivo de caractere. |
| **`b`** | Arquivo de dispositivo de bloco. |
| **`...`** | Outros tipos menos comuns. |

### 2. Permissões do Usuário Dono (2º ao 4º Caractere)

Define as permissões para o usuário que é o dono do arquivo. Exemplo: `rw-`

| Caractere | Permissão | Significado |
| :---: | :--- | :--- |
| **`r`** (read) | Leitura | Permissão para ver o conteúdo do arquivo. |
| **`w`** (write) | Escrita | Permissão para modificar ou apagar o arquivo. |
| **`x`** (execute) | Execução | Permissão para executar o arquivo (ou acessar/entrar em um diretório). |
| **`-`** | Inativo | Indica que a permissão correspondente não está ativa. |

### 3. Permissões do Grupo Dono (5º ao 7º Caractere)

Define as permissões para todos os membros do grupo que é dono do arquivo. Exemplo: `rw-`

* Permissões de leitura, escrita e execução para os membros do grupo proprietário.

### 4. Permissões de Outros (8º ao 10º Caractere)

Define as permissões para todos os outros usuários do sistema (que não são nem o dono, nem membros do grupo dono). Exemplo: `r--`

* Permissões de leitura, escrita e execução para todos os demais usuários do sistema.

***

### Análise Completa: (`-rw-rw-r--`)

| Posição | Caracteres | Dono | Permissões | Significado |
| :---: | :---: | :---: | :---: | :--- |
| 1º | `-` | - | - | É um **arquivo regular**. |
| 2º-4º | `rw-` | Usuário | Leitura/Escrita | O **Usuário Dono** tem permissão de **leitura** e **escrita**. |
| 5º-7º | `rw-` | Grupo | Leitura/Escrita | O **Grupo Dono** tem permissão de **leitura** e **escrita**. |
| 8º-10º | `r--` | Outros | Leitura | **Outros Usuários** têm permissão apenas de **leitura**. |

> 17.2.3 Step 3

Nesta seção vemos como lista as propriedades dos arquivos ocultos com o comando `ls -la`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.3-Step-3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 53:</strong> Listando propriedades ocultas</figcaption>
</p>

> 17.2.4 Step 4

section, we see how to change the permissions of a directory with the `chmod` command.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.4-Step-4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 54:</strong> Alterando propriedades</figcaption>
</p>

Analisando o que foi alterado:

- Dono **(Usuário):** Continua sendo `sysadmin`.

- Dono **(Grupo):** Continua sendo `sysadmin`.

- **Permissões para o Usuário Dono (`sysadmin`):** Continuam sendo `rwx` (Total).

- **Permissões para o Grupo Dono (`sysadmin`):** Continuam sendo `rwx` (Total).

- ***Permissões para Outros Usuários (o resto do sistema):***
    - **Original:** O diretório tinha permissões de Leitura (`r`) e `Execução/Acesso (x)`. Isso significa que qualquer pessoa no sistema poderia listar o conteúdo e entrar no diretório.
    - **Alterado:** As permissões foram removidas para Outros, passando para `---`.

> 17.2.5 Step 5

Adicionando permissão de escrita para Outros usuários com o comando `chmod` utilizado para deixar o diretório "mais" público.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.5-Step-5.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 55:</strong> Alterando propriedades</figcaption>
</p>

> 17.2.6 Step 6

Utilizando o comando `chmod` para remover qualquer permissão do grupo e de outros usuários no arquivo `priv`:

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.6-Step-6.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 54:</strong> Alterando propriedades</figcaption>
</p>

> 17.2.7 Step 7

Concedendo a todos os usuários as mesmas permissões de leitura e escrita para o arquivo pub-file:

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.7-Step-7.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 54:</strong> Alterando propriedades</figcaption>
</p>

> 17.2.8 Step 8 -> 17.2.10 Step 10

Agora vamos analisar como criar e executar um arquivo `.sh`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.10-Step-10.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 55:</strong> Tornando arquivo executável</figcaption>
</p>

> 17.2.11 Step 11

Nesta seção veremos o comando `stat`que é utilizado para descrever as informações sobre o arquivo de maneira geral.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.2.11-Step-11.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 55:</strong> Detalhes</figcaption>
</p>

---
***File Ownership***

Nesta seção veremos o uso dos comandos `chown e chgrp`.

| Comando |	Uso Principal |	Descrição Simples |	Sintaxe Básica |
| -------- | -------- | -------- | -------- |
| `chown`	  |  Muda o DONO (Usuário) e/ou o Grupo. |	Usado para transferir a propriedade de um arquivo de um usuário para outro, e, opcionalmente, mudar seu grupo. | chown [NOVO_USUÁRIO]:[NOVO_GRUPO] [ARQUIVO]
| `chgrp` |	Muda apenas o GRUPO dono. |	Usado para atribuir um novo grupo de usuários como proprietário de um arquivo ou diretório.	| chgrp [NOVO_GRUPO] [ARQUIVO]

> 17.3.1 Step 1 -> 17.3.3 Step 3

Usando o comando `chown` para alterar o usuário e o grupo proprietarios do pub-dir para o usuario root e o grupo root

Captura de tela da parte inicial:

<p style="text-align:center">
    <img src=imgs/17.3.3-Step-31.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 56:</strong> Detalhes</figcaption>
</p>

Captura de tela da parte final:

<p style="text-align:center">
    <img src=imgs/17.3.3-Step-3.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 57:</strong> Detalhes</figcaption>
</p>

> 17.3.4 Step 4

Usando o comando `chown` para alterar o proprietário do arquivo pub para o usuário bin.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.3.4-Step-4.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 58:</strong> Comando chown</figcaption>
</p>

> 17.3.6 Step 6

Alterando o proprietário do grupo do `priv-dir` e do `priv-file` para o grupo de usuários recursivamente com o comando `chgrp` utilizando `-R`.

Captura de tela:

<p style="text-align:center">
    <img src=imgs/17.3.6-Step-6.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 59:</strong> Comando chgrp</figcaption>
</p>

### 1.5. Laboratório 11
