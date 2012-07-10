/**
 * Status
 *
 * Represents the result status for the command execution.
 *
 * @author Ariel Gerardo Rios <mailto:ariel.gerardo.rios@gmail.com>
 *
 */

package org.gabrielle.example.enums;

/** 
 * Represents the result status for the command execution.
 *
 * @author Ariel Gerardo Ríos <mailto:ariel.gerardo.rios@gmail.com>
*/
public enum Status {

    SUCCESSFUL(0),
    FAILED(1);

    private final int id;

    /** 
     * Constructor.
     *
     * @param id The ID for this status, as int.
    */
    Status(final int id) {
        this.id = id;
    }

    /** 
     * Returns the ID for current status.
     *
     * @return The ID for the current status, as int.
    */
    public int getId() {
        return this.id;
    }
}


// vim:ft=java:
