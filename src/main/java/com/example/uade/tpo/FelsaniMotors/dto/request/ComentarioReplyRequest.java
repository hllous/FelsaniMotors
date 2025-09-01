package com.example.uade.tpo.FelsaniMotors.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ComentarioReplyRequest {
    @NotBlank
    private String texto;
}
