JavaExecutorFrameworkExample
=============================

An example of using the Executor framework in Java. This program shows how to process enqueued simple tasks (they only generates random UUID values and returns them) using threads. Also shows how to manage exceptions raised inside the thread, since the task implements the [Callable interface](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/Callable.html).


How to compile the code
-----------------------

    $ ant  # executes the default task -> build
    $ ant build

You can also see other Ant tasks by entering:

    $ ant help
    $ ant -p


How to execute the example
--------------------------

Once compiled, you can run the program example by entering the following:

    $ chmod +x ./src/main/scripts/run.sh  # execution permission to the shell script
    $ ./src/main/scripts/run.sh

### Changing the behaviour

* Overwritting the number of threads to spawn

        $ # Spawn 10 threads for processing tasks in this execution
        $ ./src/main/scripts/run.sh -nthreads 10

* Changing the path for pidfile

        $ # Placing the pidfile somewhere else
        $ ./src/main/scripts/run.sh -pidfile /var/run/pidfile-example


Need help?
----------

If you need help with the program, you can type:

    $ ./src/main/scripts/run.sh -help

[Or you can mail me ;)](mailto:ariel.gerardo.rios@gmail.com)


