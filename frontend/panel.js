// =====================================================================
//  panel.js — Panel "Mi barbería"
//  Requiere auth.js (sesión, token y la constante AUTH_API).
// =====================================================================

const $ = (id) => document.getElementById(id);

let miBarberia = null; // la barbería del usuario (si ya creó una)

// ---------- Peticiones al backend ----------
async function api(ruta, opciones = {}) {
  let r;
  try {
    r = await fetch(AUTH_API + ruta, {
      ...opciones,
      headers: Auth.headers(),
    });
  } catch (e) {
    throw new Error("No se pudo conectar con el servidor. Verifica que el backend esté corriendo.");
  }
  let data = null;
  try { data = await r.json(); } catch (e) {}
  if (!r.ok) {
    if (r.status === 401 || r.status === 403) {
      throw new Error("Tu sesión no tiene permiso para esta acción. Vuelve a iniciar sesión.");
    }
    throw new Error(textoError(data));
  }
  return data;
}
const post = (ruta, cuerpo) =>
  api(ruta, { method: "POST", body: JSON.stringify(cuerpo) });

function ver(id, mostrar) { $(id).classList.toggle("d-none", !mostrar); }
function error(id, msg) { $(id).textContent = msg; ver(id, true); }
function aviso(msg, tipo = "success") {
  $("avisoGlobal").innerHTML = `<div class="alert alert-${tipo} py-2 small">${esc(msg)}</div>`;
  ver("avisoGlobal", true);
  setTimeout(() => ver("avisoGlobal", false), 3500);
}

// ---------- Acceso ----------
function sinAcceso(msg) {
  ver("bloqueCrear", false);
  ver("bloqueMiBarberia", false);
  $("bloqueSinAcceso").innerHTML = msg;
  ver("bloqueSinAcceso", true);
}

async function iniciarPanel() {
  const sesion = Auth.getSesion();
  ver("bloqueSinAcceso", false);

  if (!sesion) {
    return sinAcceso(`<p class="mb-2">Inicia sesión con una cuenta de <strong>Barbería</strong> para administrar tu negocio.</p>
      <button class="btn btn-gold" onclick="document.querySelector('.navbar-bh .btn-outline-cream').click()">Iniciar sesión</button>`);
  }
  if (sesion.rol !== "BARBERIA" && sesion.rol !== "ADMIN") {
    return sinAcceso(`<p class="mb-0">Esta sección es solo para cuentas de tipo <strong>Barbería</strong>. Tu cuenta es de tipo ${esc(sesion.rol)}.</p>`);
  }

  try {
    const barberias = await api("/barberias");
    miBarberia = barberias.find((b) => b.usuarioId === sesion.idUsuario) || null;

    if (miBarberia) {
      ver("bloqueCrear", false);
      pintarMiBarberia();
      ver("bloqueMiBarberia", true);
      await cargarServicios();
    } else {
      ver("bloqueMiBarberia", false);
      await cargarZonas();
      ver("bloqueCrear", true);
    }
  } catch (e) {
    sinAcceso(`<p class="mb-0 text-danger">${esc(e.message)}</p>`);
  }
}

// ---------- Crear barbería ----------
async function cargarZonas(seleccionar) {
  const zonas = await api("/zonas");
  const sel = $("bZona");
  sel.innerHTML = zonas.length
    ? zonas.map((z) => `<option value="${z.idZona}">${esc(z.nombre)}</option>`).join("")
    : `<option value="">(Primero agrega una zona con el botón +)</option>`;
  if (seleccionar) sel.value = seleccionar;
}

$("formBarberia").addEventListener("submit", async (e) => {
  e.preventDefault();
  ver("errorBarberia", false);
  const sesion = Auth.getSesion();
  const datos = {
    nombre: $("bNombre").value.trim(),
    direccion: $("bDireccion").value.trim(),
    telefono: $("bTelefono").value.trim(),
    horarioAtencion: $("bHorario").value.trim(),
    fotoUrl: $("bFoto").value.trim() || null,
    descripcion: $("bDescripcion").value.trim(),
    zonaId: Number($("bZona").value),
    usuarioId: sesion.idUsuario,
  };
  if (!datos.nombre || !datos.direccion) return error("errorBarberia", "El nombre y la dirección son obligatorios.");
  if (!datos.zonaId) return error("errorBarberia", "Elige una zona (o agrega una con el botón +).");

  const btn = $("btnGuardarBarberia");
  btn.disabled = true;
  try {
    await post("/barberias", datos);
    aviso("¡Barbería creada! Ya aparece en la lista pública.");
    await iniciarPanel();
  } catch (err) {
    error("errorBarberia", err.message);
  } finally {
    btn.disabled = false;
  }
});

