package com.alura.challenge.Tortulan_Biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String idioma;
    private Double numeroDeDescargas;
    @ManyToOne
    private Autor autor;
    public Libro(){}
    public Libro(DatosLibro  d){
        this.titulo = d.titulo();
        this.idioma = d.idiomas().get(0);
        this.numeroDeDescargas = d.numeroDeDescargas();
    }
    @Override
    public String toString(){
        String idiomaFormateado = switch (idioma){
            case "es" -> "Español";
            case "en" -> "Ingles";
            case "hu" -> "Hungaro";
            case "fr" -> "Frances";
            default -> idioma;
        };
        return "\n----- LIBRO -----\n" +
                "Título: " + titulo + "\n" +
                "Autor: " + (autor != null ? autor.getNombre() : "Desconocido") + "\n" +
                "Idioma: " + idiomaFormateado + "\n" +
                "Número de descargas: " + numeroDeDescargas + "\n" +
                "-----------------\n";
    }
}
