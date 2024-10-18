package sde.virginia.edu.hw4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SectionTest {
    private Section section;

    @Mock
    private Course course;

    private Semester semester;

    private Location location;

    private TimeSlot timeSlot;

    @Mock
    private Lecturer lecturer;
    @Mock
    private Enrollment enrollment;

    @BeforeEach
    public void setup() {
        semester = new Semester(Term.FALL, 2023);
        location = new Location("Nau Hall", "101", 245);
        timeSlot = new TimeSlot(TimeSlot.TUESDAY_THURSDAY, 12, 30, 13, 45);

        section = new Section(12345,
                1,
                course,
                semester,
                location,
                timeSlot,
                lecturer,
                enrollment
        );
    }


    @Test
    void getCourseRegistrationNumber() {
        assertEquals(12345, section.getCourseRegistrationNumber());
    }

    @Test
    void getSectionNumber() {
        assertEquals(1, section.getSectionNumber());
    }

    @Test
    void getCourse() {
        assertEquals(course, section.getCourse());
    }

    @Test
    void getSemester() {
        assertEquals(semester, section.getSemester());
    }

    @Test
    void getLocation() {
        assertEquals(location, section.getLocation());
    }

    @Test
    void getTimeSlot() {
        assertEquals(timeSlot, section.getTimeSlot());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void overlapsWith(boolean input) {
        var mockTimeSlot = mock(TimeSlot.class);
        when(mockTimeSlot.overlapsWith(timeSlot)).thenReturn(input);

        assertEquals(input, section.overlapsWith(mockTimeSlot));
    }

    @Test
    void getLecturer() {
        assertEquals(lecturer, section.getLecturer());
    }

    @Test
    void setEnrollmentCapacity() {
        section.setEnrollmentCapacity(200);

        verify(enrollment).setEnrollmentCapacity(200);
    }

    @Test
    void setEnrollmentCapacity_exception_negative() {
        assertThrows(IllegalArgumentException.class, () ->
                section.setEnrollmentCapacity(-1));
        verify(enrollment, never()).setEnrollmentCapacity(-1);
    }

    @Test
    void setEnrollmentCapacity_exception_fireCode() {
        assertThrows(IllegalArgumentException.class, () ->
                section.setEnrollmentCapacity(246));
        verify(enrollment, never()).setEnrollmentCapacity(246);
    }
}