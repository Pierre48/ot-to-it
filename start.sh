docker-compose up -d
curl -X POST -H "Content-Type: application/json" --data "@config/plc4x-source.json" http://localhost:8083/connectors
