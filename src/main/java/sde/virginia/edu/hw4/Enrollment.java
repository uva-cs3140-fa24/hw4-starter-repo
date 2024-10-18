package sde.virginia.edu.hw4;

import java.util.*;

/**
 * //TODO: Extract the enrollment logic from {@link Section} into this class
 */

public class Enrollment {
    private int enrollmentCapacity;
    private int waitListCapacity;
    private Set<Student> enrolledStudents;
    private List<Student> waitListedStudents;
    private EnrollmentStatus enrollmentStatus;

    public Enrollment(int enrollmentCapacity, int waitListCapacity) {
        this(
                enrollmentCapacity,
                waitListCapacity,
                new HashSet<>(enrollmentCapacity),
                new ArrayList<>(waitListCapacity),
                EnrollmentStatus.OPEN
        );
    }

    protected Enrollment(
        int enrollmentCapacity,
        int waitListCapacity,
        Set<Student> enrolledStudents,
        List<Student> waitListedStudents,
        EnrollmentStatus enrollmentStatus
    ) {
        if (enrollmentCapacity < 0 || waitListCapacity < 0 || enrolledStudents == null ||
                waitListedStudents == null || enrollmentStatus == null) {
            throw new IllegalArgumentException();
        }

        this.enrollmentCapacity = enrollmentCapacity;
        this.waitListCapacity = waitListCapacity;
        this.enrolledStudents = enrolledStudents;
        this.waitListedStudents = waitListedStudents;
        this.enrollmentStatus = enrollmentStatus;
    }

    public int getEnrollmentCapacity() {
        return enrollmentCapacity;
    }

    public int getWaitListCapacity() {
        return waitListCapacity;
    }

    /**
     * Returns the set of students enrolled in the section
     * @return an unmodifiable {@link Set} of students enrolled in the course.
     */

    public Set<Student> getEnrolledStudents() {
        return Collections.unmodifiableSet(enrolledStudents);
    }

    public int getEnrollmentSize() {
        return enrolledStudents.size();
    }

    public boolean isEnrollmentFull() {
        return enrolledStudents.size() >= enrollmentCapacity;
    }

    public List<Student> getWaitListedStudents() {
        return Collections.unmodifiableList(waitListedStudents);
    }

    public boolean isWaitListEmpty() {
        return waitListedStudents.isEmpty();
    }

    public int getWaitListSize() {
        return waitListedStudents.size();
    }

    public EnrollmentStatus getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentCapacity(int enrollmentCapacity) {
        this.enrollmentCapacity = enrollmentCapacity;
    }

    public void setWaitListCapacity(int waitListCapacity) {
        if (waitListCapacity < 0) {
            throw new IllegalArgumentException("Course waitlist size cannot be negative");
        }
        this.waitListCapacity = waitListCapacity;
    }

    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    /**
     * Adds the student to the section enrollment if there is space.
     * @param student the student to add to enrollment
     * @throws IllegalStateException if the section enrollment is already full.
     * @throws IllegalArgumentException if the student is already enrolled in the section.
     */
    public void addStudentToEnrolled(Student student) {
        if (isEnrollmentClosed()) {
            throw new IllegalStateException("Enrollment closed");
        }
        if (isEnrollmentFull()) {
            throw new IllegalStateException(
                    "Enrollment full. Cannot add student: " + student + " to enrollment for " + this);
        }
        if (enrolledStudents.contains(student)) {
            throw new IllegalArgumentException("Student: " + student + " is already enrolled in the section " + this);
        }

        enrolledStudents.add(student);
    }

    /**
     * Checks if a student is enrolled
     * @param student the {@Student}
     * @return true if the student is enrolled, false if wait listed or not enrolled at all.
     */
    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }

    /**
     * Removes the student from the section enrollment.
     * @param student the student to be removed from the section enrollment
     */
    public void removeStudentFromEnrolled(Student student) {
        if (!enrolledStudents.contains(student)) {
            throw new IllegalArgumentException(
                    "Student: " + student + " is not enrolled in " + this);
        }
        enrolledStudents.remove(student);
    }

    /**
     * Checks if the wait list is full
     * @return true if the wait list is full or over capacity.
     */
    public boolean isWaitListFull() {
        return getWaitListSize() >= waitListCapacity;
    }

    /**
     * Returns the first student on the wait-list (the next student to be added if space opens up)
     * @return the first student on the wait list.
     */
    public Student getFirstStudentOnWaitList() {
        if (waitListedStudents.isEmpty()) {
            throw new IllegalStateException("Wait list is empty for section " + this);
        }
        return waitListedStudents.get(0);
    }

    /**
     * Add a student to the wait list if the section enrollment is already full
     * @param student the student to add to the wait list.
     * @throws IllegalStateException if the section's enrollment is not full (that is, the student can enroll directly)
     * OR the wait list is already full.
     * @throws IllegalArgumentException if the student is already enrolled or waitlisted in that section.
     */
    public void addStudentToWaitList(Student student) {
        if (isEnrollmentClosed()) {
            throw new IllegalStateException("Enrollment closed");
        }
        if (!isEnrollmentFull()) {
            throw new IllegalStateException(
                    "Enrollment not full. Cannot add student: " + student + " to wait list for " + this);
        }
        if (isWaitListFull()) {
            throw new IllegalStateException("Wait list is full. Cannot ads student: " + student + " to wait list for " + this);
        }
        if (enrolledStudents.contains(student)) {
            throw new IllegalArgumentException("Student " + student + " is already enrolled in section " + this);
        }
        if (waitListedStudents.contains(student)) {
            throw new IllegalArgumentException("Student " + student + " is already on the waitlist for section " + this);
        }

        waitListedStudents.add(student);
    }

    /**
     * Checks if a student is wait listed
     * @param student the {@Student}
     * @return true if the student is wait-listed, false if enrolled or not enrolled at all.
     */
    public boolean isStudentWaitListed(Student student) {
        return waitListedStudents.contains(student);
    }

    /**
     * Removes a student from the wait list.
     * @param student the student to be removed from the wait list.
     * @throws IllegalArgumentException if the student is not on the wait list.
     */
    public void removeStudentFromWaitList(Student student) {
        if (!waitListedStudents.contains(student)) {
            throw new IllegalArgumentException(
                    "Student: " + student + " is not on wait list for " + this);
        }
        waitListedStudents.remove(student);
    }

    public boolean isEnrollmentClosed() {
        return enrollmentStatus == EnrollmentStatus.CLOSED;
    }

    public void removeFromEnrollment(Student student) {
        if (!enrolledStudents.contains(student)) {
            throw new IllegalArgumentException(
                    "Student: " + student + " is not enrolled in " + this);
        }
        enrolledStudents.remove(student);
    }


}
