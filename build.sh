#!/usr/bin/env bash

rm -fr dist
git clone git@github.com:jarppe/times-tables.git dist
cd dist
git checkout gh-pages
rm js/app.js
cd ..
lein dist
cp ./resources/public/index.html dist
cd dist
git add .
git commit -m "."
git push
cd ..
