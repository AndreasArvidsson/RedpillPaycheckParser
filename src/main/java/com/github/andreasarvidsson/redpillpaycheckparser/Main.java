package com.github.andreasarvidsson.redpillpaycheckparser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class Main {

    final static String FOLDER_PATH = "ENTER PATH HERE";
    final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        final List<Salary> salaries = Parser.parseFolder(FOLDER_PATH);
        listSalaries(salaries);
        listPerYear(salaries);
    }

    private static void listSalaries(final List<Salary> salaries) {
        System.out.printf("Datum\t\tMånadslön\tProvision\tBrutto\t\tNetto\n");
        for (final Salary salary : salaries) {
            System.out.printf("%s\t%d\t\t%d\t\t%d\t\t%d\n",
                    salary.formatDate(),
                    Math.round(salary.salary),
                    Math.round(salary.provision),
                    Math.round(salary.brutto),
                    Math.round(salary.netto)
            );
        }
        System.out.printf("\n");
    }

    private static void listPerYear(final List<Salary> salaries) {
        final Map<Integer, List<Salary>> map = groupByYear(salaries);
        final List<Integer> years = new ArrayList(map.keySet());
        Collections.sort(years);
        years.forEach(year -> {
            listYear(year, map.get(year));
        });
    }

    private static void listYear(final int year, final List<Salary> salaries) {
        double sumSalary = 0;
        double sumProvision = 0;
        double sumBrutto = 0;
        double sumNetto = 0;
        for (final Salary salary : salaries) {
            sumSalary += salary.salary;
            sumProvision += salary.provision;
            sumBrutto += salary.brutto;
            sumNetto += salary.netto;
        }
        System.out.printf("År\t\t%d\n", year);
        System.out.printf("Antal månader\t%d\n", salaries.size());
        System.out.printf("Medel månadslön\t%d\n", Math.round(sumSalary / salaries.size()));
        System.out.printf("Medel provision\t%d\n", Math.round(sumProvision / salaries.size()));
        System.out.printf("Medel brutto\t%d\n", Math.round(sumBrutto / salaries.size()));
        System.out.printf("Medel netto\t%d\n", Math.round(sumNetto / salaries.size()));
        System.out.printf("\n");
    }

    private static Map<Integer, List<Salary>> groupByYear(final List<Salary> salaries) {
        final Map<Integer, List<Salary>> res = new HashMap();
        for (final Salary salary : salaries) {
            final int year = salary.getYear();
            res.putIfAbsent(year, new ArrayList());
            res.get(year).add(salary);
        }
        return res;
    }

}
