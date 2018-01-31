package Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.example.liyang.meterialdesigntest.News_Detail;
import com.example.liyang.meterialdesigntest.R;

import java.util.List;
import java.util.Random;

import Entity.card_image;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import tools.TypeFace;

import static tools.TypeFace.typeface;

/**
 * Created by liyang on 2017/10/23.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private Context mContext;
    private List<card_image> mCardList;
    private int lastAnimatedPosition=-1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private int itemCount=0;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        //ImageView cardimage;
        TextView new_class;
        TextView cardname;
        TextView cardinfo;
        ImageView touxiang;
        ImageView cardbg;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
           // cardimage = (ImageView) view.findViewById(R.id.card_image);
            cardbg = (ImageView) view.findViewById(R.id.cardbg);
            new_class = (TextView) view.findViewById(R.id.new_class);
            cardname = (TextView) view.findViewById(R.id.card_name);
//            Random random = new Random();
//            GradientDrawable g =new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
//                    new int[]{(int) (random.nextLong()%16777216),(int) (random.nextLong()%16777216),(int) (random.nextLong()%16777216),(int) (random.nextLong()%16777216)});
//            cardView.setBackground(g);
            cardinfo = (TextView) view.findViewById(R.id.card_info_other);
            touxiang = (ImageView) view.findViewById(R.id.touxiang);
           // ButterKnife.inject(this,cardView);
        }
    }
    public CardAdapter(List<card_image> cardlist){
        mCardList = cardlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.card,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                card_image card = mCardList.get(position);
                Intent intent = new Intent(mContext, News_Detail.class);
                intent.putExtra(News_Detail.News_Title,card.getName());
                intent.putExtra(News_Detail.News_Image_Id,card.getImageId());
                intent.putExtra(News_Detail.News_Image_Url,card.getImage_url());
                intent.putExtra(News_Detail.News_Url,card.getUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // runEnterAnimation(holder.cardView,position);
        card_image card = mCardList.get(position);
        //Glide.with(mContext).load(R.drawable.cardbg).into(holder.cardbg);
        typeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/PingFang Regular.ttf");
        holder.cardname.setTypeface(typeface);
        holder.cardname.setText(card.getName());
        holder.cardinfo.setText(card.getInfo_other());
        holder.new_class.setText(card.getCategory());
      // Glide.with(mContext).load(R.drawable.baonv)
              // .into(holder.touxiang);
        //MultiTransformation mutil = new MultiTransformation(new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.BOTTOM);
        if(card.getImage_url().equals("")){
            Glide.with(mContext).load(card.getImageId()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(70,0, RoundedCornersTransformation.CornerType.ALL))).into(holder.touxiang);
        }else{
            Glide.with(mContext).load(card.getImage_url()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(70,0, RoundedCornersTransformation.CornerType.ALL))).into(holder.touxiang);
        }

    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }
    private void runEnterAnimation(View view, int position) {

        if (animationsLocked) return;//animationsLocked是布尔类型变量，一开始为false，确保仅屏幕一开始能够显示的item项才开启动画


        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果
            lastAnimatedPosition = position;
            view.setTranslationY(500);//相对于原始位置下方400
            view.setAlpha(0.f);//完全透明
            //每个item项两个动画，从透明到不透明，从下方移动到原来的位置
            //并且根据item的位置设置延迟的时间，达到一个接着一个的效果
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
                    .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
                    .setDuration(700)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                        }
                    })
                    .start();
        }
    }

}
