/* UI: animasi muncul saat scroll, menu geser, tombol tema. */
(function () {
  document.documentElement.classList.add('js');
  document.querySelectorAll('#tentang h2').forEach(function (h) {
    var w = h.textContent.trim().split(/\s+/);
    h.innerHTML = w
      .map(function (x, i) {
        return '<span class="wd"><span style="transition-delay:' + i * 0.09 + 's">' + x + '</span></span>';
      })
      .join(' ');
    h.classList.add('sp');
  });
  document
    .querySelectorAll(
      '#tentang .two,#karya h2,#kontak h2,#kontak p,#kontak .cols,#lahan .lh-info,#lahan .lh-cv',
    )
    .forEach(function (e, i) {
      e.classList.add('rv');
      e.style.setProperty('--d', (i % 3) * 0.12 + 's');
    });
  document.querySelectorAll('.card').forEach(function (c, i) {
    c.style.setProperty('--d', (i % 2) * 0.14 + 's');
  });
  var io = new IntersectionObserver(
    function (es) {
      es.forEach(function (e) {
        if (e.isIntersecting) {
          e.target.classList.add('in');
          io.unobserve(e.target);
        }
      });
    },
    { threshold: 0.15 },
  );
  document.querySelectorAll('.rv,.sp,.card').forEach(function (e) {
    io.observe(e);
  });
  var m = document.getElementById('menu');
  document.getElementById('mb').onclick = function () {
    m.classList.add('open');
  };
  document.getElementById('mc').onclick = function () {
    m.classList.remove('open');
  };
  m.addEventListener('click', function (e) {
    var a = e.target.closest('a');
    if (!a) return;
    m.classList.remove('open');
    if (a.dataset.spec) {
      e.preventDefault();
      var h = document.getElementById('hero');
      scrollTo({ top: h.offsetTop + (h.offsetHeight - innerHeight) * 0.88, behavior: 'smooth' });
    }
  });
  document.getElementById('th').onclick = function () {
    var d = document.documentElement,
      dk = d.dataset.theme === 'dark';
    d.dataset.theme = dk ? 'light' : 'dark';
  };
})();
