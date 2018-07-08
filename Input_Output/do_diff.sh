#!/bin/bash

for (( i = 1; i <= 30; ++i )); do
    diff "rtr_${i}a" "rtr_${i}b"
done
