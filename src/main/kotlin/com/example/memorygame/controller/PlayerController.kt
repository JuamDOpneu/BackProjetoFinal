package com.example.memorygame.controller

import com.example.memorygame.model.Player
import com.example.memorygame.repository.PlayerRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = ["*"]) // Permite o front acessar
class PlayerController(private val playerRepository: PlayerRepository) {

    @GetMapping
    fun getAllPlayers(): List<Player> = playerRepository.findAll()

    @PostMapping
    fun createPlayer(@RequestBody player: Player): Player {
        return playerRepository.save(player)
    }
}