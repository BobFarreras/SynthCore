// SynthCoreApp.kt (al paquet principal, al mateix nivell que MainActivity)
package com.deixebledenkaito.synthcore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp  // 👈 Aquesta anotació és crucial
class SynthCoreApp : Application()