/**
 * WorkerExample
 *
 * This is an example implementation of asynchronous worker, using the Callable
 * interface.
 *
 * @author Ariel Gerardo Rios <mailto:ariel.gerardo.rios@gmail.com>
 *
 */

package org.gabrielle.example.worker;

import java.util.concurrent.Callable;

import java.util.Date;
import java.util.UUID;

/** 
 * An example implementation of an asynchronous worker, using the Callable
 * interface.
 *
 * @author Ariel Gerardo Ríos <mailto:ariel.gerardo.rios@gmail.com>
*/
public class WorkerExample implements Callable<UUID>{ 

    /** 
     * Implementation for Call, due to interface contract. The advantage of this
     * interface is that it can throw checked exceptions (not as in Runnable
     * interface).
     *
     * @throws Exception
    */
    public UUID call() throws Exception {

        // Just a simple task: Let's return a random UUID. Just that... but if
        // current timestamp is divisible by 2, then throws an exception.

        Date now = new Date();

        if (now.getTime() % 2 == 0) {  // divisible
            throw new Exception("Ups! An exception inside a worker...");
        }

        return UUID.randomUUID();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {    
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }

        final WorkerExample newObject = (WorkerExample) o;
        if (this.hashCode() == newObject.hashCode()) {
            return true;
        }

        return false;
    }
}

// vim:ft=java:
