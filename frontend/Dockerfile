FROM node:22.18.0-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

RUN npm run build
RUN ls -la /app/dist/

FROM nginx:1.27.0-alpine

COPY --from=build /app/dist/todo-app/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]