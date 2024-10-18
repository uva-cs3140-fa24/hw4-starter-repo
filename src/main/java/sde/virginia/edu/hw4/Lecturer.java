package sde.virginia.edu.hw4;

/**
 * Represents a lecturer, or professor.
 */
public record Lecturer(
        int id,
        String computingId,
        String firstName,
        String lastName
) {
    /**
     * Creates a lecturer
     * @param id a unique integer id for each professor
     * @param computingId a unique {@link String} id of the form abc2def
     * @param firstName a first name as a {@link String}
     * @param lastName a last name as a {@link String}
     */
    public Lecturer {
        if (computingId == null) {
            throw new IllegalArgumentException("computingID cannot be null");
        }
    }
}
