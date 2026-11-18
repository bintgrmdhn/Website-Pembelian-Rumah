# Aplikasi Pembelian Rumah (Java Swing, untuk BlueJ)

Program desktop dengan jendela (Swing): daftar rumah, cari/filter, detail rumah, simulasi KPR, beli rumah dengan struk, dan riwayat pembelian. Semua data rumah hanya contoh dan disimpan di memori (kembali ke awal setiap program dijalankan ulang).

## Cara menjalankan di BlueJ

1. Buka BlueJ, pilih **Project > Open Non BlueJ...**, lalu pilih folder `aplikasi-java` ini.
   (Cara lain: **Project > New Project**, lalu salin semua file `.java` ke folder project itu.)
2. Klik **Compile** di jendela BlueJ. Semua kelas harus tidak ada tanda silang.
3. Klik kanan kelas **AplikasiRumah**, pilih `void main(String[] args)`, lalu **OK**.

Butuh Java 8 atau lebih baru. Tidak ada library tambahan.

## Pembagian kelas

| Kelas | Fungsi |
|---|---|
| `AplikasiRumah` | Jendela utama (tampilan dan tombol). Jalankan kelas ini. |
| `DataRumah` | Daftar rumah, pencarian/filter, dan proses beli. Ubah data contoh di `isiDataContoh()`. |
| `Rumah` | Model satu rumah (nama, tipe, lokasi, luas, harga, status). |
| `SimulasiKPR` | Hitung DP, pokok, cicilan per bulan, dan total bayar (cicilan tetap/anuitas). |
| `Pembelian` | Catatan satu transaksi dan pembuat teks struk. |
| `FormatRupiah` | Menulis angka sebagai `Rp 1.250.000`. |

## Cara pakai

- **Cari**: ketik nama atau lokasi, atau pilih tipe dan rentang harga. Centang "Hanya yang tersedia" untuk menyembunyikan rumah yang sudah terjual. Klik judul kolom untuk mengurutkan.
- **KPR**: pilih rumah di tabel, lalu atur DP, tenor, dan bunga di panel kanan. Hasil cicilan berubah langsung. Bunga boleh ditulis `7.5` atau `7,5`.
- **Beli**: klik **Beli Rumah**, isi nama dan No. HP, pilih Tunai atau KPR, lalu struk muncul. Rumah yang sudah dibeli berstatus Terjual dan tombol beli nonaktif.
- **Riwayat**: klik **Riwayat Pembelian** untuk melihat semua transaksi sejak program dibuka.

## Rumus KPR

```
cicilan = pokok * r / (1 - (1 + r)^(-n))
r = bunga tahunan / 12 / 100        n = tenor (tahun) * 12
pokok = harga - DP
```

Bunga 0 persen berarti pokok dibagi rata per bulan. Ini simulasi sederhana: tidak memasukkan biaya provisi, asuransi, atau bunga yang berubah.

## Hak cipta

Copyright (c) 2026 **Bintang Rhamadan** (NPM 202443501817) dan **Yusuf Wibisono** (NPM 202443501888). Hak cipta dilindungi.
