# Relationship Center

Projeto desenvolvido em **Spring Boot** para gerenciamento e distribuição de requests entre atendentes e times.

---

## 🚀 Tecnologias
- Java 21 (Zulu JDK)
- Spring Boot
- Maven (ou Gradle, se aplicável)
- JUnit 5

---

## ⚙️ Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:
- [Java 21 (Zulu ou OpenJDK)](https://www.azul.com/downloads/?package=jdk)
- [Maven](https://maven.apache.org/download.cgi) (se usar Maven)  
  ou  
  [Gradle](https://gradle.org/install/) (se usar Gradle)
- Uma IDE como [IntelliJ IDEA](https://www.jetbrains.com/idea/) ou [Eclipse](https://www.eclipse.org/)

---

## ▶️ Executando o projeto

### 1. Clonar o repositório
```bash
[git clone https://github.com/seu-usuario/relationshipcenter.git](https://github.com/MarhlonKorb/relationshipcenter.git)
cd relationshipcenter

## Abrir postman ou insomnia:
  Executar post para http://localhost:8080/api/v1/request-assistance
  body do tipo json com usa das alternativas abaixo:
  
  // {
//   "subject": {
//     "description": "Problemas com cartão"
//   }
// }

// {
//   "subject": {
//     "description": "Contratação de empréstimo"
//   }
// }

{
  "subject": {
    "description": "Reclamações"
  }
}
```
![Texto alternativo](caminho/para/imagem.png)

Existem 3 filas de atendimento cadastradas no start da aplicação:

- Cartões
- Empréstimo
- Outros assuntos

  Todas as descrições que forem diferentes de "Problemas com cartão" ou "Contratação de empréstimo" serão direcionadas a fila "Outros assuntos".

  As solicitações adicionadas nas filas expiram em 30 segundos.

  Cada atendente de cada equipe pode atender apenas a 3 solicitações simultaneamente.
