package top.wefor.now.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.wefor.now.R;
import top.wefor.now.model.entity.Zcool;
import top.wefor.now.ui.WebActivity;
import top.wefor.now.utils.NowAppUtils;

/**
 * Created by ice on 15/10/26.
 */
public class ZcoolAdapter extends TestRecyclerViewAdapter<Zcool> {

    public Integer IMAGE_WIDTH, IMAGE_HEIGHT;

    /**
     * fit px by px
     */
    public void setImageWidthAndHeight(int columns) {
        int d3 = context.getResources().getDimensionPixelSize(R.dimen.d3);
        IMAGE_WIDTH = (NowAppUtils.getWidth() - d3 * 2) / columns - d3 * 2;
        IMAGE_HEIGHT = IMAGE_WIDTH * 3 / 4;
    }


    public ZcoolAdapter(Context context, List<Zcool> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_zcool);
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
            case 1:
                return TYPE_HEADER;
            case 2:
                if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT)
                    return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getBigViewResId(), parent, false);
                return new CardViewHolder(view, TYPE_HEADER) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getSmallViewResId(), parent, false);
                return new CardViewHolder(view) {
                };
            }
        }
        return null;
    }

    @Override
    protected void bindCellViewHolder(RecyclerView.ViewHolder cellViewHolder, int position) {
        super.bindCellViewHolder(cellViewHolder, position);
        Zcool news = contents.get(position);
        Logger.d(position + "");
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;
        Uri imgUri = Uri.parse(news.imgUrl);
        cardViewHolder.mSimpleDraweeView.setImageURI(imgUri);
        cardViewHolder.mTitleTv.setText(news.title);
        cardViewHolder.mNameTv.setText("by " + news.name);
        cardViewHolder.mReadTv.setText(news.readCount + " 看过");
        cardViewHolder.mLikeTv.setText(news.likeCount + " 赞");
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.news_list_card_view)
        CardView mCardView;
        @Bind(R.id.title_textView)
        TextView mTitleTv;
        @Bind(R.id.name_textView)
        TextView mNameTv;
        @Bind(R.id.read_textView)
        TextView mReadTv;
        @Bind(R.id.like_textView)
        TextView mLikeTv;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView mSimpleDraweeView;

        public CardViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            if (NowAppUtils.isBelowLollipop()) {
                //set round corner
                RoundingParams roundingParams = new RoundingParams();
                int d2 = context.getResources().getDimensionPixelSize(R.dimen.d2);
                roundingParams.setCornersRadii(d2, d2, 0, 0);
                mSimpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
            }
            mSimpleDraweeView.setAspectRatio(1f);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL)
                ButterKnife.bind(this, v);
        }

        @OnClick(R.id.rootView)
        void onClick(View v) {
            // TODO do what you want :) you can use WebActivity to load detail content
            Zcool news = contents.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(), WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_TITLE, news.title);
            intent.putExtra(WebActivity.EXTRA_URL, news.url);
            intent.putExtra(WebActivity.EXTRA_PIC_URL, news.imgUrl);
            v.getContext().startActivity(intent);
        }
    }

}
