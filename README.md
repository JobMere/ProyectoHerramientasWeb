# Marketing de Barberías

Proyecto desarrollado para el curso **Herramientas de Desarrollo**.

## Descripción

Marketing de Barberías es una plataforma web que busca facilitar la conexión entre barberías y clientes.

Las barberías podrán registrar y administrar información sobre su negocio, como servicios, precios, horarios, ubicación y fotografías. Por otro lado, los clientes podrán buscar y comparar barberías según diferentes características y preferencias.

## Objetivo

Desarrollar una plataforma web que permita a los clientes buscar, comparar y seleccionar barberías de acuerdo con características como ubicación, precio, tipo de corte, servicios disponibles, horarios, ambiente y calificaciones.

Asimismo, la plataforma permitirá a las barberías promocionar sus servicios y aumentar su visibilidad ante potenciales clientes.

## Organización del equipo

| Integrante | Rol | Rama |
|---|---|---|
| Juan Pablo Ortiz Paiva | Frontend | `feature/frontend-pablo` |
| Walter Ridley Rosell Felix | Frontend | `feature/frontend-walter` |
| Tayarac Job Pampamallco Mere | Backend | `feature/backend-job` |
| David Alejandro Tanchiva Mendoza | Backend | `feature/backend-david` |
| Santiago Alonso Capcha Esteban | Base de datos | `feature/database-santiago` |

## Estrategia de ramas

La rama `main` se utilizará para mantener las versiones integradas y revisadas del proyecto.

Cada integrante trabajará en su propia rama según el rol asignado. Cuando una funcionalidad esté terminada y revisada, los cambios podrán integrarse posteriormente a `main`.

Flujo de trabajo:

`Actualizar → Desarrollar → Commit → Push → Revisar → Integrar en main`

## Estructura inicial

```text
ProyectoHerramientasWeb/
│
├── backend/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/sistemabarberia/
│           │       └── entity/
│           └── resources/
│               └── CodigoIniciacion.sql
│
├── Marketing de Barberias.pdf
├── README.md
└── package-lock.json
```

## Base de datos

El proyecto utiliza **MySQL** para almacenar la información necesaria de la plataforma, incluyendo datos relacionados con barberías, clientes, tipos de cortes, servicios adicionales, fotografías y zonas.

## Estado del proyecto

Actualmente el proyecto se encuentra en su etapa inicial de desarrollo, con la estructura del backend, base de datos, repositorio y ramas de trabajo establecidas.
