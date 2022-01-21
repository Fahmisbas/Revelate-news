package com.revelatestudio.revelate.view.detail



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.databinding.ActivityDetailBinding
import com.revelatestudio.revelate.util.ext.getProgressDrawable
import com.revelatestudio.revelate.util.ext.loadImage


const val EXTRA_NEWS = "news"


class DetailActivity : AppCompatActivity() {

    private var news : News? = null
    private var _binding : ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        news = intent.extras?.getParcelable(EXTRA_NEWS)

        news?.apply {
            with(binding) {
                tvAuthor.text = author
                tvContent.text = content
                tvTitle.text  = title

                imgFeature.loadImage(urlToImage, getProgressDrawable(this@DetailActivity))
            }
        }

    }
}