const CACHE_NAME = "claudplay-cache-v1";
const urlsToCache = [
  "/",
  "/index.html",
  "/style.css",
  "/manifest.json",
  "/Banner/mario_banner.jpeg",
  "/Banner/bannerbomberman.jpeg",
  "/Banner/aladdin.jpeg",
  "/Banner/bannerfutebol64.jpeg"
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
      return response || fetch(event.request);
    })
  );
});