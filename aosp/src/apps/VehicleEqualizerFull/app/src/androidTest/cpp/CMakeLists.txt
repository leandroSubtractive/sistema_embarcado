cmake_minimum_required(VERSION 3.22.1)
project(vehicleequalizer_native_lib_tests LANGUAGES CXX)

# ----------------------
# Includes GTest (using FetchContent)
# ----------------------
include(FetchContent)
FetchContent_Declare(
        googletest
        URL https://github.com/google/googletest/archive/release-1.12.1.zip
)
FetchContent_MakeAvailable(googletest)

# ----------------------
# Test source files
# ----------------------
set(TEST_SRC
        EqualizerNativeTest.cpp
)

# ----------------------
# Add test executable
# ----------------------
add_executable(vehicleequalizer_native_lib_tests ${TEST_SRC})

# ----------------------
#  Include directories
# ----------------------
target_include_directories(vehicleequalizer_native_lib_tests PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}   # Para encontrar FakeJNIEnv.h
        ${gtest_SOURCE_DIR}/include
)

# ----------------------
# Required libraries
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
