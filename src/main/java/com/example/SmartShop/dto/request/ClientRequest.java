package com.example.SmartShop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRequest {
    @NotBlank(message = "le nom est obligatoire")
    private String nom;

    @NotBlank(message = "l'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "le username est obligatoire")
    @Size(min = 3, message = "Le username doit contenir au moins 3 caractères")
    private String username;

    @NotBlank(message = "le password est obligatoire")
    @Size(min = 6, message = "Le password doit contenir au moins 6 caractères")
    private String password;
}