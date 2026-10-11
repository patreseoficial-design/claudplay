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

## App Android (APK)

A automação `.github/workflows/apk.yml` gera o APK a cada atualização e publica em
**Releases → "ClaudPlay para Android"**:
https://github.com/patreseoficial-design/claudplay/releases/download/apk/ClaudPlay.apk

O app (Capacitor) leva o site e o emulador dentro dele, então funciona sem internet.
Os arquivos ficam em `app/`; `app/montar-www.sh` copia o site para dentro do app.
