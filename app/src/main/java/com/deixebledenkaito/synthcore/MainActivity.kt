package com.deixebledenkaito.synthcore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import com.deixebledenkaito.synthcore.ui.SynthCoreApp

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