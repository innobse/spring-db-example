package ru.bse71.learnup.spring.dbexample.dao.exceptions;

/**
 * Created by bse71
 * Date: 22.08.2021
 * Time: 0:11
 */

public class MyDbBaseException extends RuntimeException {

    public MyDbBaseException(String msg, Throwable err) {
        super(String.format("Произошла ошибка при %s: %s", msg, err.getMessage()), err);
    }
}
