package com.macularehab.patient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.macularehab.R;
import com.macularehab.TestDisplayActivity;

import java.util.ArrayList;
import java.util.List;

public class TestsListAdapter  extends RecyclerView.Adapter<TestsListAdapter.TestsViewHolder> implements View.OnClickListener, Filterable {

    private Context context;
    private Context activityContext;
    private LayoutInflater mInflater;
    private ArrayList<String> testsList;
    private ArrayList<String> testsListFull;

    public TestsListAdapter(Context context, List<String> testsList, Context activityContext) {

        this.context = context;
        this.activityContext = activityContext;
        mInflater = LayoutInflater.from(context);
        setTestsListData(testsList);

    }

    public void setTestsListData(List<String> testsList) {
        this.testsList = (ArrayList<String>) testsList;
        this.testsListFull = new ArrayList<>(testsList);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public Filter getFilter() {
        return testListFiltered;
    }

    private Filter testListFiltered = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(testsListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String date : testsListFull) {
                    if (date.toLowerCase().contains(filterPattern)) {
                        filteredList.add(date);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            testsList.clear();
            testsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    @NonNull
    @Override
    public TestsListAdapter.TestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.recycler_view_test, parent, false);
        return new TestsListAdapter.TestsViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsListAdapter.TestsViewHolder holder, int position) {

        holder.date.setText(testsList.get(position));
    }



    @Override
    public int getItemCount() {
        return testsList.size();
    }

    public class TestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView date;
        private TestsListAdapter adapter;

        public TestsViewHolder(@NonNull View itemView, TestsListAdapter testsListAdapter) {
            super(itemView);

            adapter=testsListAdapter;
            date  = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(v->sig(v));

        }

        @Override
        public void onClick(View v) {

        }

        private void sig(View v){
            Intent intent = new Intent(activityContext, TestDisplayActivity.class);
            intent.putExtra("date",date.getText().toString());
            activityContext.startActivity(intent);
        }
    }
}

