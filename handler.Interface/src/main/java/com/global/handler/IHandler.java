package com.global.handler;

import java.util.concurrent.TimeoutException;

public interface IHandler {
    void handleTask(long time) throws TimeoutException;
}
