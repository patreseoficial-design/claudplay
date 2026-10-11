package com.claudplay.app

import android.app.Activity
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

/*
 * =============================================================
 * ANÚNCIOS (AdMob)
 *
 * Regras para não atrapalhar o jogo:
 *  - um banner discreto embaixo, só fora do jogo;
 *  - tela cheia só quando a pessoa SAI de um jogo, no máximo
 *    uma vez a cada 3 minutos;
 *  - nada de anúncio durante a partida.
 * =============================================================
 */

object Anuncios {

    private val sdkIniciado =
        AtomicBoolean(false)

    private var intersticial: InterstitialAd? = null

    private var carregandoIntersticial = false

    private var ultimoIntersticial = 0L

    private const val INTERVALO_MINIMO_MS =
        3 * 60 * 1000L

    // Pede o consentimento (exigido na Europa; no Brasil o
    // formulário normalmente nem aparece) e liga os anúncios
    fun iniciar(
        activity: Activity,
        aoFicarPronto: () -> Unit
    ) {

        val consentimento =
            UserMessagingPlatform.getConsentInformation(activity)

        consentimento.requestConsentInfoUpdate(
            activity,
            ConsentRequestParameters.Builder().build(),
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { _ ->

                    if (consentimento.canRequestAds()) {
                        iniciarSdk(activity, aoFicarPronto)
                    }
                }
            },
            { _ ->

                if (consentimento.canRequestAds()) {
                    iniciarSdk(activity, aoFicarPronto)
                }
            }
        )

        // Consentimento de uma sessão anterior: já pode carregar
        if (consentimento.canRequestAds()) {
            iniciarSdk(activity, aoFicarPronto)
        }
    }

    private fun iniciarSdk(
        activity: Activity,
        aoFicarPronto: () -> Unit
    ) {

        if (sdkIniciado.getAndSet(true)) {
            activity.runOnUiThread { aoFicarPronto() }
            return
        }

        MobileAds.initialize(activity) {

            activity.runOnUiThread {

                carregarIntersticial(activity)

                aoFicarPronto()
            }
        }
    }

    // Banner adaptado à largura da tela, dentro do "container"
    fun mostrarBanner(
        activity: Activity,
        container: ViewGroup
    ) {

        if (!sdkIniciado.get()) {
            container.visibility = View.GONE
            return
        }

        // Já tem banner: só volta a mostrar
        if (container.childCount > 0) {
            container.visibility = View.VISIBLE
            return
        }

        val metricas =
            activity.resources.displayMetrics

        val larguraDp =
            (metricas.widthPixels / metricas.density).toInt()

        val banner =
            AdView(activity)

        banner.adUnitId =
            activity.getString(R.string.admob_banner_id)

        banner.setAdSize(
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                activity,
                larguraDp
            )
        )

        container.addView(banner)

        container.visibility = View.VISIBLE

        banner.loadAd(
            AdRequest.Builder().build()
        )
    }

    // Esconde durante o jogo (o banner fica guardado para depois)
    fun esconderBanner(
        container: ViewGroup
    ) {

        container.visibility = View.GONE
    }

    fun destruirBanner(
        container: ViewGroup
    ) {

        for (i in 0 until container.childCount) {
            (container.getChildAt(i) as? AdView)?.destroy()
        }

        container.removeAllViews()
    }

    private fun carregarIntersticial(
        activity: Activity
    ) {

        if (
            intersticial != null ||
            carregandoIntersticial
        ) {
            return
        }

        carregandoIntersticial = true

        InterstitialAd.load(
            activity,
            activity.getString(R.string.admob_intersticial_id),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(
                    anuncio: InterstitialAd
                ) {
                    intersticial = anuncio
                    carregandoIntersticial = false
                }

                override fun onAdFailedToLoad(
                    erro: LoadAdError
                ) {
                    intersticial = null
                    carregandoIntersticial = false
                }
            }
        )
    }

    // Mostrar só quando a pessoa sai de um jogo
    fun mostrarAoSairDoJogo(
        activity: Activity
    ) {

        if (!sdkIniciado.get()) {
            return
        }

        val agora =
            SystemClock.elapsedRealtime()

        val anuncio =
            intersticial

        if (
            anuncio == null ||
            (ultimoIntersticial != 0L &&
                agora - ultimoIntersticial < INTERVALO_MINIMO_MS)
        ) {
            carregarIntersticial(activity)
            return
        }

        anuncio.fullScreenContentCallback =
            object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    carregarIntersticial(activity)
                }
            }

        intersticial = null

        ultimoIntersticial = agora

        anuncio.show(activity)
    }
}
