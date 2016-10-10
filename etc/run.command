#!/bin/bash
cd "$(dirname "$0")"
java -Xms512m -Xmx1024m -jar hafen.jar -U http://game.havenandhearth.com/hres/ game.havenandhearth.com
