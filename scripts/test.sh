#!/bin/sh
# test the app manually

curl -H "Accept: application/json" \
    -X POST \
    -H "Content-Type: application/json" \
    -d @scripts/strikes.json http://127.0.0.1:4000/
