# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        ffplay-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ffplay.c
        cmdutils.c
        NativeFfplayer.cpp
        JniRuntime.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# FFmpeg include 文件
include_directories(${CMAKE_SOURCE_DIR}/include)
# SDL2 include 文件
include_directories(${CMAKE_SOURCE_DIR}/include/sdl2)
#  设置native库路径
set(NATIVE_LIBS_DIR ${CMAKE_SOURCE_DIR}/../../../libs/armeabi-v7a)
# 编解码库
add_library(
        avcodec-lib
        SHARED
        IMPORTED
)
set_target_properties(
        avcodec-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libavcodec.so )
# 滤镜库 暂时没用上
add_library(
        avfilter-lib
        SHARED
        IMPORTED
)
set_target_properties(
        avfilter-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libavfilter.so )
# 文件格式库 大部分操作都需要这个库
add_library(
        avformat-lib
        SHARED
        IMPORTED
)
set_target_properties(
        avformat-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libavformat.so
)
# 工具库
add_library(
        avutil-lib
        SHARED
        IMPORTED
)
set_target_properties(
        avutil-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libavutil.so
)
# 重采样库 主要用于音频的转换
add_library(
        swresample-lib
        SHARED
        IMPORTED
)
set_target_properties(
        swresample-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libswresample.so
)
# 视频格式转换库 主要用于视频的转换
add_library(
        swscale-lib
        SHARED
        IMPORTED
)
set_target_properties(
        swscale-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libswscale.so
)

# avdevice lib
add_library(
        avdevice-lib
        SHARED
        IMPORTED
)
set_target_properties(
        avdevice-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libavdevice.so
)

# sdl lib
add_library(
        sdl2-lib
        SHARED
        IMPORTED
)
set_target_properties(
        sdl2-lib
        PROPERTIES IMPORTED_LOCATION
        ${NATIVE_LIBS_DIR}/libSDL2.so
)


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        ffplay-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        android
        avcodec-lib
        avfilter-lib
        avformat-lib
        avutil-lib
        swresample-lib
        swscale-lib
        OpenSLES
        avdevice-lib
        sdl2-lib)