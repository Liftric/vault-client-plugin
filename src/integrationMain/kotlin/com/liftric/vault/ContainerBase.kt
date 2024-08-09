package com.liftric.vault

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

abstract class ContainerBase {
    companion object {
        val vaultContainer: GenericContainer<*> =
            GenericContainer(DockerImageName.parse("hashicorp/vault:1.13.3"))
                .withEnv("VAULT_TOKEN", VAULT_TOKEN)
                .withEnv("VAULT_ADDR", VAULT_ADDR)
                .withCommand("server -dev -dev-root-token-id $VAULT_TOKEN")
                .withCreateContainerCmdModifier { cmd ->
                    cmd.withHostConfig(
                        HostConfig().withPortBindings(
                            PortBinding(
                                Ports.Binding.bindPort(VAULT_PORT),
                                ExposedPort(VAULT_PORT)
                            )
                        )
                    )
                }
                .withExposedPorts(VAULT_PORT)
                .apply { start() }

        init {
            val vault = Vault(
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

            vault.logical().write(
                "secret/example2", hashMapOf(
                    "examplestring2" to "helloworld2",
                    "exampleint2" to 1338,
                ) as Map<String, Any>?
            )
        }

    }
}
