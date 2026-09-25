# rag-spring-boot-setup
In this project we setup a spring boot RAG application used in order to create a simple RAG application

## Tech stack:
1. Spring Boot
2. Spring AI
3. Pg Vector(For embedding)
4. Swagger(For documentation)
5. llama3.2 model(LLM model)-running remotely
6. nomic-embed-text model(embedding-model)-running-remotely

## What was done?
Basically we have a documentation called (Constitution-of-Nepal.pdf).

First, we used Spring Boot's CommandLineRunner to ingest the pdf into pgvector db, the table vector_store was created on running docker image.

```sql
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store (
                                            id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(1024)
    );

CREATE INDEX ON vector_store USING HNSW (embedding vector_cosine_ops);
```

And we used spring ai libraries in order to create PDF pages and used text splitter in order to tokenize them, and finally feeding them to vector store in order to store chunks and embeddings.

```java
    @Override
    public void run(String... args) throws Exception {
        Integer count = jdbcClient.sql("SELECT COUNT(*) FROM vector_store").query(Integer.class).single();
        if (count == 0) {
            var reader = new PagePdfDocumentReader(
                    resource,
                    PdfDocumentReaderConfig
                            .builder()
                            .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                            .withPagesPerDocument(1)
                            .build());
            List<Document> pages = reader.get();
            List<Document> cleanedPages = pages.stream()
                    .map(this::cleanWhitespace)
                    .toList();
            TextSplitter splitter = TokenTextSplitter.builder().build();
            List<Document> chunks = splitter.apply(cleanedPages);
            vectorStore.accept(chunks);
            LOG.info("The chunks are store in the vector store...");
        } else
            LOG.info("The embeddings already present in vector store");

    }

    private Document cleanWhitespace(Document doc) {
        String cleaned = doc.getText()
                .replaceAll("[ \\t]{2,}", " ")   // collapse repeated spaces/tabs into one
                .replaceAll("\\n{3,}", "\n\n")   // collapse excessive blank lines
                .trim();
        return new Document(cleaned, doc.getMetadata());
    }
```

And finally from service we call chat client in order to call upon LLMs.
The chat client configuration looks like this. Here we add system prompt and default advisor as vector_store so that
vector similarity searching is done and chunks are augmented when sending the request to LLM
```java
    @Bean
    ChatClient chatClient(ChatClient.Builder builder, VectorStore store) {
        return builder
                .defaultSystem("You are a chat assistant that only answers questions, regarding the information stored in vector store," +
                        "if asked for anything else deny the request ")
                .defaultAdvisors(QuestionAnswerAdvisor.builder(store).build())
                .build();
    }
```

Currently this is the whole process that is being carried out, nothing else is done here.