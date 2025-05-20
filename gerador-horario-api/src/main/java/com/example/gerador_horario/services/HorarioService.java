package com.example.gerador_horario.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class HorarioService {
    public List<String> gerarHorarios(LocalTime horaInicio, int duracaoAula,
                                        int aulasAntesIntervalo, int totalAulas,
                                        int duracaoIntervalo) {
        
        List<String> list = new ArrayList<>();
        LocalTime cur = horaInicio;
        for (int i = 1; i <= totalAulas; i++) {
            LocalTime end = cur.plusMinutes(duracaoAula);
            list.add(String.format("%02d:%02d - %02d:%02d",
                    cur.getHour(), cur.getMinute(),
                    end.getHour(), end.getMinute()));
            cur = end;
            if (i == aulasAntesIntervalo) {
                LocalTime fi = cur.plusMinutes(duracaoIntervalo);
                list.add("__INTERVALO__:" +
                        String.format("%02d:%02d - %02d:%02d",
                                cur.getHour(), cur.getMinute(),
                                fi.getHour(), fi.getMinute()));
                cur = fi;
            }
        }
        return list;
    }

    public Map<String, String> calcularDisponibilidade(Map<String, String> raw, LocalTime horaInicio,
                                                        int duracaoAula, int aulasAntesIntervalo, int duracaoIntervalo) {
            
            Map<String, String> out = new HashMap<>();
        for (var e : raw.entrySet()) {
            String dia = e.getKey(), val = e.getValue();
            if (val == null || val.isBlank()) continue;

            String[] tokens = val.split(",");
            List<Integer> idxs = new ArrayList<>();
            for (String t : tokens) {
                try { idxs.add(Integer.parseInt(t.trim())); }
                catch (NumberFormatException ignored) {}
            }
            if (idxs.isEmpty()) continue;
            int maxIdx = Collections.max(idxs);

            List<String> all = new ArrayList<>();
            LocalTime cur = horaInicio;
            for (int i = 1; i <= maxIdx; i++) {
                LocalTime end = cur.plusMinutes(duracaoAula);
                all.add(String.format("%02d:%02d - %02d:%02d",
                        cur.getHour(), cur.getMinute(),
                        end.getHour(), end.getMinute()));
                cur = end;
                if (i == aulasAntesIntervalo) {
                    cur = cur.plusMinutes(duracaoIntervalo);
                }
            }

            List<String> sel = new ArrayList<>();
            for (int i : idxs) {
                if (i >= 1 && i <= all.size()) {
                    sel.add(all.get(i - 1));
                }
            }

            out.put(dia, String.join("\n", sel));
        }
        return out;
    }

    public String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1, 0);
    }
}
