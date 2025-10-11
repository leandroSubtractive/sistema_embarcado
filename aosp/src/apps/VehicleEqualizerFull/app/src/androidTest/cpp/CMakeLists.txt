cmake_minimum_required(VERSION 3.22.1)
project(vehicleequalizer_native_lib_tests LANGUAGES CXX)

# ----------------------
# Inclui o GTest (usando FetchContent)
# ----------------------
include(FetchContent)
FetchContent_Declare(
        googletest
        URL https://github.com/google/googletest/archive/release-1.12.1.zip
)
FetchContent_MakeAvailable(googletest)

# ----------------------
# Arquivos fonte do teste
# ----------------------
set(TEST_SRC
        EqualizerNativeTest.cpp
)

# ----------------------
# Adiciona executável do teste
# ----------------------
add_executable(vehicleequalizer_native_lib_tests ${TEST_SRC})

# ----------------------
# Diretórios de include
# ----------------------
target_include_directories(vehicleequalizer_native_lib_tests PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}   # Para encontrar FakeJNIEnv.h
        ${gtest_SOURCE_DIR}/include
)

# ----------------------
# Bibliotecas necessárias
# ----------------------
target_link_libraries(vehicleequalizer_native_lib_tests
        gtest
        gtest_main
        log
        android
)

# ----------------------
# Define C++17
# ----------------------
set_target_properties(vehicleequalizer_native_lib_tests PROPERTIES
        CXX_STANDARD 17
        CXX_STANDARD_REQUIRED YES
)
