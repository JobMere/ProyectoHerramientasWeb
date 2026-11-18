// ================== CONFIGURACIÓN ==================
// Dirección del backend Spring Boot
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

// Petición GET al backend
async function pedir(ruta) {
  const r = await fetch(API_URL + ruta);

  if (!r.ok) {
    throw new Error("Error " + r.status + " en " + ruta);
  }

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
        `<option value="${z.idZona}">
          ${esc(z.nombre)}
        </option>`
      );
    });

    servicios.forEach((s) => {
      selServicio.insertAdjacentHTML(
        "beforeend",
        `<option value="${s.idServicio}">
          ${esc(s.nombre)}
        </option>`
      );
    });

  } catch (e) {
    console.error(e);
  }
}

// ================== TARJETA DE BARBERÍA REAL ==================

function tarjetaHTML(b) {

  const fondo = b.fotoUrl
    ? `style="background-image:url('${esc(b.fotoUrl)}')"`
    : "";

  const clase = b.fotoUrl
    ? "has-img"
    : "p" + ((b.idBarberia % 4) + 1);

  return `
    <div class="col-md-6 col-lg-4">

      <div class="barber-card h-100"
           data-id="${b.idBarberia}">

        <div class="barber-photo ${clase}" ${fondo}>

          <span>
            ${esc(b.nombre)}
          </span>

        </div>

        <div class="barber-body">

          <div class="rating mb-1"
               data-rating>

            <i class="bi bi-star-fill"></i>

            <span class="text-secondary">
              Sin reseñas
            </span>

          </div>

          <div class="info mb-1">

            <i class="bi bi-geo-alt"></i>

            ${esc(b.zona || "")}

            ${
              b.direccion
                ? " · " + esc(b.direccion)
                : ""
            }

          </div>

          ${
            b.horarioAtencion
              ? `
                <div class="info mb-1">
                  <i class="bi bi-clock"></i>
                  ${esc(b.horarioAtencion)}
                </div>
              `
              : ""
          }

          ${
            b.telefono
              ? `
                <div class="info mb-2">
                  <i class="bi bi-telephone"></i>
                  ${esc(b.telefono)}
                </div>
              `
              : ""
          }

          ${
            b.descripcion
              ? `
                <p class="small text-secondary mb-2">
                  ${esc(b.descripcion)}
                </p>
              `
              : ""
          }

          <div data-servicios class="mt-2">

            <span class="sin-datos">
              Cargando servicios…
            </span>

          </div>

        </div>

      </div>

    </div>
  `;
}

// ================== BARBERÍAS ADICIONALES ==================

function barberiasAdicionalesHTML() {

  return `

    <!-- URBAN CUTS -->
    <div class="col-md-6 col-lg-4">

      <div class="barber-card h-100">

        <div
          class="barber-photo has-img"
          style="
            background-image:
            url('assets/images/urban-cuts.png');
          "
        >

          <span>
            Urban Cuts
          </span>

        </div>

        <div class="barber-body">

          <div class="rating mb-2">

            <i class="bi bi-star-fill"></i>

            4.7

            <span class="text-secondary">
              (64 reseñas)
            </span>

          </div>

          <div class="info mb-2">

            <i class="bi bi-geo-alt"></i>

            San Isidro, Lima

          </div>

          <p class="small text-secondary mb-2">

            Barbería moderna especializada
            en cortes urbanos, fade y barba.

          </p>

          <div class="servicio-row">

            <span>
              Fade
            </span>

            <span class="precio">
              S/ 35.00
            </span>

          </div>

          <div class="servicio-row">

            <span>
              Barba
            </span>

            <span class="precio">
              S/ 25.00
            </span>

          </div>

        </div>

      </div>

    </div>


    <!-- LEGENDARY BARBERS -->
    <div class="col-md-6 col-lg-4">

      <div class="barber-card h-100">

        <div
          class="barber-photo has-img"
          style="
            background-image:
            url('assets/images/legendary-barbers.png');
          "
        >

          <span>
            Legendary Barbers
          </span>

        </div>

        <div class="barber-body">

          <div class="rating mb-2">

            <i class="bi bi-star-fill"></i>

            4.8

            <span class="text-secondary">
              (86 reseñas)
            </span>

          </div>

          <div class="info mb-2">

            <i class="bi bi-geo-alt"></i>

            Santiago de Surco, Lima

          </div>

          <p class="small text-secondary mb-2">

            Barbería premium especializada
            en cortes clásicos y modernos.

          </p>

          <div class="servicio-row">

            <span>
              Corte clásico
            </span>

            <span class="precio">
              S/ 30.00
            </span>

          </div>

          <div class="servicio-row">

            <span>
              Fade
            </span>

            <span class="precio">
              S/ 40.00
            </span>

          </div>

        </div>

      </div>

    </div>


    <!-- ÉLITE BARBERÍA -->
    <div class="col-md-6 col-lg-4">

      <div class="barber-card h-100">

        <div
          class="barber-photo has-img"
          style="
            background-image:
            url('assets/images/elite-barberia.png');
          "
        >

          <span>
            Élite Barbería
          </span>

        </div>

        <div class="barber-body">

          <div class="rating mb-2">

            <i class="bi bi-star-fill"></i>

            4.9

            <span class="text-secondary">
              (92 reseñas)
            </span>

          </div>

          <div class="info mb-2">

            <i class="bi bi-geo-alt"></i>

            La Molina, Lima

          </div>

          <p class="small text-secondary mb-2">

            Servicio premium de barbería
            y cuidado masculino.

          </p>

          <div class="servicio-row">

            <span>
              Corte moderno
            </span>

            <span class="precio">
              S/ 40.00
            </span>

          </div>

          <div class="servicio-row">

            <span>
              Barba
            </span>

            <span class="precio">
              S/ 30.00
            </span>

          </div>

        </div>

      </div>

    </div>


    <!-- CLASSIC BARBER HOUSE -->
    <div class="col-md-6 col-lg-4">

      <div class="barber-card h-100">

        <div
          class="barber-photo has-img"
          style="
            background-image:
            url('assets/images/barber-club.png');
          "
        >

          <span>
            Classic Barber House
          </span>

        </div>

        <div class="barber-body">

          <div class="rating mb-2">

            <i class="bi bi-star-fill"></i>

            4.6

            <span class="text-secondary">
              (55 reseñas)
            </span>

          </div>

          <div class="info mb-2">

            <i class="bi bi-geo-alt"></i>

            Miraflores, Lima

          </div>

          <p class="small text-secondary mb-2">

            Especialistas en estilos clásicos,
            barba y cuidado masculino.

          </p>

          <div class="servicio-row">

            <span>
              Corte clásico
            </span>

            <span class="precio">
              S/ 28.00
            </span>

          </div>

          <div class="servicio-row">

            <span>
              Barba
            </span>

            <span class="precio">
              S/ 22.00
            </span>

          </div>

        </div>

      </div>

    </div>

  `;
}

