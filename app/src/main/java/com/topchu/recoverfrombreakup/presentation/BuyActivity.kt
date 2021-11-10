package com.topchu.recoverfrombreakup.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.topchu.recoverfrombreakup.databinding.ActivityBuyBinding
import com.topchu.recoverfrombreakup.utils.Constants.FIREBASE_VERIFY_PURCHASE_URL
import org.json.JSONObject

class BuyActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBuyBinding
    private var billingClient: BillingClient? = null

    private val viewModel: BuyViewModel by viewModels()

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
                binding.productTitle.text = details.title
                binding.buy.text = details.price
                binding.buy.setOnClickListener {
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
        billingClient?.queryPurchasesAsync(
            BillingClient.SkuType.INAPP
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
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

    private fun verifyPurchase(purchase: Purchase) {
        Log.d("TESTTEST", "VerifyPurchase: ".plus(purchase.toString()))
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
                    if(purchaseInfo.getBoolean("isValid")) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams
                            .newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()
                        billingClient?.acknowledgePurchase(
                            acknowledgePurchaseParams
                        ) { billingResult ->
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                Toast
                                    .makeText(this@BuyActivity, "ACKNOWLEDGED!", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                } catch(e: Exception) {

                }
            }
        ) {
            Toast
                .makeText(this@BuyActivity,
                    "Не удалось выполнить запрос.\nКод ошибки: 5\nСообщение:\n${it.message}",
                    Toast.LENGTH_LONG)
                .show()
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun connectToBilling() {
        billingClient?.startConnection(
            object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    Log.d("TESTTEST", "onBillingServiceDisconnected")
                    connectToBilling()
                }

                override fun onBillingSetupFinished(result: BillingResult) {
                    if(result.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d("TESTTEST", "onBillingSetupFinished")
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