/* Pemetaan lahan 3D (three.js): batas lahan, sempadan, bangunan, ruang hijau. */
(function () {
  var sec = document.getElementById('lahan');
  if (!sec || !window.THREE) return;
  var T = THREE,
    wrap = document.getElementById('lhw'),
    cv = document.getElementById('lhc'),
    lb = document.getElementById('lhl'),
    na = document.getElementById('lhna'),
    R;
  try {
    R = new T.WebGLRenderer({ canvas: cv, antialias: true, alpha: true });
  } catch (e) {
    wrap.classList.add('lh-off');
    return;
  }
  var rm = Rumah.rm;
  R.setPixelRatio(Math.min(devicePixelRatio || 1, 2));
  R.outputEncoding = T.sRGBEncoding;
  R.toneMapping = T.ACESFilmicToneMapping;
  R.toneMappingExposure = 0.92;
  R.shadowMap.enabled = true;
  R.shadowMap.type = T.PCFSoftShadowMap;
  var S = new T.Scene(),
    C = new T.PerspectiveCamera(32, 1, 1, 600);
  S.add(new T.HemisphereLight(0xffffff, 0x98a0b2, 0.85));
  var dl = new T.DirectionalLight(0xffffff, 0.75);
  dl.position.set(-28, 52, 34);
  dl.castShadow = true;
  dl.shadow.mapSize.set(1024, 1024);
  var sc = dl.shadow.camera;
  sc.left = -42;
  sc.right = 42;
  sc.top = 42;
  sc.bottom = -42;
  sc.near = 1;
  sc.far = 150;
  dl.shadow.bias = -0.0006;
  S.add(dl);
  var sd = 11;
  function rr() {
    sd = (sd * 16807) % 2147483647;
    return sd / 2147483647;
  }
  var G = {
    ctx: new T.Group(),
    plot: new T.Group(),
    set: new T.Group(),
    bld: new T.Group(),
    grn: new T.Group(),
  };
  for (var kk in G) S.add(G[kk]);
  var gp = new T.Mesh(new T.PlaneGeometry(300, 300), new T.ShadowMaterial({ opacity: 0.16 }));
  gp.rotation.x = -Math.PI / 2;
  gp.position.y = -0.02;
  gp.receiveShadow = true;
  S.add(gp);
  var grow = [],
    dashes = [],
    trees = [],
    labels = [];
  function bx(g, w, h, d, col, x, y, z, s, e, ax) {
    var m = new T.Mesh(
      new T.BoxGeometry(w, h, d).translate(0, h / 2, 0),
      new T.MeshStandardMaterial({ color: col, roughness: 0.9, metalness: 0 }),
    );
    m.position.set(x, y, z);
    m.castShadow = true;
    m.receiveShadow = true;
    g.add(m);
    ax = ax || 'y';
    grow.push({ m: m, s: s, e: e, ax: ax });
    if (!rm) m.scale[ax] = 0.001;
    return m;
  }
  /* konteks: lahan tetangga, jalan */
  bx(G.ctx, 12, 0.3, 30, 0xe7e2d8, -15.4, 0, 0, 0, 0.25);
  bx(G.ctx, 12, 0.3, 30, 0xe7e2d8, 15.4, 0, 0, 0, 0.25);
  bx(G.ctx, 8, 5, 10, 0xc9c9cf, -16, 0.3, -4, 0.15, 0.4);
  bx(G.ctx, 8, 4, 9, 0xc9c9cf, 16, 0.3, 5, 0.2, 0.45);
  bx(G.ctx, 170, 0.3, 7, 0x3d3f48, 0, 0, 18.5, 0, 0.25);
  for (var i = -14; i <= 14; i++) bx(G.ctx, 1.6, 0.04, 0.2, 0xffffff, i * 3.2, 0.3, 18.5, 0.2, 0.35);
  /* lahan */
  bx(G.plot, 18, 0.4, 30, 0xdce5d0, 0, 0, 0, 0, 0.2);
  var BL = 0x2b36ff;
  bx(G.plot, 18.25, 0.14, 0.25, BL, 0, 0.4, -15, 0.15, 0.35, 'x');
  bx(G.plot, 18.25, 0.14, 0.25, BL, 0, 0.4, 15, 0.15, 0.35, 'x');
  bx(G.plot, 0.25, 0.14, 30.25, BL, -9, 0.4, 0, 0.15, 0.35, 'z');
  bx(G.plot, 0.25, 0.14, 30.25, BL, 9, 0.4, 0, 0.15, 0.35, 'z');
  [
    [-9, -15],
    [9, -15],
    [9, 15],
    [-9, 15],
  ].forEach(function (p) {
    bx(G.plot, 0.5, 1.4, 0.5, BL, p[0], 0.4, p[1], 0.3, 0.45);
  });
  /* sempadan (garis putus-putus) */
  (function (x0, z0, x1, z1) {
    [
      [x0, z0, x1, z0],
      [x1, z0, x1, z1],
      [x1, z1, x0, z1],
      [x0, z1, x0, z0],
    ].forEach(function (g) {
      var dx = g[2] - g[0],
        dz = g[3] - g[1],
        L = Math.hypot(dx, dz),
        ux = dx / L,
        uz = dz / L,
        h = Math.abs(ux) > 0.5;
      for (var t = 0; t + 0.8 <= L; t += 1.5) {
        var m = new T.Mesh(
          new T.BoxGeometry(h ? 0.8 : 0.18, 0.08, h ? 0.18 : 0.8),
          new T.MeshBasicMaterial({ color: 0xe58a00 }),
        );
        m.position.set(g[0] + ux * (t + 0.4), 0.44, g[1] + uz * (t + 0.4));
        G.set.add(m);
        dashes.push(m);
      }
    });
  })(-5.6, -12, 5.6, 11);
  /* ruang hijau */
  bx(G.grn, 18, 0.06, 30, 0x9ec67f, 0, 0.4, 0, 0.2, 0.4);
  bx(G.grn, 5, 0.04, 2.6, 0x6bc3da, 2.4, 0.43, -8, 0.6, 0.75);
  bx(G.plot, 4, 0.08, 4, 0xa9abb3, -2.8, 0.4, 13, 0.5, 0.7);
  function tree(x, z, s, n) {
    var g = new T.Group(),
      tr = new T.Mesh(
        new T.CylinderGeometry(0.12 * s, 0.18 * s, 1.6 * s, 6).translate(0, 0.8 * s, 0),
        new T.MeshStandardMaterial({ color: 0x6b4a2f, roughness: 1 }),
      ),
      cn = new T.Mesh(
        new T.IcosahedronGeometry(1.2 * s, 1),
        new T.MeshStandardMaterial({
          color: [0x3f7d3a, 0x4f8f43, 0x2f6b34][n % 3],
          roughness: 1,
          flatShading: true,
        }),
      );
    cn.position.y = 2.1 * s;
    tr.castShadow = true;
    cn.castShadow = true;
    g.add(tr);
    g.add(cn);
    g.position.set(x, 0.46, z);
    G.grn.add(g);
    trees.push({ g: g, sz: 1, ts: 0.6 + rr() * 0.25 });
    g.scale.setScalar(rm ? 1 : 0.001);
  }
  for (var i = 0; i < 7; i++) tree(-7.5 + rr() * 15, -14 + rr() * 4.5, 0.8 + rr() * 0.5, i);
  for (var i = 0; i < 3; i++) {
    tree(-7.7 - rr() * 0.5, -8 + i * 6 + rr() * 2, 0.8 + rr() * 0.4, i);
    tree(7.7 + rr() * 0.5, -6 + i * 6 + rr() * 2, 0.8 + rr() * 0.4, i + 1);
  }
  tree(-7.4, 13, 0.6, 0);
  tree(7.4, 13, 0.6, 2);
  /* bangunan */
  var B = [
    [11.2, 3.2, 13.2, 0x2c2e36, 0, 0.4, 4.4],
    [4, 2.4, 0.15, 0x171920, -2.8, 0.4, 11.05],
    [3.6, 2.6, 0.15, 0x80786b, 2.6, 0.4, 11.05],
    [10.6, 3.1, 11.8, 0xf1deb0, 0, 3.6, 4],
    [7.6, 2.3, 0.15, 0x9fc4cf, -0.8, 4, 9.95],
    [2.4, 2.5, 0.15, 0x3fa88c, 3.6, 3.95, 9.95],
    [11.8, 0.25, 13.8, 0x20222a, 0, 6.7, 4.4],
    [10, 3, 10.6, 0xf1deb0, -0.2, 6.95, 3.2],
  ];
  B.forEach(function (b, n) {
    var s0 = 0.4 + n * 0.035;
    bx(G.bld, b[0], b[1], b[2], b[3], b[4], b[5], b[6], s0, s0 + 0.22);
  });
  for (var i = 0; i < 9; i++) {
    var s1 = 0.62 + i * 0.02;
    bx(G.bld, 0.18, 2.8, 0.3, 0xa2703c, -4.6 + i * 1.1, 7.05, 8.62, s1, s1 + 0.2);
  }
  bx(G.bld, 11.4, 0.3, 12.8, 0x20222a, 0, 9.95, 4, 0.8, 1);
  /* label */
  [
    ['18 m', 0, 0.8, -16.6, 'plot', 0.4],
    ['30 m', -10.9, 0.8, 0, 'plot', 0.4],
    ['Jalan', 0, 0.8, 19.2, 'ctx', 0.3],
    ['Sempadan samping 3,4 m', -7.3, 1, -5, 'set', 0.55],
    ['Bangunan ±148 m²', 0, 11, 4.4, 'bld', 0.9],
    ['Taman belakang', -3, 1.6, -11.5, 'grn', 0.8],
    ['Kolam', 3, 1.4, -8, 'grn', 0.8],
  ].forEach(function (l) {
    var el = document.createElement('div');
    el.className = 'lh-l';
    el.textContent = l[0];
    lb.appendChild(el);
    labels.push({ el: el, v: new T.Vector3(l[1], l[2], l[3]), g: l[4], s: l[5] });
  });
  /* kamera & interaksi */
  var mg = null,
    mt0 = 0,
    loaded = false,
    rotQ = 0;
  function loadModel() {
    if (loaded || !T.GLTFLoader) return;
    loaded = true;
    try {
      new T.GLTFLoader().load(
        'assets/rumah.glb',
        function (g) {
          var m = g.scene;
          m.updateMatrixWorld(true);
          var bb = new T.Box3().setFromObject(m),
            sz = bb.getSize(new T.Vector3()),
            ct = bb.getCenter(new T.Vector3());
          m.traverse(function (o) {
            if (o.isMesh) {
              o.castShadow = true;
              o.receiveShadow = true;
              [].concat(o.material).forEach(function (mt) {
                mt.metalness = 0;
                mt.roughness = 1;
                mt.polygonOffset = true;
                mt.polygonOffsetFactor = 1;
                mt.polygonOffsetUnits = 1;
                if (mt.map) mt.map.anisotropy = R.capabilities.getMaxAnisotropy();
              });
            }
          });
          var inner = new T.Group();
          m.position.set(-ct.x, -bb.min.y, -ct.z);
          inner.add(m);
          inner.scale.setScalar(11.2 / sz.x);
          var grp = new T.Group();
          grp.add(inner);
          grp.position.set(0, 0.4, 4.4);
          grp.rotation.y = (rotQ * Math.PI) / 2;
          G.bld.children.slice().forEach(function (o) {
            o.visible = false;
          });
          G.bld.add(grp);
          mg = grp;
          mt0 = performance.now();
          if (!rm) mg.scale.y = 0.001;
        },
        undefined,
        function () {},
      );
    } catch (e) {}
  }
  document.getElementById('lhr').addEventListener('click', function () {
    rotQ = (rotQ + 1) % 4;
    if (mg) mg.rotation.y = (rotQ * Math.PI) / 2;
  });
  var yaw = -0.6,
    pit = 0.66,
    tYaw = -0.6,
    tPit = 0.66,
    view = '3d',
    dn = false,
    lx = 0,
    ly = 0,
    vis = false,
    ent = false,
    t0 = 0,
    last = performance.now(),
    W = 1,
    H = 1;
  function size() {
    W = wrap.clientWidth || 1;
    H = wrap.clientHeight || 1;
    R.setSize(W, H, false);
    C.aspect = W / H;
    C.updateProjectionMatrix();
  }
  size();
  addEventListener('resize', size);
  function ez(p) {
    p = p < 0 ? 0 : p > 1 ? 1 : p;
    return 1 - Math.pow(1 - p, 3);
  }
  function bk(p) {
    p = p < 0 ? 0 : p > 1 ? 1 : p;
    var a = 1.70158,
      b = a + 1;
    return 1 + b * Math.pow(p - 1, 3) + a * Math.pow(p - 1, 2);
  }
  function frame(now) {
    requestAnimationFrame(frame);
    var dt = Math.min(0.05, (now - last) / 1000);
    last = now;
    if (!vis) return;
    var k = rm ? 1 : ent ? Math.min(1, (now - t0) / 3400) : 0;
    grow.forEach(function (a) {
      a.m.scale[a.ax] = rm ? 1 : Math.max(0.001, ez((k - a.s) / (a.e - a.s)));
    });
    var nd = dashes.length,
      kd = rm ? 1 : ez((k - 0.3) / 0.25);
    dashes.forEach(function (m, i) {
      m.visible = i < kd * nd;
    });
    trees.forEach(function (t) {
      t.g.scale.setScalar(Math.max(0.001, rm ? 1 : bk((k - t.ts) / 0.15)));
    });
    if (mg) mg.scale.y = rm ? 1 : Math.max(0.001, ez((now - Math.max(mt0, t0 + 1500)) / 1400));
    if (view === '3d' && !dn) tYaw += 0.06 * dt;
    yaw += (tYaw - yaw) * Math.min(1, dt * 6);
    pit += (tPit - pit) * Math.min(1, dt * 6);
    var d = Math.max(70, 91 / C.aspect);
    C.position.set(d * Math.cos(pit) * Math.sin(yaw), d * Math.sin(pit), d * Math.cos(pit) * Math.cos(yaw));
    C.lookAt(0, 1, 0);
    na.style.transform = 'rotate(' + yaw * 57.3 + 'deg)';
    R.render(S, C);
    labels.forEach(function (l) {
      var v = l.v.clone().project(C),
        on = G[l.g].visible,
        show = on && k >= l.s && v.z < 1;
      l.el.style.opacity = show ? 1 : 0;
      l.el.style.transform =
        'translate(' + (v.x * 0.5 + 0.5) * W + 'px,' + (-v.y * 0.5 + 0.5) * H + 'px) translate(-50%,-50%)';
    });
  }
  requestAnimationFrame(frame);
  new IntersectionObserver(
    function (es) {
      es.forEach(function (e) {
        vis = e.isIntersecting;
        if (vis) loadModel();
        if (e.intersectionRatio >= 0.3 && !ent) {
          ent = true;
          t0 = performance.now();
          size();
        }
        if (!e.isIntersecting) ent = false;
      });
    },
    { threshold: [0, 0.3] },
  ).observe(wrap);
  cv.addEventListener('pointerdown', function (e) {
    dn = true;
    lx = e.clientX;
    ly = e.clientY;
    try {
      cv.setPointerCapture(e.pointerId);
    } catch (x) {}
  });
  cv.addEventListener('pointermove', function (e) {
    if (!dn) return;
    tYaw -= (e.clientX - lx) * 0.006;
    if (e.pointerType === 'mouse') tPit = Math.min(1.5, Math.max(0.25, tPit + (e.clientY - ly) * 0.005));
    lx = e.clientX;
    ly = e.clientY;
  });
  ['pointerup', 'pointercancel'].forEach(function (t) {
    cv.addEventListener(t, function () {
      dn = false;
    });
  });
  [].forEach.call(document.querySelectorAll('.lh-ch'), function (b) {
    b.addEventListener('click', function () {
      var g = G[b.dataset.g];
      g.visible = !g.visible;
      b.classList.toggle('on', g.visible);
    });
  });
  [].forEach.call(document.querySelectorAll('.lh-v'), function (b) {
    b.addEventListener('click', function () {
      view = b.dataset.v;
      [].forEach.call(document.querySelectorAll('.lh-v'), function (x) {
        x.classList.toggle('on', x === b);
      });
      yaw = yaw % (2 * Math.PI);
      if (view === 'top') {
        tPit = 1.5;
        tYaw = 0;
      } else {
        tPit = 0.66;
        tYaw = -0.6;
      }
    });
  });
})();
