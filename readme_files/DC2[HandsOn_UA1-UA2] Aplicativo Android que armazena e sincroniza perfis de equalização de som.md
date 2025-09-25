# Aplicativo Android que armazena e sincroniza perfis de equalização de som

Relatório de implementação de aplicativo Android para gerenciamento de perfis de audio.

## Links

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

Link da Aplicação: [VehicleEqualizerApp](https://github.com/leandroSubtractive/sistema_embarcado/tree/devel/aosp/src/apps/VehicleEqualizerApp)

Link do Vídeo: [Vídeo](https://drive.google.com/drive/folders/1xURd7VnulzM-5QFO18Kk7mN_jLk9DK0C)

## Objetivos De Aprendizagem

- Explicar o ciclo de vida das Activities;
- Configurar corretamente as permissões de segurança no arquivo Android Manifest;
- Utilizar princípios de Programação Orientada a Objetos no desenvolvimento Android;
- Integrar componentes de interface gráfica com a classe Java ou Kotlin;
- Configurar Intents para navegação entre Activities.

## Introdução

Em um contexto geral, o sistema de áudio de um veículo se torna o principal meio de entretenimento para o condutor durante o seu período de permanência no veículo, seja ouvindo músicas, notícias ou até mesmo realizando chamadas via Bluetooth. Assim como existe gosto para comida, também existe gosto para músicas e dentro do gosto para músicas, existem aqueles mais exigentes que gostam de manipular o áudio. A principal ferramenta para isso em um infotainment de um veículo é o equalizador, que, por ajustes em determinadas frequências, permite ajustar a intensidade de graves, médios e agudos, por exemplo.

O problema que isso gera é que nem sempre aquela equalização agrada a todos. Se pensamos em um veículo compartilhado por mais de um condutor e cada um tendo um gosto musical diferente, toda vez que um deles entrasse no carro para ouvir música, seria necessário refazer a equalização para o seu perfil pessoal. Além disso, existem aqueles que, para cada estilo musical, mudam a equalização para melhor agradar seus ouvidos.

## Objetivo

A principal função deste aplicativo é permitir a customização das configurações de equalização por diversos usuários ou até mesmo tipos diferentes de equalização do mesmo usuário conforme o gênero de música que esteja ouvindo. Ou seja, a ideia é que o aplicativo permita salvar diferentes perfis de equalização de acordo com o gosto do condutor do veículo.

## Sumário

- [1. Ciclo De Vida das Activities](#1-cilco-de-vida-das-activities)
- [2. Descrição Da Solução](#2-descrição-da-solução)

## 1. Ciclo De Vida das Activities

No desenvolvimento de aplicativos Android, a `Activity` é um componente fundamental que representa uma única tela de interface do usuário. Seu comportamento e estado são controlados pelo `ciclo de vida da Activity`, que é gerenciado por um conjunto de callbacks que o sistema executa em momentos específicos (como quando a tela é criada, iniciada ou destruída)[[1]](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=pt-br#java)[[2]](https://www.alura.com.br/artigos/activity-lifecycle-por-que-conhecer-ciclo-de-vida-activity?srsltid=AfmBOoq_IdyHgJK3VRbXPUxNUXCgH_amOGegJ6gmiI2nsLbdlPUtpyuP).

A figura 1 abaixo apresenta o fluxo do ciclo de vida de uma activity.

<p style="text-align:center">
    <img src=imgs/activity_lifecycle.png alt style="width:65%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 1:</strong> Ciclo de vida de uma Activity</figcaption>
</p>

Analisando a imagem anterior, temos os seguintes estados:

- **onCreate()**
    - A Activity é criada. É o ponto de entrada único para o ciclo de vida. Aqui, ocorre a inicialização da interface do usuário (UI) e os componentes essenciais.
- **onStart()**
    - A Activity se torna visível para o usuário.
- **onResume()**
    - A Activity está em primeiro plano e pronta para a interação do usuário. Este é o estado de "execução" da Activity.
- **onPause()**
    - A Activity está parcialmente visível, mas não está mais em primeiro plano. Este estado é chamado quando outra Activity aparece por cima (como um diálogo) ou quando o usuário está prestes a sair dela.
- **onStop()**
    - A Activity não está mais visível para o usuário.
- **onDestroy()**
    - A Activity está sendo destruída e finalizada. É o último estado do ciclo de vida, onde todos os recursos devem ser liberados.
- **onRestart()**
    - A Activity que estava em estado de `onStop()` é chamada para ser reiniciada, passando para o estado de `onStart()` em seguida.

## 2. Descrição Da Solução



## 3. Tecnologias Abordadas

## 4. Conclusão

## 5. Referências

- 1 [Ciclo de vida da atividade](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=pt-br#java)
- 2 [Ciclo de vida das Activities](https://www.alura.com.br/artigos/activity-lifecycle-por-que-conhecer-ciclo-de-vida-activity?srsltid=AfmBOoq_IdyHgJK3VRbXPUxNUXCgH_amOGegJ6gmiI2nsLbdlPUtpyuP)
