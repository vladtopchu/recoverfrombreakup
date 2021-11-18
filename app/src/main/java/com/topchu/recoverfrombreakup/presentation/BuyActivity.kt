package com.topchu.recoverfrombreakup.presentation

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
class BuyActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBuyBinding
    private var billingClient: BillingClient? = null

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var meditationDao: MeditationDao

    @Inject
    lateinit var firestoreUsers: CollectionReference

    private val viewModel: BuyViewModel by viewModels()

    private var shouldVerifyOnResume: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener { result, purchasesList ->
                if(result.responseCode == BillingClient.BillingResponseCode.OK && purchasesList != null){
                    purchasesList.forEach {
                        if(it.purchaseState == Purchase.PurchaseState.PURCHASED && !it.isAcknowledged) {
                            verifyPurchase(it)
                        }
                    }
                }
            }
            .build()
        viewModel.details.observe(this, { details ->
            if(details != null) {
                binding.progressCircular.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
                binding.productTitle.text = details.title
                binding.productDescription.text = details.description
                binding.buy.text = details.price
                binding.buy.setOnClickListener {
                    if(sharedPref.isFirstStoreVisit()){
                        sharedPref.setNotFirstStoreVisit()
                    }
                    billingClient?.launchBillingFlow(
                        this@BuyActivity,
                        BillingFlowParams.newBuilder().setSkuDetails(details).build()
                    )
                }
            }
        })
        connectToBilling()
    }

    override fun onResume() {
        super.onResume()
        if(shouldVerifyOnResume && !sharedPref.isFirstStoreVisit()) {
            Timber.d("ONRESUME")
            if (billingClient?.isReady == true) {
                Timber.d("BILLING CLIENT READY")
                queryPurchases()
            } else {
                Timer("Test", false).schedule(1000) {
                    Timber.d("??")
                    queryPurchases()
                }
            }
        }
    }

    private fun queryPurchases() {
        billingClient?.queryPurchasesAsync(
            BillingClient.SkuType.INAPP
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases.isNotEmpty()) {
                Timber.d("QUERYPURCHASES")
                Timber.d(purchases.toString())
                purchases.forEach {
                    if (it.purchaseState == Purchase.PurchaseState.PURCHASED &&
                        !it.isAcknowledged
                    ) {
                        verifyPurchase(it)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        shouldVerifyOnResume = false
    }

    private fun verifyPurchase(purchase: Purchase) {
        Timber.d("VerifyPurchase: ".plus(purchase.toString()))
        binding.content.visibility = View.GONE
        binding.progressCircular.visibility = View.VISIBLE

        val requestUrl = FIREBASE_VERIFY_PURCHASE_URL +
                "?purchaseToken=" + purchase.purchaseToken +
                "&purchaseTime=" + purchase.purchaseTime +
                "&orderId=" + purchase.orderId

        val stringRequest = StringRequest(
            Request.Method.POST,
            requestUrl,
            { response ->
                try {
                    val purchaseInfo = JSONObject(response)
                    Timber.d("Запрос успешен! ".plus(purchaseInfo.toString()))
                    if (purchaseInfo.getBoolean("isValid")) {
                        Timber.d("isValid == true")
                        updateUserContentStatus(purchase)
                    }
                } catch (e: Exception) {
                    Timber.d("Запрос завершился с ошибкой:" + "\n".plus(e.message))
                }
            }
        ) {
            Toast
                .makeText(this@BuyActivity,
                    "Не удалось выполнить запрос.\nКод ошибки: 5",
                    Toast.LENGTH_LONG)
                .show()
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun updateUserContentStatus(purchase: Purchase) {
        Timber.d("update user")
        if (Firebase.auth.currentUser != null) {
            Timber.d("update user current user not null")
            val updateData = hashMapOf("paid" to 1)
            firestoreUsers.document(Firebase.auth.currentUser!!.uid)
                .set(updateData, SetOptions.merge())
                .addOnCompleteListener {
                    Timber.d("update user complete")
                    acknowledgePurchase(purchase)
                }
                .addOnFailureListener {
                    Timber.d("update user failure")
                    Toast
                        .makeText(applicationContext,
                            "Не удалось обновить статус пользователя!",
                            Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient?.acknowledgePurchase(
            acknowledgePurchaseParams
        ) { billingResult ->
            Timber.d("ACKNOWLEDGED RESPONSE")
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                applicationScope.launch {
                    Timber.d("ACKNOWLEDGED")
                    withContext(Dispatchers.Main) {
                        Toast
                            .makeText(applicationContext,
                                "Контент успешно приобретён!\nПеренаправляю на главную страницу...",
                                Toast.LENGTH_LONG)
                            .show()
                    }
                    taskDao.unlockTasks()
                    meditationDao.unlockMeditations()
                }
                sharedPref.setContentBought(true)
                this@BuyActivity.finish()
            }
        }
    }

    private fun connectToBilling() {
        billingClient?.startConnection(
            object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    Toast
                        .makeText(
                            this@BuyActivity,
                            "Не удалось подключиться к сервисам Google!\nПроверьте интернет-соеднинение и попробуйте позже",
                            Toast.LENGTH_LONG)
                        .show()
                }

                override fun onBillingSetupFinished(result: BillingResult) {
                    if(result.responseCode == BillingClient.BillingResponseCode.OK) {
                        Timber.d("onBillingSetupFinished")
                        getProductDetails()
                    }
                }
            }
        )
    }

    private fun getProductDetails() {
        val productIds = listOf("unblock_content")
        val productsDetailsQuery = SkuDetailsParams
            .newBuilder()
            .setSkusList(productIds)
            .setType(BillingClient.SkuType.INAPP)
            .build()
        billingClient?.querySkuDetailsAsync(
            productsDetailsQuery
        ) { result, skuList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && skuList != null) {
                viewModel.setDetails(skuList[0])
            }
        }
    }
}