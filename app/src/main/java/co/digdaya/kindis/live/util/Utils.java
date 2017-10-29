package co.digdaya.kindis.live.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created by vincenttp on 10/28/17.
 */

public class Utils {
    public static String currencyFormat(int number){
        NumberFormat df = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp ");
        dfs.setGroupingSeparator('.');
        df.setMaximumFractionDigits(0);
        ((DecimalFormat) df).setDecimalFormatSymbols(dfs);
        return df.format(number);
    }
}
