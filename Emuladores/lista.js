// Monta a grade de jogos de um console.
// pasta: nome da pasta do console dentro de Emuladores/ (ex.: "SuperNintendo")
// core:  core do EmulatorJS (ex.: "snes")
// jogos: [{ titulo, rom, banner }] — rom fica em <pasta>/roms/,
//        banner é relativo à página do console (opcional)
function montarLista(pasta, core, jogos){
  const lista = document.getElementById("listaJogos");
  const busca = document.getElementById("busca");

  jogos.forEach(jogo => {
    const card = document.createElement("a");
    card.className = "card";
    card.href = "../jogar.html?" + new URLSearchParams({
      core: core,
      rom: pasta + "/roms/" + jogo.rom,
      nome: jogo.titulo
    });

    const capa = document.createElement("div");
    capa.className = "capa";
    capa.textContent = jogo.titulo;

    if (jogo.banner) {
      const img = document.createElement("img");
      img.loading = "lazy";
      img.alt = jogo.titulo;
      img.src = jogo.banner.split("/").map(p => p === ".." ? p : encodeURIComponent(p)).join("/");
      img.onerror = () => img.remove();
      capa.appendChild(img);
    }

    const titulo = document.createElement("p");
    titulo.textContent = jogo.titulo;

    card.appendChild(capa);
    card.appendChild(titulo);
    card.dataset.busca = jogo.titulo.toLowerCase();
    lista.appendChild(card);
  });

  if (busca) {
    busca.addEventListener("input", () => {
      const termo = busca.value.trim().toLowerCase();
      lista.querySelectorAll(".card").forEach(card => {
        card.style.display = card.dataset.busca.includes(termo) ? "" : "none";
      });
    });
  }
}
