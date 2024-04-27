CREATE table customers (
    id serial NOT NULL PRIMARY KEY,
    name  varchar(50) NOT NULL UNIQUE);

CREATE table authors (
    id serial NOT NULL PRIMARY KEY,
    name  varchar(50) NOT NULL UNIQUE);

CREATE table books (
    id serial NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE,
    price numeric(10,2),
    authors_id integer,
CONSTRAINT authors_fkey FOREIGN KEY (authors_id) REFERENCES authors(id));

CREATE table customers_books (
    customer_id integer,
    book_id integer,
CONSTRAINT customers_fkey FOREIGN KEY (customer_id) REFERENCES customers(id)  ON DELETE CASCADE ,
CONSTRAINT books_fkey FOREIGN KEY (book_id) REFERENCES books(id)  ON DELETE CASCADE);

CREATE INDEX customers_name_idx ON customers (name);
CREATE INDEX authors_name_idx ON authors (name);
CREATE INDEX books_name_idx ON books (name);


INSERT INTO customers (name)
VALUES ('James'),('William'),('Jacob'),('Michael');

INSERT INTO authors (name)
VALUES ('Joanne_Rowling'),('Morgan_Meyer'),('Stephen_King'),('Winston_Groom'),('Pamela_Travers');

INSERT INTO books (name,price,authors_id)
VALUES
('Harry_Potter_part1',9.95,1),
('Harry_Potter_part2',8.95,1),
('Twilight',18.21,2),
('It',11.99,3),
('Forrest_Gump',11.95,4),
('Mary_Poppins',24.99,5);

INSERT INTO customers_books (customer_id, book_id)
VALUES (1,3),(1,2),(2,5),(2,2),(3,1),(3,4),(4,1),(4,5),(1,6),(3,6);
