#!/bin/bash

# Script de implantação e teste para o aplicativo Vehicle Equalizer
# Este script automatiza a instalação, execução e coleta de logs do aplicativo em um dispositivo/emulador Android

# Caminho para o APK do seu aplicativo​

# find . -name "*.apk" 
APK_PATH="app/build/intermediates/apk/debug/app-debug.apk"

# TAG para filtrar logs no Logcat (usada no seu código Kotlin/Java)​
LOGCAT_TAG="VehicleEqualizerApp|AudioService|PlaybackModule"

# Nome do pacote do seu aplicativo (definido no AndroidManifest.xml)​
# Retona o comainho do pacote, caso esteja instalado.
PACKAGE_NAME=$(adb shell pm list packages | grep vehicleequalizer | sed 's/package://')

echo "============================================================"
echo "Iniciando script de implantação e teste do Equalizador..."
echo "============================================================"

# 1. Verificar se um dispositivo/emulador está conectado e online​
echo "Verificando dispositivos Android conectados..."
if ! adb devices | grep -q "device"; 
then
    echo "ERRO: Nenhum dispositivo ou emulador Android encontrado. "
    echo "Certifique-se de que um emulador esteja rodando ou um dispositivo esteja conectado e online (modo depuração USB ativado)."
exit 1
fi
echo "Dispositivo/emulador encontrado e online."

# 2. Desinstalar a versão anterior do aplicativo (se existir) para garantir uma instalação limpa​\
# Só tenta desinstalar se o pacote for encontrado
if [ -n "$PACKAGE_NAME" ]; then
    echo "Desinstalando versão anterior do aplicativo ($PACKAGE_NAME)..."
    adb uninstall $PACKAGE_NAME
else
    echo "Nenhum pacote encontrado com 'vehicleequalizer'."
fi

# 3. Instalar o novo APK​
echo "Instalando o APK: $APK_PATH..."
if [ ! -f "$APK_PATH" ]; then
echo "ERRO: APK não encontrado em $APK_PATH. Por favor, construa o
projeto primeiro (ex: ./gradlew assembleDebug)."
exit 1
fi
# Adição de -t para permitir a instalação de APKs de teste (debug)​
# Isso evita o erro: "Failure [INSTALL_FAILED_TEST_ONLY: install
adb install -t "$APK_PATH"

# Verifica o código de saída do comando 'adb install'​
if [ $? -ne 0 ]; then
echo "ERRO: Falha na instalação do APK. Verifique o caminho do APK e
as permissões."
exit 1
fi
echo "Aplicativo instalado com sucesso."

# 4. Iniciar o aplicativo (Activity principal)​
echo "Iniciando o aplicativo $PACKAGE_NAME..."
adb shell am start -n "$PACKAGE_NAME/.ui.MainActivity"

# # 5. Esperar um pouco para o aplicativo iniciar completamente e a UI carregar
echo "Aguardando 5 segundos para o aplicativo iniciar..."
sleep 5

# Verifica se está na tela de permissão e concede a permissão de notificações se necessário
echo "Verificando se a permissão de notificações precisa ser concedida..."
CURRENT_APP=$(adb shell dumpsys window | grep -E 'mCurrentFocus' | sed 's/.* //g')
if [[ "$CURRENT_APP" == *"com.google.android.permissioncontroller"* ]]; then
    adb shell pm grant "$PACKAGE_NAME" android.permission.POST_NOTIFICATIONS
else
    echo "A pemissão de notificações já foi concedida"
fi

# # 6. Limpar o Logcat para capturar apenas logs relevantes do teste atual​
# echo "Limpando Logcat..."
# adb logcat -c

# # 7. Simular interações básicas e capturar logs​
# echo "Simulando interação: Ativando e desativando o equalizador..."

# 7. Envia comando via adb para da play na musica via AudioService
echo "Iniciando o serviço de áudio para simular reprodução..."
adb shell am start-foreground-service -n $PACKAGE_NAME/.service.AudioService -a $PACKAGE_NAME.ACTION_PLAY
sleep 5 # Espera 5 segundos com a música tocando
echo "Parando a reprodução de áudio..."
adb shell am start-foreground-service -n $PACKAGE_NAME/.service.AudioService -a $PACKAGE_NAME.ACTION_STOP
sleep 2 # Espera 2 segundos para garantir que o serviço pare       

echo "Coletando logs relevantes do Logcat (TAG: $LOGCAT_TAG)..."
# Coleta logs da MainActivity e AudioService
adb logcat -v time -d | grep -E $LOGCAT_TAG > app_logs.txt
echo "Logs salvos em app_logs.txt no diretório do projeto."

# 8. Verificar se o aplicativo está rodando (opcional)​
echo "Verificando se o processo do aplicativo está ativo..."
if adb shell ps | grep -q $PACKAGE_NAME; then
    echo "Processo do aplicativo $PACKAGE_NAME está ativo."
else
    echo "Processo do aplicativo $PACKAGE_NAME NÃO está ativo."
fi

echo "============================================================"
echo "Script de implantação e teste concluído."
echo "Verifique 'app_logs.txt' para os logs coletados."
echo "============================================================"





