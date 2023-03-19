package com.fxdata.fxeventsmonitor.writer;


import java.util.Collection;

public interface FxRateWriter<T> {

    void write(Collection<T> collection);
}
