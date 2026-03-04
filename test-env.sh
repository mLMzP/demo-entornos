#!/bin/bash
echo "Iniciando nuestro entorno de pruebas"
docker-compose down -v #Cuidado: -v borra volumenes previos
docker-compose up --build -d #Construye y levanta
echo "Esperando que la App este funcionando..."
sleep 15
echo "El entorno esta listo. Datos iniciales cargados"
curl http://localhost:8080/tareas

