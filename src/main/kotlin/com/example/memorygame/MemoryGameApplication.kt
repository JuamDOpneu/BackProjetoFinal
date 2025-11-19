package com.example.memorygame

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemoryGameApplication

fun main(args: Array<String>) {
    runApplication<MemoryGameApplication>(*args)
}