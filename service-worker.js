const CACHE_NAME = "claudplay-cache-v1";
const urlsToCache = [
  "/",
  "/index.html",
  "/style.css",
  "/manifest.json",

  // Banners
  "/Banner/mario_banner.jpeg",
  "/Banner/bannerbomberman.jpeg",
  "/Banner/aladdin.jpeg",
  "/Banner/bannerfutebol64.jpeg",

  // Ícones (importante para o PWA)
  "/icons/icon-192x192.png",
  "/icons/icon-512x512.png"
];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(urlsToCache);
    })
  );
});

self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => {
      // Se existir no cache, retorna
      if (response) return response;

      // Se não, tenta buscar na web
      return fetch(event.request).catch(() => {
        // Se falhar (sem internet), retorna o index (fallback)
        return caches.match("/index.html");
      });
    })
  );
});