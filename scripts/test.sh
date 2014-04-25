# test the app manually

curl -H "Accept: application/json" \
    -X POST \
    -H "Content-Type: application/json" \
    -d @scripts/strikes.json http://localhost:4000/
