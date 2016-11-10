package pas.com.mm.shoopingcart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
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

    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.activity_detail, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final Context context=this.getContext();

        final ProgressBar pb=(ProgressBar) v.findViewById(R.id.progressbar_detail_img);
        final Button button = (Button) v.findViewById(R.id.btnChangeImage);
        final ToggleButton tb=(ToggleButton)v.findViewById(R.id.tbFavDetail);
       final String pos = String.valueOf(getActivity().getIntent().getIntExtra("POSITION", 6000));

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
                {Toast.makeText(context,"Checked",Toast.LENGTH_SHORT);
                    Log.d("tag", "checked");
                    editor.putString(i.getCode(),detailJson);
                }
                else{
                   Toast.makeText(context,"Checked",Toast.LENGTH_SHORT);
                   Log.d("tag", "unchecked");
                   editor.remove(i.getCode());
               }






                editor.commit();


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /**
                Toast.makeText(context, "anonymous", Toast.LENGTH_SHORT);
                View frame = (View) v.findViewById(R.id.sliding_detail_frame);
                TranslateAnimation anim = new TranslateAnimation(0f, 100f, 0f, 0f);  // might need to review the docs
                anim.setDuration(1000); // set how long you want the animation
                frame.setAnimation(anim);
                frame.startAnimation(anim);

                frame.setLeft(100);
                //  frame.setVisibility(View.VISIBLE);
                // Perform action on click
                 **/
                boolean sms_success=false;
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", "+6591094326");
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
                        Uri uri = Uri.parse("smsto:+6591814799");
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

                String number = "tel:91094326";
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);
            }
        });
     //   SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);


        button.setText("CAL");

        final ZoomImageView thumb1View =(ZoomImageView) v.findViewById(R.id.imageView1);
        //ImageWorker.OnImageLoadedListener imageListener=new ImageWorker.OnImageLoadedListener() {
       //     @Override
       //     public void onImageLoaded(boolean success) {
                        pb.setVisibility(View.GONE);
                thumb1View.setVisibility(View.VISIBLE);
       //     }
       // };
       // mImageFetcher.loadImage("https://s-media-cache-ak0.pinimg.com/564x/4c/84/03/4c84030879a89cf9dde78ca79b454340.jpg", thumb1View,imageListener);
String url="http://i.imgur.com/DvpvklR.png";
        Picasso.with(context)
                .load(url)
               // .resize(150, 50)
              //  .centerCrop()
                .into(thumb1View);

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


    /* Called whenever we call invalidateOptionsMenu() */
    // @Override
    // public boolean onPrepareOptionsMenu(Menu menu) {
    // If the nav drawer is open, hide action items related to the content view
    // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
    // return super.onPrepareOptionsMenu(menu);
    // }

    /**
     * "Zooms" in a thumbnail view by assigning the high resolution image to a hidden "zoomed-in"
     * image view and animating its bounds to fit the entire activity content area. More
     * specifically:
     * <p>
     * <ol>
     * <li>Assign the high-res image to the hidden "zoomed-in" (expanded) image view.</li>
     * <li>Calculate the starting and ending bounds for the expanded view.</li>
     * <li>Animate each of four positioning/sizing properties (X, Y, SCALE_X, SCALE_Y)
     * simultaneously, from the starting bounds to the ending bounds.</li>
     * <li>Zoom back out by running the reverse animation on click.</li>
     * </ol>
     *
     * @param thumbView  The thumbnail view to zoom in.
     * @param imageResId The high-resolution version of the image represented by the thumbnail.
     */
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

}
