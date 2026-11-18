/*
 * Aplikasi Pembelian Rumah
 * Copyright (c) 2026 Bintang Rhamadan (NPM 202443501817)
 *                    dan Yusuf Wibisono (NPM 202443501888).
 * Hak cipta dilindungi.
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Menyimpan daftar rumah dan riwayat pembelian (di memori saja,
 * jadi data kembali ke awal setiap program dijalankan ulang).
 * Semua data rumah di sini hanya contoh.
 */
public class DataRumah {
    private final List<Rumah> daftar = new ArrayList<Rumah>();
    private final List<Pembelian> riwayat = new ArrayList<Pembelian>();

    public DataRumah() {
        isiDataContoh();
    }

    private void isiDataContoh() {
        tambah("Griya Harapan",           "Tipe 30",  "Cikarang",           60,  30, 2, 1,   165_000_000L);
        tambah("Griya Asri Melati",       "Tipe 36",  "Bekasi Timur",       72,  36, 2, 1,   385_000_000L);
        tambah("Cluster Taman Sari",      "Tipe 45",  "Depok",              84,  45, 2, 1,   520_000_000L);
        tambah("Rumah Minimalis Cipta",   "Tipe 45",  "Jakarta Timur",      78,  45, 2, 1,   690_000_000L);
        tambah("Villa Bukit Hijau",       "Tipe 60",  "Bogor",             105,  60, 3, 2,   745_000_000L);
        tambah("Sakura Residence",        "Tipe 60",  "Depok",              96,  60, 3, 2,   810_000_000L);
        tambah("Graha Tropis",            "Tipe 70",  "Bekasi Barat",      112,  70, 3, 2,   880_000_000L);
        tambah("Puri Nusa Indah",         "Tipe 90",  "Jakarta Selatan",   150,  90, 3, 2, 1_450_000_000L);
        tambah("Taman Lestari Modern",    "Tipe 120", "Tangerang",         200, 120, 4, 3, 1_980_000_000L);
        tambah("The Emerald House",       "Tipe 150", "BSD City",          240, 150, 4, 3, 2_650_000_000L);
        tambah("Royal Hill Estate",       "Tipe 200", "Jakarta Selatan",   320, 200, 5, 4, 4_200_000_000L);
    }

    private void tambah(String nama, String tipe, String lokasi,
                        int luasTanah, int luasBangunan, int kt, int km, long harga) {
        int idBaru = daftar.size() + 1;
        daftar.add(new Rumah(idBaru, nama, tipe, lokasi, luasTanah, luasBangunan, kt, km, harga));
    }

    /** Semua rumah (salinan, aman dari perubahan di luar). */
    public List<Rumah> getSemua() {
        return new ArrayList<Rumah>(daftar);
    }

    /** Daftar tipe rumah tanpa duplikat, urut seperti di data. */
    public List<String> getDaftarTipe() {
        List<String> hasil = new ArrayList<String>();
        for (Rumah r : daftar) {
            if (!hasil.contains(r.getTipe())) {
                hasil.add(r.getTipe());
            }
        }
        return hasil;
    }

    /**
     * Mencari rumah dengan beberapa syarat sekaligus.
     *
     * @param kata          dicocokkan dengan nama atau lokasi (kosong = semua)
     * @param tipe          tipe rumah, atau null untuk semua tipe
     * @param minHarga      batas bawah harga (termasuk)
     * @param maxHarga      batas atas harga (tidak termasuk)
     * @param hanyaTersedia true = sembunyikan rumah yang sudah terjual
     */
    public List<Rumah> cari(String kata, String tipe, long minHarga, long maxHarga, boolean hanyaTersedia) {
        String k = (kata == null) ? "" : kata.trim().toLowerCase();
        List<Rumah> hasil = new ArrayList<Rumah>();
        for (Rumah r : daftar) {
            boolean cocokKata = k.isEmpty()
                    || r.getNama().toLowerCase().contains(k)
                    || r.getLokasi().toLowerCase().contains(k);
            boolean cocokTipe = (tipe == null) || r.getTipe().equals(tipe);
            boolean cocokHarga = r.getHarga() >= minHarga && r.getHarga() < maxHarga;
            boolean cocokStatus = !hanyaTersedia || !r.isTerjual();
            if (cocokKata && cocokTipe && cocokHarga && cocokStatus) {
                hasil.add(r);
            }
        }
        return hasil;
    }

    /**
     * Memproses pembelian. Rumah ditandai terjual dan transaksi dicatat.
     *
     * @param kpr data KPR, atau null bila bayar tunai
     * @throws IllegalStateException bila rumah sudah terjual
     */
    public Pembelian beli(Rumah rumah, String namaPembeli, String noHp, SimulasiKPR kpr) {
        if (rumah.isTerjual()) {
            throw new IllegalStateException("Rumah ini sudah terjual.");
        }
        rumah.tandaiTerjual();
        String nomor = String.format("TRX-%04d", riwayat.size() + 1);
        Pembelian p = new Pembelian(nomor, LocalDate.now(), rumah, namaPembeli, noHp, kpr);
        riwayat.add(p);
        return p;
    }

    public List<Pembelian> getRiwayat() {
        return Collections.unmodifiableList(new ArrayList<Pembelian>(riwayat));
    }
}
