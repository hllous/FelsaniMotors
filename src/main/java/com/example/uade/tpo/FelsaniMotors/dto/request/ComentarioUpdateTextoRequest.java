package com.example.uade.tpo.FelsaniMotors.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioUpdateTextoRequest {
    @NotBlank
    private String texto;
}
