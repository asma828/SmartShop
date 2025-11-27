package com.example.SmartShop.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
}
