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