// ---------- Mi barbería ----------
function pintarMiBarberia() {
  const b = miBarberia;
  $("miNombre").textContent = b.nombre;
  $("miZona").textContent = b.zona || "";
  $("miDireccion").textContent = b.direccion || "";
  $("miHorario").textContent = b.horarioAtencion || "";
  $("miTelefono").textContent = b.telefono || "";
  $("miDescripcion").textContent = b.descripcion || "";
  ver("miHorarioFila", !!b.horarioAtencion);
  ver("miTelefonoFila", !!b.telefono);
}

let serviciosAsignados = [];

async function cargarServicios() {
  serviciosAsignados = await api(`/barberias/${miBarberia.idBarberia}/servicios`);
  const activos = serviciosAsignados.filter((s) => s.activo !== false);

  $("listaMisServicios").innerHTML = activos.length
    ? activos.map((s) => `<div class="servicio-row">
          <span>${esc(s.servicio)}</span>
          <span class="precio">S/ ${Number(s.precio).toFixed(2)}</span>
        </div>`).join("")
    : `<span class="sin-datos">Aún no agregaste servicios.</span>`;

  // Selector: solo servicios que todavía no tiene asignados
  const todos = await api("/servicios");
  const yaTiene = new Set(serviciosAsignados.map((s) => s.servicioId));
  const libres = todos.filter((s) => s.activo !== false && !yaTiene.has(s.idServicio));
  $("sServicio").innerHTML = libres.length
    ? libres.map((s) => `<option value="${s.idServicio}">${esc(s.nombre)}</option>`).join("")
    : `<option value="">(No hay servicios disponibles: crea uno con +)</option>`;
}

$("formServicio").addEventListener("submit", async (e) => {
  e.preventDefault();
  ver("errorServicio", false);
  const servicioId = Number($("sServicio").value);
  const precio = parseFloat($("sPrecio").value);
  if (!servicioId) return error("errorServicio", "Elige un servicio (o crea uno con el botón +).");
  if (isNaN(precio) || precio < 0) return error("errorServicio", "Escribe un precio válido.");

  const btn = $("btnAgregarServicio");
  btn.disabled = true;
  try {
    await post(`/barberias/${miBarberia.idBarberia}/servicios`, { servicioId, precio });
    $("sPrecio").value = "";
    aviso("Servicio agregado.");
    await cargarServicios();
  } catch (err) {
    error("errorServicio", err.message);
  } finally {
    btn.disabled = false;
  }
});

// ---------- Modales: nueva zona y nuevo servicio ----------
document.addEventListener("DOMContentLoaded", () => {
  const modalZona = new bootstrap.Modal($("modalZona"));
  const modalServicio = new bootstrap.Modal($("modalServicio"));

  $("btnNuevaZona").addEventListener("click", () => { ver("errorZona", false); modalZona.show(); });
  $("btnNuevoServicio").addEventListener("click", () => { ver("errorNuevoServicio", false); modalServicio.show(); });

  $("formZona").addEventListener("submit", async (e) => {
    e.preventDefault();
    ver("errorZona", false);
    const nombre = $("zNombre").value.trim();
    if (!nombre) return error("errorZona", "Escribe el nombre de la zona.");
    try {
      const z = await post("/zonas", { nombre });
      modalZona.hide();
      e.target.reset();
      await cargarZonas(z.idZona);
    } catch (err) { error("errorZona", err.message); }
  });

  $("formNuevoServicio").addEventListener("submit", async (e) => {
    e.preventDefault();
    ver("errorNuevoServicio", false);
    const nombre = $("nsNombre").value.trim();
    if (!nombre) return error("errorNuevoServicio", "Escribe el nombre del servicio.");
    try {
      const s = await post("/servicios", { nombre, descripcion: $("nsDescripcion").value.trim() });
      modalServicio.hide();
      e.target.reset();
      await cargarServicios();
      $("sServicio").value = s.idServicio;
    } catch (err) { error("errorNuevoServicio", err.message); }
  });
});

// ---------- Inicio y cambios de sesión ----------
document.addEventListener("DOMContentLoaded", iniciarPanel);
document.addEventListener("sesion-cambiada", iniciarPanel);