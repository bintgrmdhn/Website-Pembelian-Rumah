/* Hero fallback: rumah 3D dari <div> CSS, dipakai bila WebGL/model gagal. */
(function () {
  var hero = Rumah.hero,
    rnd = Rumah.rnd,
    ease = Rumah.ease;
  var sc = document.getElementById('scene'),
    pcs = [];
  function box(x, y, z, w, h, d, c, sh) {
    var e = document.createElement('div');
    e.className = 'p';
    e.style.cssText = 'width:0;height:0';
    var F = [
      ['translateZ(' + d / 2 + 'px)', w, h, 1],
      ['rotateY(180deg) translateZ(' + d / 2 + 'px)', w, h, 0.7],
      ['rotateY(90deg) translateZ(' + w / 2 + 'px)', d, h, 0.82],
      ['rotateY(-90deg) translateZ(' + w / 2 + 'px)', d, h, 0.6],
      ['rotateX(90deg) translateZ(' + h / 2 + 'px)', w, d, 1.12],
      ['rotateX(-90deg) translateZ(' + h / 2 + 'px)', w, d, 0.5],
    ];
    F.forEach(function (f) {
      var q = document.createElement('div');
      q.className = 'f';
      q.style.cssText =
        'width:' +
        f[1] +
        'px;height:' +
        f[2] +
        'px;margin:' +
        -f[2] / 2 +
        'px 0 0 ' +
        -f[1] / 2 +
        'px;background:' +
        c +
        ';filter:brightness(' +
        f[3] +
        ');transform:' +
        f[0];
      e.appendChild(q);
    });
    sc.appendChild(e);
    var a = rnd() * 6.28,
      r = 500 + rnd() * 900;
    pcs.push({
      e: e,
      x: x,
      y: y,
      z: z,
      tx: x + Math.cos(a) * r * 1.4,
      ty: y + Math.sin(a) * r * 0.8 - 60,
      tz: z + (rnd() - 0.5) * 1100,
      rx: (rnd() - 0.5) * 720,
      ry: (rnd() - 0.5) * 720,
      rz: (rnd() - 0.5) * 540,
      k: 0.75 + rnd() * 0.5,
    });
  }
  var K = '#2c2e36',
    W = '#a2703c',
    GL = '#f1deb0',
    CR = '#d3c3a8',
    GR2 = '#3f6b34';
  box(0, 8, 40, 840, 10, 560, '#7b7e88');
  box(-20, -70, 0, 520, 140, 300, K);
  box(-130, -60, 154, 200, 112, 6, '#171920');
  box(130, -60, 154, 150, 120, 6, '#80786b');
  box(-20, -132, 10, 560, 12, 330, '#20222a');
  box(-30, -190, -10, 470, 100, 250, GL);
  for (var i = 0; i < 6; i++) box(-250 + i * 20, -190, 118, 8, 96, 8, W);
  box(30, -190, 122, 330, 92, 4, '#e8d6a8');
  box(215, -150, 110, 130, 6, 130, '#c5bdb0');
  box(215, -175, 172, 130, 34, 3, '#9fc4cf');
  box(-30, -244, 10, 500, 10, 300, '#20222a');
  box(-40, -292, -20, 440, 90, 230, GL);
  for (var i = 0; i < 13; i++) box(-230 + i * 34, -292, 96, 12, 92, 14, W);
  box(-200, -262, 60, 100, 16, 60, '#58713f');
  box(-200, -282, 60, 80, 26, 50, '#6a9a45');
  box(-30, -344, 0, 540, 12, 330, '#20222a');
  box(215, -246, 112, 170, 10, 130, '#c79a62');
  box(285, -150, -60, 70, 240, 130, CR);
  box(-290, -120, 140, 90, 40, 40, '#6a9a45');
  box(300, -130, 150, 110, 30, 36, '#5f9440');
  box(-300, -30, 190, 90, 50, 60, GR2);
  box(110, -26, 200, 170, 44, 50, '#4f8a3e');
  box(40, 4, 235, 260, 6, 60, '#5b9142');
  box(-400, -110, 60, 18, 220, 18, '#4b3a2a');
  box(-400, -250, 60, 130, 100, 130, GR2);
  box(-410, -320, 80, 90, 70, 90, '#2f5a2b');
  box(430, -100, 0, 16, 200, 16, '#4b3a2a');
  box(430, -230, 0, 140, 110, 140, GR2);
  box(-330, -16, 210, 56, 30, 40, '#b0b0b4');
  box(380, -28, 200, 120, 40, 56, '#4a4e57');
  box(375, -58, 200, 70, 26, 48, '#3a3d45');
  for (var i = 0; i < 10; i++)
    box(
      -320 + rnd() * 640,
      -20 - rnd() * 60,
      190 + rnd() * 60,
      16 + rnd() * 24,
      10 + rnd() * 14,
      16 + rnd() * 24,
      i % 2 ? '#4f8a3e' : '#6a9a45',
    );
  function up() {
    var h = document.getElementById('hero'),
      t = -h.getBoundingClientRect().top / (h.offsetHeight - innerHeight);
    t = Math.min(1, Math.max(0, t));
    var p = Math.max(ease(Math.min(1, t / 0.85)), hero.intro),
      u = Math.min(innerWidth * (innerWidth < 760 ? 0.001 : 0.00068), innerHeight * 0.0012);
    sc.style.transform =
      'scale(' +
      u * (1 - p * 0.15) +
      ') rotateX(-13deg) rotateY(' +
      (-26 + p * 40 + hero.sw * (1 - p)) +
      'deg)';
    pcs.forEach(function (o) {
      var q = p * o.k > 1 ? 1 : p * o.k;
      o.e.style.transform =
        'translate3d(' +
        (o.x + (o.tx - o.x) * q) +
        'px,' +
        (o.y + (o.ty - o.y) * q) +
        'px,' +
        (o.z + (o.tz - o.z) * q) +
        'px) rotateX(' +
        o.rx * q +
        'deg) rotateY(' +
        o.ry * q +
        'deg) rotateZ(' +
        o.rz * q +
        'deg)';
    });
  }
  hero.updateDom = up;
})();
