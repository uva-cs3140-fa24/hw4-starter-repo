package sde.virginia.edu.hw4;

import java.util.*;

public class Section {
    /** Minimum CRN - CRNs must be between 00001 and 99999 */
    public static final int MINIMUM_COURSE_REGISTRATION_NUMBER = 1;

    /** Maximum CRN - CRNs must be between 00001 and 99999 */
    public static final int MAXIMUM_COURSE_REGISTRATION_NUMBER = 99999;

    /**Minumum Section number - Section numbers must be between 001 and 999 */
    public static final int MINIMUM_SECTION_NUMBER = 1;

    /**Maximum Section number - Section numbers must be between 001 and 999 */
    public static final int MAXIMUM_SECTION_NUMBER = 999;

    /**
     * CRN (Course Registration Number) a unique 5-digit ID to identify a specific section
     */
    private final int courseRegistrationNumber;
    /**
     * The section number of this section to differentiate it from other offerings of
     * the same course.
     */
    private final int sectionNumber;

    /**
     * The course the section is an offering for.
     */
    private final Course course;

    /**
     * The semester this section is offered.
     */
    private final Semester semester;

    /**
     * What classroom the course is held in.
     */
    private final Location location;

    /**
     * The timeslot for this section
     */
    private final TimeSlot timeSlot;

    /**
     * The lecturer for this section
     */
    private final Lecturer lecturer;

    private final Enrollment enrollment;

    public Section(
            int courseRegistrationNumber,
            int sectionNumber,
            Course course,
            Semester semester,
            Location location,
            TimeSlot timeSlot,
            Lecturer lecturer,
            Enrollment enrollment
    ) {
        this.courseRegistrationNumber = courseRegistrationNumber;
        this.sectionNumber = sectionNumber;
        this.course = course;
        this.semester = semester;
        this.location = location;
        this.timeSlot = timeSlot;
        this.lecturer = lecturer;
        this.enrollment = enrollment;
    }


    private void validateInputs(
            int courseRegistrationNumber,
            int sectionNumber,
            Course course,
            Semester semester,
            Location location,
            TimeSlot timeSlot,
            Lecturer lecturer,
            Enrollment enrollment
    ) {
        if (courseRegistrationNumber < MINIMUM_COURSE_REGISTRATION_NUMBER || courseRegistrationNumber > MAXIMUM_COURSE_REGISTRATION_NUMBER) {
            throw new IllegalArgumentException(
                "Invalid CRN: %d - CRNs must be between %d and %d inclusive.".formatted(
                        courseRegistrationNumber,
                        MINIMUM_COURSE_REGISTRATION_NUMBER,
                        MAXIMUM_COURSE_REGISTRATION_NUMBER
                ));
        }
        if (sectionNumber < MINIMUM_SECTION_NUMBER || sectionNumber > MAXIMUM_SECTION_NUMBER) {
            throw new IllegalArgumentException(
                "Invalid Section Number: %d - Sections must be between %d and %d inclusive.".formatted(
                        sectionNumber,
                        MINIMUM_SECTION_NUMBER,
                        MAXIMUM_SECTION_NUMBER
                )
            );
        }
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        if (semester == null) throw new IllegalArgumentException("Semester cannot be null");
        if (location == null) throw new IllegalArgumentException("Location cannot be null");
        if (timeSlot == null) throw new IllegalArgumentException("TimeSlot cannot be null");
        if (lecturer == null) throw new IllegalArgumentException("Lecturer cannot be null");

        if (enrollment.getEnrollmentCapacity() > location.roomCapacity()) {
            throw new IllegalArgumentException(
                String.format("Enrollment capacity: %d exceeds the location fire code capacity %d",
                    enrollment.getEnrollmentCapacity(),
                    location.roomCapacity()));
        }
    }



    /**
     * Gets the CRN (course registration number). This is typically a 5-digit unique numeric identifier for the course
     * @return the course registration number as an int
     */
    public int getCourseRegistrationNumber() {
        return courseRegistrationNumber;
    }

    /**
     * Returns the section number. This is used to differentiate multiple sections of the same course in a given
     * semester.
     * @return the section number as an int
     */
    public int getSectionNumber() {
        return sectionNumber;
    }

    /**
     * Get the {@link Course} this section is an offering for.
     * @return the {@link Course} associated with this section.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Get the {@link Semester} this section is offered in
     * @return the section's {@link Semester}
     */
    public Semester getSemester() {
        return semester;
    }

    /**
     * Get the {@link Location} (i.e., the classroom) of the section
     * @return the {@link Location} of the section.
     * @see Location
     */
    public Location getLocation() {
        return location;
    }


    /**
     * Returns the {@link TimeSlot} (the scheduled time) of the section.
     * @return the {@link TimeSlot} for the section
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public boolean overlapsWith(TimeSlot otherTimeSlot) {
        return otherTimeSlot.overlapsWith(timeSlot);
    }


    /**
     * Get the {@link Lecturer} (teacher) of the section
     * @return the {@link Lecturer} of the section
     */
    public Lecturer getLecturer() {
        return lecturer;
    }

    /**
     * Get the enrollment capacity for the section
     * @return the number of students which can be enrolled in the course.
     */
    public int getEnrollmentCapacity() {
        return enrollment.getEnrollmentCapacity();
    }

