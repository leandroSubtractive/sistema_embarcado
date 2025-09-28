# Customização do AOSP

Relatório descrevendo os passos utilizados para a customização do Android OSP.

## CURSO

FORMAÇÃO EM SISTEMAS EMBARCADOS

LEANDRO MENDES DOS SANTOS

## Links

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

## Objetivos De Aprendizagem

- Configurar um dispositivo virtual, como o "Pixel 5", com a API adequada e personalizar
configurações, como resolução e armazenamento, para garantir simulações precisas
- Definir os principais subsistemas do sistema embarcado, como a interface do usuário, além de
desenvolver um diagrama claro que mostre os fluxos de dados e interações entre os
subsistemas.

## Sumário

1.

tools/bazel run //common-modules/virtual-device:virtual_device_x86_64_dist
export AOSP_HOME="$HOME/Documents/Courses/Embedded_Systems/android-kernel"
launch_cvd --gpu_mode=gfxstream --resume=false -kernel_path=${AOSP_HOME}/out/android13-5.15/dist/bzImage -initramfs_path=${AOSP_HOME}/out/android13-5.15/dist/initramfs.img
BUILD_CONFIG=common-modules/virtual-device/build.config.virtual_device.x86_64 build/config.sh