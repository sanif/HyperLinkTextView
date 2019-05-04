package `in`.sanifss.hyperlinktextview

import `in`.sanifss.hyperlinktextview.HyperLinkTextView.LinkType.*
import android.annotation.TargetApi
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.content.ClipboardManager
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.Toast

class HyperLinkTextView : TextView {

    enum class LinkType {
        WEB, EMAIL, TELEPHONE, COPY

    }

    enum class ClickBehaviourType {
        CLICK, LONG_CLICK
    }

    companion object {
        private const val DEFAULT_LINK_TYPE = 0
        private const val DEFAULT_LINK_COLOR = Color.BLUE
        private const val DEFAULT_UNDERLINED = true
        private const val DEFAULT_CLICK_BEHAVIOUR = 0
        private const val DEFAULT_LINK_START_OFFSET = 0
        private const val DEFAULT_LINK_END_OFFSET = 0
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(attrs)
    }

    private var mLinkType: LinkType = values()[DEFAULT_LINK_TYPE]

    private var mLinkColor: Int = DEFAULT_LINK_COLOR

    private var mUnderLine: Boolean = DEFAULT_UNDERLINED

    private var mClickBehaviour: ClickBehaviourType = ClickBehaviourType.values()[DEFAULT_CLICK_BEHAVIOUR]

    private var mLinkStartOffset: Int = DEFAULT_LINK_START_OFFSET

    private var mLinkEndOffset: Int = DEFAULT_LINK_END_OFFSET

    private fun initAttrs(attrs: AttributeSet?) {

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.hyperlink_text_view_attrs, 0, 0
            )

            val linkTypeInt = typedArray.getInt(R.styleable.hyperlink_text_view_attrs_linkType, DEFAULT_LINK_TYPE)
            mLinkType = values()[linkTypeInt]

            mLinkColor = typedArray.getColor(R.styleable.hyperlink_text_view_attrs_linkColor, DEFAULT_LINK_COLOR)

            mUnderLine = typedArray.getBoolean(R.styleable.hyperlink_text_view_attrs_underlined, DEFAULT_UNDERLINED)

            var clickBehaviourInt =
                typedArray.getInt(R.styleable.hyperlink_text_view_attrs_clickBehaviour, DEFAULT_CLICK_BEHAVIOUR)

            mClickBehaviour = ClickBehaviourType.values()[clickBehaviourInt]

            mLinkStartOffset =
                typedArray.getInt(R.styleable.hyperlink_text_view_attrs_linkStartOffset, DEFAULT_LINK_START_OFFSET)

            mLinkEndOffset =
                typedArray.getInt(R.styleable.hyperlink_text_view_attrs_linkEndOffset, DEFAULT_LINK_END_OFFSET)

            update()
        }
    }

    fun setCustomText(text: CharSequence?) {
        this.text = text
        update()
    }

    private fun update() {
        var text = text;
        text?.let {

            movementMethod = LinkMovementMethod.getInstance()

            val endOffset = if (mLinkEndOffset <= 0) text.length else mLinkEndOffset
            var hyperlinkStr = text.subSequence(mLinkStartOffset, endOffset)
            val spannable = SpannableStringBuilder(it)

            val click = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    OnLinkClick(hyperlinkStr)
                }

            }
            spannable.setSpan(click, mLinkStartOffset, endOffset, 0)

            spannable.setSpan(ForegroundColorSpan(mLinkColor), mLinkStartOffset, endOffset, 0)

            spannable.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = mUnderLine
                }
            }, mLinkStartOffset, endOffset, 0)

            this.text = spannable

        }
    }

    private fun OnLinkClick(str: CharSequence) {
        when (mLinkType) {

            WEB -> openWebLink(str)
            EMAIL -> openEmailLink(str)
            TELEPHONE -> openTelephoneLink(str)
            COPY -> copyToClipboard(str)
        }
    }

    private fun copyToClipboard(str: CharSequence) {
        val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val myClip = ClipData.newPlainText("text", str)
        myClipboard?.primaryClip = myClip;

        Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
    }

    private fun openTelephoneLink(str: CharSequence) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$str")
        context.startActivity(intent)
    }

    private fun openEmailLink(str: CharSequence) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$str")
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    private fun openWebLink(str: CharSequence) {
        var webLink = str
        if (!webLink.startsWith("http")) {
            webLink = "http://$webLink"
        }
        val uris = Uri.parse(webLink.toString())
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        context.startActivity(intents)
    }


}