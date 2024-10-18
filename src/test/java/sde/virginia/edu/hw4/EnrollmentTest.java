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
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class EnrollmentTest {
    Enrollment enrollment;

    @Mock
    Set<Student> enrolledStudents;

    @Mock
    List<Student> waitListedStudents;

    @BeforeEach
    public void setup() {
        enrollment = new Enrollment(
                245,
                199,
                enrolledStudents,
                waitListedStudents,
                EnrollmentStatus.OPEN);
    }

    @Test
    void isEnrollmentFull_true() {
        when(enrolledStudents.size()).thenReturn(enrollment.getEnrollmentCapacity());
        assertTrue(enrollment.isEnrollmentFull());
    }

    @Test
    void isEnrollmentFull_true_overCapacity() {
        when(enrolledStudents.size()).thenReturn(enrollment.getEnrollmentCapacity() + 1);
        assertTrue(enrollment.isEnrollmentFull());
    }

    @Test
    void isEnrollmentFull_false_oneUnderCapacity() {
        when(enrolledStudents.size()).thenReturn(enrollment.getEnrollmentCapacity() - 1);
        assertFalse(enrollment.isEnrollmentFull());
    }

    @Test
    void getEnrollmentSize() {
        when(enrolledStudents.size()).thenReturn(100);
        assertEquals(100, enrollment.getEnrollmentSize());
    }

    @Test
    void addStudentToEnrolled_equivalence() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(100);
        when(enrolledStudents.contains(student)).thenReturn(false);

        enrollment.addStudentToEnrolled(student);
        verify(enrolledStudents).add(student);
    }

    @Test
    void addStudentToEnrolled_exception_Closed() {
        var student = mock(Student.class);
        enrollment.setEnrollmentStatus(EnrollmentStatus.CLOSED);

        assertThrows(IllegalStateException.class, () -> enrollment.addStudentToEnrolled(student));
        verify(enrolledStudents, never()).add(student);
    }

    @Test
    void addStudentToEnrolled_exception_Full() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(245);

        assertThrows(IllegalStateException.class, () -> enrollment.addStudentToEnrolled(student));
        verify(enrolledStudents, never()).add(student);
    }

    @Test
    void addStudentToEnrolled_exception_AlreadyEnrolled() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(244);
        when(enrolledStudents.contains(student)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> enrollment.addStudentToEnrolled(student));
        verify(enrolledStudents, never()).add(student);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isStudentEnrolled(boolean input) {
        var student = mock(Student.class);
        when(enrolledStudents.contains(student)).thenReturn(input);

        assertEquals(input, enrollment.isStudentEnrolled(student));
    }

    @Test
    void removeStudentFromEnrolled() {
        var student = mock(Student.class);
        when(enrolledStudents.contains(student)).thenReturn(true);

        enrollment.removeStudentFromEnrolled(student);

        verify(enrolledStudents).remove(student);
    }

    @Test
    void removeStudentFromEnrolled_exception() {
        var student = mock(Student.class);
        when(enrolledStudents.contains(student)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> enrollment.removeStudentFromEnrolled(student));

        verify(enrolledStudents, never()).remove(student);
    }

    @Test
    void getWaitListCapacity() {
        assertEquals(199, enrollment.getWaitListCapacity());
    }

    @Test
    void getWaitListSize() {
        when(waitListedStudents.size()).thenReturn(99);
        assertEquals(99, enrollment.getWaitListSize());
    }

    @Test
    void isWaitListFull_true() {
        when(waitListedStudents.size()).thenReturn(enrollment.getWaitListCapacity());
        assertTrue(enrollment.isWaitListFull());
    }

    @Test
    void isWaitListFull_true_overCapacity() {
        when(waitListedStudents.size()).thenReturn(enrollment.getWaitListCapacity() + 1);
        assertTrue(enrollment.isWaitListFull());
    }

    @Test
    void isWaitListFull_false() {
        when(waitListedStudents.size()).thenReturn(enrollment.getWaitListCapacity() - 1);
        assertFalse(enrollment.isWaitListFull());
    }

    @Test
    void setWaitListCapacity() {
        enrollment.setWaitListCapacity(50);
        assertEquals(50, enrollment.getWaitListCapacity());
    }

    @Test
    void setWaitListCapacity_exception() {
        assertThrows(IllegalArgumentException.class, () -> enrollment.setWaitListCapacity(-1));
    }

    @Test
    void getFirstStudentOnWaitList() {
        var student = mock(Student.class);
        when(waitListedStudents.isEmpty()).thenReturn(false);
        when(waitListedStudents.get(0)).thenReturn(student);

        assertEquals(student, enrollment.getFirstStudentOnWaitList());
    }

    @Test
    void getFirstStudentOnWaitList_exception() {
        var student = mock(Student.class);
        when(waitListedStudents.isEmpty()).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> enrollment.getFirstStudentOnWaitList());
    }

    @Test
    void addStudentToWaitList_equivalence() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(245);
        when(waitListedStudents.size()).thenReturn(198);
        when(enrolledStudents.contains(student)).thenReturn(false);
        when(waitListedStudents.contains(student)).thenReturn(false);

        enrollment.addStudentToWaitList(student);

        verify(waitListedStudents).add(student);
    }

    @Test
    void addStudentToWaitList_enrollmentClosed() {
        enrollment.setEnrollmentStatus(EnrollmentStatus.CLOSED);
        var student = mock(Student.class);

        assertThrows(IllegalStateException.class, () -> enrollment.addStudentToWaitList(student));

        verify(waitListedStudents, never()).add(student);
    }

    @Test
    void addStudentToWaitList_enrollmentNotFull() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(244);

        assertThrows(IllegalStateException.class, () -> enrollment.addStudentToWaitList(student));

        verify(waitListedStudents, never()).add(student);
    }

    @Test
    void addStudentToWaitList_waitListFull() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(245);
        when(enrolledStudents.size()).thenReturn(199);

        assertThrows(IllegalStateException.class, () -> enrollment.addStudentToWaitList(student));

        verify(waitListedStudents, never()).add(student);
    }

    @Test
    void addStudentToWaitList_alreadyEnrolled() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(245);
        when(waitListedStudents.size()).thenReturn(198);
        when(enrolledStudents.contains(student)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> enrollment.addStudentToWaitList(student));

        verify(waitListedStudents, never()).add(student);
    }

    @Test
    void addStudentToWaitList_alreadyWaitListed() {
        var student = mock(Student.class);
        when(enrolledStudents.size()).thenReturn(245);
        when(waitListedStudents.size()).thenReturn(198);
        when(enrolledStudents.contains(student)).thenReturn(false);
        when(waitListedStudents.contains(student)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> enrollment.addStudentToWaitList(student));

        verify(waitListedStudents, never()).add(student);
    }



    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isStudentWaitListed(boolean input) {
        var student = mock(Student.class);
        when(waitListedStudents.contains(student)).thenReturn(input);

        assertEquals(input, enrollment.isStudentWaitListed(student));
    }

    @Test
    void removeStudentFromWaitList() {
        var student = mock(Student.class);
        when(waitListedStudents.contains(student)).thenReturn(true);

        enrollment.removeStudentFromWaitList(student);

        verify(waitListedStudents).remove(student);
    }

    @Test
    void removeStudentFromWaitList_Exception() {
        var student = mock(Student.class);
        when(waitListedStudents.contains(student)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> enrollment.removeStudentFromWaitList(student));

        verify(waitListedStudents, never()).remove(student);
    }



    @Test
    void isEnrollmentOpen_true() {
        assertFalse(enrollment.isEnrollmentClosed());
    }

    @Test
    void isEnrollmentOpen_false() {
        enrollment.setEnrollmentStatus(EnrollmentStatus.CLOSED);
        assertTrue(enrollment.isEnrollmentClosed());
    }

    @Test
    void getEnrollmentStatus() {
        assertEquals(EnrollmentStatus.OPEN, enrollment.getEnrollmentStatus());
    }
}
