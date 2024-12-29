# Customização do AOSP

Relatório descrevendo os passos utilizados para a customização do Android OSP.

## Objetivo

- Customizar o sistema operacional Android com funcionalidades emuladas para que seja
capaz de receber sinais da Controller Area Network (CAN);
- Reproduzir um fluxo CAN simulado através das funcionalidades criadas.

## Sumário

1.

tools/bazel run //common-modules/virtual-device:virtual_device_x86_64_dist
export AOSP_HOME="$HOME/Documents/Courses/Embedded_Systems/android-kernel"
launch_cvd --gpu_mode=gfxstream --resume=false -kernel_path=${AOSP_HOME}/out/android13-5.15/dist/bzImage -initramfs_path=${AOSP_HOME}/out/android13-5.15/dist/initramfs.img
BUILD_CONFIG=common-modules/virtual-device/build.config.virtual_device.x86_64 build/config.sh