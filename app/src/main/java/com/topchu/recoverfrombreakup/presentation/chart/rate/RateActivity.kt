package com.topchu.recoverfrombreakup.presentation.chart.rate

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.databinding.ActivityBuyBinding
import com.topchu.recoverfrombreakup.databinding.ActivityRateBinding
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.Constants.FIREBASE_VERIFY_PURCHASE_URL
import com.topchu.recoverfrombreakup.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
class RateActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRateBinding

    private val viewModel: RateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.numberPicker.apply {
            minValue = 0
            maxValue = 10
        }

        viewModel.rateValue.observe(this) {
            if(it != null){
                Toast.makeText(applicationContext, "Вы успешно оценили своё состояние!", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        binding.apply.setOnClickListener {
            viewModel.setRateValue(binding.numberPicker.value)
        }
    }
}