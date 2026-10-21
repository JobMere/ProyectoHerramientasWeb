// =====================================================================
//  auth.js — Registro, inicio de sesión y menú según sesión
//  Se carga en TODAS las páginas, DESPUÉS del script de Bootstrap.
//  No necesitas tocar el HTML: se engancha a los botones del navbar
//  (.btn-outline-cream = Iniciar sesión, .btn-gold = Registrarse).
// =====================================================================

// Dirección del backend. Cámbiala si usas otro puerto.
const AUTH_API = "http://localhost:8080/api";

// ---------- Sesión guardada en el navegador ----------
const Auth = {
  getSesion() {
    try {
      return JSON.parse(localStorage.getItem("sesion"));
    } catch (e) {
      return null;
    }
  },
  guardar(sesion) {
    localStorage.setItem("sesion", JSON.stringify(sesion));
  },
  cerrar() {
    localStorage.removeItem("sesion");
  },
  token() {
    const s = this.getSesion();
    return s ? s.token : null;
  },
  // Para usar en otras peticiones protegidas:
  //   fetch(url, { headers: Auth.headers() })
  headers() {
    const h = { "Content-Type": "application/json" };
    if (this.token()) h["Authorization"] = "Bearer " + this.token();
    return h;
  },
};
window.Auth = Auth;

// ---------- Utilidades ----------
function esc(texto) {
  const d = document.createElement("div");
  d.textContent = texto ?? "";
  return d.innerHTML;
}

// Convierte la respuesta de error del backend en un texto legible
function textoError(data) {
  if (!data) return "Ocurrió un error inesperado.";
  if (typeof data.error === "string") return data.error;
  if (typeof data === "object") return Object.values(data).join(". ");
  return "Ocurrió un error inesperado.";
}

async function enviar(ruta, cuerpo) {
  let respuesta;
  try {
    respuesta = await fetch(AUTH_API + ruta, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(cuerpo),
    });
  } catch (e) {
    throw new Error(
      "No se pudo conectar con el servidor. Verifica que el backend esté corriendo."
    );
  }
  let data = null;
  try {
    data = await respuesta.json();
  } catch (e) {}
  if (!respuesta.ok) {
    throw new Error(
      respuesta.status === 401 || respuesta.status === 403
        ? "Correo o contraseña incorrectos."
        : textoError(data)
    );
  }
  return data;
}

