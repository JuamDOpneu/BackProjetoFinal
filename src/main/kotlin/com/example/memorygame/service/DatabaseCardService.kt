package com.example.memorygame.service

import com.example.memorygame.model.MemoryCard
import com.example.memorygame.repository.MemoryCardRepository
import org.springframework.stereotype.Service

@Service
class DatabaseCardService(
    private val repository: MemoryCardRepository // Injeção de dependência
) : CardService { // Implementa a interface

    override fun findAll(): List<MemoryCard> = repository.findAll()

    override fun findById(id: Long): MemoryCard? = repository.findById(id).orElse(null)

    /**
     * Requisito: Busca/Filtro.
     */
    override fun search(name: String?, theme: String?): List<MemoryCard> {
        return when {
            !name.isNullOrBlank() -> repository.findByNameContainingIgnoreCase(name)

            // ✅ A CORREÇÃO ESTÁ AQUI:
            // Trocamos 'findByThemeContainingIgnoreCase' pelo novo nome 'findByThemeIgnoreCase'
            !theme.isNullOrBlank() -> repository.findByThemeIgnoreCase(theme)

            else -> repository.findAll()
        }
    }

    /**
     * Requisito: Salvar entidade
     */
    override fun save(card: MemoryCard): MemoryCard = repository.save(card)

    /**
     * Requisito: Editar entidade
     */
    override fun update(id: Long, card: MemoryCard): MemoryCard? {
        if (!repository.existsById(id)) {
            return null
        }

        val cardToUpdate = MemoryCard(
            name = card.name,
            theme = card.theme,
            imageUrl = card.imageUrl
        ).apply {
            this.id = id // Define o ID da entidade existente
        }

        return repository.save(cardToUpdate)
    }

    /**
     * Requisito: Excluir entidade
     */
    override fun delete(id: Long): Boolean {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            true
        } else {
            false
        }
    }

    /**
     * Funcionalidade Extra: Buscar Temas Únicos
     */
    override fun findDistinctThemes(): List<String> {
        return repository.findDistinctThemes()
    }
}