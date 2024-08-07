FROM hashicorp/vault:latest
ENV VAULT_DEV_ROOT_TOKEN_ID myroottoken
COPY vault.sh .
RUN chmod +x vault.sh
ENTRYPOINT ["/vault.sh"]
CMD []
