/**
 * PIDFile
 *
 * Handles an application lock for execution through a PID file.
 *
 * Author: Ariel Gerardo Ríos <mailto:ariel.gerardo.rios@gmail.com>
 */


package org.gabrielle.example.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/** 
 * Creates a PID file lock based
 *
 * @author Ariel Gerardo Rios <mailto:ariel.gerardo.rios@gmail.com>
*/
public class PIDFile { 

    private final String path;

    /** 
     * Constructor.
     *
     * @param path The file path to lock.
     *
    */
    public PIDFile(final String path) { 
        this.path = path;
    }

    /** 
     * Locks a pid file.
     *
     * @return Boolean
     * @throws IOException
    */
    public Boolean lock() throws IOException {
        File f = new File(this.path);
        if (f.exists()) {
            return false;
        }

        FileWriter pid = new FileWriter(f);
        BufferedWriter buffer = new BufferedWriter(pid);
        buffer.write(System.getProperty("pid"));
        buffer.flush();
        buffer.close();
        pid.close();
        return true;
    }
                                                                                 
    /** 
     * Unlock the pid file, if some.
     *
     * @return Boolean
     * @throws IOException
    */
    public Boolean unlock() throws IOException {
        File pid = new File(this.path);
        return pid.delete();
    }
}
