package com.example.memorygame.model

import jakarta.persistence.*

@Entity
@Table(name = "players")
data class Player(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val username: String,
    val email: String,
    var totalGamesPlayed: Int = 0
)