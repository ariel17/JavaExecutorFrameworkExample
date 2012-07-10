/**
 * JavaExecutorFrameworkExample
 *
 * An example of using the Java Executor Framework for asynchronous processing.
 *
 * @author Ariel Gerardo Rios <mailto:ariel.gerardo.rios@gmail.com>
 *
 */

package org.gabrielle.example;

import java.io.InputStream;
import java.io.IOException;

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

    public static final String PROPERTIES_PATH = "resources.properties";

    public static final String PARAM_NAME_NTHREADS = "nthreads";

    public static final int DEFAULT_NTHREADS = 1;

    private static final Logger logger = Logger.getLogger(
            JavaExecutorFrameworkExample.class);

    private static final Logger exceptionLogger = Logger.getLogger("exception");

    private final Properties properties = new Properties();

    private PIDFile pidfile = new PIDFile();                                       


    /** 
     * Locks the PID file if avialable.
     *
     * @param 
     * @return 
     * @throws 
    */
    public boolean lockPIDFile(final UUID uuid, final String path) throws
        IOException {
                                                                                
        logger.info(String.format("UUID=%s - Locking PID file %s ...", uuid,
                    path));
        this.pidfile.setPath(path);
        boolean result = this.pidfile.lock();
        logger.info(String.format("UUID=%s - Locking PID file %s result: %s",
                    uuid, path, result));
        return result;
    }
                                                                                
                                                                                
    /** 
     * Removes the PID file.
     *
     * @param 
     * @return 
     * @throws 
    */
    public boolean unlockPIDFile(final UUID uuid) {
                                                                                
        boolean result;
        logger.info(String.format("UUID=%s - Unlocking PID file ...", uuid));
        try {
            result = this.pidfile.unlock();
        }
        catch(Exception e){
            logger.error(String.format("UUID=%s - There was an exception " +
                        "unlocking the PID file. See exception log for more " +
                        "details.", uuid));
            exceptionLogger.error(String.format("UUID=%s - There was an " +
                            "exception unlocking the PID file: %s", uuid,
                            ExceptionUtils.getStackTrace(e)));
            return false;
        }
                                                                                
        logger.info(String.format("UUID=%s - Unlocking PID file result: %s",
                    uuid, result));
        return result;
    }
                                                                                
                                                                                
    /** 
     * Loads the configuration stored in a properties file using a
     * Properties object.
     *
     * @param path The path as String of Properties file.
    */
    public void loadProperties(final UUID uuid, final String path) throws
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
     * Contains the logic to execute the application.
     *
     * @param args The program arguments, as a String array.
     * @return 0 for successful execution, 1 instead.
    */
    public static int main (String [] args) {

        OptionBuilder.withArgName(PARAM_NAME_NTHREADS);
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Overwrites the configured value for " +
                "the number of threads to create.");
        Option optNthreads = OptionBuilder.create(PARAM_NAME_NTHREADS);
                                                                                   
        Options options = new Options();
        options.addOption(optNthreads);
                                                                                   
        CommandLineParser parser = new GnuParser();

        UUID uuid = UUID.randomUUID();
        
        try {
            // Parsing program parameters 
            CommandLine line = parser.parse(options, args);

            int nthreads;
                                                                                
            if (line.hasOption(PARAM_NAME_NTHREADS)) {
                nthreads = Integer.parseInt(line.getOptionValue(
                            PARAM_NAME_NTHREADS));
            }
            else {
                // TODO Get from properties or defautl value
            }
        }
        catch (Exception e) {
        }

        return Status.SUCCESSFUL.getId();
    }

}


// vim:ft=java:
