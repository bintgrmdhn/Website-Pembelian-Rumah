/*
 * Aplikasi Pembelian Rumah
 * Copyright (c) 2026 Bintang Rhamadan (NPM 202443501817)
 *                    dan Yusuf Wibisono (NPM 202443501888).
 * Hak cipta dilindungi.
 */
/**
 * Model data untuk satu rumah yang dijual.
 * Objek Rumah menyimpan data saja (tidak ada tampilan di sini).
 */
public class Rumah {
    private final int id;
    private final String nama;
    private final String tipe;
    private final String lokasi;
    private final int luasTanah;      // dalam m2
    private final int luasBangunan;   // dalam m2
    private final int kamarTidur;
    private final int kamarMandi;
    private final long harga;         // dalam rupiah
    private boolean terjual;

    public Rumah(int id, String nama, String tipe, String lokasi,
                 int luasTanah, int luasBangunan, int kamarTidur, int kamarMandi, long harga) {
        this.id = id;
        this.nama = nama;
        this.tipe = tipe;
        this.lokasi = lokasi;
        this.luasTanah = luasTanah;
        this.luasBangunan = luasBangunan;
        this.kamarTidur = kamarTidur;
        this.kamarMandi = kamarMandi;
        this.harga = harga;
        this.terjual = false;
    }

    public int getId()           { return id; }
    public String getNama()      { return nama; }
    public String getTipe()      { return tipe; }
    public String getLokasi()    { return lokasi; }
    public int getLuasTanah()    { return luasTanah; }
    public int getLuasBangunan() { return luasBangunan; }
    public int getKamarTidur()   { return kamarTidur; }
    public int getKamarMandi()   { return kamarMandi; }
    public long getHarga()       { return harga; }
    public boolean isTerjual()   { return terjual; }

    /** Menandai rumah ini sudah terjual. */
    public void tandaiTerjual() {
        terjual = true;
    }

    /** Teks status untuk ditampilkan: "Tersedia" atau "Terjual". */
    public String getStatus() {
        return terjual ? "Terjual" : "Tersedia";
    }

    @Override
    public String toString() {
        return nama + " (" + tipe + ", " + lokasi + ")";
    }
}
