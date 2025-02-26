package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import org.entdes.mail.IEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GestorTasquesMockTest {

    @Mock
    private IEmailService emailService;

    @InjectMocks
    private GestorTasques gestorTasques;

    @BeforeEach
    public void setUp() {
        Tasca.resetIdCounter();
        MockitoAnnotations.openMocks(this);
        gestorTasques = new GestorTasques(emailService, "test@example.com");
    }

    @Test
    public void testAfegirTascaEnviaCorreu() throws Exception {
        String descripcio = "Nova tasca";
        LocalDate dataInici = LocalDate.now().plusDays(1);
        LocalDate dataFiPrevista = LocalDate.now().plusDays(2);
        Integer prioritat = 3;

        int id = gestorTasques.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat);

        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
        verify(emailService).enviarCorreu(eq("test@example.com"), eq("Nova Tasca Creada"), eq("Has creat la tasca: Nova tasca"));
    }

    @Test
    public void testAfegirTascaNoEnviaCorreuSiEmailServiceEsNull() throws Exception {
        gestorTasques = new GestorTasques(null, "test@example.com");

        String descripcio = "Nova tasca";
        LocalDate dataInici = LocalDate.now().plusDays(1);
        LocalDate dataFiPrevista = LocalDate.now().plusDays(2);
        Integer prioritat = 3;

        int id = gestorTasques.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat);

        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
        verify(emailService, never()).enviarCorreu(anyString(), anyString(), anyString());
    }

    @Test
    public void testModificarTascaAmbDescripcioDuplicada() throws Exception {
        gestorTasques.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.modificarTasca(1, "Tasca 2", true, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        });
        assertEquals("Ja existeix una tasca amb la mateixa descripció", exception.getMessage());
    }
}