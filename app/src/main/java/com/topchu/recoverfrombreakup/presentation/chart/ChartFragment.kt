package com.topchu.recoverfrombreakup.presentation.chart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.entities.ChartEntryEntity
import com.topchu.recoverfrombreakup.databinding.FragmentChartBinding
import dagger.hilt.android.AndroidEntryPoint
import com.topchu.recoverfrombreakup.presentation.chart.rate.RateActivity


@AndroidEntryPoint
class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.entries.observe(viewLifecycleOwner, { chartData ->
            if(chartData != null) {
                binding.rateButton.visibility = View.GONE
                binding.message.visibility = View.GONE
                binding.chart.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
                if(chartData.size < 3) {
                    binding.progressCircular.visibility = View.GONE
                    binding.message.text = "Оцени своё состояние ещё ${3 - chartData.size} раз${if(chartData.size == 1) "" else "а"},\nчтобы увидеть график"
                    binding.message.visibility = View.VISIBLE
                    toggleRateButton()
                } else {
                    prepareChart(chartData)
                }
            }
        })
    }

    private fun toggleRateButton() {
        binding.rateButton.visibility = View.VISIBLE
        binding.rateButton.setOnClickListener {
            startActivity(Intent(requireActivity(), RateActivity::class.java))
        }
    }

    private fun prepareChart(chartData: List<ChartEntryEntity>) {
        val labels = mutableListOf<String>()
        val entries = mutableListOf<Entry>()
        chartData.forEachIndexed { index, entry ->
            labels.add(entry.label)
            entries.add(Entry(index.toFloat(), entry.value.toFloat()))
        }
        val dataSet = LineDataSet(entries, "First").apply {
            color = requireActivity().getColor(R.color.indigo)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            lineWidth = 20f
            setDrawValues(false)
            setDrawCircles(false)
        }
        val data = LineData(dataSet)
        binding.progressCircular.visibility = View.GONE

        binding.chart.apply {
            axisRight.isEnabled = false

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 10f
            }

            xAxis.apply {
                isGranularityEnabled = true
                granularity = 1f
                valueFormatter = IndexAxisValueFormatter().apply {
                    values = labels.toTypedArray()
                }
                setLabelCount(labels.size, false)
                position = XAxis.XAxisPosition.BOTTOM
                spaceMin = 0.2f
                spaceMax = 0.2f
                extraBottomOffset = 0.4f
            }

            legend.isEnabled = false
            description.isEnabled = false

            visibility = View.VISIBLE
        }

        binding.chart.data = data
        binding.chart.notifyDataSetChanged()
        if(System.currentTimeMillis() - 1000*60*60*24 >= chartData.last().createdAt) {
            toggleRateButton()
        }
    }
}