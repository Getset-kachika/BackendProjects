#!/bin/bash

GCP_BUCKET="gs://rds_log_ingestion_service/Logs/"
TOTAL_REQUESTS=1000

TEMP_DIR=$(mktemp -d)
echo "Using temporary directory: $TEMP_DIR"

echo "Downloading all files from GCP..."
gsutil -m cp -r "${GCP_BUCKET}" "$TEMP_DIR/"

if [[ $? -ne 0 ]]; then
    echo "Failed to download files from GCP."
    exit 1
fi

LINE_COUNT=0
for file in "$TEMP_DIR"/*; do
    if [[ -f "$file" ]]; then
        FILE_LINE_COUNT=$(wc -l < "$file")
        LINE_COUNT=$((LINE_COUNT + FILE_LINE_COUNT))
        echo "File: $(basename "$file") - Lines: $FILE_LINE_COUNT"
    fi
done

if [[ "$LINE_COUNT" -eq "$TOTAL_REQUESTS" ]]; then
    echo "Total line count matches total requests: $LINE_COUNT"
else
    echo "Total line count ($LINE_COUNT) does not match total requests ($TOTAL_REQUESTS)"
fi

rm -rf "$TEMP_DIR"
echo "Temporary directory cleaned up."

