package com.udgar.rag.learning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IngestionService implements CommandLineRunner {

    private final Logger LOG = LoggerFactory.getLogger(IngestionService.class);
    private final VectorStore vectorStore;
    private final JdbcClient jdbcClient;

    @Value("classpath:docs/Constitution-of-Nepal.pdf")
    private Resource resource;

    public IngestionService(VectorStore vectorStore, JdbcClient jdbcClient) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
    }

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
}
