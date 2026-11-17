/* Loop utama: progres scroll hero, teks besar, panel spesifikasi, header yang bersembunyi. */
(function () {
  var hero = Rumah.hero,
    ease = Rumah.ease,
    rm = Rumah.rm;
  var ly = 0,
    hid = false,
    bar = document.querySelector('.bar'),
    t0 = performance.now();
  function cnt() {
    document.querySelectorAll('#spec em').forEach(function (el) {
      var n = +el.dataset.n,
        d = +el.dataset.d,
        st = performance.now();
      (function f(now) {
        var k = Math.min(1, (now - st) / 1600);
        k = 1 - Math.pow(1 - k, 3);
        el.textContent = (n * k).toLocaleString('id-ID', {
          minimumFractionDigits: d,
          maximumFractionDigits: d,
        });
        if (k < 1) requestAnimationFrame(f);
      })(st);
    });
  }
  function loop(n) {
    var hh = document.getElementById('hero'),
      tt = Math.min(1, Math.max(0, -hh.getBoundingClientRect().top / (hh.offsetHeight - innerHeight)));
    document.getElementById('big').style.opacity = Math.max(0, 1 - tt * 4);
    var sp = Math.min(1, Math.max(0, (tt - 0.55) / 0.2)),
      spE = document.getElementById('spec');
    spE.style.opacity = sp;
    spE.style.transform = 'translateY(' + (1 - sp) * 44 + 'px)';
    var o2 = spE.classList.contains('on');
    if (sp > 0.12 && !o2) {
      spE.classList.add('on');
      cnt();
    } else if (sp < 0.02 && o2) spE.classList.remove('on');
    var y = scrollY;
    if (y < 80 || y < ly - 4) hid = false;
    else if (y > ly + 4) hid = true;
    ly = y;
    bar.style.transform = hid ? 'translateY(-130%)' : 'none';
    bar.style.pointerEvents = hid ? 'none' : 'auto';
    hero.intro = rm ? 0 : 1 - ease(Math.min(1, Math.max(0, (n - t0 - 300) / 2400)));
    hero.sw = rm ? 0 : Math.sin(n / 1500) * 3;
    if (hero.gl) hero.gl(n);
    else hero.updateDom();
    requestAnimationFrame(loop);
  }
  hero.updateDom();
  requestAnimationFrame(loop);
})();
