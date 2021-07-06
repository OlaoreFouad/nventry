package dev.olaore.nventry.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentShareBinding

class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShareBinding.inflate(inflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sharedProductsList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SharedProductsAdapter(requireContext())
        }
    }

}

class SharedProductsAdapter(
    private val ctx: Context,
    private val sharedProducts: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
) : RecyclerView.Adapter<SharedProductsAdapter.SharedProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedProductViewHolder =
        SharedProductViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.item_shared_product, parent, false)
        )

    override fun onBindViewHolder(holder: SharedProductViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return sharedProducts.size
    }

    inner class SharedProductViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
    }

}