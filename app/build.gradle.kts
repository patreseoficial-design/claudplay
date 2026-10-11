import javax.inject.Inject

plugins {
    id("com.android.application") version "8.13.0"
    id("org.jetbrains.kotlin.android") version "2.2.20"
}

// Assinatura da versão da Play Store: os dados vêm dos "Secrets"
// do GitHub (veja docs/PUBLICAR-NA-PLAY-STORE.md). Sem eles, só a
// versão de teste (debug) é gerada.
val arquivoChave = System.getenv("KEYSTORE_FILE")
val temChave = !arquivoChave.isNullOrBlank() && file(arquivoChave).exists()

// IDs do AdMob do ClaudPlay (os reais já estão abaixo; os Secrets
// do GitHub com o mesmo nome, se existirem, têm prioridade)
fun idAdMob(nome: String, padrao: String): String =
    System.getenv(nome)?.takeIf { it.isNotBlank() } ?: padrao

android {
    namespace = "com.claudplay.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.claudplay.app"
        minSdk = 24
        targetSdk = 36

        // Cada envio para a Play Store precisa de um número maior:
        // usa o número da execução do GitHub Actions
        versionCode = System.getenv("VERSION_CODE")?.toIntOrNull() ?: 1
        versionName = "1.0.${System.getenv("VERSION_CODE") ?: "0"}"
    }

    buildFeatures {
        resValues = true
    }

    signingConfigs {
        if (temChave) {
            create("release") {
                storeFile = file(arquivoChave!!)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        // Versão de teste (debug): anúncios de TESTE do Google, para
        // ninguém clicar por engano em anúncio de verdade
        debug {
            resValue("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")
            resValue("string", "admob_banner_id", "ca-app-pub-3940256099942544/9214589741")
            resValue("string", "admob_intersticial_id", "ca-app-pub-3940256099942544/1033173712")
        }

        // Versão da Play Store: anúncios de verdade da conta AdMob
        release {
            resValue("string", "admob_app_id", idAdMob("ADMOB_APP_ID", "ca-app-pub-9393930095097786~1786455388"))
            resValue("string", "admob_banner_id", idAdMob("ADMOB_BANNER_ID", "ca-app-pub-9393930095097786/4954624336"))
            resValue("string", "admob_intersticial_id", idAdMob("ADMOB_INTERSTICIAL_ID", "ca-app-pub-9393930095097786/7987981783"))
            isMinifyEnabled = false
            if (temChave) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    // Os núcleos do emulador já vêm compactados: não comprimir de novo
    androidResources {
        noCompress += listOf("data", "wasm")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}

/*
 * O site do ClaudPlay (index.html, jogar.html, jogos/, EmulatorJS...)
 * fica na raiz do repositório. Esta tarefa copia ele para dentro do
 * app em assets/www, levando do EmulatorJS só os núcleos dos consoles
 * que o ClaudPlay usa.
 */
abstract class CopiarSite @Inject constructor(
    private val arquivos: FileSystemOperations
) : DefaultTask() {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val origem: ConfigurableFileCollection

    @get:Internal
    abstract val raiz: DirectoryProperty

    @get:Input
    abstract val incluir: ListProperty<String>

    @get:OutputDirectory
    abstract val saida: DirectoryProperty

    @TaskAction
    fun copiar() {
        arquivos.sync {
            from(raiz) { include(incluir.get()) }
            into(saida.dir("www"))
        }
    }
}

val nucleos = listOf(
    "fceumm", "snes9x", "gambatte", "mgba",
    "genesis_plus_gx", "mupen64plus_next", "pcsx_rearmed"
)

val arquivosDoSite = listOf(
    "index.html", "jogar.html", "creditos.html", "style.css", "manifest.json",
    "icons/**", "jogos/**",
    "emulatorjs/EmulatorJS/Data/*",
    "emulatorjs/EmulatorJS/Data/compression/**",
    "emulatorjs/EmulatorJS/Data/localization/**",
    "emulatorjs/EmulatorJS/Data/src/**"
) + nucleos.flatMap {
    listOf(
        "emulatorjs/EmulatorJS/Data/cores/$it-wasm.data",
        "emulatorjs/EmulatorJS/Data/cores/$it-legacy-wasm.data",
        "emulatorjs/EmulatorJS/Data/cores/reports/$it.json"
    )
}

val copiarSite = tasks.register<CopiarSite>("copiarSite") {
    val pastaRaiz = rootProject.layout.projectDirectory
    raiz.set(pastaRaiz)
    incluir.set(arquivosDoSite)
    origem.from(pastaRaiz.asFileTree.matching { include(arquivosDoSite) })
}

androidComponents {
    onVariants { variante ->
        variante.sources.assets?.addGeneratedSourceDirectory(copiarSite, CopiarSite::saida)
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")

    // Abre o site de dentro do app como https (necessário para o emulador)
    implementation("androidx.webkit:webkit:1.14.0")

    // Anúncios (AdMob) e tela de consentimento de privacidade
    implementation("com.google.android.gms:play-services-ads:24.5.0")
    implementation("com.google.android.ump:user-messaging-platform:3.2.0")
}
