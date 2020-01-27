package com.scriptbasic.utility.functions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    
    public interface DateFormatter {

        /**
         * Function to format date 
         * 
         * @param localDate date to format
         * @return String representing date
         */
        String formatDate(LocalDate localDate);
    }

    /**
     * Date parser to be used by date specific functions
     */
    static DateParser dateParser = DateFunctions::isoDateParser;
    
    /**
     * Date formatter to be used by date specific functions
     */
    static DateFormatter dateFormatter = DateFunctions::isoDateFormatter;
    
    /**
     * Date zero is 30.12.1899
     * 
     * Due to compatibility with MS VBA
     */
    static public final LocalDate DATE_ZERO = LocalDate.of(1899, 12, 30);
    
    static public void setDateParser(final DateParser parser) {
        dateParser = parser;
    }
    
    static public DateParser getDateParser() {
        return dateParser;
    }
    
    static public void setDateFormatter(final DateFormatter formatter) {
        dateFormatter = formatter;
    }
    
    static public DateFormatter getDateFormatter() {
        return dateFormatter;
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
    
    @BasicFunction(classification = {com.scriptbasic.classification.Date.class,
            com.scriptbasic.classification.Utility.class})
    static public LocalDate date() {
        return LocalDate.now();
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
     * Returns a Variant (Long) specifying the number of time intervals between two specified dates.
     * 
     * @param interval Interval of time used to calculate the difference between date1 and date2.
     * @param date1 Date used for calculation
     * @param date2 Date used for calculation
     * @return  number of time intervals between two specified dates
     * @throws ScriptBasicException when unsupported type of interval
     */
    @BasicFunction(classification = {com.scriptbasic.classification.Date.class,
            com.scriptbasic.classification.Utility.class})
    static public long datediff(String interval, LocalDate date1, LocalDate date2) throws ScriptBasicException {
        switch(interval.toLowerCase()) {
        case "d":
            return date2.toEpochDay()-date1.toEpochDay();
        case "m":
            return (date2.getYear()*12+date2.getMonthValue())-
                    (date1.getYear()*12+date1.getMonthValue());
        case "yyyy":
            return date2.getYear()-date1.getYear();
        default:
            throw new ScriptBasicException("Unsupported interval type: "+interval);
        }
    }

    /**
     * Basic implementation of date parser
     * 
     * Obtains an instance of LocalDate from a text string such as 2007-12-03. 
     * 
     * @param src date to be parsed
     * @return date
     */
    public static LocalDate isoDateParser(String src) {
        return LocalDate.parse(src);
    }
    
    
    /**
     * Obtains an instance of LocalDate from a text string such as 2007-12-03. 
     * 
     * @param localDate date to format
     * @return formatted date
     */
    public static String isoDateFormatter(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.Date.class,
            com.scriptbasic.classification.Utility.class})
    static public boolean isDate(Object o) {
        if(o instanceof LocalDate) {
            return true;
        }
        if(o instanceof String) {
            try {
                String s = (String)o;
                dateParser.parseDate(s);
                return true;
            } catch(Exception e) {
                
            }
        }
        return false;
    }
}
