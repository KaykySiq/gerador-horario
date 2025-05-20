package com.example.gerador_horario.dtos;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.example.gerador_horario.domain.entities.Turma;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormularioDTO {
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;
    
    private int duracaoMin;
    private int aulasAntesIntervalo;
    private int duracaoIntervalo;
    private Map<String, Integer> aulasPorDia;
    private List<Turma> turmas;


}