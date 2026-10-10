const CACHE_NAME = "claudplay-cache-v4";
const urlsToCache = [
  "./",
  "./index.html",
  "./style.css",
  "./manifest.json",
  "./icons/icon-192x192.png",
  "./icons/icon-512.png",
  "./jogar.html",
  "./creditos.html",
  "./jogos/catalogo.js"
];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(urlsToCache))
  );
  self.skipWaiting();
});

// Remove caches antigos (a v1 tinha arquivos que não existem)
self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((nomes) =>
      Promise.all(nomes.filter((n) => n !== CACHE_NAME).map((n) => caches.delete(n)))
    )
  );
  self.clients.claim();
});

// Busca na rede primeiro (para pegar atualizações) e usa o cache se estiver offline.
// Arquivos de outros sites não passam pelo cache.
self.addEventListener("fetch", (event) => {
  const url = new URL(event.request.url);
  if (event.request.method !== "GET" || url.origin !== self.location.origin) {
    return;
  }
  event.respondWith(
    fetch(event.request).catch(() => caches.match(event.request))
  );
});
