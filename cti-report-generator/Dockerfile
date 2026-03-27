FROM python:3.12-slim

LABEL maintainer="Nokia Networks Security Intelligence Center"
LABEL description="CTI Daily Digest Report Generator"
LABEL version="1.0.0"

# Install font dependencies for PIL image generation
RUN apt-get update && apt-get install -y --no-install-recommends \
    fonts-dejavu-core \
    fonts-dejavu-extra \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy generator source
COPY requirements.txt .
RUN pip install --no-cache-dir --trusted-host pypi.org --trusted-host files.pythonhosted.org -r requirements.txt

COPY generate.py .
COPY images.py   .

# Output directory — mount a volume here to get the report out
RUN mkdir -p /app/output /app/manifest

VOLUME ["/app/manifest", "/app/output"]

# Default: read manifest from /app/manifest/manifest.yaml
# Output goes to /app/output/
ENTRYPOINT ["python3", "generate.py"]
CMD ["--manifest", "/app/manifest/manifest.yaml", \
     "--out",      "/app/output/CTI_Digest.docx",  \
     "--img-dir",  "/app/output/images"]
