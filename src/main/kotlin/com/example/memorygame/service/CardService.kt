package com.example.memorygame.service

import com.example.memorygame.model.MemoryCard

/**
 * Requisito: Usar elementos de polimorfismo.
 * Esta interface define o contrato para nosso serviço.
 * O Controller (próxima etapa) vai usar esta interface,
 * sem saber qual implementação está por trás dela.
 */
interface CardService {
    fun findAll(): List<MemoryCard>
    fun findById(id: Long): MemoryCard?
    fun search(name: String?, theme: String?): List<MemoryCard>
    fun save(card: MemoryCard): MemoryCard
    fun update(id: Long, card: MemoryCard): MemoryCard?
    fun delete(id: Long): Boolean
    fun findDistinctThemes(): List<String>
}