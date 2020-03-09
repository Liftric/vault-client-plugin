#!bin/sh
export VAULT_DEV_ROOT_TOKEN_ID=myroottoken
/usr/local/bin/docker-entrypoint.sh server -dev &
echo "started vault"
sleep 1
export VAULT_ADDR='http://0.0.0.0:8200'
export VAULT_TOKEN='myroottoken'
vault token lookup
vault kv put secret/example examplestring=helloworld exampleint=1337
vault kv get secret/example
wait $pid
