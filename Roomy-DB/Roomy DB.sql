-- Tabla de usuarios
CREATE TABLE Usuarios (
    IdUsuarios SERIAL PRIMARY KEY,
    Nombres TEXT NOT NULL,
    Apellidos TEXT NOT NULL,
    Correo TEXT UNIQUE NOT NULL,
    Contraseña TEXT NOT NULL,
    FechaRegistro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Rol TEXT CHECK (Rol IN ('usuario', 'admin')) DEFAULT 'usuario'
);

-- Valores rellenables
CREATE TABLE Rellenables (
    IdRellenable SERIAL PRIMARY KEY,
    TipoDeSangre TEXT CHECK (TipoDeSangre IN ('A+', 'B+', 'AB+', 'O+', 'A-', 'B-', 'AB-', 'O-')),
    Tipo TEXT CHECK (Tipo IN ('feria', 'jornada', 'clinica móvil'))
);

-- Perfil clinico del usuario
CREATE TABLE PerfilClinico (
    IdPerfilClinico SERIAL PRIMARY KEY,
    IdUsuario INTEGER REFERENCES Usuarios(IdUsuarios),
    TipoDeSangre TEXT REFERENCES Rellenables(TipoDeSangre),
    Alergias TEXT,
    EnfermedadesCronicas TEXT,
    MedicamentosActuales TEXT,
    ContactoEmergencia TEXT
);

-- Centros de atencion medica
CREATE TABLE CentrosDeAtencion (
    IdCentros SERIAL PRIMARY KEY,
    TipoCentros TEXT CHECK (TipoCentros IN (
        'HospitalPublico', 'HospitalPrivado', 'CentroSalud',
        'ClinicaPrivada', 'ClinicaMovil', 'Brigada', 'Temporal'
    )),
    Direccion TEXT,
    Latitud FLOAT,
    Longitud FLOAT,
    Telefono TEXT,
    Horario TEXT
);

-- Eventos de salud 
CREATE TABLE EventosDeSalud (
    IdEventos SERIAL PRIMARY KEY,
    Titulo TEXT NOT NULL,
    Descripcion TEXT,
    FechaInicio DATE,
    FechaFin DATE,
    Direccion INTEGER REFERENCES CentrosDeAtencion(IdCentros),
    Tipo INTEGER REFERENCES Rellenables(IdRellenable),
    Latitud FLOAT,
    Longitud FLOAT,
    CreadoPor INTEGER REFERENCES Usuarios(IdUsuarios)
);

-- Activaciones SOS
CREATE TABLE Emergencia (
    IdEmergencia SERIAL PRIMARY KEY,
    IdPerfilClinico INTEGER REFERENCES PerfilClinico(IdPerfilClinico),
    IdUsuario INTEGER REFERENCES Usuarios(IdUsuarios),
    Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Ubicacion TEXT,
    Latitud FLOAT,
    Longitud FLOAT,
    Estado TEXT CHECK (Estado IN ('activa', 'resuelta', 'pendiente')) DEFAULT 'activa'
);

-- Protocolos de seguridad
CREATE TABLE Protocolos (
    IdProtocolo SERIAL PRIMARY KEY,
    TipoEmergencia TEXT CHECK (TipoEmergencia IN (
        'sismo', 'inundación', 'incendio', 'huracán', 'otro'
    )),
    Instrucciones TEXT NOT NULL,
    UltimaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Logs chatbot
CREATE TABLE LogsChat (
    IdLogs SERIAL PRIMARY KEY,
    IdUsuario INTEGER REFERENCES Usuarios(IdUsuarios),
    Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Mensaje TEXT
);

-- Historial de consultas medicas
CREATE TABLE HistorialConsultas (
    IdConsulta SERIAL PRIMARY KEY,
    IdUsuario INTEGER REFERENCES Usuarios(IdUsuarios),
    Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Sintomas TEXT,
    Diagnostico TEXT,
    Recomendaciones TEXT
);

-- Notificaciones
CREATE TABLE Notificaciones (
    IdNotificacion SERIAL PRIMARY KEY,
    IdUsuario INTEGER REFERENCES Usuarios(IdUsuarios),
    Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Titulo TEXT,
    Mensaje TEXT,
    Leido BOOLEAN DEFAULT FALSE
);
