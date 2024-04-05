FROM alpine:3.19.1

ENV GLIBC_VERSION=2.35-r1

ENV BASE_URL_HELM="https://get.helm.sh"

ENV HELM_VERSION=v3.13.3

# Install necessary dependencies
RUN apk add --no-cache curl ca-certificates

# Download and install kubectl
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
    chmod +x kubectl && \
    mv kubectl /usr/local/bin/

# Download and install Helm
RUN curl -LO https://get.helm.sh/helm-${HELM_VERSION}-linux-amd64.tar.gz && \
    tar -zxvf helm-${HELM_VERSION}-linux-amd64.tar.gz && \
    mv linux-amd64/helm /usr/local/bin/helm && \
    chmod +x /usr/local/bin/helm && \
    rm -rf helm-${HELM_VERSION}-linux-amd64.tar.gz linux-amd64

# Install jq
RUN apk --no-cache add jq