// ================== COMPLETAR TARJETA REAL ==================

async function completarTarjeta(b) {

  const card =
    lista.querySelector(
      `[data-id="${b.idBarberia}"]`
    );

  if (!card) return;

  // Servicios y precios
  try {

    const servicios =
      await pedir(
        `/barberias/${b.idBarberia}/servicios`
      );

    const activos =
      servicios.filter(
        (s) => s.activo !== false
      );

    card.querySelector(
      "[data-servicios]"
    ).innerHTML = activos.length

      ? activos
          .map(
            (s) => `

              <div class="servicio-row">

                <span>
                  ${esc(s.servicio)}
                </span>

                <span class="precio">
                  S/ ${Number(s.precio).toFixed(2)}
                </span>

              </div>

            `
          )
          .join("")

      : `
          <span class="sin-datos">
            Aún sin servicios registrados
          </span>
        `;

  } catch (e) {

    card.querySelector(
      "[data-servicios]"
    ).innerHTML = `

      <span class="sin-datos">
        No se pudieron cargar los servicios
      </span>

    `;
  }

  // Calificación promedio
  try {

    const resenas =
      await pedir(
        `/barberias/${b.idBarberia}/resenas`
      );

    if (resenas.length) {

      const prom =
        resenas.reduce(
          (t, r) =>
            t + r.calificacion,
          0
        ) / resenas.length;

      card.querySelector(
        "[data-rating]"
      ).innerHTML = `

        <i class="bi bi-star-fill"></i>

        ${prom.toFixed(1)}

        <span class="text-secondary">

          (${resenas.length}
          ${
            resenas.length === 1
              ? " reseña"
              : " reseñas"
          })

        </span>

      `;
    }

  } catch (e) {

    // Si falla, se mantiene "Sin reseñas"

  }
}

// ================== LISTADO ==================

async function cargarBarberias() {

  const params =
    new URLSearchParams();

  if (selZona.value) {
    params.set(
      "zonaId",
      selZona.value
    );
  }

  if (selServicio.value) {
    params.set(
      "servicioId",
      selServicio.value
    );
  }

  const query =
    params.toString()
      ? "?" + params.toString()
      : "";

  // Detectamos si hay filtros
  const hayFiltros =
    Boolean(
      selZona.value ||
      selServicio.value
    );

  lista.innerHTML = "";

  contador.textContent =
    "Cargando…";

  ocultarEstado();

  try {

    const barberias =
      await pedir(
        "/barberias" + query
      );

    // Si NO hay filtros:
    // agregamos las 4 barberías adicionales
    const adicionales =
      hayFiltros
        ? ""
        : barberiasAdicionalesHTML();

    const cantidadAdicionales =
      hayFiltros
        ? 0
        : 4;

    const total =
      barberias.length +
      cantidadAdicionales;

    // Si no hay ningún resultado
    if (total === 0) {

      contador.textContent =
        "0 barberías encontradas";

      mostrarEstado(
        "No hay barberías que coincidan con tu búsqueda."
      );

      return;
    }

    // Contador correcto
    contador.textContent =
      total +
      (
        total === 1
          ? " barbería encontrada"
          : " barberías encontradas"
      );

    // Primero las del backend
    lista.innerHTML =
      barberias
        .map(tarjetaHTML)
        .join("");

    // Después agregamos las adicionales
    lista.insertAdjacentHTML(
      "beforeend",
      adicionales
    );

    // Completar datos de las reales
    barberias.forEach(
      completarTarjeta
    );

  } catch (e) {

    console.error(e);

    contador.textContent = "";

    mostrarEstado(
      "No se pudo conectar con el servidor. " +
      "Verifica que el backend esté corriendo en " +
      API_URL,
      true
    );
  }
}

// ================== EVENTOS ==================

document
  .getElementById("filtrosForm")
  .addEventListener(
    "submit",
    (e) => {

      e.preventDefault();

      cargarBarberias();
    }
  );

document
  .getElementById("limpiarFiltros")
  .addEventListener(
    "click",
    () => {

      selZona.value = "";
      selServicio.value = "";

      cargarBarberias();
    }
  );

// ================== INICIO ==================

cargarFiltros();
cargarBarberias();