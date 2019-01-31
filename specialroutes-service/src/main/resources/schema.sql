DROP TABLE IF EXISTS abtesting;

CREATE TABLE abtesting (
  service_name VARCHAR(100) PRIMARY KEY NOT NULL,
  active       boolean default false    not null,
  endpoint     VARCHAR(100)             NOT NULL,
  weight       INT
);


INSERT INTO abtesting (service_name, active, endpoint, weight)
VALUES ('organization-service', false, 'http://localhost:8610', 5);
