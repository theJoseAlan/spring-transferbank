
# Simulador de transferências bancárias
![](https://github.com/theJoseAlan/spring-transferbank/assets/117518719/1966541e-90ca-4ea8-984d-ef3b8b4a692d)

<h4 align="center"> 
	💰 TransferBank 💰
</h4>

<p align="center">
	<img alt="Status Concluído" src="https://img.shields.io/badge/STATUS-CONCLU%C3%8DDO-brightgreen">
</p>

## 💻 Sobre o projeto

📄 Trata-se de um simulador de transferências bancárias. O usuário poderá realizar seu cadastro, de seu endereço e abrir uma conta para utilizar os serviços da plataforma.

---

## ⚙️ Funcionalidades

- [x] Cliente:
  - [x] Cadastro
  - [x] Login
  - [x] Visualizar informações do usuário
  - [x] Atualizar
  - [x] Excluir conta (apenas sem saldo na conta)

- [x] Endereço:
  - [x] Cadastrar
  - [x] Visualizar informações do endereço
  - [x] Atualizar
     
- [x] Conta:
  - [x] Abrir
  - [x] Obter dados da conta
  - [x] Depositar (na conta logada)
  - [x] Depositar para outro usuario
  - [x] Sacar
  - [x] Transferir

- [x] Extrato:
  - [x] Listar todos
  - [x] // por tipo
  - [x] // por data
  - [x] // por data e hora

- Obs: Apenas o cadastro do cliente é realizado sem o token, que pode ser obtido no login

---

## 🛣️ Como executar o projeto

É importante que o seu computador esteja configurado para rodar uma aplicação java, além da configuração das variáveis de ambiente. 

O SQL da aplicação está disponível na pasta __resources__ e pode ser executado no seu SGBD ou bekeeper!
Feito isso, altere as propriedades do application.properties com os dados para conexão com o banco de dados.

Configure também seu servidor de e-mails, mas caso não queira utilizar essa funcionalidade basta apenas comentar as ocorrências nas classes ContaService e ClienteService.

- Veja uma demonstração do projeto em execução: linkYoutube

---

## 🛠 Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:
- Spring
  - Data Jpa
  - Starter Mail
  - Validation
  - Web
  - Devtools
  - Security crypto
  - Security JWT

- Utilitários
  - Banco de dados postgres (nuvem - railway)
  - Lombok
  - JWT (api/impl/jackson)
  - Model Mapper
  - Java JWT (auth0)

---

## 🧙‍♂️ Autor

<tr>
    <td align="center">
      <a href="https://github.com/theJoseAlan">
        <img src="https://avatars.githubusercontent.com/u/117518719?v=4" width="100px;" alt="Foto do José no GitHub"/><br>
        <sub>
          <b>José Alan</b>
        </sub>
      </a>
    </td>
  </tr>

---

## 💪 Como contribuir para o projeto

1. Faça um **fork** do projeto.
2. Crie uma nova branch com as suas alterações: `git checkout -b my-feature`
3. Salve as alterações e crie uma mensagem de commit contando o que você fez: `git commit -m "feature: My new feature"`
4. Envie as suas alterações: `git push origin my-feature`

---

## 📝 Licença

<!-- Este projeto esta sobe a licença [MIT](./LICENSE). -->

Créditos: Caio Lopes 👋🏽 [Entre em contato!](https://www.linkedin.com/in/caiovslopes/)

