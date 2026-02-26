package com.alura.challenge.Tortulan_Biblioteca;

import com.alura.challenge.Tortulan_Biblioteca.menu.Menu;
import com.alura.challenge.Tortulan_Biblioteca.repository.AutorRepository;
import com.alura.challenge.Tortulan_Biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TortulanBibliotecaApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepo;
    @Autowired
    private AutorRepository autorRepo;

    public static void main(String[] args) {
        SpringApplication.run(TortulanBibliotecaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Menu menu = new Menu(libroRepo, autorRepo);
        menu.ejecutaMenu();
    }
}