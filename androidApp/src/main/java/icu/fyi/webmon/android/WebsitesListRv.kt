package icu.fyi.webmon.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import icu.fyi.webmon.shared.entity.WebsiteEntry

class WebsitesRvAdapter(
    var websites: List<WebsiteEntry>,
    private val onItemClicked: (WebsiteEntry) -> Unit
    ) : RecyclerView.Adapter<WebsitesRvAdapter.WebsiteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_website, parent, false)
        return WebsiteViewHolder(viewHolder) {
            onItemClicked(websites[it])
        }
    }

    override fun getItemCount(): Int = websites.count()

    override fun onBindViewHolder(holder: WebsiteViewHolder, position: Int) {
        holder.bindData(websites[position])
    }

    inner class WebsiteViewHolder(
        itemView: View,
        onItemClicked: (Int) -> Unit
        ) : RecyclerView.ViewHolder(itemView) {
        private val nameText = itemView.findViewById<TextView>(R.id.websiteName)
        private val liveText = itemView.findViewById<TextView>(R.id.websiteLive)
        private val checkedAtText = itemView.findViewById<TextView>(R.id.websiteCheckedAt)

        init {
            itemView.setOnClickListener {
                onItemClicked(absoluteAdapterPosition)
            }
        }

        fun bindData(website: WebsiteEntry) {
            val ctx = itemView.context
            //nameTextView.text = ctx.getString(R.string.site_name_field, website.name, website.type)
            val text = ctx.getString(R.string.site_name_field)
            val dynamicText = String.format(text, website.name, website.type)
            val dynamicStyledText =
                HtmlCompat.fromHtml(dynamicText, HtmlCompat.FROM_HTML_MODE_COMPACT)
            nameText.text = dynamicStyledText
            checkedAtText.text = website.checkedAt
            val siteLive = website.live
            if (siteLive != null ) {
                if (siteLive) {
                    liveText.text = ctx.getString(R.string.server_up)
                    liveText.setTextColor((ContextCompat.getColor(itemView.context, R.color.colorServerUp)))
                } else {
                    liveText.text = ctx.getString(R.string.server_down)
                    liveText.setTextColor((ContextCompat.getColor(itemView.context, R.color.colorServerDown)))
                }
            } else {
                liveText.text = ctx.getString(R.string.no_data)
                liveText.setTextColor((ContextCompat.getColor(itemView.context, R.color.colorNoData)))
            }
        }
    }
}