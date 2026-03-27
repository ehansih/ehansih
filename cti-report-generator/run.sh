#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# run.sh — One-command CTI Report Generator
# Usage:
#   ./run.sh                          # uses manifest.yaml, auto-names output
#   ./run.sh --manifest my.yaml       # custom manifest
#   ./run.sh --out reports/daily.docx # custom output path
# ─────────────────────────────────────────────────────────────────────────────
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "=========================================="
echo "  CTI Daily Digest Report Generator"
echo "=========================================="

# Install dependencies if needed
if ! python3 -c "import docx, PIL, yaml" 2>/dev/null; then
    echo "[*] Installing Python dependencies..."
    pip install -r requirements.txt -q --break-system-packages 2>/dev/null || \
    pip install -r requirements.txt -q
fi

echo "[*] Generating report..."
python3 generate.py "$@"

echo ""
echo "[✓] Done! Check the output/ directory."
