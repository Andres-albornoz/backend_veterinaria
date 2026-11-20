package com.example.backend_veterinaria.config;

import com.example.backend_veterinaria.model.MedicamentosVacunas;
import com.example.backend_veterinaria.model.Producto;
import com.example.backend_veterinaria.model.Servicios;
import com.example.backend_veterinaria.model.User;
import com.example.backend_veterinaria.repository.MVRepository;
import com.example.backend_veterinaria.repository.ProductoRepository;
import com.example.backend_veterinaria.repository.ServiciosRepository;
import com.example.backend_veterinaria.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository usuarios,
                           ProductoRepository productos,
                           ServiciosRepository servicios,
                           MVRepository medicamentos,
                           PasswordEncoder enc) {
        return args -> {
            if (usuarios.findByEmail("admin@admin.cl").isEmpty()) {
                usuarios.save(new User(null, "admin", "admin@admin.cl", enc.encode("admin123"), "ADMIN"));
            }
            if (usuarios.findByEmail("user@user.cl").isEmpty()) {
                usuarios.save(new User(null, "user", "user@user.cl", enc.encode("user123"), "USER"));
            }
            if (productos.count() == 0) {
                productos.save(new Producto(0, "correaA", "correaA", 1234, 10, true));
                productos.save(new Producto(0, "correaB", "correaB", 1234, 10, true));
                productos.save(new Producto(0, "correaC", "correaC", 1234, 10, true));
            }
            // Catálogo de Servicios - Forma A (DSY1104). La duración se expresa en minutos ("24 h" = 1440).
            if (servicios.count() == 0) {
                // Consultas
                servicios.save(new Servicios("Consultas", "Consulta general", "Perro / Gato", 30, 15000, ""));
                servicios.save(new Servicios("Consultas", "Consulta urgencia", "Perro / Gato", 30, 25000,
                        "Fuera de horario +$10.000"));
                servicios.save(new Servicios("Consultas", "Control postoperatorio", "Perro / Gato", 20, 10000, ""));
                servicios.save(new Servicios("Consultas", "Consulta ave / conejo", "Ave / Conejo", 30, 18000, ""));
                servicios.save(new Servicios("Consultas", "Segunda opinión médica", "Todas", 40, 20000,
                        "Requiere ficha previa"));
                // Vacunación
                servicios.save(new Servicios("Vacunación", "Vacuna antirrábica canina", "Perro", 10, 12000,
                        "Obligatoria por ley"));
                servicios.save(new Servicios("Vacunación", "Vacuna sextuple canina", "Perro", 10, 18000,
                        "Refuerzo anual"));
                servicios.save(new Servicios("Vacunación", "Vacuna bivalente felina", "Gato", 10, 15000,
                        "Refuerzo anual"));
                servicios.save(new Servicios("Vacunación", "Vacuna triple felina", "Gato", 10, 17000,
                        "Refuerzo anual"));
                servicios.save(new Servicios("Vacunación", "Vacuna Bordetella canina", "Perro", 10, 14000,
                        "Tos de las perreras"));
                servicios.save(new Servicios("Vacunación", "Vacuna antirrábica felina", "Gato", 10, 12000, ""));
                // Cirugía
                servicios.save(new Servicios("Cirugía", "Esterilización hembra canina", "Perra", 90, 80000,
                        "Incluye anestesia y hospitalización 24h"));
                servicios.save(new Servicios("Cirugía", "Esterilización macho canino", "Perro", 60, 60000,
                        "Incluye anestesia"));
                servicios.save(new Servicios("Cirugía", "Esterilización hembra felina", "Gata", 60, 65000,
                        "Incluye anestesia y hospitalización 12h"));
                servicios.save(new Servicios("Cirugía", "Esterilización macho felino", "Gato", 45, 50000,
                        "Incluye anestesia"));
                servicios.save(new Servicios("Cirugía", "Extirpación de tumor cutáneo", "Perro / Gato", 60, 120000,
                        "Precio referencial; varía según tamaño"));
                servicios.save(new Servicios("Cirugía", "Cesárea de urgencia", "Perra / Gata", 120, 180000, ""));
                // Desparasitación
                servicios.save(new Servicios("Desparasitación", "Desparasitación interna pequeños (<10 kg)",
                        "Perro", 5, 8000, ""));
                servicios.save(new Servicios("Desparasitación", "Desparasitación interna medianos (10-25 kg)",
                        "Perro", 5, 9500, ""));
                servicios.save(new Servicios("Desparasitación", "Desparasitación interna grandes (>25 kg)",
                        "Perro", 5, 11000, ""));
                servicios.save(new Servicios("Desparasitación", "Desparasitación interna felina", "Gato", 5, 8000, ""));
                servicios.save(new Servicios("Desparasitación", "Antiparasitario externo (pipeta)",
                        "Perro / Gato", 5, 7500, "Incluye aplicación"));
                // Exámenes
                servicios.save(new Servicios("Exámenes", "Hemograma completo", "Perro / Gato", 30, 22000,
                        "Resultado en 24-48 h"));
                servicios.save(new Servicios("Exámenes", "Perfil bioquímico completo", "Perro / Gato", 30, 35000,
                        "Resultado en 24-48 h"));
                servicios.save(new Servicios("Exámenes", "Radiografía (1 proyección)", "Perro / Gato", 20, 28000, ""));
                servicios.save(new Servicios("Exámenes", "Ecografía abdominal", "Perro / Gato", 30, 45000, ""));
                servicios.save(new Servicios("Exámenes", "Test de leishmaniasis", "Perro", 20, 18000, ""));
                // Otros
                servicios.save(new Servicios("Otros", "Corte de uñas", "Perro / Gato", 15, 5000, ""));
                servicios.save(new Servicios("Otros", "Limpieza dental", "Perro / Gato", 45, 55000,
                        "Requiere anestesia"));
                servicios.save(new Servicios("Otros", "Microchip identificación", "Perro / Gato", 10, 15000,
                        "Incluye registro"));
                servicios.save(new Servicios("Otros", "Hospitalización (por día)", "Perro / Gato", 1440, 30000,
                        "Incluye monitoreo y alimentación básica"));
            }
            // Medicamentos y Vacunas en Stock - Forma A (DSY1104)
            if (medicamentos.count() == 0) {
                // Antibióticos
                medicamentos.save(new MedicamentosVacunas("ME001", "Antibióticos", "Amoxibay 250mg",
                        "Amoxicilina", "Blíster 10 comp.", "Perro / Gato", 45, 4200));
                medicamentos.save(new MedicamentosVacunas("ME002", "Antibióticos", "Enrox 50mg",
                        "Enrofloxacino", "Blíster 10 comp.", "Perro / Gato", 30, 6800));
                medicamentos.save(new MedicamentosVacunas("ME003", "Antibióticos", "Metrobay 250mg",
                        "Metronidazol", "Blíster 10 comp.", "Perro / Gato", 28, 3900));
                // Antiparasitarios
                medicamentos.save(new MedicamentosVacunas("ME004", "Antiparasitarios", "Nexgard",
                        "Afoxolaner", "Masticable 1 unid.", "Perro", 60, 9500));
                medicamentos.save(new MedicamentosVacunas("ME005", "Antiparasitarios", "Bravecto",
                        "Fluralaner", "Masticable 1 unid.", "Perro", 40, 18900));
                medicamentos.save(new MedicamentosVacunas("ME006", "Antiparasitarios", "Revolution Plus",
                        "Selamectina+Sarolaner", "Pipeta 1 unid.", "Gato", 35, 14500));
                medicamentos.save(new MedicamentosVacunas("ME007", "Antiparasitarios", "Drontal Plus",
                        "Praziquantel+Pamoato", "Comprimido 1 unid.", "Perro", 80, 3200));
                medicamentos.save(new MedicamentosVacunas("ME008", "Antiparasitarios", "Milbemax Gato",
                        "Milbemicina+Praziq.", "Comprimido 2 unid.", "Gato", 50, 6800));
                // Antiinflamatorios
                medicamentos.save(new MedicamentosVacunas("ME009", "Antiinflamatorios", "Meloxicam 1mg",
                        "Meloxicam", "Blíster 10 comp.", "Perro / Gato", 55, 4500));
                medicamentos.save(new MedicamentosVacunas("ME010", "Antiinflamatorios", "Carprofen 50mg",
                        "Carprofeno", "Blíster 10 comp.", "Perro", 30, 9800));
                // Dermatología
                medicamentos.save(new MedicamentosVacunas("ME011", "Dermatología", "Clorhexidina shampoo",
                        "Clorhexidina 2%", "Frasco 250ml", "Perro / Gato", 25, 8900));
                medicamentos.save(new MedicamentosVacunas("ME012", "Dermatología", "Malaseb shampoo",
                        "Miconazol+Clorhex.", "Frasco 250ml", "Perro / Gato", 20, 12500));
                medicamentos.save(new MedicamentosVacunas("ME013", "Dermatología", "Apoquel 16mg",
                        "Oclacitinib", "Blíster 10 comp.", "Perro", 18, 22000));
                // Digestivo
                medicamentos.save(new MedicamentosVacunas("ME014", "Digestivo", "Probifor",
                        "Bacillus clausii", "Sobre 5ml x10", "Perro / Gato", 40, 5600));
                medicamentos.save(new MedicamentosVacunas("ME015", "Digestivo", "Omeprazol 10mg vet",
                        "Omeprazol", "Blíster 10 comp.", "Perro / Gato", 35, 3800));
                // Cardíaco y Analgésicos
                medicamentos.save(new MedicamentosVacunas("ME016", "Cardíaco", "Vetmedin 2.5mg",
                        "Pimobendan", "Blíster 10 comp.", "Perro", 15, 28000));
                medicamentos.save(new MedicamentosVacunas("ME017", "Analgésicos", "Tramadol 50mg vet",
                        "Tramadol", "Blíster 10 comp.", "Perro", 22, 5200));
                // Vacunas
                medicamentos.save(new MedicamentosVacunas("ME018", "Vacunas", "Nobivac DHPPi",
                        "Vacuna polivalente", "Vial 1 dosis", "Perro", 48, 8500));
                medicamentos.save(new MedicamentosVacunas("ME019", "Vacunas", "Nobivac Rabies",
                        "Vacuna antirrábica", "Vial 1 dosis", "Perro / Gato", 60, 5800));
                medicamentos.save(new MedicamentosVacunas("ME020", "Vacunas", "Felocell CVR",
                        "Vacuna triple felina", "Vial 1 dosis", "Gato", 36, 7200));
                // Suplementos
                medicamentos.save(new MedicamentosVacunas("ME021", "Suplementos", "Omega vet 3-6-9",
                        "Ácidos grasos omega", "Frasco 100ml", "Perro / Gato", 30, 9900));
                medicamentos.save(new MedicamentosVacunas("ME022", "Suplementos", "Condrovet forte",
                        "Condroitín+Glucos.", "Blíster 30 comp.", "Perro", 25, 14500));
            }
        };
    }
}
