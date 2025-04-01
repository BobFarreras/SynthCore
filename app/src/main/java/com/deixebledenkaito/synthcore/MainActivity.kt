package com.deixebledenkaito.synthcore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.deixebledenkaito.synthcore.ui.SynthCoreApp
import com.deixebledenkaito.synthcore.ui.theme.SynthCoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint  // Necessari per a Hilt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SynthCoreApp()  // Aquesta és la teva arrel de Compose
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSynthCoreApp() {
    SynthCoreApp()  // Previsualització per al mode disseny
}