# `strike 2`

App for crossing sections in PDF documents

[![Build Status](https://travis-ci.org/ministryofjustice/strike2.svg)](https://travis-ci.org/ministryofjustice/strike2)

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server-headless

## Deploying

Run:

    lein ring uberjar

then start it with:

    java -jar target/strike2-X.Y.Z-standalone.jar

Also look at [scripts/strike2.conf](scripts/strike2.conf) for
supervisord startup script.

## License

Copyright © 2014 Ministry of Justice
