self.addEventListener("install", event => {
  event.waitUntil(
    caches.open("claudplay-cache").then(cache => {
      return cache.addAll([
        "/claudplay/",
        "/claudplay/index.html",
        "/claudplay/manifest.json"
      ]);
    })
  );
});

self.addEventListener("fetch", event => {
  event.respondWith(
    caches.match(event.request).then(response => {
      return response || fetch(event.request);
    })
  );
});
