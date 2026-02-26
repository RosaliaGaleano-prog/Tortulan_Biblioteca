package com.alura.challenge.Tortulan_Biblioteca.menu;

import com.alura.challenge.Tortulan_Biblioteca.model.*;
import com.alura.challenge.Tortulan_Biblioteca.repository.AutorRepository;
import com.alura.challenge.Tortulan_Biblioteca.repository.LibroRepository;
import com.alura.challenge.Tortulan_Biblioteca.service.ConsumoAPI;
import com.alura.challenge.Tortulan_Biblioteca.service.ConvierteDatos;

import java.util.*;

public class Menu {
    private Scanner teclado = new Scanner((System.in));
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private LibroRepository repositorioLibro;
    private AutorRepository autorRepositorio;

    public Menu(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorioLibro = repository;
        this.autorRepositorio = autorRepository;
    }

    public void ejecutaMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                                        ---------------------------------
                                        BIENVENIDO A TORTULAN BIBLIOTECA
                                        ---------------------------------
                                        Elija la opcion a traves de su numero:
                                        1 - Buscar libro por título (Web)
                                        2 - Listar libros registrados
                                        3 - Listar autores registrados
                                        4 - Listar autores vivos en un año
                                        5 - Listar libros por idioma
                                        6- Genera estadisticas de descarga
                                        7- Top 10 libros mas descargados
                                        8- Buscar autor por nombre
                                        0 - Salir
                    """;
            System.out.println(menu);
            System.out.println("Escriba su opcion: ");
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Por favor ingrese un numero valido");
                teclado.nextLine();
                continue;
            }
            switch (opcion) {
                case 1 -> buscarLibroWeb();
                case 2 -> listarlibros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> generarEstadisticas();
                case 7 -> top10Libros();
                case 8 -> buscarAutorPorNombre();
                case 0 -> System.out.println("Cerrando Tortulan Biblioteca..");
                default -> System.out.println("opcion invalida");
            }
        }
    }

    private void buscarLibroWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var nombreLibro = teclado.nextLine();

        var json = consumo.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda.resultado().isEmpty()) {
            System.out.println("Libro no encontrado");
        } else {
            DatosLibro dLibro = datosBusqueda.resultado().get(0);

            Optional<Libro> libroExistente = repositorioLibro.findByTituloContainsIgnoreCase(dLibro.titulo());
            if (libroExistente.isPresent()) {
                System.out.println("El libro '" + dLibro.titulo() + "' ya está registrado.");
                return;
            }

            if (!dLibro.autor().isEmpty()) {
                DatosAutor dAutor = dLibro.autor().get(0);
                Autor autor = autorRepositorio.findByNombreContainsIgnoreCase(dAutor.nombre())
                        .orElseGet(() -> autorRepositorio.save(new Autor(dAutor)));

                Libro libro = new Libro(dLibro);
                libro.setAutor(autor);
                repositorioLibro.save(libro);
                System.out.println(libro);

                System.out.println("\n✅ Libro guardado con éxito:");
                System.out.println(libro);
            } else {
                System.out.println("No se pudo registrar el libro porque no tiene autor.");
            }
        }
    }

    private void listarlibros() {
        List<Libro> libros = repositorioLibro.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año que desea consultar:");
        var anio = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autores = autorRepositorio.buscarAutoresVivosEnAnio(anio);
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma (Codigo):
                es - Español
                en - Ingles
                fr - Frances
                pt - Portuges
                """);
        var idioma = teclado.nextLine();
        List<Libro> libros = repositorioLibro.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma");
        } else {
            libros.forEach(System.out::println);
        }
    }
    private void generarEstadisticas() {
        List<Libro> libros = repositorioLibro.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados para generar estadísticas.");
            return;
        }

        DoubleSummaryStatistics est = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() > 0)
                .mapToDouble(Libro::getNumeroDeDescargas)
                .summaryStatistics();

        System.out.println("\n----------- ESTADÍSTICAS -----------");
        System.out.println("Media de descargas: " + String.format("%.2f", est.getAverage()));
        System.out.println("Máxima de descargas: " + est.getMax());
        System.out.println("Mínima de descargas: " + est.getMin());
        System.out.println("Cantidad de libros evaluados: " + est.getCount());
        System.out.println("------------------------------------\n");
    }
    private void top10Libros() {
        System.out.println("\n--- TOP 10 LIBROS MÁS DESCARGADOS ---");
        List<Libro> libros = repositorioLibro.findAll();
        libros.stream()
                .sorted(Comparator.comparing(Libro::getNumeroDeDescargas).reversed())
                .limit(10)
                .forEach(System.out::println);
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que desea buscar:");
        var nombre = teclado.nextLine();
        Optional<Autor> autor = autorRepositorio.findByNombreContainsIgnoreCase(nombre);

        if (autor.isPresent()) {
            System.out.println("\nDatos del autor encontrado:");
            System.out.println(autor.get());
        } else {
            System.out.println(" Autor no encontrado en la base de datos.");
        }
    }
}