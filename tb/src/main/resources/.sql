CREATE DATABASE transferbank

CREATE TABLE enderecos(

  id SERIAL PRIMARY KEY,
  logradouro varchar(255),
  numero varchar(255),
  complemento varchar(255),
  bairro varchar(255),
  cidade varchar(255),
  estado varchar(255),
  cliente_id integer references cliente(id)

)

CREATE TABLE cliente(
  id SERIAL PRIMARY KEY,
  nome varchar(225),
  email varchar(225),
  telefone varchar(225),
  cpf varchar(225),
  senha varchar(255)
)

CREATE TABLE conta (

  id serial primary key,
  cliente_id integer references cliente(id),
  agencia integer,
  numero integer,
  saldo real

)

CREATE TABLE extrato (

  id serial primary key,
  tipo varchar(255),
  valor real,
  data_hora timestamp default now(),
  nome_cliente_destino varchar(255),
  cliente_id integer references cliente(id)

)
