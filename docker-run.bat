@echo off
echo Starting Farm Monitoring Platform with Docker...
echo.

docker-compose up -d --build

echo.
echo Farm Monitoring Platform is starting up!
echo.
echo Web interface will be available at: http://localhost:8080/web
echo API endpoint will be available at: http://localhost:8080/api
echo.
echo Press any key to view logs (Ctrl+C to exit logs)
pause > nul
docker-compose logs -f 