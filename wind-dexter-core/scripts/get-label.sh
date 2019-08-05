#!/usr/bin/env bash

EXPECTED_ARGS=1
E_BADARGS=65

source scripts/config.sh


if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` id"
  exit $E_BADARGS
fi

$JAVA $CLI.GetArticleLabelCLI -id $1

