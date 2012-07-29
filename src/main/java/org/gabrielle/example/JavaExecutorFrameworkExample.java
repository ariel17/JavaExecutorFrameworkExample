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

import java.lang.String;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.lang.exception.ExceptionUtils;

import org.apache.log4j.Logger;

import org.gabrielle.example.enums.Status;

import org.gabrielle.example.tasks.TaskExample;
import org.gabrielle.example.util.PIDFile;

import org.javatuples.Pair;
import org.javatuples.Tuple;

/** 
 * A class containing the main method to execute the example for using the
 * Executor Framework.
 *
 * @author Ariel Gerardo Ríos <mailto:ariel.gerardo.rios@gmail.com>
 * @version 1.00    
*/
public class JavaExecutorFrameworkExample extends Object { 

    // nthreads parameter

    public static final String PARAM_NAME_NTHREADS = "nthreads";

    public static final int TUPLE_INDEX_NTHREADS = 0;

    public static final int DEFAULT_NTHREADS = 1;

    // pidfile parameter

    public static final String PARAM_NAME_PIDFILE_PATH = "pidfile";

    public static final int TUPLE_INDEX_PIDFILE_PATH = 1;

    public static final String DEFAULT_PIDFILE_PATH = "/tmp/pid-example";

    // task constants

    public static final int DEFAULT_NTASKS = 1000;

    // execution constants

    public static final String SHELL_SCRIPT_NAME = "run.sh";

    public static final int DEFAULT_WAIT_TIME = 10;  // seconds

    // Loggers

    private static final Logger logger = Logger.getLogger(
            JavaExecutorFrameworkExample.class);

    private static final Logger exceptionLogger = Logger.getLogger("exception");

    // Other variables

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
     * Generates the Options object to use for command line argument parsing.
     *
     * @return An Options object instance.
    */
    public static Options createOptions() {

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

        return options;
    }

    /** 
     * Parses the command line arguments using the Options object previously
     * initialized.
     *
     * @param args The command line arguments as String[].
     * @param options The Options object previously initialized to use for
     * parsing.
     * @return A Tuple object containig the parsed values or null instead.
     * @throws ParseException 
    */
    public static Tuple parseArguments(final String[] args, final Options
            options) throws ParseException { 

        int nthreads = 0;
        String pidfilePath = null;

        CommandLineParser parser = new GnuParser();
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

        return new Pair<Integer, String>(nthreads, pidfilePath);
    }

    /** 
     * Shows the CLI help with a customized message.
     *
     * @param message A message to show.
    */
    public static void showCLIHelp(final String message, final Options
            options) { 
        System.out.println(message);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(String.format("./%s", SHELL_SCRIPT_NAME),
                options);
    }

    /** 
     * Process required tasks, with validated parameters.
     *
     * @param nthreads The number of threads to execute or null.
     * @return The execution status, as Status.
     * @throws IOException 
     * @throws ExecutionException 
     * @throws InterruptedException 
    */
    public Status process(final Integer nthreads) throws IOException,
           InterruptedException, ExecutionException {

        logger.info(String.format("UUID=%s - *** Program example finished ***",
                    this.uuid));

        logger.info(String.format("UUID=%s - Thread pool size: %d", this.uuid,
                    nthreads));

        ExecutorService executorThreadPool = Executors.newFixedThreadPool(
                nthreads);

        List<Future<UUID>> futures = new ArrayList<Future<UUID>>(nthreads);

        // Creating many tasks to be processed
        for (int i = 0; i < DEFAULT_NTASKS; i++) {
            // Adding the futures for later result
            futures.add(
                    // enqueuing tasks as they are created into the internal
                    // queue
                    executorThreadPool.submit(new TaskExample(this.uuid))
            );
        }
                    
        // Getting the tasks processing results
        UUID task_uuid;
        for(Future<UUID> future : futures){
            try {
                task_uuid = future.get();
                logger.info(String.format("UUID=%s - Tasks process result: %s",
                            this.uuid, task_uuid));
            }
            catch (Exception e) {
                exceptionLogger.error(String.format("UUID=%s - There was an " +
                            "exception in the program: %s", uuid,
                            ExceptionUtils.getStackTrace(e)));
            }
        }

        // Asking to all threads to shut down.
        logger.info(String.format("UUID=%s - Shutting down the thread pool.",
                    this.uuid));
        executorThreadPool.shutdown();

        // Checking for correct for pool shutdown
        if (!executorThreadPool.awaitTermination(DEFAULT_WAIT_TIME,
                    TimeUnit.SECONDS)) {
            logger.warn(String.format("UUID=%s - Pool is still running; " +
                        "forcing to shutdown now.", this.uuid));
            executorThreadPool.shutdownNow();
        }

        // TODO Here goes some general processing with all task results

        Status status = Status.SUCCESSFUL;
        logger.info(String.format("UUID=%s - Process result: %s", this.uuid,
                    status));
        logger.info(String.format("UUID=%s - *** Program example finished ***",
                    this.uuid));

        // Returning a general processing result
        return status;
    }

    /** 
     * Contains the logic to execute the application.
     *
     * @param args The program arguments, as a String array.
     * @return 0 for successful execution, 1 instead.
    */
    public static void main(String[] args) {

        Options options = JavaExecutorFrameworkExample.createOptions();

        UUID uuid = UUID.randomUUID();

        JavaExecutorFrameworkExample jefe = new JavaExecutorFrameworkExample(
                uuid);

        PIDFile pidfile = null;
        
        try {
            // Parsing program parameters 
            Tuple parsedArgs = JavaExecutorFrameworkExample.parseArguments(args,
                    options);

            Integer nthreads = (Integer) parsedArgs.getValue(
                    TUPLE_INDEX_NTHREADS);

            String pidfilePath = (String) parsedArgs.getValue(
                    TUPLE_INDEX_PIDFILE_PATH);
            pidfile = new PIDFile(pidfilePath);
            pidfile.lock();
            logger.info(String.format("UUID=%s - Locked PID file: %s", uuid,
                        pidfile));

            Status status = jefe.process(nthreads);
            System.exit(status.getId());
        }
        catch (ParseException pe) {
            JavaExecutorFrameworkExample.showCLIHelp(String.format("There was" +
                        " an error parsing arguments: %s",
                        ExceptionUtils.getStackTrace(pe)), options);
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
                    logger.info(String.format("UUID=%s - Released PID file: " +
                                "%s", uuid, pidfile));
                }
                catch(Exception e) {
                    exceptionLogger.error(String.format("UUID=%s - There was " +
                                "an exception releasing the PID file: %s", uuid,
                                ExceptionUtils.getStackTrace(e)));
                }
            }
        }

        logger.info(String.format("UUID=%s - *** Program example finished ***",
                    uuid));
        System.exit(Status.FAILED.getId());
    }
}


// vim:ft=java:
