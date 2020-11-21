# Confident PS Spring Boot Application - Microservices and Web Engineering

## Projeto criado para a PS referente a aplicação do Babysitter pela DSRPT2021.

### Turma: 3SIT

### Integrantes do projeto:

|        Nome       |   RM   |
|-------------------|--------|
|Vinicius Felix     | 81247  |
|Lucas Barreto      | 80511  |
|Ysmar Victor       | 80196  |
|Vinicius Andrade   | 79937  |
|Jhoe Nascimento    | 81525  |

### Tecnologias
- [X] Gradle
- [X] Spring Boot
- [X] Spring Data JPA
- [X] Hibernate
- [X] Bean Validation
- [X] Oracle Driver

### Como rodar a aplicação:

O projeto está em formato REST, portanto, é apenas necessário importar o projeto Gradle diretamente na IDE e alterar o ```application.properties``` e o ```flyway.properties``` informando seu acesso para o usuário e senha referentes ao Oracle da FIAP nas propriedades: 
```
spring.datasource.username=usuario
spring.datasource.password=senha
```
e
```
flyway.username=usuario
flyway.password=senha
```


O projeto está utilizando FlyWay migrations, então, para a primeira execução é necessário DROPAR as TABELAS ACAO e EXECUCAO do banco de dados Oracle.


Para testes, a aplicação provê um banco em memória que é utilizado apenas para execução dos testes de integração.


### Acessar a aplicação


O endpoint relacionado ao CRUD de acoes se encontra em ```localhost:8080/acoes```.

O endpoint relacionado ao CRUD de execucoes se encontra em ```localhost:8080/execucoes```.

Para ver a aplicação funcionando em produção com um front-end 
feito em Angular utilizando a MESMA tecnologia em um banco PostgreSQL, basta acessar: https://fiap-babysitter-ui.vercel.app/acoes

_Obrigado!_
