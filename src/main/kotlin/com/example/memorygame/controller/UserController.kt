package com.example.memorygame.controller

import com.example.memorygame.model.User
import com.example.memorygame.repository.UserRepository
import com.example.memorygame.service.FileStorageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

data class GameResultRequest(val win: Boolean, val moves: Int, val time: Int)

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository,
    private val fileStorageService: FileStorageService // Reutiliza seu serviço existente!
) {

    @GetMapping("/{id}/stats")
    fun getStats(@PathVariable id: Long): ResponseEntity<Map<String, Int>> {
        val user = userRepository.findById(id).orElseThrow()
        return ResponseEntity.ok(mapOf(
            "gamesPlayed" to user.gamesPlayed,
            "wins" to user.wins
        ))
    }

    @PostMapping("/{id}/game-result")
    fun saveGameResult(@PathVariable id: Long, @RequestBody result: GameResultRequest): ResponseEntity<Any> {
        val user = userRepository.findById(id).orElseThrow()

        user.gamesPlayed += 1
        if (result.win) {
            user.wins += 1
        }
        userRepository.save(user)

        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}/avatar")
    fun uploadAvatar(
        @PathVariable id: Long,
        @RequestParam("avatar") file: MultipartFile
    ): ResponseEntity<User> {
        // 1. Usa seu serviço existente para salvar o arquivo no disco
        val imagePath = fileStorageService.storeFile(file)

        // 2. Monta a URL completa (ajuste a porta se necessário)
        val fullUrl = "http://localhost:8080$imagePath"

        // 3. Atualiza o usuário no banco
        val user = userRepository.findById(id).orElseThrow()
        user.avatarUrl = fullUrl
        userRepository.save(user)

        return ResponseEntity.ok(user)
    }
}