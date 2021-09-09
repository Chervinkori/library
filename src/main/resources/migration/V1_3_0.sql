ALTER TABLE subscriber ADD email varchar;
UPDATE subscriber SET email = 'romachervinko@gmail.com';
ALTER TABLE subscriber ALTER COLUMN email SET NOT NULL;