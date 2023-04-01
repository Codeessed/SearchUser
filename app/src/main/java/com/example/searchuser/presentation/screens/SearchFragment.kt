package com.example.searchuser.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.searchuser.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment: Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

//    private val viewModel: ImageViewModel by viewModels()
//
//    private lateinit var imagesRecycler: RecyclerView
//    private lateinit var imagesAdapter: ImagesAdapter
//    private lateinit var pagingAdapter: ImagesPagingAdapter
//    private lateinit var images: List<PhotoResponseItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setUpImagesRecycler()


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED){
//                viewModel.images.collectLatest {
//                    Log.d("got here", "$it")
//                    pagingAdapter.submitData(it)
//                }
            }
        }
//        observer(viewModel.allImages){ allImage ->
//            when(allImage){
//                is ImageViewModel.ImageEvents.AllImageSuccess -> {
//                    images = allImage.photoResponse
//                    setUpImagesRecycler()
//                }
//                is ImageViewModel.ImageEvents.Error -> {
//                    Toast.makeText(requireContext(), allImage.message, Toast.LENGTH_SHORT).show()
//                }
//                is ImageViewModel.ImageEvents.Loading -> {
//                    Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
//                }
//                else -> viewModel.getAllImages(null, null, null)
//
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun setUpImagesRecycler(){
//        pagingAdapter = ImagesPagingAdapter(this, requireContext())
//        imagesRecycler = binding.imagesRecycler
//        imagesRecycler.adapter = pagingAdapter
//        imagesRecycler.layoutManager = LinearLayoutManager(requireContext())
//    }
//
//    override fun onImageClicked(pos: Int) {
//        Toast.makeText(requireContext(), images[pos].user.last_name, Toast.LENGTH_SHORT).show()
//    }

}
