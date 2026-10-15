// Catálogo de jogos do ClaudPlay.
// Só entram jogos cuja licença permite distribuir (homebrew livre, domínio
// público ou com autorização do autor). Para adicionar um jogo:
//   1. coloque a ROM em jogos/<console>/ e a capa em jogos/capas/
//   2. adicione uma entrada abaixo (rom e capa são relativos à pasta jogos/)
// Enquanto o arquivo da ROM não existir, o jogo aparece como "Em breve".
const CATALOGO = [
  {
    titulo: "Thwaite",
    console: "nes",
    genero: "Ação",
    rom: "nes/thwaite.nes",
    capa: "capas/thwaite.png",
    autor: "Damian Yerrick",
    licenca: "GPL-3.0",
    fonte: "https://github.com/pinobatch/thwaite-nes",
    descricao: "Defenda as vilas dos mísseis inimigos, no estilo Missile Command."
  },
  {
    titulo: "µCity",
    console: "gbc",
    genero: "Simulação",
    rom: "gbc/ucity.gbc",
    capa: "capas/ucity.png",
    autor: "Antonio Niño Díaz",
    licenca: "GPL-3.0 (código) / CC BY-SA 4.0 (artes e música)",
    fonte: "https://codeberg.org/SkyLyrac/ucity",
    descricao: "Construa e administre sua própria cidade, no estilo SimCity."
  },
  {
    titulo: "Libbet and the Magic Floor",
    console: "gb",
    genero: "Quebra-cabeça",
    rom: "gb/libbet.gb",
    capa: "capas/libbet.png",
    autor: "Damian Yerrick",
    licenca: "zlib",
    fonte: "https://github.com/pinobatch/libbet",
    descricao: "Role pelo chão mágico e pinte todos os quadrados."
  },
  {
    titulo: "Concentration Room",
    console: "nes",
    genero: "Memória",
    rom: "nes/croom.nes",
    capa: "capas/croom.png",
    autor: "Damian Yerrick",
    licenca: "GPL-3.0",
    fonte: "https://github.com/pinobatch/croom-nes",
    descricao: "Jogo da memória: encontre os pares de cartas."
  }
];

// Consoles suportados: core do EmulatorJS, nome e extensões de ROM
const CONSOLES = {
  snes:   { nome: "Super Nintendo",   curto: "SNES",  core: "snes",   ext: ["sfc", "smc"] },
  nes:    { nome: "NES",              curto: "NES",   core: "nes",    ext: ["nes"] },
  gb:     { nome: "Game Boy",         curto: "GB",    core: "gb",     ext: ["gb"] },
  gbc:    { nome: "Game Boy Color",   curto: "GBC",   core: "gbc",    ext: ["gbc"] },
  gba:    { nome: "Game Boy Advance", curto: "GBA",   core: "gba",    ext: ["gba"] },
  segaMD: { nome: "Mega Drive",       curto: "MD",    core: "segaMD", ext: ["md", "gen", "smd"] },
  n64:    { nome: "Nintendo 64",      curto: "N64",   core: "n64",    ext: ["n64", "z64", "v64"] },
  psx:    { nome: "PlayStation",      curto: "PS1",   core: "psx",    ext: ["cue", "chd", "pbp"] }
};
