#!/bin/bash

URL="http://localhost:8080/log" 
login="login.json"

TOTAL_REQUESTS=15000

CONCURRENCY=10

ab -n $TOTAL_REQUESTS -c $CONCURRENCY -p $login -T application/json $URL