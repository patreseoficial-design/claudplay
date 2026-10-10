# ClaudPlay

Jogos retrô no navegador, sem baixar nada. Usa o [EmulatorJS](https://github.com/EmulatorJS/EmulatorJS).

- `index.html`: catálogo de jogos
- `jogar.html`: abre um jogo do catálogo (`?jogo=...`) ou, sem parâmetro, uma ROM do próprio aparelho ("Abrir meu jogo")
- `jogos/catalogo.js`: lista de jogos e consoles
- `creditos.html`: autores e licenças

## Adicionar um jogo ao catálogo

Só entram jogos cuja licença permite distribuir (homebrew livre, domínio público ou com autorização por escrito do autor).

1. Coloque a ROM em `jogos/<console>/` (ex.: `jogos/nes/thwaite.nes`).
2. Adicione uma entrada em `jogos/catalogo.js` com título, autor, licença e link do código-fonte.

Enquanto o arquivo da ROM não existir, o jogo aparece como "Em breve".
