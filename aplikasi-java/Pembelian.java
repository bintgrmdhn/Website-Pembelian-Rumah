/*
 * Aplikasi Pembelian Rumah
 * Copyright (c) 2026 Bintang Ramadhan (NPM 202443501817)
 *                    dan Yusuf Wibisono (NPM 202443501888).
 * Hak cipta dilindungi.
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Catatan satu transaksi pembelian rumah.
 * Jika parameter kpr bernilai null, berarti pembayaran tunai.
 */
public class Pembelian {
    private final String nomor;
    private final LocalDate tanggal;
    private final Rumah rumah;
    private final String namaPembeli;
    private final String noHp;
    private final SimulasiKPR kpr;   // null = tunai

    public Pembelian(String nomor, LocalDate tanggal, Rumah rumah,
                     String namaPembeli, String noHp, SimulasiKPR kpr) {
        this.nomor = nomor;
        this.tanggal = tanggal;
        this.rumah = rumah;
        this.namaPembeli = namaPembeli;
        this.noHp = noHp;
        this.kpr = kpr;
    }

    public String getNomor()        { return nomor; }
    public Rumah getRumah()         { return rumah; }
    public String getNamaPembeli()  { return namaPembeli; }
    public String getNoHp()         { return noHp; }
    public boolean isKpr()          { return kpr != null; }

    public String getMetode() {
        return isKpr() ? "KPR" : "Tunai";
    }

    public String getTanggalTeks() {
        return tanggal.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /** Total uang yang dibayar pembeli sampai lunas. */
    public long getTotalBayar() {
        return isKpr() ? kpr.getTotalBayar() : rumah.getHarga();
    }

    private static String baris(String label, String isi) {
        return String.format("%-15s: %s\n", label, isi);
    }

    /** Membuat teks struk siap tampil. */
    public String buatStruk() {
        StringBuilder sb = new StringBuilder();
        String garis = "==========================================\n";
        String strip = "------------------------------------------\n";
        sb.append(garis);
        sb.append("          STRUK PEMBELIAN RUMAH\n");
        sb.append(garis);
        sb.append(baris("No. Transaksi", nomor));
        sb.append(baris("Tanggal", getTanggalTeks()));
        sb.append(baris("Pembeli", namaPembeli));
        sb.append(baris("No. HP", noHp));
        sb.append(strip);
        sb.append(baris("Rumah", rumah.getNama()));
        sb.append(baris("Tipe", rumah.getTipe()));
        sb.append(baris("Lokasi", rumah.getLokasi()));
        sb.append(baris("Harga", FormatRupiah.rp(rumah.getHarga())));
        sb.append(strip);
        sb.append(baris("Pembayaran", getMetode()));
        if (isKpr()) {
            sb.append(baris("DP (" + kpr.getDpPersen() + "%)", FormatRupiah.rp(kpr.getDp())));
            sb.append(baris("Pokok pinjaman", FormatRupiah.rp(kpr.getPokok())));
            sb.append(baris("Tenor", kpr.getTenorTahun() + " tahun (" + kpr.getJumlahBulan() + " bulan)"));
            sb.append(baris("Bunga", FormatRupiah.persen(kpr.getBungaPersen()) + "% per tahun"));
            sb.append(baris("Cicilan/bulan", FormatRupiah.rp(kpr.getCicilanPerBulan())));
        }
        sb.append(baris("Total bayar", FormatRupiah.rp(getTotalBayar())));
        sb.append(garis);
        return sb.toString();
    }
}
