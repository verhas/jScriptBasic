package com.scriptbasic.utility.functions;

import java.time.LocalDate;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.rightvalues.BasicDateValue;
import com.scriptbasic.interfaces.BasicRuntimeException;

public class DateFunctions {
    
    public interface DateParser {

        /**
         * Function for parsing date 
         * 
         * @param src String representing date
         * @return Parsed date
         * @throws ScriptBasicException Exception if failed to parse date
         */
        LocalDate parseDate(String src) throws ScriptBasicException;
    }
    
    /**
     * Date parser to be used by date specific functions
     */
    static DateParser dateParser = DateFunctions::parseDate;
    
    /**
     * Date zero is 30.12.1899
     * 
     * Due to compatibility with MS VBA
     */
    static final LocalDate DATE_ZERO = LocalDate.of(1899, 12, 30);
    
    static public void setDateParser(final DateParser parser) {
        dateParser = parser;
    }
    
    static public DateParser getDateParser() {
        return dateParser;
    }
    
    private static LocalDate getLocalDate(Object operand) throws ScriptBasicException {
        if(operand instanceof Long) {
            Long l = (Long)operand;
            return DATE_ZERO.plusDays(l);
        } else
        if(operand instanceof String) {
            String src = (String)operand;
            return dateParser.parseDate(src);                            
        } else
        if(operand instanceof LocalDate) {
            return (LocalDate)operand;
        } else {
            throw new BasicRuntimeException("Unsupported operand");
        }
    }

    /**
     * Returns a Integer containing a whole number representing the year.
     * @param operand value representing date (numeric, string, date)
     * @return whole number representing the year
     * @throws ScriptBasicException if operand is not date
     */
    @BasicFunction(classification = {com.scriptbasic.classification.Date.class,
            com.scriptbasic.classification.Utility.class})
    static public Integer year(final Object operand) throws ScriptBasicException {
        if(operand==null) {
            return null;
        }
        LocalDate localDate = getLocalDate(operand);
        return localDate.getYear();
    }

    /**
     * Returns a Integer specifying a whole number between 1 and 12, inclusive, 
     * representing the month of the year.
     * 
     * @param operand value representing date (numeric, string, date)
     * @return month, number between 1 and 12
     * @throws ScriptBasicException if operand is not date
     */
    @BasicFunction(classification = {com.scriptbasic.classification.Date.class,
            com.scriptbasic.classification.Utility.class})
    static public Integer month(final Object operand) throws ScriptBasicException {
        if(operand==null) {
            return null;
        }
        LocalDate localDate = getLocalDate(operand);
        return localDate.getMonthValue();
    }

    /**
     * Returns a Integer specifying a whole number between 1 and 31, inclusive, 
     * representing the day of the month.
     * 
     * @param operand value representing date (numeric, string, date)
     * @return day of the month, number between 1 and 31
     * @throws ScriptBasicException if operand is not date
     */
    @BasicFunction(classification = {com.scriptbasic.classification.Date.class,
            com.scriptbasic.classification.Utility.class})
    static public Integer day(final Object operand) throws ScriptBasicException {
        if(operand==null) {
            return null;
        }
        LocalDate localDate = getLocalDate(operand);
        return localDate.getDayOfMonth();
    }

    /**
     * Returns a Date for a specified year, month, and day.
     *
     * @param year number between 100 and 9999
     * @param month the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @return date object
     * @throws ScriptBasicException error when parameter is out of range
     */
    @BasicFunction(classification = {com.scriptbasic.classification.Date.class,
            com.scriptbasic.classification.Utility.class})
    static public BasicDateValue dateserial(final Integer year, final Integer month, final Integer dayOfMonth) throws ScriptBasicException {
        if(year<100 || year>9999) {
            throw new ScriptBasicException("Year is out of range (100, 9999), value: "+year); 
        }
        
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        
        return new BasicDateValue(localDate);
    }

    /**
     * Basic implementation of date parser
     * 
     * @param src date to be parsed
     * @return date
     */
    private static LocalDate parseDate(String src) {
        return LocalDate.parse(src);
    }
}
