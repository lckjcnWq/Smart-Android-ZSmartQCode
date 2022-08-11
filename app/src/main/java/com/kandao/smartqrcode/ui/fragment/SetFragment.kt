package com.kandao.smartqrcode.ui.fragment

import android.content.Intent
import com.blankj.utilcode.util.AppUtils
import com.kandao.smartqrcode.R
import com.kandao.smartqrcode.constant.AppContants
import com.kandao.smartqrcode.databinding.FragmentSetBinding
import com.kandao.smartqrcode.ui.activity.MainActivity
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.theswitchbot.common.manager.ActivityManager
import com.theswitchbot.common.ui.BaseFragment
import com.theswitchbot.common.util.LocaleUtil
import com.theswitchbot.common.util.SpAccessor
import com.theswitchbot.common.util.initTitleBar
import com.theswitchbot.common.ui.dialog.WoAlertDialog
import java.util.Locale

/**
 * <pre>
 * author : Administrator
 * time : 2022/08/05
 * </pre>
 */
class SetFragment : BaseFragment<FragmentSetBinding>() {

  companion object {
    @JvmStatic
    fun newInstance() = SetFragment()
  }

  private val languageList by lazy {
    listOf(
      Pair(getString(R.string.language_default), LocaleUtil.DEFAULT_LOCALE),
      Pair(getString(R.string.language_english), Locale.ENGLISH),
      Pair(getString(R.string.language_simplify_chinese), Locale.SIMPLIFIED_CHINESE),
    )
  }

  override fun setup() {
    initTitleBar(
      binding.topbar,
      leftImageRes = null,
      centerText = getString(R.string.remote_setting)
    )
    initLanguageItem()
    initVersionItem()
    initWarnItem()
    initVibrateItem()
    setListener()
  }

  private fun setListener(){
    binding.itemQuestion.setOnClickListener {

    }
    binding.itemPrivacy.setOnClickListener {

    }
    binding.itemTalk.setOnClickListener {
        showTalkDialog()
    }
  }

  private fun showTalkDialog(){
    WoAlertDialog.Builder(requireActivity())
      .title(R.string.set_report)
      .content(R.string.set_report_content)
      .positiveText(R.string.ptwo_got_it)
      .show()
  }

  private fun initWarnItem(){
    binding.itemMock.setRightSwitchCheckedNoEvent(SpAccessor.getBool(AppContants.vibrateState, true))
    binding.itemMock.setOnCheckedChangeListener { _, isChecked ->
      SpAccessor.put(AppContants.vibrateState, isChecked)
    }
  }

  private fun initVibrateItem(){
    binding.itemPlayVoice.setRightSwitchCheckedNoEvent(SpAccessor.getBool(AppContants.playVoiceState, true))
    binding.itemPlayVoice.setOnCheckedChangeListener { _, isChecked ->
      SpAccessor.put(AppContants.playVoiceState, isChecked)
    }
  }

  private fun initLanguageItem() {
    val locale = LocaleUtil.getSelectedLocale()
    var selectIndex = 0
    languageList.forEachIndexed { i, pair ->
      if (LocaleUtil.equals(locale, pair.second)) {
        selectIndex = i
      }
    }

    binding.languagePref.getRightTextView().text = languageList[selectIndex].first
    binding.languagePref.setOnClickListener {
      showBottomList(
        getString(R.string.language_settings),
        languageList.map { it.first },
        selectIndex
      ) {
        if (selectIndex == it) return@showBottomList
        selectIndex = it
        val pair = languageList[it]
        LocaleUtil.setLocale(requireContext(), pair.second)
        binding.languagePref.getRightTextView().text = pair.first
        reopenApp()
      }
    }
  }

  private fun initVersionItem() {
    binding.itemAppVersion.getRightTextView().text = AppUtils.getAppVersionName()
  }

  /**
   * 切换语言后重新启动app
   */
  private fun reopenApp() {
    ActivityManager.finishAll()
    startActivity(Intent(requireActivity(), MainActivity::class.java))
  }

  private fun showBottomList(
    title: String,
    list: List<String>,
    selectIndex: Int,
    onClick: (Int) -> Unit
  ) {
    val builder = QMUIBottomSheet.BottomListSheetBuilder(requireContext(), true)
      .setTitle(title).setAddCancelBtn(true)
      .setNeedRightMark(false)
      .setCancelText(getString(R.string.str_cancel))
    list.forEachIndexed { index, it ->
      builder.addItem(it)
    }
    if (selectIndex != null) {
      builder.setCheckedIndex(selectIndex)
      builder.setNeedRightMark(true)
    }
    builder.setOnSheetItemClickListener { dialog, _, position, _ ->
      onClick.invoke(position)
      dialog.dismiss()
    }.build().show()
  }

}