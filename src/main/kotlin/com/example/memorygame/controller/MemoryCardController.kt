package com.example.memorygame.controller

import com.example.memorygame.model.MemoryCard
import com.example.memorygame.repository.MemoryCardRepository
import com.example.memorygame.service.FileStorageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = ["*"]) // Permite que o React (localhost:5173) acesse o Backend
class MemoryCardController(
    private val repository: MemoryCardRepository,
    private val fileStorageService: FileStorageService
) {

    // 1. Listar todas as cartas (com filtro opcional de tema)
    @GetMapping
    fun getAllCards(@RequestParam(required = false) theme: String?): ResponseEntity<List<MemoryCard>> {
        val cards = if (theme.isNullOrBlank()) {
            repository.findAll()
        } else {
            // Filtra na memória (ou crie um método findByThemeIgnoreCase no repository se preferir)
            repository.findAll().filter { it.theme.equals(theme, ignoreCase = true) }
        }
        return ResponseEntity.ok(cards)
    }

    // 2. Pegar carta por ID (Necessário para carregar os dados na tela de Edição)
    @GetMapping("/{id}")
    fun getCardById(@PathVariable id: Long): ResponseEntity<MemoryCard> {
        return repository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    // 3. Listar Temas Únicos (Para o Dropdown do Frontend)
    @GetMapping("/themes")
    fun getDistinctThemes(): ResponseEntity<List<String>> {
        val themes = repository.findAll()
            .map { it.theme }
            .distinct() // Remove duplicados
            .sorted()   // Ordena alfabeticamente
        return ResponseEntity.ok(themes)
    }

    // 4. CRIAR Carta (Recebe Arquivo + Dados via FormData)
    @PostMapping(consumes = ["multipart/form-data"])
    fun createCard(
        @RequestParam("name") name: String,
        @RequestParam("theme") theme: String,
        @RequestParam("image") image: MultipartFile // Arquivo Obrigatório na criação
    ): ResponseEntity<MemoryCard> {

        // Salva o arquivo físico na pasta 'uploads'
        val imagePath = fileStorageService.storeFile(image)

        // Gera a URL completa para salvar no banco
        val fullUrl = "http://localhost:8080$imagePath"

        val newCard = MemoryCard(
            name = name,
            theme = theme,
            imageUrl = fullUrl
        )

        return ResponseEntity.ok(repository.save(newCard))
    }

    // 5. EDITAR Carta (Imagem é Opcional)
    @PutMapping("/{id}", consumes = ["multipart/form-data"])
    fun updateCard(
        @PathVariable id: Long,
        @RequestParam("name") name: String,
        @RequestParam("theme") theme: String,
        @RequestParam(value = "image", required = false) image: MultipartFile? // Arquivo Opcional
    ): ResponseEntity<MemoryCard> {

        val existingCard = repository.findById(id).orElseThrow { RuntimeException("Carta não encontrada") }

        var newImageUrl = existingCard.imageUrl

        // Lógica: Só substitui a imagem se o usuário enviou uma nova
        if (image != null && !image.isEmpty) {
            val imagePath = fileStorageService.storeFile(image)
            newImageUrl = "http://localhost:8080$imagePath"
        }

        // Cria objeto atualizado mantendo os dados antigos onde necessário
        val updatedCard = existingCard.copy(
            name = name,
            theme = theme,
            imageUrl = newImageUrl
        )
        // Garante que o ID continua o mesmo para o JPA atualizar em vez de criar novo
        updatedCard.id = existingCard.id

        return ResponseEntity.ok(repository.save(updatedCard))
    }

    // 6. DELETAR Carta
    @DeleteMapping("/{id}")
    fun deleteCard(@PathVariable id: Long): ResponseEntity<Void> {
        if (repository.existsById(id)) {
            repository.deleteById(id)
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.notFound().build()
    }
}