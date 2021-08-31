package com.example.android.observability.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.observability.R
import com.example.android.observability.adapter.GithubRepositoriesAdapter
import com.example.android.observability.databinding.MainFragmentBinding
import com.example.android.observability.model.GithubModel
import com.example.android.observability.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private val adapter: GithubRepositoriesAdapter = GithubRepositoriesAdapter()
    private val LANGUAGE = "Kotlin"

    private val observePage = Observer<Int> { page ->
        viewModel.fetchRepositories(LANGUAGE, page)
    }

    private val observeRepositories = Observer<List<GithubModel>> { list ->
        /**
         * Escondemos o label de Loading quando a lista foi carregada.
         */
        binding.linearLayout.visibility = View.GONE
        adapter.refresh(list)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = MainFragmentBinding.bind(view)

        viewModel.repositories.observe(viewLifecycleOwner, observeRepositories)
        viewModel.page.observe(viewLifecycleOwner, observePage)

        binding.repositoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.repositoriesRecyclerView.adapter = adapter

        setEventsForButtons()

        callForMoreItems()
    }

    private fun setEventsForButtons() {

        /**
         * Add um listener no scroll do recycler view para verificar quando ele chega no
         * final da lista e chamamos a próxima página
         *
         */
        binding.repositoriesRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                /**
                 * Verificar se o scroll chegou ao final da lista, se sim chama o nextPage()
                 *
                 */
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    callForMoreItems()
                }
            }

        })
    }

    /**
     * Sempre que for chamar a página mostra o label de Loading para o user. Este campo será escondido
     * no observer da lista de repositorios.
     */
    fun callForMoreItems() {
        binding.linearLayout.visibility = View.VISIBLE
        viewModel.nextPage()
    }


}