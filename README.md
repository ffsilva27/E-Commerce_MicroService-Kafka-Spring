# :man_technologist: Curso Web Full Stack - Let's Code :man_student:

## :rocket: Projeto Módulo 10 - Tópicos Avançados (Micro Serviço | Mensageria-Kafka | BD-PostgreSQL) 

O módulo possibilitou desenvolvermos uma aplicação utilizando a arquitetura de micro serviços e serviço de mensageria(Kafka), além de apliarmos nossos conhecimentos
quanto ao padrão de arquitetura REST, as funcionalidades do Spring data JPA, MVC e Security e o banco de dados relacional(PostgreSQL).


Obs.: Para o correto funcionamento, rode o script de criação das tabelas do BD(CreateDataBase.txt), os 03 micro serviços(User, Compra e Produto) e o kafka precisam estar em execução.

___________________________________________________________________________________________________________________________________________________________________

### :scroll: Atividade

Construção de uma API que possibilite:
 - Criação de produtos;
 - Listagem dos produtos cadastrados (com paginação);
 - Informar, na rota acima, dados do produto conforme filtro pelo código;
 - Compra do(s) produto(s) cadastrado(s);
 - Listagem das compras realizadas (com paginação);
 - Informar, na rota acima, dados da compra conforme filtro pelo cpf;
 - Criação de usuário (utilizando criptografia - BCrypt);
 - Proteção das rotas possibilitando acesso apenas por usuários cadastrados.

<br>

#### Requisitos funcionais
Na API implementamos:
 - Padrão de arquitetura REST;
 - Arquitetura de micro serviço;
 - Serviço de mensageria(Kafka); 
 - Autenticação de rotas;
 - Banco de dados relacional.
___________________________________________________________________________________________________________________________________________________________________

### :chains: Tecnologias
- Java
- Spring (WebFlux | Data JPA | Security | MVC)
- Kafka
- Banco de dados PostgreSQL
- Lombok
- Maven

___________________________________________________________________________________________________________________________________________________________________

### :man_technologist: Implementações


#### :chart: Criação de usuário
Todas as rotas devem possuir autenticação, sendo assim para acessa-las o usuário deve possuir cadastro e efetuar o login para que possa usar o token JWT no header da requisição com topico "Authorization" e valor "Bearer {token_jwt}" .

<br>

As informações para a realização do cadastro do usuário em sistema são: nome de usuário, senha e nível de autoridade.

##### Método HTTP para criação do produto/JSON
 - (POST) `localhost:8083/user`
```json
{
  "userName": "user04",
  "password": "user04",
  "authority": ["CLIENTE", "ADMIN"],
  "enabled": true
}
```

##### Retorno da aplicação
`Status Code - 201 - Created`

<br>

#### :chart: Login

##### Método HTTP para criação do produto/JSON
 - (POST) `localhost:8083/user/login`
```json
{
  "userName": "user04",
  "password": "user04"
}
```

##### Retorno da aplicação/JSON
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNeUFQUCIsInN1YiI6InVzZXIwNCIsImlhdCI6MTY1MTM0NjQxOSwiZXhwI
  joxNjUxMzUwMDE5LCJyb2xlIjoiUk9MRV9BRE1JTiJ9.OcM8L7vLKrBKUBM9uwJQAFuD7PVJuOEH8OW7ksXCK2I"
}
```

#### :chart: Criar produtos
O produto deve conter os seguintes dados: código do produto (implementado lógica para geração pela aplicação), preço e quantidade disponível.

##### Método HTTP para criação do produto/JSON
 - (POST) `localhost:8081/produto`
```json
{
	"nome": "Smart Watch",
	"preco": 1500.00,
	"qtdeDisponivel": 22
}
```
##### Retorno da aplicação/JSON
```json
{
	"codigo": "V905",
	"nome": "Smart Watch",
	"preco": 1500.0,
	"qtde_disponivel": 22
}
```
<br>

#### :chart: Listar os produtos cadastrados
A rota deve possibilitar a geração da listagem (paginada) de todos os produtos cadastrados, assim como o filtro por código do produto. 

##### Método HTTP para obter a listagem total dos produtos (paginada)
 - (GET) `localhost:8081/produto`

##### Método HTTP para obter um produto especificando seu código
 - (GET) `localhost:8081/produto?codigo={codigoDoProduto}`

Obs.: Para obtenção das informações de um produtos específico deve-se informar o código deste na query, conforme acima (sem as chaves).

##### Retorno da aplicação/JSON
```json
{
	"content": [
		{
			"codigo": "V905",
			"nome": "Smart Watch",
			"preco": 1500.0,
			"qtde_disponivel": 22
		}
	],
	"pageable": {
		"sort": {
			"empty": true,
			"sorted": false,
			"unsorted": true
		},
		"offset": 0,
		"pageNumber": 0,
		"pageSize": 20,
		"unpaged": false,
		"paged": true
	},
	"last": true,
	"totalElements": 1,
	"totalPages": 1,
	"size": 20,
	"number": 0,
	"sort": {
		"empty": true,
		"sorted": false,
		"unsorted": true
	},
	"first": true,
	"numberOfElements": 1,
	"empty": false
}
```
<br>

#### :chart: Efetuar uma compra
A aplicação possibilita que os produtos cadastrados possam ser adiquiridos, sendo os elementos mínimos para o correto funcionamento
a inserção dos seguintes dados: data da compra, cpf do cliente, código do produto e quantidade.

##### Método HTTP para criação do produto/JSON
 - (POST) `localhost:8082/compra`

```json
{
	"data": "2022-04-02T13:34:00.000",
	"cpf": "123456789",
	"produtos": 
		{
			"V905": 2
		}
}
```

Obs.: É possivel adicionar vários produtos à mesma compra.

##### Retorno da aplicação
`Status Code - 200 - OK`

<br>

#### :chart: Listar compras
A rota deve possibilitar a geração da listagem (paginada) de todas as compras realizadas, assim como o filtro por cpf do cliente. 

##### Método HTTP para obter a listagem total dos produtos (paginada)
 - (GET) `localhost:8082/compra`

##### Método HTTP para obter um produto especificando seu código
 - (GET) `localhost:8082/compra?cpf={cpfDoCliente}`

Obs.: Para obtenção das informações de compra sobre um cliente específico, deve-se informar o cpf do cliente na query, conforme acima (sem as chaves).

##### Retorno da aplicação/JSON
```json
{
	"content": [
		{
            "data_compra": "2022-04-02T13:34:00",
            "cpf_cliente": "123456789",
            "valor_total_compra": 3000.0,
            "status": "CONCLUIDO",
			"produtos": [
				{
					"codigo": "V905",
					"nome": "Smart Watch",
					"preco_unitario": 1500.0,
					"quantidade": 2
				}
			]
		}
	],
	"pageable": {
		"sort": {
			"empty": true,
			"sorted": false,
			"unsorted": true
		},
		"offset": 0,
		"pageNumber": 0,
		"pageSize": 20,
		"unpaged": false,
		"paged": true
	},
	"last": true,
	"totalElements": 1,
	"totalPages": 1,
	"size": 20,
	"number": 0,
	"sort": {
		"empty": true,
		"sorted": false,
		"unsorted": true
	},
	"first": true,
	"numberOfElements": 1,
	"empty": false
}
```

## :man_technologist: Desenvolvedores<br>
[Filipe Silva](https://github.com/ffsilva27) , 
[Felipe Garé](https://github.com/FelipeRodriguesGare) ,  
[Vitor Zillig](https://github.com/VitorZillig) ,
[Samuel Bruing](https://github.com/sgbruing) .
