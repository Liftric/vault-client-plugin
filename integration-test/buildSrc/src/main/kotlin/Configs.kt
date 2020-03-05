import com.liftric.vault.vault
import org.gradle.api.Project

object Configs {
    fun Project.secretStuff(): String {
        val secrets = project.vault("secret/example")
        return "${secrets["examplestring"]}:${secrets["exampleint"]}"
    }
}