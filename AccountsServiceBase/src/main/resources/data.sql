-- Crear la tabla customers
-- LO AÑADI PARA PODER HACER TESTING YA QUE ME DABA ERROR

CREATE TABLE IF NOT EXISTS customers
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Crear la tabla accounts
CREATE TABLE IF NOT EXISTS accounts
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    type         VARCHAR(50)    NOT NULL,
    opening_date DATE           NOT NULL,
    balance      DECIMAL(10, 2) NOT NULL,
    owner_id     BIGINT,
    FOREIGN KEY (owner_id) REFERENCES customers (id)
);

INSERT INTO customers (name, email) VALUES
('Adrian', 'adri@adri.com'),
('Adri', 'adrian@adrian.com'),
('Daniel', 'daniel@daniel.com'),
('Carol', 'carol@carol.com');

INSERT INTO accounts (type, opening_date,balance,owner_id) VALUES
('Personal','2023-10-01', 1000, 1),
('Personal','2023-09-01', 100, 1),
('Company','2023-10-01', 80000, 3),
('Personal','2023-07-01', 1000, 2),
('Company','2023-07-01', 5000, 4);
