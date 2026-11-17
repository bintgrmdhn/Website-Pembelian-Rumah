# Website Pembelian Rumah

Website satu halaman bergaya scroll 3D: rumah 3D yang buyar saat digulir, spesifikasi bangunan, pemetaan lahan 3D, dan profil kontraktor.

## Struktur
```
index.html          markup halaman (hanya HTML)
assets/
  rumah.glb         model 3D rumah
css/                urutan file = urutan dimuat, jangan ditukar
  base.css          variabel tema, reset, tipografi, latar
  header.css        header, logo, tombol pill
  hero.css          panggung 3D hero, teks besar, kanvas WebGL
  sections.css      bagian Tentang, Karya, Kontak
  menu.css          menu navigasi geser
  responsive.css    penyesuaian layar kecil
  animations.css    animasi, transisi, hover, reduced motion
  spec.css          bagian spesifikasi bangunan
  lahan.css         bagian pemetaan lahan 3D
  construction.css  ilustrasi kontraktor (SVG beranimasi)
js/                 urutan file = urutan dimuat, jangan ditukar
  core.js           namespace bersama (window.Rumah): helper dan state
  hero-dom.js       hero fallback 3D CSS (jika WebGL/model gagal)
  hero-webgl.js     hero three.js, memuat rumah.glb
  hero-scroll.js    loop animasi, progres scroll, header, hitung angka
  ui.js             reveal saat scroll, menu, tombol tema
  lahan.js          pemetaan lahan 3D
```

## Cara upload ke repo
```bash
git clone https://github.com/bintgrmdhn/Website-Pembelian-Rumah.git
cd Website-Pembelian-Rumah
# salin index.html, folder assets, css, dan js ke sini (langsung di root repo)
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
- Data spesifikasi dan lahan masih contoh. Teks spesifikasi dan profil ada di `index.html`, sedangkan angka dan geometri peta lahan ada di `js/lahan.js`.
- Semua file JS dimuat sebagai script biasa (bukan module) supaya halaman tetap tampil dengan fallback 3D CSS walau dibuka tanpa server.
