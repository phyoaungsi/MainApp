package pas.com.mm.shoopingcart.fragments.orderlist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.util.ArrayList;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.common.listeners.SwipeableRecyclerViewTouchListener;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.fragments.orderconfirm.OrderConfirmPopupFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderListFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Item newItem;
    public static ArrayList<Item> items=new ArrayList<Item>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private OrderDataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    public OrderListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderListFragment newInstance(String param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        if (getArguments() != null) {
            String jsonStr=getArguments().getString("item");
            Gson gson=new Gson();
            newItem =gson.fromJson(jsonStr,Item.class);
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_order_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
         OrderListFragment.items.add(newItem);
        mAdapter = new OrderDataAdapter(OrderListFragment.items);
        mAdapter.setmContext(getContext());
        mRecyclerView.setAdapter(mAdapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    OrderListFragment.items.remove(position);

                                    mAdapter.notifyItemRemoved(position);
                                }
                                getActivity().invalidateOptionsMenu();
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    OrderListFragment.items.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                getActivity().invalidateOptionsMenu();
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
        mRecyclerView.smoothScrollToPosition(items.size()-1);
      TextView tv=(TextView)  v.findViewById(R.id.txtTotal);
        tv.setText(OrderListFragment.getTotalText()+" "+getContext().getResources().getString(R.string.currency));

        submitOrder(v);
        close(v);
        return v;
    }

    private void submitOrder(View v) {
        Button button=(Button) v.findViewById(R.id.submit_order);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = OrderConfirmPopupFragment.newInstance("HELLO","WORLD");
                newFragment.show(ft, "dialog");
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void close(View v){

        Button descButton=(Button) v.findViewById(R.id.btn_back);
        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Clieck", "clicked to change");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

                OrderListFragment newFragment = new OrderListFragment();
                ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                Fragment f = getFragmentManager().findFragmentByTag("ORDERS");
                if(f==null || !f.isVisible()) {
                    Log.d("Clieck", "show fragment");
                    ft.replace(R.id.desc_frag_container, newFragment, "ORDERS");
                    ft.addToBackStack("ORDERS");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        MenuItem it=menu.getItem(0);
        it.setTitle( OrderListFragment.getTotalText());
    }

    public static String getTotalText() {
        double total=0.00;
        for(Item i: OrderListFragment.items)
        {
            total+=i.getAmount();
        }
        DecimalFormat df = new DecimalFormat("###.##");
      return String.valueOf(df.format(total));

    }
}
