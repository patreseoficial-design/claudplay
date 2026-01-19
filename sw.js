self.addEventListener("install", function(event) {
  event.waitUntil(
    caches.open("v1").then(function(cache) {
      return cache.addAll([
        "/",
        "/index.html",
        "/login.html",
        "/cadastro.html",
        "/profile.html",
        "/mario-banner.jpeg",
        "/Racepromaxbanner.jpeg"
      ]);
    })
  );
});

self.addEventListener("fetch", function(event) {
  event.respondWith(
    caches.match(event.request).then(function(response) {
      return response || fetch(event.request);
    })
  );
});