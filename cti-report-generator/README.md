# CTI Daily Digest Report Generator

Generates a fully formatted **Cyber Threat Intelligence Daily Digest** Word document from a YAML manifest.

## Quick Start

```bash
# 1. Edit the manifest with today's threats
nano manifest.yaml

# 2. Run the generator
./run.sh

# 3. Find your report in output/
ls output/
```

## File Structure

```
cti-report-generator/
├── manifest.yaml      ← EDIT THIS every day with new threats/IOCs
├── generate.py        ← Main report builder (reads manifest → .docx)
├── images.py          ← Section banner image generator
├── run.sh             ← One-command runner
├── requirements.txt   ← Python dependencies
└── output/            ← Generated reports land here (git-ignored)
```

## Manifest Structure

The `manifest.yaml` file drives everything. Update these sections daily:

| Section | Description |
|---------|-------------|
| `report` | Org name, date, classification, title |
| `vulnerabilities` | CVEs and exploits with CVSS scores |
| `malware` | Emerging malware families |
| `campaigns` | Active threat campaigns |
| `breaches` | Breach notifications |
| `iocs` | Domains, IPs, hashes, URLs |
| `threat_actors` | Actor profiles |
| `action_items` | Priority 1/2/3 remediation steps |

## Severity & Telecom Relevance Values

```yaml
severity: CRITICAL | HIGH | MEDIUM | LOW
telecom_relevance: HIGH | MEDIUM | LOW
```

## Advanced Usage

```bash
# Custom manifest
./run.sh --manifest 2026-02-17.yaml

# Custom output path
./run.sh --out reports/Nokia_CTI_17Feb2026.docx

# Custom image directory
./run.sh --img-dir /tmp/report-images

# All options
python3 generate.py --manifest manifest.yaml --out output/report.docx --img-dir output/images
```

## Adding a New Threat

Copy this block into the appropriate section in `manifest.yaml`:

```yaml
- title: "Threat Title Here"
  date: "17 February 2026"
  severity: HIGH
  telecom_relevance: HIGH
  cve:
    - CVE-2026-XXXX          # optional
  cvss: "8.5"                # optional
  description: >
    Detailed description of the threat.
  affected: "Affected products and versions"
  mitre_ttps:
    - "T1566.002 Spearphishing Link"
    - "T1059.001 PowerShell"
  recommendations:
    - "First action item."
    - "Second action item."
  reference: "https://source-url.com"
```

## Generated Report Sections

1. **Cover** — Cyber Telligence newspaper-style front page
2. **Executive Summary** — Threat count dashboard + landscape overview table
3. **Vulnerabilities & Exploit Alert** — CVEs with CVSS scores
4. **Emerging Malware Notification** — New malware families
5. **Active Campaigns & Threat Groups** — Ongoing campaigns
6. **Breaches Notification** — Active breach campaigns
7. **Indicators of Compromise** — Domains, IPs, hashes, URLs tables
8. **Threat Actor Profiles** — Actor background and targeting
9. **Consolidated Action Items** — Priority 1/2/3 remediation plan
