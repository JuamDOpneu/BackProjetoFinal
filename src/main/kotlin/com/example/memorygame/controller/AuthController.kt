package com.example.memorygame.controller

import com.example.memorygame.model.User
import com.example.memorygame.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

// === CERTIFIQUE-SE QUE ESTAS CLASSES EXISTEM ===
data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)

// Esta classe é fundamental para o botão Perfil aparecer no frontend!
data class AuthResponse(val token: String, val user: User)
// ===============================================

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<Any> {
        if (userRepository.findByEmail(req.email).isPresent) {
            return ResponseEntity.badRequest().body("Email já cadastrado")
        }

        val newUser = User(
            name = req.name,
            email = req.email,
            password = passwordEncoder.encode(req.password)
        )
        userRepository.save(newUser)
        return ResponseEntity.ok("Usuário criado com sucesso")
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<Any> {
        // 1. Busca usuário
        val user = userRepository.findByEmail(req.email)
            .orElse(null) ?: return ResponseEntity.badRequest().body("Usuário não encontrado")

        // 2. Confere senha
        if (!passwordEncoder.matches(req.password, user.password)) {
            return ResponseEntity.badRequest().body("Senha incorreta")
        }

        // 3. Gera token fake (ou JWT real se você implementou)
        val fakeToken = UUID.randomUUID().toString()

        // 4. RETORNA O DTO 'AuthResponse' QUE CONTÉM 'user' E 'token'
        return ResponseEntity.ok(AuthResponse(fakeToken, user))
    }
}