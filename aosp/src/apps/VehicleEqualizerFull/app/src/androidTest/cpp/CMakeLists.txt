cmake_minimum_required(VERSION 3.22.1)
project(VehicleEqualizerTests LANGUAGES CXX)

# Habilita C++17
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -static-libstdc++ -static-libgcc")

# Adiciona o GoogleTest com FetchContent (método moderno e confiável)
include(FetchContent)

FetchContent_Declare(
        googletest
        URL https://github.com/google/googletest/archive/refs/tags/release-1.12.1.zip
)

# Evita warnings no Android
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

# Inclui o diretório com os arquivos fontes nativos
include_directories(${CMAKE_SOURCE_DIR})

# Adiciona o executável de testes
add_executable(vehicleequalizer_tests
        test_native_lib.cpp
)

# Linka com o GTest e a lib nativa principal
target_link_libraries(vehicleequalizer_tests
        GTest::gtest
        GTest::gtest_main
        log
)

# Registra o teste para o CTest
enable_testing()
add_test(NAME vehicleequalizer_tests COMMAND vehicleequalizer_tests)


