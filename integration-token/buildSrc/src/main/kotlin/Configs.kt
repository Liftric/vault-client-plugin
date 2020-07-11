import com.liftric.vault.GetVaultSecretTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName

object Configs {
    fun Project.secretStuff(): String {
        val needsSecrets: GetVaultSecretTask = tasks.getByName<GetVaultSecretTask>("needsSecrets").apply {
            execute()
        }
        val secret = needsSecrets.secret.get()
        return "${secret["examplestring"]}:${secret["exampleint"]}"
    }
}
