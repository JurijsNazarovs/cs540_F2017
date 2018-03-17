#!/bin/bash

dir="$1"
if [[ ! -d "$dir" ]]; then
    echo "No input directory is provided or exist"
    exit 1
fi

nFiles="$(ls "$dir" | awk 'END{print NR}')"
echo "Analysis of $nFiles files from $dir"

LC_CTYPE=C && LANG=C && cat "$dir"/* | tr [:space:] "\n" | grep '[^[:blank:]]' | sort | uniq -c | sort -bgr > sorted.list
head -20 sorted.list > top20.list
tail -20 sorted.list > bottom20.list

less sorted.list | awk '{i = i + $1} END {print "Total words:  " i "\nUnique words: " NR}'
echo "count,rank" > ranked.list
less sorted.list | awk '{print $1 "," NR}' >> ranked.list



