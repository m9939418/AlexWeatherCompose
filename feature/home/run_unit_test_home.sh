#!/bin/bash

set -e

# ------------------------------
# AlexWeatherDemo - Test & Coverage Helper
# Usage:
#   ./run_unit_test_home.sh [module]
# Default module: feature:home
# ------------------------------

# 將工作目錄切換到專案根目錄
cd "$(dirname "$0")/../.."

# Run local unit tests (data module)
./gradlew :feature:home:test

# Generate HTML coverage report (Kover)
./gradlew :feature:home:koverHtmlReport

# Open report in browser (macOS)
open "feature/home/build/reports/kover/html/index.html"

