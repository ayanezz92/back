-- 🚨 CORREGIDO: este archivo se llamaba "import.sql" e insertaba en columnas
-- ("campaña", "donante_nombre") que no existen en la entidad Donacion
-- (donante, monto, destino, metodo). Además, "import.sql" no es un nombre que
-- Spring Boot ejecute automáticamente (solo lee data.sql/schema.sql), así que
-- estos datos de ejemplo nunca llegaban a la base de datos.
INSERT INTO donaciones (donante, monto, destino, metodo) VALUES ('Juan Perez', '25000', 'Alimento Invierno Alerce', 'Transferencia');
INSERT INTO donaciones (donante, monto, destino, metodo) VALUES ('Camila Soto', '45000', 'Operacion Canina Milo', 'Webpay');
INSERT INTO donaciones (donante, monto, destino, metodo) VALUES ('Anonimo', '15000', 'Campana General Rescate', 'Webpay');
