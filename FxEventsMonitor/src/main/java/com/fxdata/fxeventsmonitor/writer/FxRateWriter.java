package com.fxdata.fxeventsmonitor.writer;


import java.util.Collection;

public interface FxRateWriter<T> {

    String write(Collection<T> collection);
}
