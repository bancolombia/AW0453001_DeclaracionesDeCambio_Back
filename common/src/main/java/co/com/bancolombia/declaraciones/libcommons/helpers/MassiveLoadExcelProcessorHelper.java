package co.com.bancolombia.declaraciones.libcommons.helpers;

import co.com.bancolombia.declaraciones.libcommons.exceptions.MassiveLoadException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MassiveLoadExcelProcessorHelper {

    public String getCellDescription(int column, int row) {
        int asciiALetter = 65;
        return (char) (asciiALetter + column) + "" + (row + 1);
    }

    public Date getDateFromString(String dateStr) throws ParseException {
        return new Date(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                .parse(dateStr).getTime());
    }

}
