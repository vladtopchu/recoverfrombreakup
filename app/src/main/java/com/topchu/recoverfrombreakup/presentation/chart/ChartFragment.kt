package com.topchu.recoverfrombreakup.presentation.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
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
import timber.log.Timber
import com.github.mikephil.charting.components.YAxis




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
                Timber.d(chartData.toString())
                val labels = mutableListOf<String>()
                val entries = mutableListOf<Entry>()
                chartData.forEachIndexed { index, entry ->
                    labels.add(entry.date)
                    entries.add(Entry(index.toFloat(), entry.value.toFloat()))
                }
                val dataSet = LineDataSet(entries, "First").apply {
                    color = requireActivity().getColor(R.color.indigo)
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    lineWidth = 40f
                    setDrawCircles(false)
                }
                val data = LineData(dataSet)
                prepareChart(labels)
                binding.chart.data = data
                binding.chart.notifyDataSetChanged()
            }
        })

    }

    fun prepareChart(_labels: MutableList<String>) {
        binding.chart.apply {

            axisRight.isEnabled = false

            axisLeft.apply {
                isEnabled = false
                axisMinimum = 0f
                axisMaximum = 10f
            }

            xAxis.apply {
                isGranularityEnabled = true
                granularity = 4f
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter().apply {
                    values = _labels.toTypedArray()
                }
                setLabelCount(_labels.size, true)
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM
            }

            setTouchEnabled(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)

            legend.isEnabled = false
            description.isEnabled = false
        }
    }
}