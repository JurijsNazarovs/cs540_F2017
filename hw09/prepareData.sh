#!/bin/bash

fileInp=${1:-"data.txt"}
fileOut=${2:-"dataOutJava.txt"}
dataOut=${3:-"dataOut.txt"}

nData=0
printf "" > "$fileOut"
printf "" > "$dataOut"
while IFS=" " read -r years days; do
  year=$(cut -d "-" -f 1 <<< "$years")
  if [[ -z "$year" ]]; then
      continue
  fi

  if [[ $year != "\"" ]]; then
      printf "this.x.add($year.); " >> "$fileOut"
      printf "$year " >> "$dataOut"
  fi

  if [[ "$days" != *-* ]]; then
      printf "this.y.add($days.);\n" >> "$fileOut"
      printf "$days\n" >> "$dataOut"
      ((nData++))
  fi
done < "$fileInp"
printf "\nthis.size = this.y.size();\n" >> "$fileOut"
echo "number of points: $nData"


