#!/usr/bin/env bash
# Copia o site para app/www, que vai dentro do APK.
# Do EmulatorJS entram só os núcleos dos consoles que o ClaudPlay usa.
set -euo pipefail
cd "$(dirname "$0")"
RAIZ=..
WWW=www
DATA=emulatorjs/EmulatorJS/Data
NUCLEOS="fceumm snes9x gambatte mgba genesis_plus_gx mupen64plus_next pcsx_rearmed"

rm -rf "$WWW"
mkdir -p "$WWW/$DATA/cores/reports"

cp "$RAIZ"/index.html "$RAIZ"/jogar.html "$RAIZ"/creditos.html "$RAIZ"/style.css \
   "$RAIZ"/manifest.json "$RAIZ"/service-worker.js "$WWW/"
cp -r "$RAIZ"/icons "$RAIZ"/jogos "$WWW/"

# EmulatorJS sem a pasta de núcleos
for item in "$RAIZ/$DATA"/*; do
  [ "$(basename "$item")" = "cores" ] || cp -r "$item" "$WWW/$DATA/"
done

for n in $NUCLEOS; do
  cp "$RAIZ/$DATA/cores/$n-wasm.data" "$RAIZ/$DATA/cores/$n-legacy-wasm.data" "$WWW/$DATA/cores/"
  cp "$RAIZ/$DATA/cores/reports/$n.json" "$WWW/$DATA/cores/reports/"
done

du -sh "$WWW"
