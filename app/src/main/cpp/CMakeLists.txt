# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

#set(libs_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../app/src/main/jniLibs)

set(libs_DIR /Users/dmstrider/Documents/GitHub/tkeyandroid/app/src/main/jniLibs)

add_library(
        tkey
        STATIC
        IMPORTED
)

set_target_properties(
        tkey
        PROPERTIES
        IMPORTED_LOCATION ${libs_DIR}/${ANDROID_ABI}/libtkey.a
)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

add_library(
        native-lib SHARED
        jniCommon.cpp
        jniHash.cpp
)

find_library(
        log-lib
        log
)

target_include_directories(
        native-lib PRIVATE ${libs_DIR}/
)

target_link_libraries(
        native-lib
        android
        tkey
        ${log-lib}
        "-Wl,--allow-multiple-definition"
)