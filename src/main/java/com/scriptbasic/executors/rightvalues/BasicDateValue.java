package com.scriptbasic.executors.rightvalues;

import java.time.LocalDate;

import com.scriptbasic.utility.functions.DateFunctions;

public class BasicDateValue extends BasicLongValue {
    
    static final long DATE_ZERO_TO_EPOCH_DAYS = DateFunctions.DATE_ZERO.toEpochDay();

    public BasicDateValue(LocalDate localDate) {
        super(localDate.toEpochDay()-DATE_ZERO_TO_EPOCH_DAYS);
    }
    
    public LocalDate getLocalDate() {
        return LocalDate.ofEpochDay(this.getValue()+DATE_ZERO_TO_EPOCH_DAYS);
    }
}
