package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TascaTest {

    private Tasca tasca;

    @BeforeEach
    public void setUp() {
        tasca = new Tasca("Descripció de prova");
    }

    @Test
    public void testConstructor() {
        assertNotNull(tasca);
        assertEquals("Descripció de prova", tasca.getDescripcio());
        assertFalse(tasca.isCompletada());
        assertNull(tasca.getDataInici());
        assertNull(tasca.getDataFiPrevista());
        assertNull(tasca.getDataFiReal());
        assertNull(tasca.getPrioritat());
    }

    @Test
    public void testSettersAndGetters() {
        LocalDate dataInici = LocalDate.now();
        LocalDate dataFiPrevista = LocalDate.now().plusDays(1);
        LocalDate dataFiReal = LocalDate.now().plusDays(2);
        int prioritat = 3;

        tasca.setCompletada(true);
        tasca.setDataInici(dataInici);
        tasca.setDataFiPrevista(dataFiPrevista);
        tasca.setDataFiReal(dataFiReal);
        tasca.setPrioritat(prioritat);

        assertTrue(tasca.isCompletada());
        assertEquals(dataInici, tasca.getDataInici());
        assertEquals(dataFiPrevista, tasca.getDataFiPrevista());
        assertEquals(dataFiReal, tasca.getDataFiReal());
        assertEquals(prioritat, tasca.getPrioritat());
    }

    @Test
    public void testToString() {
        tasca.setCompletada(true);
        assertEquals("Descripció de prova: Completada", tasca.toString());

        tasca.setCompletada(false);
        assertEquals("Descripció de prova: Pendent", tasca.toString());
    }

    @Test
    public void testIdIncrement() {
        Tasca tasca1 = new Tasca("Tasca 1");
        Tasca tasca2 = new Tasca("Tasca 2");
        assertTrue(tasca2.getId() > tasca1.getId());
    }
}