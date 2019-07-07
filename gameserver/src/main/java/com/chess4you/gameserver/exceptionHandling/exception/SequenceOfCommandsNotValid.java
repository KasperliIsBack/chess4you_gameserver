package com.chess4you.gameserver.exceptionHandling.exception;

public class SequenceOfCommandsNotValid extends Exception {
    public SequenceOfCommandsNotValid(){
        super("The Sequence of commands is not valid! The Sequence is: 1. get possible Turn, 2. do Turn");
    }
}
