package com.alura.challenge.Tortulan_Biblioteca.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(DatosAutor dAutor) {
        this.nombre = formatearNombre(dAutor.nombre());
        this.fechaDeNacimiento = dAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = dAutor.fechaDeFallecimiento();
    }

    private String formatearNombre(String nombre) {
        if (nombre == null || !nombre.contains(",")) {
            return nombre;
        }
        String[] partes = nombre.split(",");
        return partes[1].trim() + " " + partes[0].trim();
    }

    @Override
    public String toString() {
        return "Autor: " + nombre +
                " | Nacimiento: " + (fechaDeNacimiento != null ? fechaDeNacimiento : "N/A") +
                " | Fallecimiento: " + (fechaDeFallecimiento != null ? fechaDeFallecimiento : "N/A");
    }
}

