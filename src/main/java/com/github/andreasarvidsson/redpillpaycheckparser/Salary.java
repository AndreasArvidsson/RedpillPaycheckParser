package com.github.andreasarvidsson.redpillpaycheckparser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Andreas Arvidsson
 */
public class Salary implements Comparable {

    final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    final Date date;
    final double brutto, netto, salary, provision;

    public Salary(
            final Date date,
            final double brutto,
            final double netto,
            final double salary,
            final double provision) {
        this.date = date;
        this.brutto = brutto;
        this.netto = netto;
        this.salary = salary;
        this.provision = provision;
    }

    @Override
    public int compareTo(Object o) {
        return date.compareTo(((Salary) o).date);
    }

    public String formatDate() {
        return FORMAT.format(date);
    }

    public int getYear() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

}