    /**
     * Changes the enrollmentCapacity of the course. Note that if this number is smaller than the current number of
     * enrolled students, no students will be removed from enrollment. However, no one can add the class while
     * the number of enrolled students is greater than or equal to the capacity.
     * @param enrollmentCapacity the new enrollmentCapacity
     * @throws IllegalArgumentException if the new enrollment capacity is larger than the {@link Location}'s fire code
     * capacity
     * @see Section#addStudentToEnrolled(Student)
     */
    public void setEnrollmentCapacity(int enrollmentCapacity) {
        if (enrollmentCapacity < 0) {
            throw new IllegalArgumentException("Enrollment Capacity cannot be negative");
        }
        if (location.roomCapacity() < enrollmentCapacity) {
            throw new IllegalArgumentException("New enrollment capacity: " + enrollmentCapacity +
                    " is too large for " + location + ". Cannot change enrollment capacity for: " + this);
        }
        this.enrollment.setEnrollmentCapacity(enrollmentCapacity);
    }

    /**
     * Get the current number of enrolled students
     * @return the number of students currently enrolled.
     */
    public int getEnrollmentSize() {
        return enrollment.getEnrollmentSize();
    }

    public boolean isEnrollmentFull() {
        return enrollment.isEnrollmentFull();
    }

    /**
     * Returns the set of students enrolled in the section
     * @return an unmodifiable {@link Set} of students enrolled in the course.
     */
    public Set<Student> getEnrolledStudents() {
        return enrollment.getEnrolledStudents();
    }

    /**
     * Adds the student to the section enrollment if there is space.
     * @param student the student to add to enrollment
     * @throws IllegalStateException if the section enrollment is already full.
     * @throws IllegalArgumentException if the student is already enrolled in the section.
     */
    public void addStudentToEnrolled(Student student) {
        enrollment.addStudentToEnrolled(student);
    }

    /**
     * Checks if a student is enrolled
     * @param student the {@link Student}
     * @return true if the student is enrolled, false if wait listed or not enrolled at all.
     */
    public boolean isStudentEnrolled(Student student) {
        return enrollment.isStudentEnrolled(student);
    }

    /**
     * Removes the student from the section enrollment.
     * @param student the student to be removed from the section enrollment
     */
    public void removeStudentFromEnrolled(Student student) {

        enrollment.removeFromEnrollment(student);
    }

    /**
     * Get the waitlisted capacity for the section
     * @return the number of students which can be waitlisted in the course.
     */
    public int getWaitListCapacity() {
        return enrollment.getWaitListCapacity();
    }

    /**
     * Get the current number of students on the wait list
     * @return the number of students currently wait listed.
     */
    public int getWaitListSize() {
        return enrollment.getWaitListSize();
    }

    /**
     * Checks if the wait list is full
     * @return true if the wait list is full or over capacity.
     */
    public boolean isWaitListFull() {
        return enrollment.isWaitListFull();
    }

    /**
     * Changes the waitListCapacity of the course. This does not remove students already on the wait list if the
     * capacity is less than the size.
     * @param waitListCapacity the new wait list capacity for the course.
     */
    public void setWaitListCapacity(int waitListCapacity) {
        enrollment.setWaitListCapacity(waitListCapacity);
    }

    /**
     * Returns the list of students enrolled in the section, in order of their wait list priority
     * @return an unmodifiable {@link List} of students waitListed in the course.
     */
    public List<Student> getWaitListedStudents() {
        return enrollment.getWaitListedStudents();
    }

    /**
     * Returns the first student on the wait-list (the next student to be added if space opens up)
     * @return the first student on the wait list.
     */
    public Student getFirstStudentOnWaitList() {
        return enrollment.getFirstStudentOnWaitList();
    }

    /**
     * Add a student to the wait list if the section enrollment is already full
     * @param student the student to add to the wait list.
     * @throws IllegalStateException if the section's enrollment is not full (that is, the student can enroll directly)
     * OR the wait list is already full.
     * @throws IllegalArgumentException if the student is already enrolled or waitlisted in that section.
     */
    public void addStudentToWaitList(Student student) {
       enrollment.addStudentToWaitList(student);
    }

    /**
     * Checks if a student is wait listed
     * @param student the {@link Student}
     * @return true if the student is wait-listed, false if enrolled or not enrolled at all.
     */
    public boolean isStudentWaitListed(Student student) {
        return enrollment.isStudentWaitListed(student);
    }

    /**
     * Removes a student from the wait list.
     * @param student the student to be removed from the wait list.
     * @throws IllegalArgumentException if the student is not on the wait list.
     */
    public void removeStudentFromWaitList(Student student) {
        enrollment.removeStudentFromWaitList(student);
    }

    public boolean isEnrollmentClosed() {
        return enrollment.isEnrollmentClosed();
    }

    public EnrollmentStatus getEnrollmentStatus() {
        return enrollment.getEnrollmentStatus();
    }

    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
        enrollment.setEnrollmentStatus(enrollmentStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (getSectionNumber() != section.getSectionNumber()) return false;
        if (!getCourse().equals(section.getCourse())) return false;
        return getSemester().equals(section.getSemester());
    }

    @Override
    public int hashCode() {
        int result = getSectionNumber();
        result = 31 * result + getCourse().hashCode();
        result = 31 * result + getSemester().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Section{" +
                "courseRegistrationNumber=" + courseRegistrationNumber +
                ", sectionNumber=" + sectionNumber +
                ", course=" + course +
                '}';
    }
}
