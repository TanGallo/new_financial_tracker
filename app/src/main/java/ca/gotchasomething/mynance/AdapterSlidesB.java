package ca.gotchasomething.mynance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class AdapterSlidesB extends PagerAdapter {

    private int[] slides;
    //int slideImage[];
    //String[] slideTitle, slideDescription;
    private LayoutInflater inflater;
    private Context context;

    public AdapterSlidesB(int[] slides, Context context) {
        this.slides = slides;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*public AdapterSlidesB(int[] slides, int[] slideImg, String[] slideTit, String[] slideDesc) {
        this.slides = slides;
        slideImage = slideImg;
        slideTitle = slideTit;
        slideDescription = slideDesc;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    @Override
    public int getCount() {
        return slides.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(slides[position], container, false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View)object;
        container.removeView(view);


    }
}
