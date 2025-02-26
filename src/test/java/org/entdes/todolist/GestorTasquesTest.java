package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GestorTasquesTest {

    private GestorTasques gestorTasques;

    @BeforeEach
    public void setUp() {
        Tasca.resetIdCounter();
        gestorTasques = new GestorTasques(null, "test@example.com");
    }

    @Test
    public void testAfegirTasca() throws Exception {
        String descripcio = "Nova tasca";
        LocalDate dataInici = LocalDate.now().plusDays(1);
        LocalDate dataFiPrevista = LocalDate.now().plusDays(2);
        Integer prioritat = 3;

        int id = gestorTasques.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat);

        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
    }

    @Test
    public void testAfegirTascaDescripcioBuida() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.afegirTasca("", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        });

        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }

    @Test
    public void testAfegirTascaDataIniciPosteriorDataFiPrevista() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(3), LocalDate.now().plusDays(2), 3);
        });

        assertEquals("La data d'inici no pot ser posterior a la data fi prevista.", exception.getMessage());
    }

    @Test
    public void testEliminarTasca() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.eliminarTasca(1);

        assertEquals(0, gestorTasques.getNombreTasques());
    }

    
    @Test
    public void testEliminarTascaNoExisteix() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.eliminarTasca(1);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    public void testMarcarCompletada() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.marcarCompletada(1);

        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertTrue(tasca.isCompletada());
    }

    @Test
    public void testModificarTasca() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.modificarTasca(1, "Tasca modificada", true, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertEquals("Tasca modificada", tasca.getDescripcio());
        assertTrue(tasca.isCompletada());
    }

    @Test
    public void testObtenirTasca() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        Tasca tasca = gestorTasques.obtenirTasca(1);

        assertNotNull(tasca);
        assertEquals("Tasca", tasca.getDescripcio());
    }

    @Test
    public void testObtenirTascaNoExisteix() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.obtenirTasca(1);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    public void testLlistarTasques() throws Exception {
        gestorTasques.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        List<Tasca> tasques = gestorTasques.llistarTasques();
        assertEquals(2, tasques.size());
    }

    @Test
    public void testLlistarTasquesPerDescripcio() throws Exception {
        gestorTasques.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        List<Tasca> tasques = gestorTasques.llistarTasquesPerDescripcio("1");
        assertEquals(1, tasques.size());
        assertEquals("Tasca 1", tasques.get(0).getDescripcio());
    }

    @Test
    public void testLlistarTasquesPerComplecio() throws Exception {
        gestorTasques.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.marcarCompletada(1);

        List<Tasca> tasques = gestorTasques.llistarTasquesPerComplecio(true);
        assertEquals(1, tasques.size());
        assertEquals("Tasca 1", tasques.get(0).getDescripcio());
    }
    @Test
    public void testAfegirTascaSenseDatesNiPrioritat() throws Exception {
        int id = gestorTasques.afegirTasca("Tasca sense dates", null, null, null);
        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
    }

    @Test
    public void testAfegirTascaAmbPrioritatForaDeRang() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.afegirTasca("Tasca amb prioritat fora de rang", null, null, 6);
        });
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    @Test
    public void testModificarTascaSenseCanviarDescripcio() throws Exception {
        gestorTasques.afegirTasca("Tasca original", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.modificarTasca(1, "Tasca original", true, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertEquals("Tasca original", tasca.getDescripcio());
        assertTrue(tasca.isCompletada());
    }

    @Test
    public void testModificarTascaDesmarcantCompletada() throws Exception {
        gestorTasques.afegirTasca("Tasca completada", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.marcarCompletada(1);
        gestorTasques.modificarTasca(1, "Tasca completada", false, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertFalse(tasca.isCompletada());
        assertNull(tasca.getDataFiReal());
    }

    @Test
    public void testLlistarTasquesPerDescripcioSenseCoincidencies() throws Exception {
        gestorTasques.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        List<Tasca> tasques = gestorTasques.llistarTasquesPerDescripcio("No coincideix");
        assertEquals(0, tasques.size());
    }

    @Test
    public void testLlistarTasquesPerComplecioSenseTasquesCompletades() throws Exception {
        gestorTasques.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        List<Tasca> tasques = gestorTasques.llistarTasquesPerComplecio(true);
        assertEquals(0, tasques.size());
    }

    @Test
    public void testAfegirTascaDataIniciAnteriorDataActual() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.afegirTasca("Tasca", LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), 3);
        });
        assertEquals("La data d'inici no pot ser anterior a la data actual.", exception.getMessage());
    }

    @Test
    public void testAfegirTascaDataIniciNullDataFiPrevistaNoNull() throws Exception {
        int id = gestorTasques.afegirTasca("Tasca", null, LocalDate.now().plusDays(2), 3);
        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
    }

    @Test
    public void testAfegirTascaDataIniciNoNullDataFiPrevistaNull() throws Exception {
        int id = gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), null, 3);
        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
    }

    @Test
    public void testAfegirTascaAmbduesDatesNull() throws Exception {
        int id = gestorTasques.afegirTasca("Tasca", null, null, 3);
        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
    }

    @Test
    public void testAfegirTascaPrioritatNull() throws Exception {
        int id = gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), null);
        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
    }

    @Test
    public void testAfegirTascaPrioritatForaDeRang() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 6);
        });
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    @Test
    public void testLlistarTasquesPerComplecioNoCompletades() throws Exception {
        gestorTasques.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.marcarCompletada(1);

        List<Tasca> tasques = gestorTasques.llistarTasquesPerComplecio(false);
        assertEquals(1, tasques.size());
        assertEquals("Tasca 2", tasques.get(0).getDescripcio());
    }

    @Test
    public void testAfegirTascaSenseEmailService() throws Exception {
        gestorTasques = new GestorTasques(null, "test@example.com");

        int id = gestorTasques.afegirTasca("Tasca sense email", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        assertEquals(1, id);
        assertEquals(1, gestorTasques.getNombreTasques());
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

    @Test
    public void testMarcarCompletadaEstableixDataFiReal() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.marcarCompletada(1);

        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertNotNull(tasca.getDataFiReal());
        assertEquals(LocalDate.now(), tasca.getDataFiReal());
    }

    @Test
    public void testDesmarcarCompletadaEsborraDataFiReal() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        gestorTasques.marcarCompletada(1);
        gestorTasques.modificarTasca(1, "Tasca", false, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);

        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertNull(tasca.getDataFiReal());
    }
    @Test
    public void testAfegirTascaDescripcioNull() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.afegirTasca(null, LocalDate.now(), LocalDate.now().plusDays(1), 3);
        });
        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }
    @Test
    public void testModificarTascaPrioritatForaDeRang() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now(), LocalDate.now().plusDays(1), 3);
        
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.modificarTasca(1, "Tasca", true, LocalDate.now(), LocalDate.now().plusDays(1), 6);
        });
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }
    @Test
    public void testModificarTascaDataIniciPosterior() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now(), LocalDate.now().plusDays(1), 3);
        
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.modificarTasca(1, "Tasca", true, LocalDate.now().plusDays(2), LocalDate.now(), 3);
        });
        assertEquals("La data d'inici no pot ser posterior a la data fi prevista.", exception.getMessage());
    }

    @Test
    public void testModificarTascaNoExisteix() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.modificarTasca(999, "Tasca", true, LocalDate.now(), LocalDate.now(), 3);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    public void testAfegirTascaDescripcioDuplicada() throws Exception {
        gestorTasques.afegirTasca("Tasca Duplicada", null, null, null);
        
        Exception exception = assertThrows(Exception.class, () -> {
            gestorTasques.afegirTasca("tasca duplicada", null, null, null); // Ignora majúscules/minúscules
        });
        assertEquals("Ja existeix una tasca amb la mateixa descripció", exception.getMessage());
    }

    @Test
    public void testModificarTascaCompletadaNull() throws Exception {
        gestorTasques.afegirTasca("Tasca", LocalDate.now(), LocalDate.now().plusDays(1), 3);
        gestorTasques.marcarCompletada(1); // Marcar com completada
        
        // Modificar amb completada = null
        gestorTasques.modificarTasca(1, "Tasca", null, LocalDate.now(), LocalDate.now().plusDays(1), 3);
        
        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertFalse(tasca.isCompletada()); // Hauria de desmarcar si estava completada
        assertNull(tasca.getDataFiReal());
    }

    @Test
    public void testModificarTascaMateixaDescripcio() throws Exception {
        gestorTasques.afegirTasca("Tasca Original", null, null, 3);
        gestorTasques.modificarTasca(1, "Tasca Original", true, null, null, 3); // Descripció igual
        
        Tasca tasca = gestorTasques.obtenirTasca(1);
        assertEquals("Tasca Original", tasca.getDescripcio());
    }
}