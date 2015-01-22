#!/bin/sh

# for testing multiple PDFs sent accross to the service

echo "run this script from the repo's root directory" >&2

# copy pdf file to file
file="/tmp/striky-$(date +%s)${RANDOM}"

cp form.pdf ${file}.pdf

# write the below JSON to a file
cat <<EOF > ${file}.json
{
    "input": "${file}.pdf",
    "output": "/tmp/blah.pdf",
    "strikes": [
        { "page": 2, "x": 267, "y": 557, "x1": 40, "y1": 0, "thickness": 2 },
        { "page": 2, "x": 309, "y": 557, "x1": 37, "y1": 0, "thickness": 2 }
    ],
    "flatten": true
}
EOF

# post the request
curl -H "Accept: application/json" \
    -X POST \
    -H "Content-Type: application/json" \
    -d @${file}.json http://127.0.0.1:4000/
