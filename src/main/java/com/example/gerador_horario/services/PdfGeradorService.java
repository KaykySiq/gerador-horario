package com.example.gerador_horario.services;

import com.example.gerador_horario.domain.entities.Professor;
import com.example.gerador_horario.domain.entities.Turma;
import com.example.gerador_horario.dtos.FormularioDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PdfGeradorService {

    private final HorarioService horarioService;

    public PdfGeradorService(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    public ByteArrayOutputStream gerarPdf(FormularioDTO aula) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Horário de Aula - Relatório", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(10f);
        infoTable.addCell(celula("Hora de Início:"));
        infoTable.addCell(celula(aula.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm"))));
        infoTable.addCell(celula("Duração (min):"));
        infoTable.addCell(celula(String.valueOf(aula.getDuracaoMin())));
        infoTable.addCell(celula("Aulas antes do intervalo:"));
        infoTable.addCell(celula(String.valueOf(aula.getAulasAntesIntervalo())));
        document.add(infoTable);

        document.add(paragrafo("Aulas por Dia da Semana", 14));

        PdfPTable diaTable = new PdfPTable(2);
        diaTable.setWidthPercentage(100);
        diaTable.addCell(celula("Dia da Semana"));
        diaTable.addCell(celula("Qtd. de Aulas"));
        aula.getAulasPorDia().forEach((dia, qtd) -> {
            diaTable.addCell(celula(dia));
            diaTable.addCell(celula(String.valueOf(qtd)));
        });
        document.add(diaTable);

        document.add(paragrafo("Turmas e Professores", 14));

        for (Turma turma : aula.getTurmas()) {
            document.add(paragrafo("Turma: " + turma.getNomeTurma(), 12));

            Map<Professor, Map<String, String>> disponibilidadeConvertida = new HashMap<>();
            for (Professor prof : turma.getProfessores()) {
                disponibilidadeConvertida.put(prof,
                    horarioService.calcularDisponibilidade(
                        prof.getDisponibilidadePorDia(),
                        aula.getHoraInicio(),
                        aula.getDuracaoMin(),
                        aula.getAulasAntesIntervalo(),
                        aula.getDuracaoIntervalo()
                    )
                );
            }

            List<String> headers = Arrays.asList("Horário", "Segunda", "Terça", "Quarta", "Quinta", "Sexta");
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);
            table.setSpacingAfter(10);
            headers.forEach(h -> table.addCell(celula(h)));

            int totalAulas = aula.getAulasPorDia().values().stream().max(Integer::compareTo).orElse(0);
            List<String> horarios = horarioService.gerarHorarios(
                aula.getHoraInicio(),
                aula.getDuracaoMin(),
                aula.getAulasAntesIntervalo(),
                totalAulas,
                aula.getDuracaoIntervalo()
            );

            List<String> dias = Arrays.asList("segunda", "terca", "quarta", "quinta", "sexta");
            int slot = 0;
            for (String h : horarios) {
                if (h.startsWith("__INTERVALO__")) {
                    String periodo = h.split(":", 2)[1];
                    PdfPCell ci = new PdfPCell(new Phrase(periodo + "\nINTERVALO"));
                    ci.setColspan(headers.size());
                    ci.setHorizontalAlignment(Element.ALIGN_CENTER);
                    ci.setPadding(5f);
                    table.addCell(ci);
                    continue;
                }

                slot++;
                table.addCell(celula(h));

                for (String dia : dias) {
                    String nome = "";
                    for (Professor p : turma.getProfessores()) {
                        String disp = disponibilidadeConvertida.get(p).getOrDefault(dia, "");
                        if (disp.contains(h)) {
                            nome = p.getNomeProfessor();
                            break;
                        }
                    }

                    int qtdDia = aula.getAulasPorDia().getOrDefault(capitalize(dia), 0);
                    String texto = (nome.isEmpty() && slot > qtdDia) ? "----" : nome;
                    table.addCell(celula(texto));
                }
            }

            document.add(table);
        }

        document.close();
        return outputStream;
    }

    private PdfPCell celula(String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c.setPadding(5f);
        return c;
    }

    private Paragraph paragrafo(String texto, int tamanho) {
        Paragraph p = new Paragraph(texto, new Font(Font.FontFamily.HELVETICA, tamanho, Font.BOLD));
        p.setSpacingBefore(10);
        p.setSpacingAfter(10);
        return p;
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
