package com.example.gerador_horario.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Aula {
    private LocalDateTime horaInicio;
    private int duracaoMin;
    private int aulasAntesIntervalo;
    private Map<String, Integer> aulasPorDia;
    private List<Turma> turmas;
    private int duracaoIntervalo;
}
