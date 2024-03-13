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
vault namespace create test
vault namespace create -namespace=test child
vault kv put -namespace=test secret/example examplestring=hellochild exampleint=1338
vault kv put -namespace=test/child secret/example examplestring=hellochildchild exampleint=1339
wait $pid
