package com.example.gerador_horario.services;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.example.gerador_horario.dtos.FormularioDTO;
import com.itextpdf.text.DocumentException;

@Service
public class FormularioService {
    private final PdfGeradorService pdfGeradorService;

    public FormularioService(PdfGeradorService pdfGeradorService) {
        this.pdfGeradorService = pdfGeradorService;
    }

    public ByteArrayOutputStream gerarFormularioPdf(FormularioDTO formulario) throws DocumentException {
        return pdfGeradorService.gerarPdf(formulario);
    }
}
