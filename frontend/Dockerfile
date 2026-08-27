# ---- build stage: produce the static SPA ----
FROM node:24 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# ---- run stage: nginx serves the build + proxies the API ----
FROM nginx:alpine AS run 
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80