// ---------- Modales (se insertan solos en la página) ----------
const MODALES_HTML = `
<div class="modal fade" id="modalLogin" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content" style="border-radius:14px;">
      <div class="modal-header border-0 pb-0">
        <h5 class="modal-title fw-bold">Iniciar <span style="color:var(--bh-gold-dark)">sesión</span></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <form id="formLogin" novalidate>
        <div class="modal-body">
          <div id="loginError" class="alert alert-danger py-2 small d-none"></div>
          <div class="mb-3">
            <label class="form-label small fw-semibold">Correo</label>
            <input type="email" class="form-control" id="loginCorreo" required autocomplete="email">
          </div>
          <div class="mb-2">
            <label class="form-label small fw-semibold">Contraseña</label>
            <input type="password" class="form-control" id="loginPassword" required autocomplete="current-password">
          </div>
        </div>
        <div class="modal-footer border-0 flex-column align-items-stretch">
          <button type="submit" class="btn btn-gold w-100" id="loginBtn">Entrar</button>
          <small class="text-center text-secondary">¿No tienes cuenta?
            <a href="#" id="irARegistro" style="color:var(--bh-gold-dark)">Regístrate</a></small>
        </div>
      </form>
    </div>
  </div>
</div>

<div class="modal fade" id="modalRegistro" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content" style="border-radius:14px;">
      <div class="modal-header border-0 pb-0">
        <h5 class="modal-title fw-bold">Crear <span style="color:var(--bh-gold-dark)">cuenta</span></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <form id="formRegistro" novalidate>
        <div class="modal-body">
          <div id="registroError" class="alert alert-danger py-2 small d-none"></div>
          <div class="row g-3">
            <div class="col-6">
              <label class="form-label small fw-semibold">Nombres</label>
              <input type="text" class="form-control" id="regNombres" required>
            </div>
            <div class="col-6">
              <label class="form-label small fw-semibold">Apellidos</label>
              <input type="text" class="form-control" id="regApellidos" required>
            </div>
            <div class="col-12">
              <label class="form-label small fw-semibold">Correo</label>
              <input type="email" class="form-control" id="regCorreo" required autocomplete="email">
            </div>
            <div class="col-6">
              <label class="form-label small fw-semibold">Contraseña</label>
              <input type="password" class="form-control" id="regPassword" minlength="6" required autocomplete="new-password">
              <div class="form-text">Mínimo 6 caracteres</div>
            </div>
            <div class="col-6">
              <label class="form-label small fw-semibold">Teléfono</label>
              <input type="tel" class="form-control" id="regTelefono">
            </div>
            <div class="col-12">
              <label class="form-label small fw-semibold">Tipo de cuenta</label>
              <select class="form-select" id="regRol">
                <option value="CLIENTE">Cliente — busco barberías</option>
                <option value="BARBERIA">Barbería — quiero promocionar mi negocio</option>
              </select>
            </div>
          </div>
        </div>
        <div class="modal-footer border-0 flex-column align-items-stretch">
          <button type="submit" class="btn btn-gold w-100" id="registroBtn">Crear cuenta</button>
          <small class="text-center text-secondary">¿Ya tienes cuenta?
            <a href="#" id="irALogin" style="color:var(--bh-gold-dark)">Inicia sesión</a></small>
        </div>
      </form>
    </div>
  </div>
</div>`;

