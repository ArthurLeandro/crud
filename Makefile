#
# define compiler and compiler flag variables
#

JFLAGS = -g -cp src/**/*
JC = javac
DATA = src/database/
MKDIR = mkdir
TOUCH = touch
DBNAME = database.db

#
# Clear any default targets for building .class files from .java files; we 
# will provide our own target entry to do this in this makefile.
# make has a set of default targets for different suffixes (like .c.o) 
# Currently, clearing the default for .java.class is not necessary since 
# make does not have a definition for this target, but later versions of 
# make may, so it doesn't hurt to make sure that we clear any default 
# definitions for these
#

.SUFFIXES: .java .class


#
# Here is our target entry for creating .class files from .java files 
# This is a target entry that uses the suffix rule syntax:
#	DSTS:
#		rule
#  'TS' is the suffix of the target file, 'DS' is the suffix of the dependency 
#  file, and 'rule'  is the rule for building a target	
# '$*' is a built-in macro that gets the basename of the current target 
# Remember that there must be a < tab > before the command line ('rule') 
#

.java.class:
	$(JC) ${JFLAGS} $*.java -d ./bin 


#
# CLASSES is a macro consisting of 4 words (one for each java source file)
#

CLASSES = \
	src/Interface/IBuildIn.java \
	src/Interface/ISchema.java \
	src/Interface/ISearchIn.java \
	src/Interface/StartProcess.java \
	src/Interface/UpdateDeleteProess.java \
	src/Utilitary/ModelState.java \
	src/Utilitary/Debug.java \
	src/Utilitary/DatabaseSchemaName.java \
	src/View/ConsoleView.java \
	src/Model/Book.java \
	src/Utilitary/CRUDManager.java \
	src/Utilitary/FileManager.java \
  src/Main.java


#
# the default make target entry
#

all:  clean model classes controller dt


#
# This target entry uses Suffix Replacement within a macro: 
# $(name:string1=string2)
# 	In the words in the macro named 'name' replace 'string1' with 'string2'
# Below we are replacing the suffix .java of all words in the macro CLASSES 
# with the .class suffix
#

classes: $(CLASSES:.java=.class)


#
# RM is a predefined macro in make (RM = rm -f)
#

clean:
	$(RM) -r ./bin/**/*.class 
	$(RM) -d ./bin/* 
	$(RM) -r ./src/database/**/${DBNAME}
	$(RM) -d ./src/database/**
	$(RM) -d ./src/database/
	

model:
	$(JC) ${FLAGS} src/Model/*.java -d ./bin 
	# $(JC) src/Interface/EndProcess.java  src/Interface/IModel.java src/Utilitary/ModelState.java src/Model/ModelBase.java src/View/ConsoleView.java src/Model/*.java -d ./bin 

controller:	
	$(JC) ${JFLAGS} src/Controller/BooksController.java  src/Main.java -d ./bin

main:
	$(JC) ${JFLAGS} src/Main.java -d ./bin

dt:	
	${MKDIR} ${DATA} 
	${MKDIR} ${DATA}/books
	${MKDIR} ${DATA}/user
	${TOUCH} ${DATA}/books/${DBNAME}
	${TOUCH} ${DATA}/user/${DBNAME}