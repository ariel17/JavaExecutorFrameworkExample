/**
 * JavaExecutorFrameworkExample
 *
 * An example of using the Java Executor Framework for asynchronous processing.
 *
 * @author Ariel Gerardo Rios <mailto:ariel.gerardo.rios@gmail.com>
 *
 */

package org.gabrielle.example;

import java.io.IOException;

import java.util.ArrayList;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import java.util.List;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import org.apache.commons.lang.exception.ExceptionUtils;

import org.apache.log4j.Logger;

import org.gabrielle.example.enums.Status;

import org.gabrielle.example.util.PIDFile;

/** 
 * A class containing the main method to execute the example for using the
 * Executor Framework.
 *
 * @author Ariel Gerardo Ríos <mailto:ariel.gerardo.rios@gmail.com>
 * @version 1.00    
*/
public class JavaExecutorFrameworkExample extends Object { 

    public static final String PARAM_NAME_NTHREADS = "nthreads";

    public static final String PARAM_NAME_PIDFILE_PATH = "pidfile";

    public static final int DEFAULT_NTHREADS = 1;

    public static final String DEFAULT_PIDFILE_PATH = "/var/run/pid";

    private static final Logger logger = Logger.getLogger(
            JavaExecutorFrameworkExample.class);

    private static final Logger exceptionLogger = Logger.getLogger("exception");

    private final UUID uuid;

    /** 
     * Constructor.
     *
     * @param uuid The unique id to use in this execution.
    */
    public JavaExecutorFrameworkExample(final UUID uuid) {
        this.uuid = uuid;
    }

    /** 
     * Process required tasks, with validated parameters.
     *
     * @param nthreads The number of threads to execute or null.
     * @return The execution status, as integer.
     * @throws IOException 
    */
    public int process(final Integer nthreads) throws IOException {

        logger.info(String.format("UUID=%s - Thread pool size: %d", this.uuid,
                    nthreads));

        ExecutorService executorThreadPool = Executors.newFixedThreadPool(
                nthreads);

        List<Future<UUID>> futures = new ArrayList<Future<UUID>>(nthreads);

        // TODO executor.execute(runnable);

        return Status.SUCCESSFUL.getId();
    }

    /** 
     * Contains the logic to execute the application.
     *
     * @param args The program arguments, as a String array.
     * @return 0 for successful execution, 1 instead.
    */
    public static int main (String [] args) {

        Options options = new Options();
        Option option;

        OptionBuilder.withArgName(PARAM_NAME_NTHREADS);
        OptionBuilder.withDescription("Overwrites the configured value for " +
                "the number of threads to create.");
        option = OptionBuilder.create(PARAM_NAME_NTHREADS);
        options.addOption(option);

        OptionBuilder.withArgName(PARAM_NAME_PIDFILE_PATH);
        OptionBuilder.withDescription(String.format("Overwrites the default " +
                    "path of the PID file. Default: %s",
                    DEFAULT_PIDFILE_PATH));
        option = OptionBuilder.create(PARAM_NAME_PIDFILE_PATH);
        options.addOption(option);

        CommandLineParser parser = new GnuParser();

        UUID uuid = UUID.randomUUID();

        Integer nthreads = null;
        String pidfilePath = null;
        PIDFile pidfile = null;

        JavaExecutorFrameworkExample jefe = new JavaExecutorFrameworkExample(
                uuid);
        
        try {
            // Parsing program parameters 
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(PARAM_NAME_NTHREADS)) {
                nthreads = Integer.parseInt(line.getOptionValue(
                            PARAM_NAME_NTHREADS));
            }
            else {
                nthreads = DEFAULT_NTHREADS;
            }

            if (line.hasOption(PARAM_NAME_PIDFILE_PATH)) {
                pidfilePath = line.getOptionValue(
                        PARAM_NAME_PIDFILE_PATH);
            }
            else {
                pidfilePath = DEFAULT_PIDFILE_PATH;
            }

            pidfile = new PIDFile(pidfilePath);
            pidfile.lock();
            logger.info(String.format("UUID=%s - Locked PID file at %s", uuid,
                        pidfilePath));

            return jefe.process(nthreads);

        }
        catch (Exception e) {
            exceptionLogger.error(String.format("UUID=%s - There was an " +
                        "exception in the program: %s", uuid, ExceptionUtils.
                        getStackTrace(e)));
        }
        finally{
            if (pidfile != null) {
                try {
                    pidfile.unlock();
                    logger.info(String.format("UUID=%s - Released PID file at" +
                                " %s", uuid, pidfilePath));
                }
                catch(Exception e) {
                    exceptionLogger.error(String.format("UUID=%s - There was " +
                                "an exception releasing the PID file: %s", uuid,
                                ExceptionUtils.getStackTrace(e)));
                }
            }
        }

        return Status.FAILED.getId();
    }
}


// vim:ft=java:
