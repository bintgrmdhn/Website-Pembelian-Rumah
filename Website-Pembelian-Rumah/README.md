# Website Pembelian Rumah

Website satu halaman bergaya scroll 3D: rumah 3D yang buyar saat digulir, spesifikasi bangunan, pemetaan lahan 3D, dan profil kontraktor.

## Isi
- `index.html` : halaman utama (HTML, CSS, JS dalam satu file)
- `assets/rumah.glb` : model 3D rumah

## Cara upload ke repo
```bash
git clone https://github.com/bintgrmdhn/Website-Pembelian-Rumah.git
cd Website-Pembelian-Rumah
# salin index.html dan folder assets ke sini
git add .
git commit -m "Tambah website rumah 3D"
git push origin main   # ganti main dengan master jika branch Anda bernama master
```

## Aktifkan GitHub Pages
1. Buka repo di GitHub, lalu **Settings > Pages**.
2. Di **Build and deployment**, pilih **Deploy from a branch**.
3. Pilih branch `main` dan folder `/ (root)`, lalu **Save**.
4. Tunggu 1 sampai 2 menit. Alamatnya: `https://bintgrmdhn.github.io/Website-Pembelian-Rumah/`

## Menjalankan di komputer sendiri
Jangan buka `index.html` dengan klik ganda (model 3D tidak akan termuat karena aturan keamanan browser). Jalankan server lokal:
```bash
python3 -m http.server 8000
```
lalu buka `http://localhost:8000`.

## Catatan
- Halaman memuat three.js dari jsDelivr (butuh internet).
- Data spesifikasi dan lahan masih contoh. Ganti dengan data asli di `index.html`.
