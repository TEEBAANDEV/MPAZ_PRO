package com.mpaz.backend;

import com.mpaz.backend.model.ObjetivoAprendizaje;
import com.mpaz.backend.model.Usuario;
import com.mpaz.backend.repository.ObjetivoRepository;
import com.mpaz.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MpazBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpazBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepo, ObjetivoRepository objetivoRepo) {
        return args -> {
            if (usuarioRepo.count() == 0) {
                usuarioRepo.save(new Usuario(null, "utp", "1234", "Jefatura UTP", "UTP"));
                usuarioRepo.save(new Usuario(null, "docente", "1234", "Prof. Camila Rojas", "DOCENTE"));
                usuarioRepo.save(new Usuario(null, "estudiante", "1234", "Mateo Pérez (2° Básico)", "ESTUDIANTE"));
            }
            if (objetivoRepo.count() == 0) {
                objetivoRepo.save(new ObjetivoAprendizaje(null, "OA 01", "Lectura comprensiva de textos breves",
                        "Leer en voz alta para adquirir fluidez y comprender cuentos breves.", "Lenguaje", "PUBLICADO"));
                objetivoRepo.save(new ObjetivoAprendizaje(null, "OA 04", "Adición y sustracción hasta el 100",
                        "Demostrar que comprende la adición y la sustracción en el ámbito del 0 al 100.", "Matemática", "PUBLICADO"));
                objetivoRepo.save(new ObjetivoAprendizaje(null, "OA 07", "Ciclo de vida de los seres vivos",
                        "Identificar y comparar las etapas del ciclo de vida de distintos animales.", "Ciencias", "AUTORIZADO"));
            }
        };
    }
}
