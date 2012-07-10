/**
 * PIDFile
 *
 * Handles an application lock by PID file.
 *
 * Author: Ariel Gerardo Rios <mailto:ariel.gerardo.rios@gmail.com>
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
 * @version 1.00    
*/
public class PIDFile { 

    private String path;

    /** 
     * Constructor.
     *
     * @param path
     *
    */
    public PIDFile setPath(final String path) { 
        this.path = path;
        return this;
    }

    /** 
     * Locks a pid file.
     *
     * @return Boolean
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
    */
    public Boolean unlock() throws IOException {
        File pid = new File(this.path);
        return pid.delete();
    }
}
