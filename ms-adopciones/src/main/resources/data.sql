-- CORREGIDO: la entidad Adopcion (model/Adopcion.java) no tiene campo "fecha",
-- por lo que Hibernate nunca crea esa columna en la tabla "adopciones" (ddl-auto=update).
-- Este INSERT original la incluía y hacía fallar el arranque de la app
-- (org.postgresql.util.PSQLException: column "fecha" of relation "adopciones" does not exist).
-- Si en algún momento se necesita trackear fecha de adopción, hay que agregar
-- el campo a la entidad primero y luego reincorporarlo aquí.
INSERT INTO adopciones (id, mascota_id, adoptante, estado) VALUES (12, 483, 'Familia Soto', 'Aprobada') ON CONFLICT (id) DO NOTHING;
INSERT INTO adopciones (id, mascota_id, adoptante, estado) VALUES (13, 482, 'M. Reyes', 'En proceso') ON CONFLICT (id) DO NOTHING;
INSERT INTO adopciones (id, mascota_id, adoptante, estado) VALUES (14, 485, 'C. Vega', 'En proceso') ON CONFLICT (id) DO NOTHING;
INSERT INTO adopciones (id, mascota_id, adoptante, estado) VALUES (15, 484, 'Familia Pérez', 'Rechazada') ON CONFLICT (id) DO NOTHING;
