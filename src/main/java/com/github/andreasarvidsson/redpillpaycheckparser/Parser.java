package com.github.andreasarvidsson.redpillpaycheckparser;

import static com.github.andreasarvidsson.redpillpaycheckparser.Main.FORMAT;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author Andreas Arvidsson
 */
public class Parser {

    public static List<Salary> parseFolder(final String path) throws Exception {
        final List<Salary> res = new ArrayList();
        final File folder = new File(path);
        final File[] files = folder.listFiles((final File dir, final String fileName)
                -> fileName.endsWith(".pdf")
        );
        for (final File file : files) {
            res.add(
                    parseFile(file)
            );
        }
        Collections.sort(res);
        return res;
    }

    public static Salary parseFile(final File file) throws Exception {
        final String text = convertPDFToTxt(file);
        final String[] lines = text.split("\r\n");
        String date = null;
        String salary = null;
        String provision = "0";
        String brutto = null;
        String netto = null;
        for (int i = 0; i < lines.length; ++i) {
            final String line = lines[i];
            if (i == 7) {
                date = line;
            }
            else if (line.startsWith("040 Månadslön")) {
                salary = line.replace("040 Månadslön ", "");
            }
            else if (line.startsWith("140 Provision konsulter")) {
                final int index = line.lastIndexOf(" ") + 1;
                final int index2 = line.indexOf(",", index) + 3;
                provision = line.substring(index, index2);
            }
            else if (line.endsWith("Bruttolön Nettolön")) {
                final int i1 = line.indexOf(",") + 3;
                final int i2 = line.lastIndexOf(",") + 3;
                netto = line.substring(0, i1);
                brutto = line.substring(i1, i2);
            }
        }
        return new Salary(
                FORMAT.parse(date),
                toDouble(brutto),
                toDouble(netto),
                toDouble(salary),
                toDouble(provision)
        );
    }

    private static String convertPDFToTxt(final File file) throws IOException {
        final byte[] thePDFFileBytes = readFileAsBytes(file);
        try (PDDocument pddDoc = PDDocument.load(thePDFFileBytes)) {
            final PDFTextStripper reader = new PDFTextStripper();
            return reader.getText(pddDoc);
        }
    }

    private static byte[] readFileAsBytes(final File file) throws IOException {
        final FileInputStream inputStream = new FileInputStream(file);
        return IOUtils.toByteArray(inputStream);
    }

    private static double toDouble(final String s) {
        return Double.parseDouble(s.replace(" ", "").replace(",", "."));
    }
}
