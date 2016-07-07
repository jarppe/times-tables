#!/usr/bin/env bash

lein dist
cp ./resources/public/index.html dist
