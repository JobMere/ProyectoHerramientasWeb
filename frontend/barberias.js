// ================== CONFIGURACIÓN ==================
// Dirección del backend (Spring Boot). Cámbiala si usas otro puerto.
const API_URL = "http://localhost:8080/api";

// ================== ELEMENTOS ==================
const lista = document.getElementById("listaBarberias");
const estado = document.getElementById("estado");
const contador = document.getElementById("contador");
const selZona = document.getElementById("filtroZona");
const selServicio = document.getElementById("filtroServicio");

// ================== UTILIDADES ==================
// Evita que texto con símbolos < > se interprete como HTML
function esc(texto) {
  const d = document.createElement("div");
  d.textContent = texto ?? "";
  return d.innerHTML;
}

async function pedir(ruta) {
  const r = await fetch(API_URL + ruta);
  if (!r.ok) throw new Error("Error " + r.status + " en " + ruta);
  return r.json();
}

function mostrarEstado(mensaje, esError = false) {
  estado.textContent = mensaje;
  estado.classList.toggle("error", esError);
  estado.classList.remove("d-none");
}

function ocultarEstado() {
  estado.classList.add("d-none");
}

// ================== FILTROS ==================
async function cargarFiltros() {
  try {
    const [zonas, servicios] = await Promise.all([
      pedir("/zonas"),
      pedir("/servicios"),
    ]);
    zonas.forEach((z) => {
      selZona.insertAdjacentHTML(
        "beforeend",
        `<option value="${z.idZona}">${esc(z.nombre)}</option>`
      );
    });
    servicios.forEach((s) => {
      selServicio.insertAdjacentHTML(
        "beforeend",
        `<option value="${s.idServicio}">${esc(s.nombre)}</option>`
      );
    });
  } catch (e) {
    console.error(e);
  }
}

// ================== TARJETA DE BARBERÍA ==================
function tarjetaHTML(b) {
  const fondo = b.fotoUrl
    ? `style="background-image:url('${esc(b.fotoUrl)}')"`
    : "";
  const clase = b.fotoUrl ? "has-img" : "p" + ((b.idBarberia % 4) + 1);

  return `
  <div class="col-md-6 col-lg-4">
    <div class="barber-card" data-id="${b.idBarberia}">
      <div class="barber-photo ${clase}" ${fondo}>
        <span>${esc(b.nombre)}</span>
      </div>
      <div class="barber-body">
        <div class="rating mb-1" data-rating>
          <i class="bi bi-star-fill"></i> <span class="text-secondary">Sin reseñas</span>
        </div>
        <div class="info mb-1"><i class="bi bi-geo-alt"></i>${esc(b.zona || "")}${b.direccion ? " · " + esc(b.direccion) : ""}</div>
        ${b.horarioAtencion ? `<div class="info mb-1"><i class="bi bi-clock"></i>${esc(b.horarioAtencion)}</div>` : ""}
        ${b.telefono ? `<div class="info mb-2"><i class="bi bi-telephone"></i>${esc(b.telefono)}</div>` : ""}
        ${b.descripcion ? `<p class="small text-secondary mb-2">${esc(b.descripcion)}</p>` : ""}
        <div data-servicios class="mt-2"><span class="sin-datos">Cargando servicios…</span></div>
      </div>
    </div>
  </div>`;
}

// Completa servicios y calificación de una tarjeta ya dibujada
async function completarTarjeta(b) {
  const card = lista.querySelector(`[data-id="${b.idBarberia}"]`);
  if (!card) return;

  // Servicios con precio
  try {
    const servicios = await pedir(`/barberias/${b.idBarberia}/servicios`);
    const activos = servicios.filter((s) => s.activo !== false);
    card.querySelector("[data-servicios]").innerHTML = activos.length
      ? activos
          .map(
            (s) => `<div class="servicio-row">
              <span>${esc(s.servicio)}</span>
              <span class="precio">S/ ${Number(s.precio).toFixed(2)}</span>
            </div>`
          )
          .join("")
      : `<span class="sin-datos">Aún sin servicios registrados</span>`;
  } catch (e) {
    card.querySelector("[data-servicios]").innerHTML =
      `<span class="sin-datos">No se pudieron cargar los servicios</span>`;
  }

  // Calificación promedio
  try {
    const resenas = await pedir(`/barberias/${b.idBarberia}/resenas`);
    if (resenas.length) {
      const prom =
        resenas.reduce((t, r) => t + r.calificacion, 0) / resenas.length;
      card.querySelector("[data-rating]").innerHTML =
        `<i class="bi bi-star-fill"></i> ${prom.toFixed(1)} ` +
        `<span class="text-secondary">(${resenas.length} reseña${resenas.length === 1 ? "" : "s"})</span>`;
    }
  } catch (e) {
    /* si falla, se deja "Sin reseñas" */
  }
}

// ================== LISTADO ==================
async function cargarBarberias() {
  const params = new URLSearchParams();
  if (selZona.value) params.set("zonaId", selZona.value);
  if (selServicio.value) params.set("servicioId", selServicio.value);
  const query = params.toString() ? "?" + params.toString() : "";

  lista.innerHTML = "";
  contador.textContent = "Cargando…";
  ocultarEstado();

  try {
    const barberias = await pedir("/barberias" + query);

    if (!barberias.length) {
      contador.textContent = "0 barberías encontradas";
      mostrarEstado("No hay barberías que coincidan con tu búsqueda.");
      return;
    }

    contador.textContent =
      barberias.length + (barberias.length === 1 ? " barbería encontrada" : " barberías encontradas");
    lista.innerHTML = barberias.map(tarjetaHTML).join("");
    barberias.forEach(completarTarjeta);
  } catch (e) {
    console.error(e);
    contador.textContent = "";
    mostrarEstado(
      "No se pudo conectar con el servidor. Verifica que el backend esté corriendo en " +
        API_URL,
      true
    );
  }
}

// ================== EVENTOS ==================
document.getElementById("filtrosForm").addEventListener("submit", (e) => {
  e.preventDefault();
  cargarBarberias();
});

document.getElementById("limpiarFiltros").addEventListener("click", () => {
  selZona.value = "";
  selServicio.value = "";
  cargarBarberias();
});

// ================== INICIO ==================
cargarFiltros();
cargarBarberias();