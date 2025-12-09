cmake_minimum_required(VERSION 4.0)
project(ellipse)

set(CMAKE_CXX_STANDARD 20)

add_executable(ellipse main.cpp
        ellipse.cpp
        ellipse.h
        cercle.cpp
        cercle.h)
