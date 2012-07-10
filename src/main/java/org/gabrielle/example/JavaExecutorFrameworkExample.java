/**
 * JavaExecutorFrameworkExample
 *
 * An example of using the Java Executor Framework for asynchronous processing.
 *
 * @author Ariel Gerardo Rios <mailto:ariel.gerardo.rios@gmail.com>
 *
 */

package org.gabrielle.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.util.Properties;
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

    public static final String PARAM_NAME_PROPERTIES_PATH = "prop";

    public static final String PARAM_NAME_PIDFILE_PATH = "pidfile";

    public static final String DEFAULT_PROPERTIES_PATH = "resources.properties";
    
    public static final int DEFAULT_NTHREADS = 1;

    public static final String DEFAULT_PIDFILE_PATH = "/var/run/pid";

    private static final Logger logger = Logger.getLogger(
            JavaExecutorFrameworkExample.class);

    private static final Logger exceptionLogger = Logger.getLogger("exception");

    private final Properties properties = new Properties();

    private final PIDFile pidfile = new PIDFile();                                       

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
     * Loads the configuration stored in a properties file using a
     * Properties object.
     *
     * @param path The path as String of Properties file.
     * @throws IOException
    */
    public void loadProperties(final String path) throws
        IOException {
                                                                                
        logger.debug(String.format("UUID=%s - Loading properties " +
                    "configuration from: '%s' ...", uuid, path));
                                                                                 
        InputStream inputStream = this.getClass().getClassLoader().
            getResourceAsStream(path);
        this.properties.load(inputStream);
        
        logger.debug(String.format("UUID=%s - Loading properties " +
                    "configuration from: '%s': SUCCESS.", uuid, path));
    }
    

    /** 
     * Process required tasks, with validated parameters.
     *
     * @param nthreads The number of threads to execute or null.
     * @param propertiesPath The path to the available Properties file to load,
     * if not null.
     * @return The execution status, as integer.
     * @throws IOException 
    */
    public int process(final Integer nthreads, final String propertiesPath,
            final String pidfilePath) throws IOException {

        this.loadProperties(propertiesPath);

        this.pidfile.setPath(pidfilePath);
        this.pidfile.lock();

        Executor executor = Executors.newFixedThreadPool(nthreads);
        // TODO executor.execute(runnable);

        // TODO do some work

        this.pidfile.unlock();
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

        OptionBuilder.withArgName(PARAM_NAME_PROPERTIES_PATH);
        OptionBuilder.withDescription(String.format("Overwrites the default " +
                    "path of the properties file to load. Default: %s",
                    DEFAULT_PROPERTIES_PATH));
        option = OptionBuilder.create(PARAM_NAME_PROPERTIES_PATH);
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
        String propertiesPath = null;
        String pidfilePath = null;

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

            if (line.hasOption(PARAM_NAME_PROPERTIES_PATH)) {
                propertiesPath = line.getOptionValue(
                        PARAM_NAME_PROPERTIES_PATH);
            }
            else {
                propertiesPath = DEFAULT_PROPERTIES_PATH;
            }

            if (line.hasOption(PARAM_NAME_PIDFILE_PATH)) {
                pidfilePath = line.getOptionValue(
                        PARAM_NAME_PIDFILE_PATH);
            }
            else {
                pidfilePath = DEFAULT_PIDFILE_PATH;
            }

            return jefe.process(nthreads, propertiesPath, pidfilePath);

        }
        catch (Exception e) {
            exceptionLogger.error(String.format("UUID=%s - There was an " +
                        "exception in the program: %s", uuid, ExceptionUtils.
                        getStackTrace(e)));
        }

        return Status.FAILED.getId();
    }

}


// vim:ft=java:
