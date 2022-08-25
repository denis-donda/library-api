# Donda library-api
[![Build Status](https://app.travis-ci.com/denis-donda/library-api.svg?branch=master)](https://app.travis-ci.com/denis-donda/library-api) [![codecov](https://codecov.io/gh/denis-donda/library-api/branch/master/graph/badge.svg?token=F0N8PE3EQ2)](https://codecov.io/gh/denis-donda/library-api)



### DESCRIÇÃO API

Está API RestFull trata-se de um serviço de Livraria com cadastro e aluguél de livros. Com ela podemos cadastrar, consultar, atualizar e deletar livros com o path [/api/books/](https://dondalibraries-api.herokuapp.com/api/books/), e podemos simular aluguéis com a possibilidade de criar, consultar e atualizar com a devolução com o path [/api/loans/](https://dondalibraries-api.herokuapp.com/api/loans/). Possui também tratativas de possíveis erros com a má utilização da api.



### STACK DO PROJETO
- Linguagem: Java 8
- Framework: Spring Boot
- Módulo Validação: Spring Validation
- Módulo Métricas: Spring Actuator
- Persistência Dados: Spring JPA
- Bacno em Memória: H2
- TDD & BDD: Junit 5 & Mockito
- Logs: Sl4j
- Cobertura de Teste: Jacoco
- CronJob: Spring Scheduling
- MailService: Spring Mail

### LIBS EXTERNAS
- Lib verificações: AssertJ
- Lib AllArgsConstructors: Lombook
- Lib Map de Objetos: ModelMapper
- Métricas: Spring Boot Admin

### FERRAMENTAS EXTERNAS
- Versionador Código: Git & GitHub
- Documentação API: Swagger
- CI: Travis
- CD: Heroku
- Cobertura de Testes: Codecov
- Endpoints Mapeados: Postman



### Books API - Rotas

```
- POST (/api/books/) 
(201 Created)
(400 BadRequest) // Quando tenta cadastrar 2 vezes o mesmo isbn. "errors": "Isbn já cadastrado."
(401 Unauthorized)
(403 Forbidden)
(404 Not Found) // Ex: URL/api/books0 ou URL/api/books/0
(405 Method Not Allwed) // Ex: URL/api/books/a
```

```
- GET (/api/books/) ou (/{id}) ou (/{id}/loans)
(200 ok)
(401 Unauthorized)
(403 Forbidden)
(404 Not Found) // Ex: URL/api/books0 ou URL/api/books/0
(405 Method Not Allwed) // Ex: URL/api/books/a
```

```
- PUT (/api/books/{id})
(201 ok)
(401 Unauthorized)
(403 Forbidden)
(404 Not Found) // Ex: URL/api/books0 ou URL/api/books/0
(405 Method Not Allwed) // Ex: URL/api/books/a
```

```
- DELETE (/api/books/{id})
(204 No Content)
(401 Unauthorized)
(403 Forbidden)
(404 Not Found) // Ex: URL/api/books0 ou URL/api/books/0
(405 Method Not Allwed) // Ex: URL/api/books/a
```



### Loans API - Rotas

```
- POST (/api/loans) // Realizar empréstimo
(201 Created)
(400 BadRequest) "Book not found for passed isbn"
(401 Unauthorized)
(403 Forbidden)
(404 Not Found) // Ex: URL/api/books0 ou URL/api/books/0
(405 Method Not Allwed) // Ex: URL/api/books/a
```

```
- PATCH (/api/loans/id)
(200 ok)
(401 Unauthorized)
(403 Forbidden)
(404 Not Found) // Ex: URL/api/books0 ou URL/api/books/0
(405 Method Not Allwed) // Ex: URL/api/books/a
```

```
- GET (/api/loans?isbn=""&customer="Denis Donda")
(200 ok)
(401 Unauthorized)
(403 Forbidden)
(404 Not Found) // Ex: URL/api/books0 ou URL/api/books/0
(405 Method Not Allwed) // Ex: URL/api/books/a
```
