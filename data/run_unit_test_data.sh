#!/bin/bash

set -e

# ------------------------------
# AlexWeatherDemo - Test & Coverage Helper
# Usage:
#   ./run_unit_test_data.sh [module]
# Default module: data
# ------------------------------

# 將工作目錄切換到專案根目錄
cd "$(dirname "$0")/.."

# Run local unit tests (data module)
./gradlew :data:test

# Generate HTML coverage report (Kover)
./gradlew :data:koverHtmlReport

# Open report in browser (macOS)
open "data/build/reports/kover/html/index.html"

