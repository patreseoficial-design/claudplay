package com.claudplay.app

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.webkit.WebViewAssetLoader

/*
 * =============================================================
 * TELA PRINCIPAL
 *
 * O ClaudPlay (catálogo, emulador e jogos) fica dentro do app,
 * em assets/www, e é aberto num WebView como se fosse um site
 * https. Assim tudo funciona sem internet.
 * =============================================================
 */

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ENDERECO =
            "https://appassets.androidplatform.net/assets/www/"
    }

    private lateinit var webView: WebView

    private lateinit var areaBanner: FrameLayout

    private lateinit var raiz: LinearLayout

    private var jogando = false

    private var escolhaDeArquivo: ValueCallback<Array<Uri>>? = null

    // Seletor de arquivos do Android para o "Abrir meu jogo"
    private val escolherArquivo =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { resultado ->

            val uri =
                resultado.data?.data

            escolhaDeArquivo?.onReceiveValue(
                if (uri != null) arrayOf(uri) else null
            )

            escolhaDeArquivo = null
        }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        webView = WebView(this)

        areaBanner = FrameLayout(this)

        raiz = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(getColor(R.color.fundo))
            addView(
                webView,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
            )
            addView(
                areaBanner,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }

        setContentView(raiz)

        // Respeita a barra de status e a de navegação
        // (no jogo, as barras somem e a tela fica inteira)
        ViewCompat.setOnApplyWindowInsetsListener(raiz) { view, insets ->

            val barras =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            if (jogando) {
                view.setPadding(0, 0, 0, 0)
            } else {
                view.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            }

            insets
        }

        configurarWebView()

        if (savedInstanceState == null) {
            webView.loadUrl(ENDERECO + "index.html")
        } else {
            webView.restoreState(savedInstanceState)
        }

        // Voltar: dentro do app primeiro; ao sair de um jogo,
        // pode aparecer um anúncio (no máximo a cada 3 minutos)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (webView.canGoBack()) {

                        val saiuDoJogo =
                            jogando

                        webView.goBack()

                        if (saiuDoJogo) {
                            Anuncios.mostrarAoSairDoJogo(this@MainActivity)
                        }

                    } else {
                        finish()
                    }
                }
            }
        )

        Anuncios.iniciar(this) {
            if (!jogando) {
                Anuncios.mostrarBanner(this, areaBanner)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configurarWebView() {

        val carregador =
            WebViewAssetLoader.Builder()
                .addPathHandler(
                    "/assets/",
                    WebViewAssetLoader.AssetsPathHandler(this)
                )
                .build()

        webView.setBackgroundColor(getColor(R.color.fundo))

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            allowFileAccess = false
            allowContentAccess = true
        }

        // O site avisa quando um jogo começa
        webView.addJavascriptInterface(Ponte(), "ClaudPlayApp")

        webView.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? =
                carregador.shouldInterceptRequest(request.url)

            // Links de fora do app (ex.: código-fonte nos créditos)
            // abrem no navegador
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {

                val url =
                    request.url

                if (url.host == "appassets.androidplatform.net") {
                    return false
                }

                try {
                    startActivity(Intent(Intent.ACTION_VIEW, url))
                } catch (_: ActivityNotFoundException) {
                }

                return true
            }

            // Toda página nova começa fora do jogo
            override fun onPageStarted(
                view: WebView,
                url: String?,
                favicon: android.graphics.Bitmap?
            ) {
                mudarModoJogo(false)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onShowFileChooser(
                view: WebView,
                callback: ValueCallback<Array<Uri>>,
                params: WebChromeClient.FileChooserParams
            ): Boolean {

                escolhaDeArquivo?.onReceiveValue(null)

                escolhaDeArquivo = callback

                // ROMs têm extensões que o Android não conhece:
                // aceita qualquer arquivo
                val intent =
                    Intent(Intent.ACTION_GET_CONTENT)
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")

                return try {
                    escolherArquivo.launch(intent)
                    true
                } catch (_: ActivityNotFoundException) {
                    escolhaDeArquivo = null
                    false
                }
            }
        }
    }

    // Jogo aberto: tela cheia e sem banner
    private fun mudarModoJogo(ativo: Boolean) {

        if (jogando == ativo) {
            return
        }

        jogando = ativo

        val controle =
            WindowInsetsControllerCompat(window, window.decorView)

        if (ativo) {
            Anuncios.esconderBanner(areaBanner)
            controle.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controle.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            controle.show(WindowInsetsCompat.Type.systemBars())
            Anuncios.mostrarBanner(this, areaBanner)
        }

        ViewCompat.requestApplyInsets(raiz)
    }

    private inner class Ponte {

        @JavascriptInterface
        fun jogando(ativo: Boolean) {
            runOnUiThread { mudarModoJogo(ativo) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroy() {
        Anuncios.destruirBanner(areaBanner)
        webView.destroy()
        super.onDestroy()
    }
}
