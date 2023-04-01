package com.example.searchuser.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.searchuser.databinding.FragmentSearchDetailsBinding

class SearchDetailsFragment: Fragment() {


    private var _binding: FragmentSearchDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<SearchDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            searchDetailTopBarBack.setOnClickListener {
                findNavController().popBackStack()
            }
            searchDetailImg.load(args.searchDetails.avatar_url)
            searchDetailAvatar.text = args.searchDetails.avatar_url
            searchDetailEvent.text = args.searchDetails.events_url
            searchDetailId.text = args.searchDetails.id.toString()
            searchDetailGist.text = args.searchDetails.gists_url
            searchDetailFollowers.text = args.searchDetails.followers_url
            searchDetailLogin.text = args.searchDetails.login
            searchDetailHtml.text = args.searchDetails.html_url
            searchDetailNode.text = args.searchDetails.node_id
            searchDetailOrg.text = args.searchDetails.organizations_url
            searchDetailRepo.text = args.searchDetails.repos_url
            searchDetailScore.text = args.searchDetails.score.toString()
            searchDetailSite.text = args.searchDetails.site_admin.toString()
            searchDetailStarred.text = args.searchDetails.starred_url
            searchDetailSubs.text = args.searchDetails.subscriptions_url
            searchDetailType.text = args.searchDetails.type
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
