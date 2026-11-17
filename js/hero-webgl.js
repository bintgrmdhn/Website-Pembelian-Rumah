/* Hero WebGL: memuat assets/rumah.glb, serpihan buyar mengikuti scroll. */
(function () {
  var hero = Rumah.hero,
    ease = Rumah.ease,
    rnd = Rumah.rnd;
  var stage = document.querySelector('.stage');
  if (!window.THREE || !THREE.GLTFLoader) {
    stage.classList.add('nogl');
    return;
  }
  var cv = document.getElementById('gl'),
    R;
  try {
    R = new THREE.WebGLRenderer({ canvas: cv, antialias: true, alpha: true });
  } catch (e) {
    stage.classList.add('nogl');
    return;
  }
  R.setPixelRatio(Math.min(devicePixelRatio || 1, 2));
  R.outputEncoding = THREE.sRGBEncoding;
  R.toneMapping = THREE.ACESFilmicToneMapping;
  R.toneMappingExposure = 0.85;
  var maxA = R.capabilities.getMaxAnisotropy();
  var S = new THREE.Scene(),
    C = new THREE.PerspectiveCamera(28, 1, 40, 1200);
  S.add(new THREE.AmbientLight(0xffffff, 0.38));
  var dl = new THREE.DirectionalLight(0xffffff, 0.7);
  dl.position.set(80, 140, 100);
  S.add(dl);
  S.add(new THREE.HemisphereLight(0xffffff, 0x8a8a99, 0.3));
  var root = new THREE.Group(),
    pivot = new THREE.Group();
  pivot.add(root);
  S.add(pivot);
  var parts = [],
    fw = 1,
    tq = new THREE.Quaternion(),
    te = new THREE.Euler(),
    yaw = 0,
    dn = false,
    lx = 0;
  function size() {
    var w = stage.clientWidth,
      h = stage.clientHeight;
    R.setSize(w, h, false);
    C.aspect = w / h;
    C.updateProjectionMatrix();
    var wide = C.aspect >= 1.15,
      f = wide ? 0.42 : 0.8,
      d = 122 / (f * 2 * Math.tan((14 * Math.PI) / 180) * C.aspect);
    C.position.set(0, d * 0.33, d * 0.94);
    C.lookAt(0, 0, 0);
    fw = 2 * d * Math.tan((14 * Math.PI) / 180) * C.aspect;
    pivot.position.x = wide ? -fw * 0.17 : 0;
    pivot.position.y = wide ? 0 : 2 * d * Math.tan((14 * Math.PI) / 180) * 0.15;
  }
  size();
  addEventListener('resize', size);
  cv.addEventListener('pointerdown', function (e) {
    dn = true;
    lx = e.clientX;
    cv.setPointerCapture(e.pointerId);
  });
  cv.addEventListener('pointermove', function (e) {
    if (dn) {
      yaw += (e.clientX - lx) * 0.008;
      lx = e.clientX;
    }
  });
  ['pointerup', 'pointercancel'].forEach(function (t) {
    cv.addEventListener(t, function () {
      dn = false;
    });
  });
  new THREE.GLTFLoader().load(
    'assets/rumah.glb',
    function (g) {
      var m = g.scene;
      m.updateMatrixWorld(true);
      var ms = [];
      m.traverse(function (o) {
        if (o.isMesh) ms.push(o);
      });
      ms.forEach(function (o) {
        root.attach(o);
      });
      var bb0 = new THREE.Box3().setFromObject(root),
        sz0 = bb0.getSize(new THREE.Vector3()),
        mx = Math.max(sz0.x, sz0.y, sz0.z);
      ms.forEach(function (o) {
        o.geometry = o.geometry.clone();
        o.geometry.computeBoundingBox();
        var c = new THREE.Vector3();
        o.geometry.boundingBox.getCenter(c);
        o.geometry.translate(-c.x, -c.y, -c.z);
        o.position.add(c.clone().multiply(o.scale).applyQuaternion(o.quaternion));
        [].concat(o.material).forEach(function (mt) {
          mt.metalness = 0;
          mt.roughness = 1;
          mt.polygonOffset = true;
          mt.polygonOffsetFactor = 1;
          mt.polygonOffsetUnits = 1 + (ms.indexOf(o) % 12);
          if (mt.map) mt.map.anisotropy = maxA;
        });
        var dir = new THREE.Vector3(rnd() - 0.5, rnd() - 0.25, (rnd() - 0.5) * 0.35)
          .normalize()
          .multiplyScalar(mx * (0.7 + rnd() * 1.3));
        parts.push({
          o: o,
          p: o.position.clone(),
          q: o.quaternion.clone(),
          d: dir,
          r: [(rnd() - 0.5) * 9, (rnd() - 0.5) * 9, (rnd() - 0.5) * 9],
          k: 0.75 + rnd() * 0.5,
          s: o.scale.clone(),
        });
      });
      root.updateMatrixWorld(true);
      var bb = new THREE.Box3().setFromObject(root),
        ct = bb.getCenter(new THREE.Vector3()),
        sc = 100 / mx;
      root.scale.setScalar(sc);
      root.position.set(-ct.x * sc, -ct.y * sc, -ct.z * sc);
      hero.gl = function (n) {
        var h = document.getElementById('hero'),
          t = Math.min(1, Math.max(0, -h.getBoundingClientRect().top / (h.offsetHeight - innerHeight)));
        var p = Math.max(ease(Math.min(1, t / 0.55)), hero.intro);
        var e = Math.min(1, Math.max(0, (t - 0.45) / 0.3));
        cv.style.opacity = 1 - e;
        parts.forEach(function (a) {
          var q = Math.min(1, p * a.k);
          a.o.position.copy(a.p).addScaledVector(a.d, q + e * 1.6);
          a.o.scale.copy(a.s).multiplyScalar(1 - e * 0.92);
          te.set(a.r[0] * q, a.r[1] * q, a.r[2] * q);
          tq.setFromEuler(te);
          a.o.quaternion.copy(a.q).multiply(tq);
        });
        pivot.rotation.y = -0.5 + yaw + p * 0.7 + (hero.sw * (1 - p) * Math.PI) / 180;
        if (e < 1) R.render(S, C);
      };
      stage.classList.add('glon');
    },
    undefined,
    function () {
      stage.classList.add('nogl');
    },
  );
})();
