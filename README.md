# 1. Start Qdrant
docker-compose up -d

# 2. Run fulltext migration
mysql -u root nhom8_db < sql/V1__add_fulltext_index.sql

# 3. Pull Ollama models
ollama pull nomic-embed-text
ollama pull qwen2.5:7b-instruct

# 4. Index stories into Qdrant
cd indexer
pip install -r requirements.txt
cp .env.example .env   # edit credentials if needed
python qdrant_indexer.py

# 5. Start Spring Boot
./mvnw spring-boot:run