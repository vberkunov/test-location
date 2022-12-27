CREATE TABLE location (
  id             BIGSERIAL PRIMARY KEY,
  street_name    VARCHAR(128)   NOT NULL,
  coordinates_id BIGSERIAL    NOT NULL,
  last_modified  TIMESTAMP
);