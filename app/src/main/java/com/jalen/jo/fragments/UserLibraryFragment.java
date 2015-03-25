package com.jalen.jo.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalen.jo.R;
import com.jalen.jo.beans.JoLibrary;
import com.jalen.jo.views.EmptyRecyclerView;

import java.util.List;

public class UserLibraryFragment extends Fragment {
    private static final String TAG = "UserLibraryFragment";

    private List<JoLibrary> libraries;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserLibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserLibraryFragment newInstance() {
        UserLibraryFragment fragment = new UserLibraryFragment();
/*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
*/
        return fragment;
    }

    public UserLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_library, container, false);
        // RecyclerView
        EmptyRecyclerView recyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.rv_library_join);
        RecyclerView.Adapter adapter = new MyAdapter(libraries, R.layout.adapter_library_item);
        recyclerView.setAdapter(adapter);
        View emptyView = inflater.inflate(R.layout.library_empty, container, false);
        recyclerView.setEmptyView(emptyView);

        return rootView;
    }

/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/

/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/

/*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/

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
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * {@link android.support.v7.widget.RecyclerView}的Adapter
     */
    private class MyAdapter extends RecyclerView.Adapter {
        private List<JoLibrary> libraries;
        private int resourceId;

        public MyAdapter(List<JoLibrary> libraries, int resourceId){
            this.libraries = libraries;
            this.resourceId = resourceId;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(resourceId, parent, false);

            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "Element " + position + " set.");
            JoLibrary library = libraries.get(position);

//            holder.getCreatorName().setText(library.get);

        }

        @Override
        public int getItemCount() {
            return libraries.size();
        }
    }

    /**
     * 继承于{@link android.support.v7.widget.RecyclerView}
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView libraryPic;
        ImageView creatorPic;
        ImageView overflow;
        TextView libraryName;
        TextView libraryBrief;
        TextView creatorName;
        TextView libraryCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            libraryBrief = (TextView) itemView.findViewById(R.id.library_brief);
            libraryCount = (TextView) itemView.findViewById(R.id.library_collection);
            libraryName = (TextView) itemView.findViewById(R.id.library_name);
            libraryPic = (ImageView) itemView.findViewById(R.id.library_pic);
            creatorName = (TextView) itemView.findViewById(R.id.library_creator);
            overflow = (ImageView) itemView.findViewById(R.id.iv_overflow);
            creatorPic = (ImageView) itemView.findViewById(R.id.library_creator_pic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "第 " + getPosition() + " 位置的Card被点击了");
                }
            });
        }

        public ImageView getLibraryPic() {
            return libraryPic;
        }

        public ImageView getCreatorPic() {
            return creatorPic;
        }

        public ImageView getOverflow() {
            return overflow;
        }

        public TextView getLibraryName() {
            return libraryName;
        }

        public TextView getLibraryBrief() {
            return libraryBrief;
        }

        public TextView getCreatorName() {
            return creatorName;
        }

        public TextView getLibraryCount() {
            return libraryCount;
        }

    }
}
