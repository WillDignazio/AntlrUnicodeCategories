# Unicode Lexer Generator Generator

## Build

   javac Unicode.java

## Usage

   java Unicode <UnicodeData.txt> > UnicodeLex.g


This project uses the official Unicode datafile to build a
lexer generator grammar for Antlr. The output of the program will
go to stdout, and can be accordingly written to a file.