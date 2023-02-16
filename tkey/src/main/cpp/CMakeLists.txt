# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

project(tKey)

set(libs_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs)

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
        tkey-native SHARED
        common/jniCommon.cpp
        jniGenerateShareStoreResult.cpp
        jniKeyDetails.cpp
        common/jniKeyPoint.cpp
        jniServiceProvider.cpp
        common/jniShareStore.cpp
        jniShareStoreMap.cpp
        jniShareTransferStore.cpp
        jniStorageLayer.cpp
        jniServiceProvider.cpp
        jniThresholdKey.cpp
        jniVersion.cpp
        jniKeyReconstructionDetails.cpp
        jniLocalMetadataTransitions.cpp
        jniShareStorePolyIdIndexMap.cpp
        jniMetadata.cpp
        common/jniPrivateKey.cpp
        modules/jniSecurityQuestionModule.cpp
        modules/jniPrivateKeysModule.cpp
        modules/jniShareTransferModule.cpp
        modules/jniSeedPhraseModule.cpp
        modules/jniShareSerializationModule.cpp
)

find_library(
        log-lib
        log
)

target_include_directories(
        tkey-native PRIVATE ${libs_DIR}/
)

target_link_libraries(
        tkey-native
        android
        tkey
        ${log-lib}
        "-Wl,--allow-multiple-definition"
)