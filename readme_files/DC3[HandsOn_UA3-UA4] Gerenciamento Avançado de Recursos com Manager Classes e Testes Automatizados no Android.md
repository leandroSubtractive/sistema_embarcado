# Gerenciamento Avançado de Recursos com Manager Classes e Testes Automatizados no Android

Para exemplificar o uso dos gerenciadores de recursos do Android, foi desenvolvido um aplicativo simples que demonstra o uso do `WindowManager` com a criação de uma tela sobreposta à tela do aplicativo, o `PackageManager` para listar aplicativos instalados no AVD e o `ActivityManager` para obter informações de processos no sistema. O aplicativo conta com uma tela inicial onde utiliza uma estrutura de `RecycleView` para listar as informações e a `TabLayout` para trocar a exibição das informações.

## Objetivo

- Explicar as funções principais das Manager Classes no gerenciamento de atividades no Android;
- Implementar a interação entre Manager Classes e System Services no Android
- Utilizar o Espresso e JUnit como ferramentas de automação de testes para componentes gerenciados pelas Manager Classes.

## Reposítorio

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

Link da Aplicação: [Resource Management](https://github.com/leandroSubtractive/sistema_embarcado/tree/devel/aosp/src/apps/resource_management)

## Sumário

1. [Ambiente de desenvolvimento](#1-ambiente-de-desenvolvimento)
    - 1.1. [Sistema Operacional](#11-sistema-operacional)
    - 1.2. [Java(JDK)](#12-javajdk)
    - 1.3. [Android Studio](#13-android-studio)
    - 1.4. [Git](#14-git)
    - 1.5. [Emulador Android](#15-emulador-android)
2. [Resultado da Atividade](#2-resultado-da-atividade)
    - 2.1. [Estrutura de pastas do repositório](#21-estrutura-de-pastas-do-repositório)
    - 2.2. [Implementação](#22-implementação)
        - 2.2.1. [Ciclo de Vida da Activity](#221-ciclo-de-vida-da-activity)
        - 2.2.2. [Gerenciadores de Recursos](#222-gerenciadores-de-recursos)
            - 2.2.2.1. [PackageManager](#2221-packagemanager)
            - 2.2.2.2. [ActivityManager](#2222-activitymanager)
            - 2.2.2.3. [WindowManager](#2223-windowmanager)
                - 2.2.2.3.1. [Permissões](#22231-permissões)
        - 2.2.3. [Layout](#223-layout)
3. [Testes](#3-android-test)
    - 3.1 [Casos de Teste](#31-casos-de-teste)
    - 3.2 [Resultados](#32-resultados)

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

Android Studio Meerkat | 2024.3.1
Build #AI-243.22562.218.2431.13114758, built on February 24, 2025
Runtime version: 21.0.5+-12932927-b750.29 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Toolkit: sun.awt.X11.XToolkit
Linux 6.8.0-52-generic
GC: G1 Young Generation, G1 Concurrent GC, G1 Old Generation
Memory: 2968M
Cores: 12
Registry:
  ide.experimental.ui=true
  i18n.locale=
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

O Emulador Android utilizado é o mesmo da atividade anterior, não foi realiza que já foi entregue e descrita na sessão [1.5 Emulador Android](https://github.com/leandroSubtractive/sistema_embarcado/blob/dc-3-ua3-ua4-hands-on/readme_files/AIDL_dc3_u1_u2.md#15-emulador-android) do relatório [Interface e Gerenciamento de Serviços no Android](https://github.com/leandroSubtractive/sistema_embarcado/blob/dc-3-ua3-ua4-hands-on/docs/reports/AIDL_dc3_u1_u2.pdf).

## 2. Resultado da Atividade

### 2.1 Estrutura de pastas do repositório

```bash
.
├── aosp
│   ├── kernel # Arquivos de configuração e patch do kernel do Android
│   └── src # Aplicativos desenvolvidos durante o curso
│       └── apps # Aplicativos desenvolvidos até o momento
│           ├── AIDL_Interface
│           ├── audio_equalizer
|           └── resource_management # Projeto Hands on UA3 e UA4 da disciplina: Gerenciamento Avançado de Recursos com Manager Classes e Testes Automatizados no Android
|               ├── app
|               ├── build
|               ├── build.gradle.kts
|               ├── .gitignore
|               ├── .gradle
|               ├── gradle
|               ├── gradle.properties
|               ├── gradlew
|               ├── gradlew.bat
|               ├── .idea
|               ├── local.properties
|               └── settings.gradle.kts
├── docs
│   └── reports # Todos os relatórios no formato PDF
├── readme_files # Todos os relatórios escritos em markdown
│   ├── AIDL_dc3_u1_u2.md
│   ├── android_application_dc2_p1.md
│   ├── aosp_customization.md
│   ├── environmental_preparation.md
│   └── imgs
├── README.md # LEIA-ME principal do repositório
└── .vscode
    └── settings.json

```

O Caminho do projeto desenvolvido conforme a estrutura de pastas acima é [Resource Management](https://github.com/leandroSubtractive/sistema_embarcado/tree/dc-3-ua3-ua4-hands-on/aosp/src/apps/resource_management) `/aosp/src/apps/resource_management`. Todos os arquivos do projeto do `android studio` estão dentro desta pasta.

### 2.2 Implementação

Conforme descrito no início do relatório, este aplicativo demonstra o uso de 3 gerenciadores de recursos do Android de maneira direta, sendo estes `PackageManager`, `ActivityManager` e `WindowManager`. As imagens abaixo apresentam a interface do aplicativo e posteriormente descreverei em detalhes a implementação e utilização dos recursos.

<p style="text-align:center">
    <img src=imgs/resource_manager_tab1.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 3:</strong> Tela inicial do aplicativo na tab de lista de Apps</figcaption>
</p>

<p style="text-align:center">
    <img src=imgs/resource_manager_tab2.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 4:</strong> Tela do aplicativo na tab de lista de processos</figcaption>
</p>

A implementação do designer da tela foi realizada utilizando o [TabLayout](https://developer.android.com/reference/com/google/android/material/tabs/TabLayout) em conjunto com o widget [ViewPager2](https://developer.android.com/reference/kotlin/androidx/viewpager2/widget/ViewPager2) onde foi adicionado como elemento de exibição uma [RecyclerView](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView).

Uma das principais vantagens de se utilizar uma `recyclerView` para esse caso de uso é a sua flexibilidade e o baixo consumo de memória devido ao seu gerenciamento eficiente das Views.

#### 2.2.1 Ciclo de Vida da Activity

O fluxo da Figura 5 apresenta o ciclo de vida de uma Activity de maneira simplificada.

<p style="text-align:center">
    <img src=imgs/activity_lifecycle.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 5:</strong> Ciclo de vida de uma Activity</figcaption>
</p>

Na Figura 6 temos os estados do ciclo de vida da nossa MainActivity, foi dada ênfase apenas nos estados que têm ação direta, os demais apenas log para fins de acompanhamento. Aqui é descrita a inicialização dos principais componentes da interface, nas próximas sessões irei explorar onde estão sendo utilizados os gerenciadores de recursos que citei anteriormente dentro do aplicativo.

<p style="text-align:center">
    <img src=imgs/mainActivity.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 6:</strong> Tela do aplicativo na tab de lista de processos</figcaption>
</p>

A imagem abaixo mostra os logs sendo impressos no terminal, mostrando em que estado está a Activity durante sua execução.

<p style="text-align:center">
    <img src=imgs/activity_states.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 7:</strong> Logs de execução</figcaption>
</p>

#### 2.2.2 Gerenciadores de Recursos

#### 2.2.2.1 PackageManager

O `PackageManager` foi utilizado para alimentar a `Tab` de lista de aplicativos, ele foi instanciado na classe `Appinfo` no método `GetAllAppsInfo()`.

```java
public class AppInfo {

    /**
     * Gets list of installed applications
     * <p>
     * @param context Context of the activity
     * @return Returns list of installed applications
     */
    @NonNull
    public static List<ApplicationInfo> GetAllAppsInfo(@NonNull Context context){
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getInstalledApplications(packageManager.GET_META_DATA);
    }
...
```

Este método retorna uma lista do tipo `ApplicationInfo` utilizada no método sobreposto (originado da classe Fragment) `onViewCreated` na classe `AppsTab` que estende `Fragment` e representa um fragmento da Tab.

```java
public class AppsTab extends Fragment {
    ...
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ApplicationInfo> allAppsInstalled = AppInfo.GetAllAppsInfo(getContext());
        AppViewAdapter appViewAdapter = new AppViewAdapter(getContext(), allAppsInstalled);
        recyclerView.setAdapter(appViewAdapter);

    }
}
```

Neste mesmo método também é instanciado a `RecycleView` que define seu adaptador (`AppViewAdapter`) que faz parte da sua implementação.

Aqui está o adaptador da `RecycleView` que exibe a lista de aplicativos. Os métodos apresentados são os métodos mínimos da classe estendida (`RecyclerView.Adapter<AppViewAdapter.AppViewHolder>`) para o correto funcionamento da `RecycleView`.

```java
public class AppViewAdapter extends RecyclerView.Adapter<AppViewAdapter.AppViewHolder> {

    private final List<ApplicationInfo> appList;
    private final PackageManager packageManager;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class AppViewHolder extends RecyclerView.ViewHolder {
        private final ImageView appIcon;
        private final TextView AppName;

        public AppViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.iconView);
            AppName = itemView.findViewById(R.id.appTextView);
        }

...

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        ApplicationInfo appInfo = appList.get(position);
        // contents of the view with that element
        holder.getAppName().setText(appInfo.loadLabel(packageManager));
        holder.getAppIcon().setImageDrawable(appInfo.loadIcon(packageManager));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appList.size();
    }
...    
```

#### 2.2.2.2 ActivityManager

A segunda Tab, que exibe uma `RecycleView` de processos, tem uma estrutura parecida com a de lista de aplicativos mostrada na seção anterior, só que neste caso é utilizado o `ActivityManager` para obter informações sobre os processos. Na classe `ProcessInfo` o método `GetAllProcessInfo()` retorna uma lista do tipo  
`ActivityManager.RunningAppProcessInfo` obtida com o activityManager conforme trecho abaixo.

```java
public class ProcessInfo {

    /**
     * Gets all the processes of the application that is running
     * <p>
     * @param context Context of the activity
     * @return Returns list of processes
     */
    @NonNull
    public static List<ActivityManager.RunningAppProcessInfo> GetAllProcessInfo(@NonNull Context context){
       ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       return activityManager.getRunningAppProcesses();
    }
}
```

Este método é chamado na classe `ProcessTab` que é exatamente igual a `AppsTab`, o adaptador do `ProcessTab` é implementado na classe `ProcessViewAdapter` que é semelhante a `AppViewAdapter` sendo diferente apenas como a View organiza as informações exibidas.

#### 2.2.2.3 WindowManager

O `WindowManager` foi utilizado para exibir uma tela sobreposta à tela do aplicativo com o total de aplicativos instalados. Como pode ser visto na Figura 8, a interface possui um botão no canto inferior que, quando pressionado, exibe a tela flutuante.

<p style="text-align:center">
    <img src=imgs/floatingView.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 8:</strong> Tela Sobreposta</figcaption>
</p>

A instância do `windowManger` é obtida no construtor `FloatingScreen()` da classe `FloatingScreen` assim como também é instanciada a tela sobreposta a uma View conforme trecho abaixo.

```java
...
public class FloatingScreen {
    private WindowManager windowManager;
    private LayoutInflater inflater;
    private WindowManager.LayoutParams params;
    private View infoFloatingView;
    private TextView textView;
    private Context context;

    public FloatingScreen(@NonNull Context context) {
        // Gets an instance of WindowManager to manage the floating window.
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // Gets a LayoutInflater to inflate the layout of the floating window.
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        infoFloatingView = inflater.inflate(R.layout.info_window, null);
        textView = infoFloatingView.findViewById(R.id.infoView);

        // Creates the layout parameters for the floating window
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, // Window width adjusted to content
                WindowManager.LayoutParams.WRAP_CONTENT, // Window height adjusted to content
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Layout type for overlay
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Prevents the window from receiving incoming focus
                PixelFormat.TRANSLUCENT); // Makes the background of the window translucent

        // Sets the gravity of the floating window to center it on the screen
        params.gravity = Gravity.CENTER;

        this.context = context;
    }
...
```

Quando o botão é pressionado, primeiro é feita uma verificação de permissão, caso não tenha, a mesma é solicitada ao usuário, após essa etapa a tela é adicionada à View e exibida conforme trecho abaixo.

```java
public void showInfoWindow() {

    // Gets and set the total number of apps on the floating screen
    textView.setText("Total Apps Installed: " + GetTotalAppsInstall(this.context));

    // Add the floating window to the screen using WindowManager
    windowManager.addView(infoFloatingView, params);
}
```

#### 2.2.2.3.1 Permissões

Para que o `WindowManager` consiga exibir corretamente a tela sobreposta é necessária a permissão
`<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />`. Esta permissão precisa ser adicionada ao arquivo `AndroidManifest.xml` conforme trecho abaixo.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
...
```

Por se tratar de uma permissão crítica, não basta apenas acrescentar ao arquivo de manifesto, é necessário ser concedida em tempo de execução. O trecho abaixo exibe o método responsável pela solicitação, o método é chamado toda vez que o botão é pressionado. Caso a permissão já tenha sido concedida, apenas exibe a tela, caso contrario requisita ação do usuário.

```java
private void requestOverlayPermission() {
    if (!Settings.canDrawOverlays(this)) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        // starts an Activity that will ask the user for permission.
        overlayPermissionLauncher.launch(intent);
    } else {
        // Permission already granted, create floating window
        floatingScreen.showInfoWindow();
    }
}
```

```java
private final ActivityResultLauncher<Intent> overlayPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (Settings.canDrawOverlays(MainActivity.this)) {
                        floatingScreen.showInfoWindow();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission denied!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
```

Quando a permissão ainda não foi concedida, a tela da permissão da figura 9 é exibida, basta entrar no aplicativo `Resource Management` para conceder.

<p style="text-align:center">
    <img src=imgs/permission_1.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 9:</strong> Permissão parte 1</figcaption>
</p>

Na tela da figura 10, basta conceder e voltar ao aplicativo.

<p style="text-align:center">
    <img src=imgs/permission_2.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 10:</strong> Permissão parte final</figcaption>
</p>

#### 2.2.3 Layout

Todos os arquivos .xml de Layout utilizados estão definidos na captura de tela da figura abaixo.

<p style="text-align:center">
    <img src=imgs/layout.png alt style="width:70%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 10:</strong> Layout</figcaption>
</p>

## 3. Android Test

### 3.1 Casos de Teste

Nesta atividade, foram implementados 3 casos de testes instrumentados.

Neste primeiro caso de teste, é verificado se, quando a Tab é selecionada, a sua respectiva Recycle View é exibida.

```java
    /**
     * Check that the RecycleView of the corresponding tab is displayed
     */
    @Test
    public void recyclerViewAppsIsDisplayed() {
        // Select the Tab by name, and check that the right RecycleView is appearing
        onView(withText(tabsName[0])).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewApps))
                    .check(matches(isDisplayed()));

    }
```

Neste outro, é realizado o mesmo procedimento, só que para a Tab de processos.

```java
    /**
     * Check that the RecycleView of the corresponding tab is displayed
     */
    @Test
    public void recyclerViewProcessIsDisplayed() {
        // Select the Tab by name, and check that the right RecycleView is appearing
        onView(withText(tabsName[1])).perform(click());
        onView(withText(tabsName[1])).check(matches(isDisplayed()));

        onView(withId(R.id.recyclerViewProcess))
                .check(matches(isDisplayed()));
    }
```

Já neste último caso, é feita uma verificação se os componentes da tela estão visíveis após a inicialização.

```java
    /**
     * Check that the screen components are showing up
     */
    @Test
    public void screenComponentsIsDisplayed() {
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.tabLayout)).check(matches(isDisplayed()));
    }
```

### 3.2 Resultados

<p style="text-align:center">
    <img src=imgs/TestResult.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 11:</strong> Resultados dos Testes</figcaption>
</p>
