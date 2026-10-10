// Catálogo de jogos do ClaudPlay.
// Só entram jogos cuja licença permite distribuir (homebrew livre, domínio
// público ou com autorização do autor). Para adicionar um jogo:
//   1. coloque a ROM em jogos/<console>/
//   2. adicione uma entrada abaixo (rom é relativo à pasta jogos/)
// Enquanto o arquivo da ROM não existir, o card aparece como "Em breve".
const CATALOGO = [
  {
    titulo: "Thwaite",
    console: "nes",
    rom: "nes/thwaite.nes",
    autor: "Damian Yerrick",
    licenca: "GPL-3.0",
    fonte: "https://github.com/pinobatch/thwaite-nes",
    descricao: "Defenda as vilas dos mísseis inimigos, no estilo Missile Command."
  },
  {
    titulo: "Concentration Room",
    console: "nes",
    rom: "nes/croom.nes",
    autor: "Damian Yerrick",
    licenca: "GPL-3.0",
    fonte: "https://github.com/pinobatch/croom-nes",
    descricao: "Jogo da memória: encontre os pares de cartas."
  },
  {
    titulo: "Libbet and the Magic Floor",
    console: "gb",
    rom: "gb/libbet.gb",
    autor: "Damian Yerrick",
    licenca: "zlib",
    fonte: "https://github.com/pinobatch/libbet",
    descricao: "Quebra-cabeça: role pelo chão mágico e pinte todos os quadrados."
  },
  {
    titulo: "Tobu Tobu Girl",
    console: "gb",
    rom: "gb/tobutobugirl.gb",
    autor: "Tangram Games",
    licenca: "MIT (código) / CC BY 4.0 (artes e música)",
    fonte: "https://github.com/SimonLarsen/tobutobugirl",
    descricao: "Plataforma arcade: pule cada vez mais alto para resgatar o gato."
  },
  {
    titulo: "µCity",
    console: "gbc",
    rom: "gbc/ucity.gbc",
    autor: "Antonio Niño Díaz",
    licenca: "GPL-3.0 (código) / CC BY-SA 4.0 (artes e música)",
    fonte: "https://codeberg.org/SkyLyrac/ucity",
    descricao: "Simulador de cidade, no estilo SimCity."
  }
];

// Consoles suportados: core do EmulatorJS, nome e extensões de ROM
const CONSOLES = {
  nes:    { nome: "NES",            core: "nes",    ext: ["nes"] },
  snes:   { nome: "Super Nintendo", core: "snes",   ext: ["sfc", "smc"] },
  gb:     { nome: "Game Boy",       core: "gb",     ext: ["gb"] },
  gbc:    { nome: "Game Boy Color", core: "gbc",    ext: ["gbc"] },
  gba:    { nome: "Game Boy Advance", core: "gba",  ext: ["gba"] },
  segaMD: { nome: "Mega Drive",     core: "segaMD", ext: ["md", "gen", "smd"] },
  n64:    { nome: "Nintendo 64",    core: "n64",    ext: ["n64", "z64", "v64"] },
  psx:    { nome: "PlayStation",    core: "psx",    ext: ["cue", "chd", "pbp"] }
};
