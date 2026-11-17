/* Namespace bersama untuk state dan helper lintas file. Dimuat paling awal. */
window.Rumah = {
  /* pengguna meminta gerak dikurangi */
  rm: matchMedia('(prefers-reduced-motion: reduce)').matches,
  ease: function (t) {
    return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
  },
  /* angka acak berseed tetap, supaya serpihan rumah selalu tersusun sama */
  rnd: (function () {
    var seed = 7;
    return function () {
      seed = (seed * 16807) % 2147483647;
      return seed / 2147483647;
    };
  })(),
  /* state hero yang dibaca/ditulis beberapa file */
  hero: {
    intro: 0 /* diisi hero-scroll.js */,
    sw: 0,
    gl: null /* fungsi render WebGL, diisi hero-webgl.js setelah model dimuat */,
    updateDom: function () {} /* fallback 3D CSS, diisi hero-dom.js */,
  },
};
Rumah.hero.intro = Rumah.rm ? 0 : 1;
