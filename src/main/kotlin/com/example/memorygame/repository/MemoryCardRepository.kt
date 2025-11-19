package com.example.memorygame.repository

import com.example.memorygame.model.MemoryCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query // <-- Importação necessária
import org.springframework.stereotype.Repository

@Repository
interface MemoryCardRepository : JpaRepository<MemoryCard, Long> {

    /**
     * Requisito: Fazer uma consulta (por tema)
     * Busca cartas onde o tema é EXATAMENTE IGUAL, ignorando maiúsculas.
     */
    fun findByThemeIgnoreCase(theme: String): List<MemoryCard>

    /**
     * Requisito: Fazer uma consulta (por nome)
     * Busca cartas onde o nome CONTÉM o texto, ignorando maiúsculas.
     */
    fun findByNameContainingIgnoreCase(name: String): List<MemoryCard>

    /**
     * Funcionalidade Extra: Buscar Temas Únicos
     * Usa uma consulta JPQL customizada para retornar apenas a lista
     * de nomes de temas distintos (ex: ["Pokémon", "Animais"]).
     */
    @Query("SELECT DISTINCT m.theme FROM MemoryCard m")
    fun findDistinctThemes(): List<String> // <-- Retorna Lista de Strings
}