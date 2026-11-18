/*
 * Aplikasi Pembelian Rumah
 * Copyright (c) 2026 Bintang Ramadhan (NPM 202443501817)
 *                    dan Yusuf Wibisono (NPM 202443501888).
 * Hak cipta dilindungi.
 */
/**
 * Simulasi Kredit Pemilikan Rumah (KPR) dengan cicilan tetap (anuitas).
 *
 * Rumus cicilan per bulan:
 *   cicilan = pokok * r / (1 - (1 + r)^(-n))
 * dengan r = bunga per bulan (bunga tahunan / 12 / 100)
 * dan n = jumlah bulan (tenor tahun * 12).
 */
public class SimulasiKPR {
    private final long harga;
    private final int dpPersen;
    private final int tenorTahun;
    private final double bungaPersen;   // bunga per tahun, dalam persen

    public SimulasiKPR(long harga, int dpPersen, int tenorTahun, double bungaPersen) {
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0");
        }
        if (dpPersen < 0 || dpPersen >= 100) {
            throw new IllegalArgumentException("DP harus 0 sampai 99 persen");
        }
        if (tenorTahun <= 0) {
            throw new IllegalArgumentException("Tenor harus lebih dari 0 tahun");
        }
        if (bungaPersen < 0) {
            throw new IllegalArgumentException("Bunga tidak boleh negatif");
        }
        this.harga = harga;
        this.dpPersen = dpPersen;
        this.tenorTahun = tenorTahun;
        this.bungaPersen = bungaPersen;
    }

    public long getHarga()          { return harga; }
    public int getDpPersen()        { return dpPersen; }
    public int getTenorTahun()      { return tenorTahun; }
    public double getBungaPersen()  { return bungaPersen; }

    /** Uang muka yang dibayar di awal. */
    public long getDp() {
        return Math.round(harga * dpPersen / 100.0);
    }

    /** Pokok pinjaman = harga dikurangi DP. */
    public long getPokok() {
        return harga - getDp();
    }

    public int getJumlahBulan() {
        return tenorTahun * 12;
    }

    /** Cicilan yang dibayar setiap bulan (dibulatkan ke rupiah). */
    public long getCicilanPerBulan() {
        return hitungCicilan(getPokok(), bungaPersen, getJumlahBulan());
    }

    /** DP ditambah seluruh cicilan sampai lunas. */
    public long getTotalBayar() {
        return getDp() + getCicilanPerBulan() * getJumlahBulan();
    }

    /** Selisih total bayar dengan harga rumah (biaya bunga). */
    public long getTotalBunga() {
        return getTotalBayar() - harga;
    }

    /** Rumus anuitas. Bunga 0 persen berarti pokok dibagi rata. */
    public static long hitungCicilan(long pokok, double bungaPersen, int bulan) {
        double r = bungaPersen / 100.0 / 12.0;
        if (r == 0) {
            return Math.round((double) pokok / bulan);
        }
        double cicilan = pokok * r / (1 - Math.pow(1 + r, -bulan));
        return Math.round(cicilan);
    }
}
