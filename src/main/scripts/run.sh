#!/bin/sh
 
# Executes this application hidding the complexity in calling a Java binary.

PROJECT_NAME="JavaExecutorFrameworkExample"
PROJECT_ROOT=`pwd`

JAVA="/usr/bin/java -Dpid=$$"
BIN_DIR="$PROJECT_ROOT/build/bin"
LIB_DIR="$PROJECT_ROOT/lib"
# RESOURCES_DIR="$PROJECT_ROOT/src/test/resources"

APPLICATION="org.gabrielle.example.JavaExecutorFrameworkExample"


echo "Loading classpath ..."
classpath=".:$PROJECT_ROOT:$BIN_DIR"  # :$RESOURCES_DIR"
for f in `ls $LIB_DIR/*.jar`; do
    classpath="$f:$classpath";
done;    
echo "Done. Classpath=$classpath"

echo
cd $BIN_DIR;

$JAVA -cp $classpath $APPLICATION $*
