package pas.com.mm.shoopingcart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.fragments.DescriptionFragment;
import pas.com.mm.shoopingcart.image.ZoomImageView;
import pas.com.mm.shoopingcart.util.ImageCache;
import pas.com.mm.shoopingcart.util.ImageFetcher;
import pas.com.mm.shoopingcart.util.ImageWorker;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    ImageView image;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private Context context;
    private String[] mPlanetTitles;
    public static final String PREFS_NAME = "PAS";
    private Item item;
    // private DrawerLayout mDrawerLayout;
    // private ListView mDrawerList;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    // private ActionBarDrawerToggle mDrawerToggle;
    // private CharSequence mDrawerTitle;
    private  ImageCache.ImageCacheParams cacheParams;
    private CharSequence mTitle;
    private ImageFetcher mImageFetcher;
    private static final String TAG = "ImageCache";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView backImage;
    private ImageView forwardImage;
    private String[] imageUrls;
    private OnFragmentInteractionListener mListener;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        int  mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_big_item_size);

        cacheParams = new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.ie_loader);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
        String object= getActivity().getIntent().getStringExtra("DETAIL_ITEM");
        Gson gson=new Gson();
        this.setItem((Item) gson.fromJson(object,Item.class));
        DetailFragment df= new DetailFragment();
        df.setItem(this.getItem());
        imageUrls= getItem().getImgUrl().split(" ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.activity_detail, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
         toolbar.setTitle(this.getItem().getTitle());
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context=this.getContext();
         backImage=(ImageView) v.findViewById(R.id.back);
        forwardImage=(ImageView) v.findViewById(R.id.forward);
       // final ProgressBar pb=(ProgressBar) v.findViewById(R.id.progressbar_detail_img);
        final Button button = (Button) v.findViewById(R.id.btnChangeImage);
        final ToggleButton tb=(ToggleButton)v.findViewById(R.id.tbFavDetail);
//       final String pos = String.valueOf(getActivity().getIntent().getIntExtra("POSITION", 6000));

        final String detailJson = getActivity().getIntent().getStringExtra("DETAIL_ITEM");
        Gson gson=new Gson();
        final Item i=gson.fromJson(detailJson,Item.class);
        final SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String saved=settings.getString(i.getCode(),"-");
        if(!saved.equals("-")){
          tb.setChecked(true);
        }
        else
        {
            tb.setChecked(false);
        }
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                SharedPreferences.Editor editor=   settings.edit();
               if(b)
                {//Toast.makeText(context,"Checked",Toast.LENGTH_SHORT);
                    Log.d("tag", "checked");
                    editor.putString(i.getCode(),detailJson);
                }
                else{
                   //Toast.makeText(context,"Checked",Toast.LENGTH_SHORT);
                   Log.d("tag", "unchecked");
                   editor.remove(i.getCode());
               }






                editor.commit();


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean sms_success=false;
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", "09451918188");
                    smsIntent.putExtra("sms_body", "Body of Message");
                    startActivity(smsIntent);
                    sms_success=true;
                }
                catch(Exception e)
                {
                   sms_success=false;
                }
                if(sms_success==false)
                {
                    try
                    {
                        Uri uri = Uri.parse("smsto:09451918138");
                        Intent share = new Intent(android.content.Intent.ACTION_SEND,uri);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, "Your text to share");
                        share.setPackage("com.viber.voip");
                        startActivity(Intent.createChooser(share, "Select"));
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context,"Viber not found",Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        final Button call = (Button) v.findViewById(R.id.button);
        call.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String number = "tel:09451913138";
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);
            }
        });
     //   SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);


        TextView text1= (TextView) v.findViewById(R.id.txtPrompt1);
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(), "fonts/font.ttf");
        text1.setTypeface(typeface);
        TextView text2= (TextView) v.findViewById(R.id.txtPrompt2);
        text2.setTypeface(typeface);
        TextView text3= (TextView) v.findViewById(R.id.txtPrompt3);
        text3.setTypeface(typeface);

        TextView textPrice= (TextView) v.findViewById(R.id.textViewPrice);
        textPrice.setTypeface(typeface);
        textPrice.setText(this.getItem().getAmount().toString()+" "+getActivity().getResources().getString(R.string.currency));


        if(getItem().getDiscount()>0 &&getItem().getDiscount()<getItem().getAmount()) {
            TextView t = (TextView) v.findViewById(R.id.oldPrice);
            textPrice.setText(getItem().getDiscount() + " " + getResources().getString(R.string.currency));
            t.setText(getItem().getAmount() + " " + getResources().getString(R.string.currency));
            t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            t.setVisibility(View.VISIBLE);
        }
        final ZoomImageView thumb1View =(ZoomImageView) v.findViewById(R.id.imageView1);
        //ImageWorker.OnImageLoadedListener imageListener=new ImageWorker.OnImageLoadedListener() {
       //     @Override
       //     public void onImageLoaded(boolean success) {
                   //     pb.setVisibility(View.GONE);
                thumb1View.setVisibility(View.VISIBLE);
       //     }
       // };
       // mImageFetcher.loadImage("https://s-media-cache-ak0.pinimg.com/564x/4c/84/03/4c84030879a89cf9dde78ca79b454340.jpg", thumb1View,imageListener);
        String url=this.getItem().getImgUrl();
        Picasso.with(context)
                .load(url)
               // .resize(150, 50)
              //  .centerCrop()
                .into(thumb1View);


        //create imageview here and setbg
        ImageView imageView = new ImageView(this.getContext());

        imageView.setImageResource(R.drawable.anime);

        ViewGroup.LayoutParams params = imageView.getLayoutParams();


        imageView.setLayoutParams(thumb1View.getLayoutParams());
        ((LinearLayout) v.findViewById(R.id.photo_frame)).addView(imageView);
            //   thumb1View.setOnClickListener(new View.OnClickListener() {
     //       @Override
      //      public void onClick(View view) {
    //            zoomImageFromThumb(thumb1View, R.drawable.wallpaper);
     //       }
     //   });

        // Retrieve and cache the system's default "short" animation time.
     //   mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);



        Button descButton=(Button) v.findViewById(R.id.btnDesc);
        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Clieck", "clicked to change");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

                DescriptionFragment newFragment = new DescriptionFragment();
                ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                Fragment f = getFragmentManager().findFragmentByTag("DESC");
                if(f==null || !f.isVisible()) {
                    Log.d("Clieck", "show fragment");
                    ft.replace(R.id.desc_frag_container, newFragment, "DESC");
                    ft.addToBackStack("DESC");
// Start the animated transition.
                    ft.commit();
                }
                else if(f!=null && f.isVisible()) {
                    ft.setCustomAnimations(R.anim.exit_slide_out_up, R.anim.exit_slide_in_up);

                    ft.remove(f);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);


                    ft.commit();
                    getFragmentManager().popBackStack();
                }
            }
        });


        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        if(imageUrls.length==1) {
            forwardImage.setVisibility(View.GONE);
        }else
        {
            backImage.setVisibility(View.VISIBLE);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if(position==0) {
                    backImage.setVisibility(View.GONE);
                }else
                {
                    backImage.setVisibility(View.VISIBLE);
                }
                if(position==imageUrls.length-1) {

                    forwardImage.setVisibility(View.INVISIBLE);
                }
                else {
                    forwardImage.setVisibility(View.VISIBLE);
                }

            }
        });
        DbSupport db=new DbSupport();
       // db.writeNewPost("CODE002","HELLO","HTTP://WWW",12.9);
        db.listenDataChange();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) getActivity().findViewById(R.id.expanded_image);
        // expandedImageView.setImageResource(imageResId);
        ImageCache mImageCache= ImageCache.getInstance(getActivity().getSupportFragmentManager(), cacheParams);
        BitmapDrawable value = mImageCache.getBitmapFromMemCache(String.valueOf("https://s-media-cache-ak0.pinimg.com/564x/4c/84/03/4c84030879a89cf9dde78ca79b454340.jpg"));
        expandedImageView.setImageDrawable(value);
        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        getActivity().findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel, back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }





    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        // END_INCLUDE (fragment_pager_adapter)

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // BEGIN_INCLUDE (fragment_pager_adapter_getitem)
        /**
         * Get fragment corresponding to a specific position. This will be used to populate the
         * contents of the {@link ViewPager}.
         *
         * @param position Position to fetch fragment for.
         * @return Fragment for specified position.
         */
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.


            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putString(DummySectionFragment.URL,imageUrls[position]);
            fragment.setArguments(args);
            return fragment;
        }
        // END_INCLUDE (fragment_pager_adapter_getitem)

        // BEGIN_INCLUDE (fragment_pager_adapter_getcount)
        /**
         * Get number of pages the {@link ViewPager} should render.
         *
         * @return Number of fragments to be rendered as pages.
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return imageUrls.length;
        }
        // END_INCLUDE (fragment_pager_adapter_getcount)

        // BEGIN_INCLUDE (fragment_pager_adapter_getpagetitle)
        /**
         * Get title for each of the pages. This will be displayed on each of the tabs.
         *
         * @param position Page to fetch title for.
         * @return Title for specified page.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            /**
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "ha";
                case 1:
                    return "funny";
                case 2:
                    return "happy";
            }
            return null;
        }
**/
        Drawable myDrawable = getResources().getDrawable(R.drawable.fav_toggle_icon_off);
        SpannableStringBuilder sb = new SpannableStringBuilder("1" ); // space added before text for convenience
        try {
            myDrawable.setBounds(5, 5, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            // TODO: handle exception
        }
            return sb;
        // END_INCLUDE (fragment_pager_adapter_getpagetitle)

    }

    }
    public static  class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String URL = "url";
        private String url;
        private Context context;

        private Animator mCurrentAnimator;
        private int mShortAnimationDuration=1;

        public DummySectionFragment() {

            context=getActivity();
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sliding_photo, container, false);
            Bundle b=  getArguments();
            url= b.getString(URL);
           final  ZoomImageView slideImageView = (ZoomImageView) rootView.findViewById(R.id.slide1);

            Picasso.with(context)
                    .load(url)
                   // .resize(850, 850)
                  //  .centerCrop()
                    .into(slideImageView);

            slideImageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                      public void onClick(View view) {
                            zoomImageFromThumb(slideImageView, url);
                       }
                   });
            return rootView;
        }



        private void zoomImageFromThumb(final View thumbView, String url) {
            // If there's an animation in progress, cancel it immediately and proceed with this one.
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Load the high-resolution "zoomed-in" image.
            final ImageView expandedImageView = (ImageView) getActivity().findViewById(R.id.expanded_image);
            // expandedImageView.setImageResource(imageResId);
           // ImageCache mImageCache= ImageCache.getInstance(getActivity().getSupportFragmentManager(), cacheParams);
          //  BitmapDrawable value = mImageCache.getBitmapFromMemCache(String.valueOf("https://s-media-cache-ak0.pinimg.com/564x/4c/84/03/4c84030879a89cf9dde78ca79b454340.jpg"));
          //  expandedImageView.setImageDrawable(value);

            Picasso.with(context)
                    .load(url)
                    .into(expandedImageView);

            // Calculate the starting and ending bounds for the zoomed-in image. This step
            // involves lots of math. Yay, math.
            final Rect startBounds = new Rect();
            final Rect finalBounds = new Rect();
            final Point globalOffset = new Point();

            // The start bounds are the global visible rectangle of the thumbnail, and the
            // final bounds are the global visible rectangle of the container view. Also
            // set the container view's offset as the origin for the bounds, since that's
            // the origin for the positioning animation properties (X, Y).
            thumbView.getGlobalVisibleRect(startBounds);
            getActivity().findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
            startBounds.offset(-globalOffset.x, -globalOffset.y);
            finalBounds.offset(-globalOffset.x, -globalOffset.y);

            // Adjust the start bounds to be the same aspect ratio as the final bounds using the
            // "center crop" technique. This prevents undesirable stretching during the animation.
            // Also calculate the start scaling factor (the end scaling factor is always 1.0).
            float startScale;
            if ((float) finalBounds.width() / finalBounds.height()
                    > (float) startBounds.width() / startBounds.height()) {
                // Extend start bounds horizontally
                startScale = (float) startBounds.height() / finalBounds.height();
                float startWidth = startScale * finalBounds.width();
                float deltaWidth = (startWidth - startBounds.width()) / 2;
                startBounds.left -= deltaWidth;
                startBounds.right += deltaWidth;
            } else {
                // Extend start bounds vertically
                startScale = (float) startBounds.width() / finalBounds.width();
                float startHeight = startScale * finalBounds.height();
                float deltaHeight = (startHeight - startBounds.height()) / 2;
                startBounds.top -= deltaHeight;
                startBounds.bottom += deltaHeight;
            }

            // Hide the thumbnail and show the zoomed-in view. When the animation begins,
            // it will position the zoomed-in view in the place of the thumbnail.
            thumbView.setAlpha(0f);
            expandedImageView.setVisibility(View.VISIBLE);

            // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
            // the zoomed-in view (the default is the center of the view).
            expandedImageView.setPivotX(0f);
            expandedImageView.setPivotY(0f);

            // Construct and run the parallel animation of the four translation and scale properties
            // (X, Y, SCALE_X, and SCALE_Y).
            AnimatorSet set = new AnimatorSet();
            set
                    .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                            finalBounds.left))
                   // .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                   //         finalBounds.top))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
            set.setDuration(mShortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCurrentAnimator = null;
                }
            });
            set.start();
            this.getActivity().getIntent().putExtra("ZOOMED","y");
           mCurrentAnimator = set;

            // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
            // and show the thumbnail instead of the expanded image.
            final float startScaleFinal = startScale;
            expandedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentAnimator != null) {
                        mCurrentAnimator.cancel();
                    }

                    // Animate the four positioning/sizing properties in parallel, back to their
                    // original values.
                    AnimatorSet set = new AnimatorSet();
                    set
                            .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                            //.with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                    set.setDuration(mShortAnimationDuration);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            thumbView.setAlpha(1f);
                            expandedImageView.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            thumbView.setAlpha(1f);
                            expandedImageView.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }
                    });
                    getActivity().getIntent().putExtra("ZOOMED","n");
                    set.start();
                    mCurrentAnimator = set;
                }
            });
        }

    }

}
