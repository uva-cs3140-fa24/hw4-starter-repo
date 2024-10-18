package sde.virginia.edu.hw4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LecturerTest {
    public Lecturer lecturer;

    @BeforeEach
    public void setup() {
        lecturer = new Lecturer(1234, "pm8fc", "Paul", "McBurney");
    }
    @Test
    void getId() {
        assertEquals(1234, lecturer.id());
    }

    @Test
    void getComputingId() {
        assertEquals("pm8fc", lecturer.computingId());
    }

    @Test
    void getFirstName() {
        assertEquals("Paul", lecturer.firstName());
    }

    @Test
    void getLastName() {
        assertEquals("McBurney", lecturer.lastName());
    }

}