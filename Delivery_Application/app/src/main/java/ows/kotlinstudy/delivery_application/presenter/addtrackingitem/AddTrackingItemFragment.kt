package ows.kotlinstudy.delivery_application.presenter.addtrackingitem

import android.app.AlertDialog
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import org.koin.android.scope.ScopeFragment
import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany
import ows.kotlinstudy.delivery_application.databinding.FragmentAddTrackingItemBinding
import ows.kotlinstudy.delivery_application.extension.toGone
import ows.kotlinstudy.delivery_application.extension.toVisible

class AddTrackingItemFragment: ScopeFragment(), AddTrackingItemsContract.View {

    override val presenter: AddTrackingItemsContract.Presenter by inject()

    private var binding: FragmentAddTrackingItemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddTrackingItemBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        presenter.onViewCreated()

        changeInvoiceIfAvailable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toVisible()
    }

    override fun hideShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toGone()
    }

    override fun showSaveTrackingItemIndicator() {
        binding?.saveButton?.apply {
            text = null
            isEnabled = false
        }
        binding?.saveProgressBar?.toVisible()
    }

    override fun hideSaveTrackingItemIndicator() {
        binding?.saveButton?.apply {
            text = "????????????"
            isEnabled = true
        }
        binding?.saveProgressBar?.toGone()
    }

    override fun showCompanies(companies: List<ShippingCompany>) {
        companies.forEach { company ->
            binding?.chipGroup?.addView(
                Chip(context).apply {
                    text = company.name
                }
            )
        }
    }

    override fun enableSaveButton() {
        binding?.saveButton?.isEnabled = true
    }

    override fun disableSaveButton() {
        binding?.saveButton?.isEnabled = false
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun finish() {
        findNavController().popBackStack()
    }

    private fun bindView(){
        binding?.chipGroup?.setOnCheckedChangeListener { group, checkedId ->
            presenter.changeSelectedShippingCompany(group.findViewById<Chip>(checkedId).text.toString())
        }

        binding?.invoiceEditText?.addTextChangedListener { editable ->
            presenter.changeShippingInvoice(editable.toString())
        }

        binding?.saveButton?.setOnClickListener {
            presenter.saveTrackingItem()
        }
    }

    override fun showRecommendCompanyLoadingIndicator() {
        binding?.recommandProgressBar?.toVisible()
    }

    override fun hideRecommendCompanyLoadingIndicator() {
        binding?.recommandProgressBar?.toGone()
    }

    override fun showRecommendCompany(shippingCompany: ShippingCompany) {
        binding?.chipGroup
            ?.children
            ?.filterIsInstance(Chip::class.java)
            ?.forEach { chip ->
                if(chip.text == shippingCompany.name){
                    binding?.chipGroup?.apply { check(chip.id) }
                    return@forEach
                }
            }
    }

    private fun changeInvoiceIfAvailable() {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val invoice= clipboard.plainTextClip()
        if(!invoice.isNullOrBlank()){
            AlertDialog.Builder(requireContext())
                .setTitle("?????? ????????? ?????? ${invoice} ??? ????????? ????????? ?????????????????????????")
                .setPositiveButton("???????????????"){ _, _ ->
                    binding?.invoiceEditText?.setText(invoice)
                    presenter.fetchRecommendShippingCompany()
                }
                .setNegativeButton("????????????"){ _, _ -> }
                .create()
                .show()
        }
    }

    private fun hideKeyboard(){
        val inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    /**
     * hasPrimaryClip : ??????????????? ???????????? ???????????? ?????????
     * hasMimeType : primaryClip??? Mime Type ??????
     */
    private fun ClipboardManager.plainTextClip(): String? =
        if(hasPrimaryClip() && (primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true)){
            primaryClip?.getItemAt(0)?.text?.toString()
        }else{
            null
        }
}