package com.liftric.vault

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.Network

abstract class ContainerBase {
    companion object {

        val network: Network = Network.newNetwork()
        val vaultContainer: GenericContainer<*> =
            GenericContainer(DockerImageName.parse("hashicorp/vault:1.13.3"))
                .withPrivilegedMode(true)
                .withNetwork(network)
                .withNetworkAliases("vault")
                .withEnv("VAULT_TOKEN", VAULT_TOKEN)
                .withEnv("VAULT_ADDR", VAULT_ADDR)
                .withCommand("server -dev")
                .withExposedPorts(VAULT_PORT, 8200)
                .apply { start() }

        lateinit var vault: Vault

        init {
            try {
                vault = Vault(
                    VaultConfig()
                        .address(VAULT_ADDR)
                        .token(VAULT_TOKEN)
                        .build()
                )

                vault.logical().write(
                    "secret/example", hashMapOf(
                        "examplestring" to "helloworld",
                        "exampleint" to 1337,
                    ) as Map<String, Any>?
                )
            } catch (e: Exception) {
                println("Error occurred: $e")
            }
        }

    }
}
