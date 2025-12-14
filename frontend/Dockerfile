FROM node:20-alpine AS build

WORKDIR /app

# Удаляем проблемные файлы
RUN rm -f package-lock.json
COPY package*.json ./

# Устанавливаем зависимости для Linux
RUN npm install --platform=linux --arch=x64

COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]