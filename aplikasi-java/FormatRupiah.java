import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Kumpulan method bantu untuk menulis angka dengan format Indonesia,
 * misalnya 1250000 menjadi "Rp 1.250.000".
 */
public class FormatRupiah {

    private static DecimalFormat buatFormat(String pola) {
        DecimalFormatSymbols simbol = new DecimalFormatSymbols();
        simbol.setGroupingSeparator('.');
        simbol.setDecimalSeparator(',');
        return new DecimalFormat(pola, simbol);
    }

    /** 1250000 -> "1.250.000" */
    public static String angka(long nilai) {
        return buatFormat("#,##0").format(nilai);
    }

    /** 1250000 -> "Rp 1.250.000" */
    public static String rp(long nilai) {
        return "Rp " + angka(nilai);
    }

    /** 7.5 -> "7,5" (maksimal dua angka di belakang koma) */
    public static String persen(double nilai) {
        return buatFormat("0.##").format(nilai);
    }
}
