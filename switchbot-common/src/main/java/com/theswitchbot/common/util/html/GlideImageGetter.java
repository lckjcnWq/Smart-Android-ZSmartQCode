package com.theswitchbot.common.util.html;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.theswitchbot.common.util.UiUtilsKt;

import java.lang.ref.WeakReference;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GlideImageGetter implements Html.ImageGetter {

    private TextView textView;

    public GlideImageGetter(TextView target) {
        textView = target;
    }

    @Override
    public Drawable getDrawable(String url) {
        BitmapDrawablePlaceholder drawable = new BitmapDrawablePlaceholder(textView);
        int round = UiUtilsKt.dp2px(textView.getContext(), 4);
        Glide.with(textView.getContext())
                .asBitmap()
                .load(url)
                .transform(new RoundedCornersTransformation(round, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(drawable);

        return drawable;
    }

    private static class BitmapDrawablePlaceholder extends BitmapDrawable implements Target<Bitmap> {

        protected Drawable drawable;
        private final WeakReference<TextView> textViewRef;

        BitmapDrawablePlaceholder(TextView textView) {
            super(textView.getContext().getResources(), Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
            textViewRef = new WeakReference<>(textView);
        }

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        private void setDrawable(Drawable drawable) {

            TextView textView = textViewRef.get();
            if (textView == null)return;

            this.drawable = drawable;
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();
            int maxWidth = textView.getMeasuredWidth();

            int calculatedHeight = maxWidth * drawableHeight / drawableWidth;
            drawable.setBounds(0, 0, maxWidth, calculatedHeight);
            setBounds(0, 0, maxWidth, calculatedHeight);

            textView.setText(textView.getText());
        }

        @Override
        public void onLoadStarted(@Nullable Drawable placeholderDrawable) {
            if(placeholderDrawable != null) {
                setDrawable(placeholderDrawable);
            }
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            if (errorDrawable != null) {
                setDrawable(errorDrawable);
            }
        }

        @Override
        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
            TextView textView = textViewRef.get();
            if (textView == null)return;

            textView.post(() -> setDrawable(new BitmapDrawable(textView.getContext().getResources(), bitmap)));
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholderDrawable) {
            if(placeholderDrawable != null) {
                setDrawable(placeholderDrawable);
            }
        }

        @Override
        public void getSize(@NonNull SizeReadyCallback cb) {
            TextView textView = textViewRef.get();
            if (textView == null)return;
            textView.post(() -> cb.onSizeReady(textView.getWidth(), textView.getHeight()));
        }

        @Override
        public void removeCallback(@NonNull SizeReadyCallback cb) {}

        @Override
        public void setRequest(@Nullable Request request) {}

        @Nullable
        @Override
        public Request getRequest() {
            return null;
        }

        @Override
        public void onStart() {}

        @Override
        public void onStop() {}

        @Override
        public void onDestroy() {}

    }
}
