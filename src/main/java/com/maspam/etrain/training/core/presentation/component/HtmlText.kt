package com.maspam.etrain.training.core.presentation.component

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

fun sanitizeHtml(input: String): String {
    return input.replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
        .replace("font-size: 36.0px", "font-size: 36px")
        .replace("font-size: 22.0px", "font-size: 22px")
}

@Composable
fun wrapHtmlWithStyle(htmlContent: String): String {
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val hexColor = String.format("#%06X", 0xFFFFFF and textColor)

    return """
        <html>
        <head>
            <style>
                body {
                    color: $hexColor;
                    background-color: transparent;
                    font-family: sans-serif;
                }
                p, span {
                    color: $hexColor;
                }
            </style>
        </head>
        <body>
            $htmlContent
        </body>
        </html>
    """.trimIndent()
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val styled = wrapHtmlWithStyle(html)

    AndroidView(
        modifier = modifier,
        factory = {
            WebView(context).apply {
                webViewClient = WebViewClient()
                setBackgroundColor(backgroundColor)
                settings.javaScriptEnabled = true
                loadDataWithBaseURL(
                    null,
                    styled,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}