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

## App Android

O app Android (Kotlin) fica em `app/`. Ele leva o site e o emulador dentro dele
(em `assets/www`, copiados na hora da compilação), então funciona sem internet.
Anúncios: banner fora do jogo e tela cheia ao sair de um jogo (no máximo a cada 3 minutos).

A automação `.github/workflows/build.yml` compila a cada atualização:
- **APK de teste** (anúncios de teste), publicado em Releases:
  https://github.com/patreseoficial-design/claudplay/releases/download/apk/ClaudPlay.apk
- **AAB da Play Store**, assinado quando estes Secrets estão cadastrados no repositório:
  `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`
- Anúncios reais na versão da loja: Secrets `ADMOB_APP_ID`, `ADMOB_BANNER_ID`,
  `ADMOB_INTERSTICIAL_ID` (de um app **ClaudPlay** criado no AdMob)

Política de privacidade: [docs/politica-de-privacidade.md](docs/politica-de-privacidade.md)
