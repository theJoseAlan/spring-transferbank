CREATE DATABASE transferbank


CREATE TABLE enderecos(

  id SERIAL PRIMARY KEY,
  logradouro varchar(255),
  numero varchar(255),
  complemento varchar(255),
  bairro varchar(255),
  cidade varchar(255),
  estado varchar(255)

)

CREATE TABLE clientes(
  id SERIAL PRIMARY KEY,
  nome varchar(225),
  email varchar(225),
  telefone varchar(225),
  cpf varchar(225),
  endereco_id integer references enderecos(id)
)

ALTER TABLE clientes ADD senha varchar(255);
