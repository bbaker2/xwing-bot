package com.bbaker.discord.xwing.exception;

public class SetupException extends Throwable {

    public SetupException(String messageTemplate, String... args) {
        super(String.format(messageTemplate, args));
    }

}