// ---------- Lógica ----------
document.addEventListener("DOMContentLoaded", () => {
  document.body.insertAdjacentHTML("beforeend", MODALES_HTML);

  const modalLogin = new bootstrap.Modal(document.getElementById("modalLogin"));
  const modalRegistro = new bootstrap.Modal(document.getElementById("modalRegistro"));

  const btnLogin = document.querySelector(".navbar-bh .btn-outline-cream");
  const btnRegistro = document.querySelector(".navbar-bh .btn-gold");

  // --- Menú: cambia según haya sesión o no ---
  function pintarMenu() {
    const previo = document.getElementById("sesionInfo");
    if (previo) previo.remove();

    const sesion = Auth.getSesion();
    if (btnLogin) btnLogin.classList.toggle("d-none", !!sesion);
    if (btnRegistro) btnRegistro.classList.toggle("d-none", !!sesion);

    // Enlace "Mi barbería" en el menú (solo para BARBERIA y ADMIN)
    const enlacePrevio = document.getElementById("navPanel");
    if (enlacePrevio) enlacePrevio.remove();
    const menu = document.querySelector(".navbar-bh .navbar-nav");
    if (menu && sesion && (sesion.rol === "BARBERIA" || sesion.rol === "ADMIN")) {
      const activo = location.pathname.endsWith("panel.html") ? " active" : "";
      menu.insertAdjacentHTML(
        "beforeend",
        `<li class="nav-item" id="navPanel"><a class="nav-link${activo}" href="panel.html">Mi barbería</a></li>`
      );
    }

    if (sesion && btnRegistro) {
      const etiqueta = sesion.rol === "BARBERIA" ? "Barbería" : sesion.rol === "ADMIN" ? "Admin" : "Cliente";
      btnRegistro.insertAdjacentHTML(
        "afterend",
        `<div id="sesionInfo" class="d-flex align-items-center gap-2">
           <span class="text-white small"><i class="bi bi-person-circle me-1" style="color:var(--bh-gold)"></i>Hola, ${esc(sesion.nombres)}</span>
           <span class="badge" style="background:var(--bh-gold);color:#20160a">${etiqueta}</span>
           <button type="button" class="btn btn-outline-cream btn-sm px-3" id="btnCerrarSesion">Cerrar sesión</button>
         </div>`
      );
      document.getElementById("btnCerrarSesion").addEventListener("click", () => {
        Auth.cerrar();
        pintarMenu();
        document.dispatchEvent(new CustomEvent("sesion-cambiada"));
      });
    }
  }

  function mostrarError(id, mensaje) {
    const caja = document.getElementById(id);
    caja.textContent = mensaje;
    caja.classList.remove("d-none");
  }
  function ocultarError(id) {
    document.getElementById(id).classList.add("d-none");
  }

  // Inicia sesión y guarda el token
  async function iniciarSesion(correo, password) {
    const d = await enviar("/auth/login", { correo, password });
    Auth.guardar({
      token: d.token,
      idUsuario: d.idUsuario,
      nombres: d.nombres,
      correo: d.correo,
      rol: d.rol,
    });
    pintarMenu();
    document.dispatchEvent(new CustomEvent("sesion-cambiada"));
  }

  // --- Abrir modales desde el navbar ---
  if (btnLogin) btnLogin.addEventListener("click", () => { ocultarError("loginError"); modalLogin.show(); });
  if (btnRegistro) btnRegistro.addEventListener("click", () => { ocultarError("registroError"); modalRegistro.show(); });

  document.getElementById("irARegistro").addEventListener("click", (e) => {
    e.preventDefault();
    modalLogin.hide();
    modalRegistro.show();
  });
  document.getElementById("irALogin").addEventListener("click", (e) => {
    e.preventDefault();
    modalRegistro.hide();
    modalLogin.show();
  });

  // --- Formulario de login ---
  document.getElementById("formLogin").addEventListener("submit", async (e) => {
    e.preventDefault();
    ocultarError("loginError");
    const correo = document.getElementById("loginCorreo").value.trim();
    const password = document.getElementById("loginPassword").value;
    if (!correo || !password) {
      return mostrarError("loginError", "Completa el correo y la contraseña.");
    }
    const btn = document.getElementById("loginBtn");
    btn.disabled = true;
    btn.textContent = "Entrando…";
    try {
      await iniciarSesion(correo, password);
      modalLogin.hide();
      e.target.reset();
    } catch (err) {
      mostrarError("loginError", err.message);
    } finally {
      btn.disabled = false;
      btn.textContent = "Entrar";
    }
  });

  // --- Formulario de registro ---
  document.getElementById("formRegistro").addEventListener("submit", async (e) => {
    e.preventDefault();
    ocultarError("registroError");
    const datos = {
      nombres: document.getElementById("regNombres").value.trim(),
      apellidos: document.getElementById("regApellidos").value.trim(),
      correo: document.getElementById("regCorreo").value.trim(),
      password: document.getElementById("regPassword").value,
      telefono: document.getElementById("regTelefono").value.trim(),
      rol: document.getElementById("regRol").value,
    };
    if (!datos.nombres || !datos.apellidos || !datos.correo || !datos.password) {
      return mostrarError("registroError", "Completa todos los campos obligatorios.");
    }
    if (datos.password.length < 6) {
      return mostrarError("registroError", "La contraseña debe tener al menos 6 caracteres.");
    }
    const btn = document.getElementById("registroBtn");
    btn.disabled = true;
    btn.textContent = "Creando cuenta…";
    try {
      await enviar("/auth/register", datos);
      // El registro no devuelve token, así que iniciamos sesión automáticamente
      await iniciarSesion(datos.correo, datos.password);
      modalRegistro.hide();
      e.target.reset();
    } catch (err) {
      mostrarError("registroError", err.message);
    } finally {
      btn.disabled = false;
      btn.textContent = "Crear cuenta";
    }
  });

  pintarMenu();
});