cmake_minimum_required(VERSION 3.25)
project(native)

set(CMAKE_CXX_STANDARD 11)

include_directories(.)
include_directories("C:/Program Files/Java/jdk-17.0.3.1/include")
include_directories("C:/Program Files/Java/jdk-17.0.3.1/include/win32")

add_library(CPUWatchDog SHARED
        CPUWatchDog.cpp
        run_halo_liquid_utils_CPUWatchDog.h)

target_link_libraries(CPUWatchDog pdh pthread)
set_target_properties(CPUWatchDog PROPERTIES
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../resources/native"
        LIBRARY_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../resources/native")
