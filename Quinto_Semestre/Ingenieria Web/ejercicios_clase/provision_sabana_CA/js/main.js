'use strict';

document.addEventListener('DOMContentLoaded', () => {

  /* ── Navegación móvil ────────────────────────────────── */
  const toggle  = document.querySelector('.nav-toggle');
  const navMenu = document.querySelector('.nav-menu');

  toggle?.addEventListener('click', () => {
    toggle.classList.toggle('open');
    navMenu.classList.toggle('open');
    toggle.setAttribute('aria-expanded', navMenu.classList.contains('open'));
  });

  navMenu?.querySelectorAll('a').forEach(a =>
    a.addEventListener('click', () => {
      toggle?.classList.remove('open');
      navMenu.classList.remove('open');
    })
  );

  /* ── Scroll-to-top ───────────────────────────────────── */
  const scrollBtn = document.querySelector('.scroll-top');
  if (scrollBtn) {
    const onScroll = () => scrollBtn.classList.toggle('show', window.scrollY > 320);
    window.addEventListener('scroll', onScroll, { passive: true });
    scrollBtn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
  }

  /* ── Tabs del catálogo (productos.html) ──────────────── */
  const tabBtns   = document.querySelectorAll('.tab-btn');
  const catSects  = document.querySelectorAll('.cat-section');

  if (tabBtns.length) {
    tabBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        tabBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        const target = btn.dataset.target;
        catSects.forEach(s => {
          s.style.display = (!target || target === 'all' || s.id === target) ? '' : 'none';
        });

        if (target && target !== 'all') {
          const el = document.getElementById(target);
          if (el) {
            const offset = document.querySelector('.cat-tabs')?.offsetHeight + 10 || 0;
            const top = el.getBoundingClientRect().top + window.scrollY - document.querySelector('header').offsetHeight - offset;
            window.scrollTo({ top, behavior: 'smooth' });
          }
        }
      });
    });
  }

  /* ── Filtros de galería (galeria.html) ───────────────── */
  const filterBtns = document.querySelectorAll('.filter-btn');
  const gallItems  = document.querySelectorAll('.masonry-item');

  if (filterBtns.length) {
    filterBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        filterBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        const cat = btn.dataset.cat;
        gallItems.forEach(item => {
          const show = cat === 'all' || item.dataset.cat === cat;
          item.classList.toggle('hidden', !show);
        });
      });
    });
  }

  /* ── Lightbox ────────────────────────────────────────── */
  const lb       = document.querySelector('.lightbox');
  const lbImg    = lb?.querySelector('img');
  const lbCap    = lb?.querySelector('.lb-caption');
  const lbClose  = lb?.querySelector('.lb-close');

  if (lb) {
    document.querySelectorAll('.masonry-item').forEach(item => {
      item.addEventListener('click', () => {
        const img = item.querySelector('img');
        lbImg.src = img?.src;
        lbImg.alt = img?.alt;
        if (lbCap) lbCap.textContent = item.querySelector('.masonry-overlay span')?.textContent ?? '';
        lb.classList.add('show');
        document.body.style.overflow = 'hidden';
      });
    });

    const closeLb = () => { lb.classList.remove('show'); document.body.style.overflow = ''; };
    lbClose?.addEventListener('click', closeLb);
    lb.addEventListener('click', e => { if (e.target === lb) closeLb(); });
    document.addEventListener('keydown', e => { if (e.key === 'Escape') closeLb(); });
  }

  /* ── Animación suave al hacer scroll (Intersection Observer) ── */
  const animEls = document.querySelectorAll('.feat-card, .product-card, .masonry-item, .c-item');
  if ('IntersectionObserver' in window && animEls.length) {
    const io = new IntersectionObserver(entries => {
      entries.forEach(e => {
        if (e.isIntersecting) {
          e.target.style.opacity = '1';
          e.target.style.transform = 'translateY(0)';
          io.unobserve(e.target);
        }
      });
    }, { threshold: 0.1 });

    animEls.forEach(el => {
      el.style.opacity = '0';
      el.style.transform = 'translateY(24px)';
      el.style.transition = 'opacity .5s ease, transform .5s ease';
      io.observe(el);
    });
  }
});
