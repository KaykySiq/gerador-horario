package com.example.gerador_horario.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Professor {

    private String nomeProfessor;
    private Map<String, String> disponibilidadePorDia;
}
