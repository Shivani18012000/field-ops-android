package com.shivani.fieldops.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivani.fieldops.FieldOpsApplication
import com.shivani.fieldops.databinding.FragmentWorkOrderListBinding
import com.shivani.fieldops.domain.WorkOrderStatus

class WorkOrderListFragment : Fragment() {
    private var _binding: FragmentWorkOrderListBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentWorkOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        val repository = (requireActivity().application as FieldOpsApplication).container.repository
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = WorkOrderViewModel(repository) as T
        })[WorkOrderViewModel::class.java]
        val adapter = WorkOrderAdapter()
        binding.workOrderList.layoutManager = LinearLayoutManager(requireContext())
        binding.workOrderList.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener(viewModel::refresh)
        binding.statusFilter.addOnButtonCheckedListener { _, id, checked ->
            if (checked) viewModel.setFilter(
                when (id) {
                    binding.filterOpen.id -> WorkOrderStatus.OPEN
                    binding.filterProgress.id -> WorkOrderStatus.IN_PROGRESS
                    else -> null
                },
            )
        }
        viewModel.workOrders.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.syncing.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorText.text = it
            binding.errorText.isVisible = !it.isNullOrBlank()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
