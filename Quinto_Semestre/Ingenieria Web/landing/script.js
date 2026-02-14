/**
 * DevMatch Landing Page - Scripts
 */

// ============================================
// NAVBAR SCROLL EFFECT
// ============================================
const navbar = document.getElementById('navbar');

window.addEventListener('scroll', () => {
    if (window.scrollY > 50) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
});

// ============================================
// SMOOTH SCROLL PARA ENLACES INTERNOS
// ============================================
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        
        const targetId = this.getAttribute('href');
        const targetElement = document.querySelector(targetId);
        
        if (targetElement) {
            targetElement.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// ============================================
// INTERSECTION OBSERVER PARA ANIMACIONES
// ============================================
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -100px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('fade-in-up');
            observer.unobserve(entry.target);
        }
    });
}, observerOptions);

// Observar elementos para animación
document.querySelectorAll('.feature-card, .step, .mockup-item, .team-member, .mv-card').forEach(element => {
    observer.observe(element);
});

// ============================================
// LAZY LOADING DE IMÁGENES (cuando las agregues)
// ============================================
document.addEventListener('DOMContentLoaded', () => {
    const lazyImages = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.add('loaded');
                imageObserver.unobserve(img);
            }
        });
    });
    
    lazyImages.forEach(img => imageObserver.observe(img));
});

// ============================================
// TOGGLE MENÚ MÓVIL (cuando lo implementes)
// ============================================
// Descomentar cuando agregues el botón hamburguesa
/*
const menuToggle = document.getElementById('menu-toggle');
const navMenu = document.querySelector('.nav-menu');

if (menuToggle) {
    menuToggle.addEventListener('click', () => {
        navMenu.classList.toggle('active');
    });
}
*/

// ============================================
// CONTADOR ANIMADO (ejemplo para stats)
// ============================================
function animateCounter(element, target, duration = 2000) {
    let current = 0;
    const increment = target / (duration / 16);
    
    const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
            element.textContent = target.toLocaleString();
            clearInterval(timer);
        } else {
            element.textContent = Math.floor(current).toLocaleString();
        }
    }, 16);
}

// Ejemplo de uso (activar cuando pases por la sección)
// const statsObserver = new IntersectionObserver((entries) => {
//     entries.forEach(entry => {
//         if (entry.isIntersecting) {
//             const counters = entry.target.querySelectorAll('[data-count]');
//             counters.forEach(counter => {
//                 const target = parseInt(counter.dataset.count);
//                 animateCounter(counter, target);
//             });
//             statsObserver.unobserve(entry.target);
//         }
//     });
// });

// ============================================
// FORMULARIO DE CONTACTO (si lo agregas)
// ============================================
/*
const contactForm = document.getElementById('contact-form');

if (contactForm) {
    contactForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = new FormData(contactForm);
        const data = Object.fromEntries(formData);
        
        try {
            const response = await fetch('/api/contact', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            
            if (response.ok) {
                alert('¡Mensaje enviado correctamente!');
                contactForm.reset();
            } else {
                alert('Error al enviar el mensaje. Por favor intenta de nuevo.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error al enviar el mensaje. Por favor intenta de nuevo.');
        }
    });
}
*/

