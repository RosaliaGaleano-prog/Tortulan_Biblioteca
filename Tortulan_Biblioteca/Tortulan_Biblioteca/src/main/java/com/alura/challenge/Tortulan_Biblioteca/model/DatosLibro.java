package com.alura.challenge.Tortulan_Biblioteca.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors")List<DatosAutor> autor,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Double numeroDeDescargas
        ) {
}
