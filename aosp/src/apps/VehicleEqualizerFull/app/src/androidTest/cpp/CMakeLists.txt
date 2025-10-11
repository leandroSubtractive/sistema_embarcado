cmake_minimum_required(VERSION 3.22.1)
project(vehicleequalizer_native_lib_tests LANGUAGES CXX)

# Diretório do código nativo principal
set(NATIVE_SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../main/cpp)

# Inclui o GTest (já presente no Android NDK)
include(FetchContent)
FetchContent_Declare(
        googletest
        URL https://github.com/google/googletest/archive/release-1.12.1.zip
)
FetchContent_MakeAvailable(googletest)

# Adiciona o executável de teste
add_executable(vehicleequalizer_native_lib_tests
        native_integration_test.cpp
        ${NATIVE_SRC_DIR}/native-lib.cpp
)

# Inclui diretórios
target_include_directories(vehicleequalizer_native_lib_tests PRIVATE
        ${NATIVE_SRC_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}
        ${gtest_SOURCE_DIR}/include
)

# Vincula bibliotecas necessárias
target_link_libraries(vehicleequalizer_native_lib_tests
        gtest
        gtest_main
        log
        android
)

# Define compilação C++17
set_target_properties(vehicleequalizer_native_lib_tests PROPERTIES
        CXX_STANDARD 17
        CXX_STANDARD_REQUIRED YES
)
