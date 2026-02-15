# 🤖SpringAI-Secretary

**(For study)** An example of using Spring AI to build a multi-agent personal secretary system. This project provides how to integrate AI-powered agents to manage tasks and handle user requests.



## 📖 Introduction



This project showcases a multi-agent system powered by Spring AI, designed to function as a personalized digital secretary. It leverages AI-driven agents to autonomously handle various tasks, including scheduling, shopping list management, and email organization.



The core objective is to demonstrate a extensible architecture that simplifies the integration of multiple intelligent agents. By decoupling agent logic and utilizing a modular builder-based pattern, this system ensures that new specialized agents can be added with minimal effort, providing a seamless and maintainable personal assistant experience.



## 🚀 Key Features



- **Multi-Agent System**: Architecture designed with independent agents specialized for distinct tasks.

- **Dynamic Routing Pattern**: Intelligent routing logic to determine the most suitable agent for each request within the multi-agent ecosystem.

- **Customizable Agent Builders**: Register builders as Spring Beans to provide independent, core functionalities tailored to each agent.

- **DTO based Tool Schema Definition**: Define standardized JSON tool schemas from DTOs to ensure seamless integration.

- **Secure MCP Integration**: Native Model Context Protocol (MCP) client support with secure credential management via `.env` files.

- **Basic RAG**: Simple implementation of Retrieval-Augmented Generation, covering vector data storage and retrieval.



## 🛠 Tech Stack


<div align="">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_AI-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <br>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker_Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
</div>

* **Language**: Java 17

* **Framework**: Spring Boot 3.x, Spring AI 1.1.2

* **Database**: PostgreSQL (PGvector)

* **Infrastructure**: Docker, Docker-compose



## ⚙️ Installation



To get started with this project, follow the steps below:



1. Clone the repository:



   ```bash
   git clone https://github.com/yourusername/Spring-AI-based-Personal-Secretary.git

   cd Spring-AI-based-Personal-Secretary
   ```


2. make `.env` file at classpath

   ```bash
   GMAIL_ACCESS_TOKEN={your_access_token}

   GOOGLE_API_KEY={your_api_key}
   ```

   you can get google access token for test by following this.

   1. **visit** *[Google OAuth Playground](https://developers.google.com/oauthplayground/ "Get Token")*

   2. **select below scope**

      - `https://www.googleapis.com/auth/gmail.modify`

      - `https://www.googleapis.com/auth/gmail.settings.basic`

   3. **click** `Authorize APIs`

   4. **click** `Exchange authorization code for tokens`

   



3. Run database container and application container

   ```bash
   docker compose up -d app
   ```



## 💬 Usage(cURL Example)



Once the application is running, you can interact with the personal secretary via the chat endpoint. Both `userId` and `ConversationId` should follow the **UUID** format.

```bash
curl -X POST http://localhost:8081/api/v1/chat \
     -H "Content-Type: application/json" \
     -H "ConversationId: f47ac10b-58cc-4372-a567-0e02b2c3d479" \
     -d '{
           "message": "Tell me more about the previous topic.",
           "userId": "550e8400-e29b-41d4-a716-446655440000"
         }'
```