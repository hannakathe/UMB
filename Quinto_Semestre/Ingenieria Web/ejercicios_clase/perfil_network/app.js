(function () {
  'use strict';

  /* =========================================================
     TOAST — notificación flotante temporal
     ========================================================= */
  function showToast(msg, duration) {
    duration = duration || 3000;
    const toast = document.querySelector('[data-toast]');
    toast.textContent = msg;
    toast.setAttribute('data-active', '');
    clearTimeout(toast._t);
    toast._t = setTimeout(function () {
      toast.removeAttribute('data-active');
    }, duration);
  }

  /* =========================================================
     MODALES — abrir / cerrar overlays
     ========================================================= */
  function openModal(name) {
    const overlay = document.querySelector('[data-overlay="' + name + '"]');
    if (overlay) overlay.setAttribute('data-active', '');
  }

  function closeOverlay(overlay) {
    if (overlay) overlay.removeAttribute('data-active');
  }

  // Botones [data-close] dentro de cualquier overlay
  document.querySelectorAll('[data-close]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      closeOverlay(btn.closest('[data-overlay]'));
    });
  });

  // Clic en el fondo del overlay (fuera de la caja) → cierra
  document.querySelectorAll('[data-overlay]').forEach(function (overlay) {
    overlay.addEventListener('click', function (e) {
      if (e.target === overlay) closeOverlay(overlay);
    });
  });

  // Cualquier botón o enlace con [data-modal] abre su overlay
  document.addEventListener('click', function (e) {
    const trigger = e.target.closest('[data-modal]');
    if (trigger) {
      e.preventDefault();
      openModal(trigger.getAttribute('data-modal'));
    }
  });

  /* =========================================================
     TABS — navegación entre secciones
     ========================================================= */
  document.querySelectorAll('[data-tab]').forEach(function (link) {
    link.addEventListener('click', function (e) {
      e.preventDefault();
      const tab = link.getAttribute('data-tab');

      // Desactivar todos los tabs del nav
      document.querySelectorAll('[data-tab]').forEach(function (l) {
        l.removeAttribute('data-active');
      });
      link.setAttribute('data-active', '');

      // Mostrar solo la sección correspondiente
      document.querySelectorAll('[data-section]').forEach(function (s) {
        s.removeAttribute('data-active');
      });
      const target = document.querySelector('[data-section="' + tab + '"]');
      if (target) target.setAttribute('data-active', '');
    });
  });

  /* =========================================================
     FOTO DE PERFIL — clic en la imagen abre selector de archivo
     ========================================================= */
  const photoWrapper = document.querySelector('body > div > section:first-child > div');
  if (photoWrapper) {
    const fileInput = photoWrapper.querySelector('input[type="file"]');
    const profileImg = photoWrapper.querySelector('img');

    // Clic en el wrapper dispara el input file
    photoWrapper.addEventListener('click', function () {
      fileInput.click();
    });

    fileInput.addEventListener('change', function (e) {
      const file = e.target.files[0];
      if (!file) return;
      const reader = new FileReader();
      reader.onload = function (ev) {
        const src = ev.target.result;
        // Actualiza todas las imágenes de avatar del perfil
        document.querySelectorAll(
          'img[alt="Foto de perfil"], img[alt="Avatar"], img[alt="Tú"], img[alt="Alex"]'
        ).forEach(function (img) {
          img.src = src;
        });
        showToast('✅ Foto de perfil actualizada');
      };
      reader.readAsDataURL(file);
    });
  }

  /* =========================================================
     EDITAR PERFIL — modal con formulario
     ========================================================= */
  const editBtn = document.querySelector('[data-edit]');
  if (editBtn) {
    editBtn.addEventListener('click', function () {
      // Pre-llenar el formulario con los valores actuales
      const form = document.querySelector('[data-edit-form]');
      ['name', 'bio', 'work', 'school', 'city', 'year'].forEach(function (field) {
        const el = document.querySelector('[data-field="' + field + '"]');
        const input = form.querySelector('[name="' + field + '"]');
        if (el && input) input.value = el.textContent.trim();
      });
      openModal('edit');
    });
  }

  const editForm = document.querySelector('[data-edit-form]');
  if (editForm) {
    editForm.addEventListener('submit', function (e) {
      e.preventDefault();
      const data = {};
      new FormData(editForm).forEach(function (val, key) { data[key] = val; });

      // Actualizar los campos visibles en el perfil
      ['name', 'bio', 'work', 'school', 'city', 'year'].forEach(function (field) {
        const el = document.querySelector('[data-field="' + field + '"]');
        if (el && data[field]) el.textContent = data[field];
      });

      // Actualizar nombre en los encabezados de los artículos
      if (data.name) {
        document.querySelectorAll('article header strong').forEach(function (s) {
          s.textContent = data.name;
        });
      }

      closeOverlay(document.querySelector('[data-overlay="edit"]'));
      showToast('✅ Perfil actualizado correctamente');
    });
  }

  /* =========================================================
     AGREGAR AMIGO — confirmar y simular envío
     ========================================================= */
  const confirmFriendBtn = document.querySelector('[data-confirm-friend]');
  if (confirmFriendBtn) {
    confirmFriendBtn.addEventListener('click', function () {
      closeOverlay(document.querySelector('[data-overlay="add-friend"]'));
      showToast('✅ Solicitud de amistad enviada');
      // Marcar el botón principal como "solicitud enviada"
      const addBtn = document.querySelector(
        'body > div > section:first-child > button[data-modal="add-friend"]'
      );
      if (addBtn) {
        addBtn.textContent = '✔ Solicitud enviada';
        addBtn.disabled = true;
        addBtn.style.opacity = '.65';
        addBtn.style.cursor = 'default';
      }
    });
  }

  /* =========================================================
     CHAT / ENVIAR MENSAJE — simulación con auto-respuesta
     ========================================================= */
  const chatArea   = document.querySelector('[data-chat]');
  const chatInputW = document.querySelector('[data-chat-input]');
  const sendMsgBtn = document.querySelector('[data-send-msg]');

  if (chatArea && chatInputW && sendMsgBtn) {
    const chatInput = chatInputW.querySelector('input');

    const autoReplies = [
      '¡Claro! Con gusto te ayudo 😊',
      'Interesante, cuéntame más...',
      '¡Qué buena idea! 💡',
      'Ahorita estoy ocupado, te escribo después 🙏',
      '¡Sí, hablemos pronto! 📱',
      '¡Gracias por escribirme! 🙌',
      'Jeje, qué bueno saber eso 😄',
    ];

    function addBubble(text, isMe) {
      const bubble = document.createElement('div');
      bubble.setAttribute('data-bubble', '');
      bubble.setAttribute(isMe ? 'data-me' : 'data-them', '');
      bubble.textContent = text;
      chatArea.appendChild(bubble);
      chatArea.scrollTop = chatArea.scrollHeight;
    }

    function sendMessage() {
      const text = chatInput.value.trim();
      if (!text) return;
      addBubble(text, true);
      chatInput.value = '';
      setTimeout(function () {
        const reply = autoReplies[Math.floor(Math.random() * autoReplies.length)];
        addBubble(reply, false);
      }, 900 + Math.random() * 600);
    }

    sendMsgBtn.addEventListener('click', sendMessage);
    chatInput.addEventListener('keydown', function (e) {
      if (e.key === 'Enter') sendMessage();
    });
  }

  /* =========================================================
     ME GUSTA — selector de reacciones flotante
     ========================================================= */
  const reactionPicker = document.querySelector('[data-reaction-picker]');
  let activeLikeBtn = null;

  function showReactionPicker(btn) {
    const rect = btn.getBoundingClientRect();
    reactionPicker.style.top  = (rect.top  + window.scrollY - 60) + 'px';
    reactionPicker.style.left = Math.max(8, rect.left - 10) + 'px';
    reactionPicker.setAttribute('data-active', '');
    activeLikeBtn = btn;
  }

  function hideReactionPicker() {
    reactionPicker.removeAttribute('data-active');
    activeLikeBtn = null;
  }

  document.addEventListener('click', function (e) {
    // Clic en botón "Me gusta"
    const likeBtn = e.target.closest('[data-action="like"]');
    if (likeBtn) {
      e.stopPropagation();
      if (reactionPicker.hasAttribute('data-active') && activeLikeBtn === likeBtn) {
        hideReactionPicker();
      } else {
        showReactionPicker(likeBtn);
      }
      return;
    }

    // Clic en una reacción
    const reactionEl = e.target.closest('[data-reaction]');
    if (reactionEl && activeLikeBtn) {
      const emoji   = reactionEl.getAttribute('data-reaction');
      const count   = parseInt(activeLikeBtn.getAttribute('data-count')) || 0;
      const liked   = activeLikeBtn.getAttribute('data-liked') === 'true';
      const newCount = liked ? count - 1 : count + 1;

      activeLikeBtn.textContent = emoji + ' ' + newCount + ' Me gusta';
      activeLikeBtn.setAttribute('data-count', newCount);
      activeLikeBtn.setAttribute('data-liked', String(!liked));
      hideReactionPicker();
      return;
    }

    // Clic fuera → cierra el picker
    if (reactionPicker.hasAttribute('data-active')) {
      hideReactionPicker();
    }
  });

  /* =========================================================
     COMENTARIOS — expandir/colapsar por artículo
     ========================================================= */
  document.addEventListener('click', function (e) {
    // Toggle de sección de comentarios
    const commentBtn = e.target.closest('[data-action="comment"]');
    if (commentBtn) {
      const article     = commentBtn.closest('article');
      const commentsDiv = article && article.querySelector('[data-comments]');
      if (commentsDiv) {
        if (commentsDiv.hasAttribute('data-active')) {
          commentsDiv.removeAttribute('data-active');
        } else {
          commentsDiv.setAttribute('data-active', '');
          const inp = commentsDiv.querySelector('input');
          if (inp) setTimeout(function () { inp.focus(); }, 60);
        }
      }
    }

    // Enviar nuevo comentario
    const sendBtn = e.target.closest('[data-send-comment]');
    if (sendBtn) {
      const commentsDiv = sendBtn.closest('[data-comments]');
      const inputEl     = commentsDiv && commentsDiv.querySelector('[data-comment-input] input');
      if (!inputEl) return;
      const text = inputEl.value.trim();
      if (!text) return;

      // Obtener datos del autor (perfil actual)
      const nameEl  = document.querySelector('[data-field="name"]');
      const avatarEl = document.querySelector('body > div > section:first-child > div > img');
      const name    = nameEl  ? nameEl.textContent.trim()  : 'Tú';
      const imgSrc  = avatarEl ? avatarEl.src : '';

      // Crear el nuevo nodo de comentario
      const newItem = document.createElement('div');
      newItem.setAttribute('data-comment-item', '');
      newItem.innerHTML =
        '<img src="' + imgSrc + '" alt="' + name + '" />' +
        '<div><strong>' + name + '</strong><p>' + text + '</p></div>';

      // Insertar antes del input
      const inputRow = commentsDiv.querySelector('[data-comment-input]');
      commentsDiv.insertBefore(newItem, inputRow);
      inputEl.value = '';

      // Actualizar contador
      const counter = commentsDiv.closest('article').querySelector('[data-action="comment"]');
      if (counter) {
        const num = parseInt((counter.textContent.match(/\d+/) || ['0'])[0]);
        counter.textContent = '💬 ' + (num + 1) + ' Comentarios';
      }
    }
  });

  /* =========================================================
     COMPARTIR — opciones de compartir
     ========================================================= */
  document.querySelectorAll('[data-share-opt]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      // Obtener solo el texto, ignorando el SVG hijo
      const action = Array.from(btn.childNodes)
        .filter(function (n) { return n.nodeType === 3; })
        .map(function (n) { return n.textContent.trim(); })
        .join('') || btn.textContent.trim();
      closeOverlay(document.querySelector('[data-overlay="share"]'));
      showToast(action + ' — ¡Listo!');
    });
  });

  /* =========================================================
     LUCIDE ICONS — inicializar todos los íconos del DOM
     ========================================================= */
  if (typeof lucide !== 'undefined') {
    lucide.createIcons();
  }

})();
