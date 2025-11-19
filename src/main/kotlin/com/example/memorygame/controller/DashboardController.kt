package com.example.memorygame.controller

import com.example.memorygame.repository.MemoryCardRepository // Importe o seu repositório de cartas
import com.example.memorygame.repository.PlayerRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Uma classe simples para transportar os dados (DTO)
data class DashboardStats(
    val totalCards: Long,
    val totalPlayers: Long,
    val availableThemes: List<String>
)

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = ["*"])
class DashboardController(
    private val cardRepository: MemoryCardRepository, // Certifique-se que esse nome bate com o seu arquivo
    private val playerRepository: PlayerRepository
) {

    @GetMapping("/stats")
    fun getStats(): DashboardStats {
        val allCards = cardRepository.findAll()
        val allPlayers = playerRepository.count()

        // Pega todos os temas únicos das cartas cadastradas
        val themes = allCards.map { it.theme }.distinct()

        return DashboardStats(
            totalCards = allCards.size.toLong(),
            totalPlayers = allPlayers,
            availableThemes = themes
        )
    }
}